package controller;

import java.util.ArrayList;

import other.PaymentService;
import other.Product;
import other.Slot;
import other.Transaction;
import user.Customer;
import user.Manager;
import user.Restocker;
import user.User;

public class VendingMachine {
    public static final String PURCHASE = "PURCHASE";
    public static final String VIEW_MENU = "VIEW_MENU";
    public static final String VIEW_BALANCE = "VIEW_BALANCE";
    public static final String TOP_UP = "TOP_UP";
    public static final String REDEEM_POINTS = "REDEEM_POINTS";
    public static final String RESTOCK = "RESTOCK";
    public static final String VIEW_INVENTORY = "VIEW_INVENTORY";
    public static final String VIEW_REVENUE = "VIEW_REVENUE";
    public static final String VIEW_TRANSACTIONS = "VIEW_TRANSACTIONS";
    public static final String MANAGE_PRODUCT = "MANAGE_PRODUCT";

    private String location;
    private ArrayList<Slot> slots;
    private double revenue;
    private boolean isOn;
    private ArrayList<Transaction> transactions;
    private ArrayList<User> users;
    private User loggedInUser;
    
    private static int machineCount = 0;
    
    public VendingMachine(String location, int capacity) {
        this.location = location;
        this.slots = new ArrayList<>();
        this.revenue = 0.0;
        this.isOn = true;
        this.transactions = new ArrayList<>();
        this.users = new ArrayList<>();
        this.loggedInUser = null;
        machineCount++;
        
        seedDefaultUsers();
        initializeDefaultProducts();
    }
    
    // =========================
    // DEFAULT PRODUCTS INITIALIZATION
    // =========================
    private void initializeDefaultProducts() {
        addSlot("A1", new Product("Chips", "Snack", 1.50), 5);
        addSlot("A2", new Product("Candy", "Snack", 1.00), 5);
        addSlot("B1", new Product("Soda", "Drink", 2.00), 5);
        addSlot("B2", new Product("Water", "Drink", 1.25), 6);
        addSlot("C1", new Product("Gum", "Snack", 0.75), 10);
        addSlot("D1", new Product("Juice", "Drink", 2.50), 4);
    }
    
    // =========================
    // DEFAULT USERS (BOOTSTRAP)
    // =========================
    private void seedDefaultUsers() {
        Manager manager = new Manager("M001", "System Admin", "00000000", "admin", "admin123", 5000.0f);
        manager.setBalance(100.0);
        users.add(manager);
        
        Restocker restocker = new Restocker("R001", "Restock Staff", "12345678", "restock", "pass123", 3000.0f);
        restocker.setBalance(50.0);
        users.add(restocker);
        
        Customer customer = new Customer("C001", "Test Customer", "87654321", "customer", "pass123");
        customer.setBalance(20.0);
        users.add(customer);
    }
    
    public static int getMachineCount() {
        return machineCount;
    }
    
    public String getLocation() {
        return location;
    }
    
    public double getRevenue() {
        if (!requirePermission(VIEW_REVENUE)) {
            System.out.println("Access denied: Insufficient permissions.");
            return 0.0;
        }
        return revenue;
    }
    
    public void addSlot(String slotID, Product product, int quantity) {
        Slot s = new Slot(slotID, product, quantity);
        slots.add(s);
    }
    
    public void restock(String slotID, int amount) {
        if (!requirePermission(RESTOCK)) return;
        Slot s = findSlot(slotID);
        if (s != null && amount > 0) {
            s.addQuantity(amount);
            System.out.println("Restocked " + slotID + " by " + amount);
        } else {
            System.out.println("Invalid slot or amount.");
        }
    }
    
    public Slot findSlot(String slotID) {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i) != null && slots.get(i).getSlotID().equals(slotID)) {
                return slots.get(i);
            }
        }
        return null;
    }
    
    public void printMenu() {
        if (!requirePermission(VIEW_MENU)) return;
        System.out.println("=== " + location + " Menu ===");
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i) != null) {
                Slot s = slots.get(i);
                System.out.println(s.getSlotID() + " - " + s.getProduct().getName() + " [" + s.getProduct().getCategory() + "] $" + s.getProduct().getPrice() + " x" + s.getQuantity());
            }
        }
    }
    
    // Overload: single item purchase (backwards compatible)
    public boolean vend(String slotID, User user) {
        return vend(slotID, user, 1);
    }
    
    // Main vend method with quantity support
    public boolean vend(String slotID, User user, int quantity) {
        if (!isOn) {
            System.out.println("Machine is off.");
            return false;
        }
        if (user == null || !user.can(PURCHASE)) {
            System.out.println("User cannot purchase.");
            return false;
        }
        if (quantity <= 0) {
            System.out.println("Invalid quantity. Must be at least 1.");
            return false;
        }
        Slot s = findSlot(slotID);
        if (s == null) {
            System.out.println("Invalid slot ID.");
            return false;
        }
        if (s.getQuantity() <= 0) {
            System.out.println("Out of stock.");
            return false;
        }
        if (s.getQuantity() < quantity) {
            System.out.println("Not enough stock. Available: " + s.getQuantity());
            return false;
        }
        if (!user.isCardActive()) {
            System.out.println("Inactive card.");
            return false;
        }
        
        double unitPrice = PaymentService.computeWithLoyalty(s.getProduct().getPrice(), user.isPremium(), user.getItemsBought());
        double totalPrice = unitPrice * quantity;
        
        if (!PaymentService.charge(user, totalPrice)) {
            System.out.println("Insufficient balance. Total cost: $" + totalPrice + ", Your balance: $" + user.getBalance());
            return false;
        }
        
        s.addQuantity(-quantity);
        revenue += totalPrice;
        for (int i = 0; i < quantity; i++) {
            user.incrementItems();
        }
        
        Transaction t = new Transaction(user, location, slots);
        t.record(slotID, s.getProduct().getName() + " x" + quantity, totalPrice);
        transactions.add(t);
        
        System.out.println("Dispensing " + quantity + "x " + s.getProduct().getName() + " | Unit: $" + unitPrice + " | Total: $" + totalPrice);
        return true;
    }
    
    public void printInventory() {
        if (!requirePermission(VIEW_INVENTORY)) return;
        System.out.println("=== " + location + " Inventory ===");
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i) != null) {
                System.out.println(slots.get(i).toString());
            }
        }
    }
    
    public void printTransactions() {
        if (!requirePermission(VIEW_TRANSACTIONS)) return;
        System.out.println("=== Transactions ===");
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i) != null) {
                System.out.println(transactions.get(i).toString());
            }
        }
    }
    
    public void addUser(User user) {
        users.add(user);
    }
    
    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                loggedInUser = user;
                System.out.println("Login success as " + loggedInUser.getRole() + " (" + username + ")");
                return true;
            }
        }
        System.out.println("Login failed: Invalid username or password");
        return false;
    }
    
    public void logout() {
        if (loggedInUser != null) {
            System.out.println("Logged out from " + loggedInUser.getRole() + " (" + loggedInUser.getUsername() + ")");
            loggedInUser = null;
        }
    }
    
    public User getLoggedInUser() {
        return loggedInUser;
    }
    
    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }
    
    public boolean requirePermission(String action) {
        if (loggedInUser == null) {
            System.out.println("Access denied: No user logged in");
            return false;
        }
        if (!loggedInUser.can(action)) {
            System.out.println("Access denied: " + loggedInUser.getRole() + " cannot perform " + action);
            return false;
        }
        return true;
    }
    
    public ArrayList<User> getUsers() {
        return users;
    }
}
