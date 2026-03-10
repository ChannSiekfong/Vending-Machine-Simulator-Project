package controller;

import java.util.Scanner;
import user.Customer;
import user.User;

public class vmMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VendingMachine vm = new VendingMachine("Lobby", 12);

        // Polymorphism in action
        System.out.println("=== Polymorphism Demo ===");
        for (User u : vm.getUsers()) {
            System.out.println(u.getUsername() + " (" + u.getRole() + ") can " + VendingMachine.PURCHASE + "? " + u.can(VendingMachine.PURCHASE));
            System.out.println(u.getUsername() + " (" + u.getRole() + ") can " + VendingMachine.RESTOCK + "? " + u.can(VendingMachine.RESTOCK));
            System.out.println(u.getUsername() + " (" + u.getRole() + ") can " + VendingMachine.VIEW_REVENUE + "? " + u.can(VendingMachine.VIEW_REVENUE));
        }
        System.out.println("=== End Demo ===");

        int choice;
        
        do {
            if (!vm.isUserLoggedIn()) {
                printMainMenu();
                System.out.print("Choose: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: {
                        System.out.print("Username: ");
                        String username = sc.nextLine();

                        System.out.print("Password: ");
                        String password = sc.nextLine();

                        vm.login(username, password);
                        break;
                    }

                    case 2: {
                        vm.printMenu();
                        break;
                    }

                    case 3: {
                        // Guest purchase (no login required)
                        System.out.print("Enter Full Name: ");
                        String name = sc.nextLine();

                        System.out.print("Phone Number: ");
                        String phone = sc.nextLine();
                        
                        System.out.print("Premium? (1=Yes, 0=No): ");
                        int premiumChoice = sc.nextInt();
                        sc.nextLine();
                        boolean isPremium = (premiumChoice == 1);
                        
                        System.out.print("Initial Balance: ");
                        double balance = sc.nextDouble();
                        sc.nextLine();
                        
                        Customer guest = new Customer("GUEST_" + System.currentTimeMillis(), name, phone, "guest", "temp");
                        guest.setPremium(isPremium);
                        guest.setBalance(balance);
                        
                        vm.printMenu();
                        System.out.print("Enter Slot ID to purchase: ");
                        String slotId = sc.nextLine();
                        
                        System.out.print("Enter amount you want to buy: ");
                        int qty = sc.nextInt();
                        sc.nextLine();
                        
                        boolean success = vm.vend(slotId, guest, qty);
                        if (success) {
                            System.out.println("Purchase successful! New balance: $" + guest.getBalance());
                        } else {
                            System.out.println("Purchase failed! (Check slot, balance, or availability)");
                        }
                        break;
                    }
                }

            } else {
                printUserMenu(vm);

                System.out.print("Choose: ");
                choice = sc.nextInt();
                sc.nextLine();

                handleUserChoice(vm, sc, choice);
            }

        } while (choice != 0);

        sc.close();
    }

    // ===== Handle dynamic user menu choices =====
    private static void handleUserChoice(VendingMachine vm, Scanner sc, int choice) {
        User user = vm.getLoggedInUser();
        int optionNumber = 1;
        
        if (user.can(VendingMachine.VIEW_MENU)) {
            if (choice == optionNumber) {
                vm.printMenu();
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.PURCHASE)) {
            if (choice == optionNumber) {
                vm.printMenu();
                System.out.print("Enter Slot ID to purchase: ");
                String slotId = sc.nextLine();
                
                System.out.print("Enter amount you want to buy: ");
                int qty = sc.nextInt();
                sc.nextLine();
                
                boolean success = vm.vend(slotId, user, qty);
                if (success) {
                    System.out.println("Purchase successful! New balance: $" + user.getBalance());
                } else {
                    System.out.println("Purchase failed! (Check slot, balance, or availability)");
                }
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.RESTOCK)) {
            if (choice == optionNumber) {
                System.out.print("Slot ID: ");
                String slotId = sc.nextLine();
                System.out.print("Amount to add: ");
                int amount = sc.nextInt();
                sc.nextLine();
                vm.restock(slotId, amount);
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_REVENUE)) {
            if (choice == optionNumber) {
                double revenue = vm.getRevenue();
                System.out.println("Total Revenue: $" + revenue);
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_TRANSACTIONS)) {
            if (choice == optionNumber) {
                vm.printTransactions();
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_INVENTORY)) {
            if (choice == optionNumber) {
                vm.printInventory();
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_BALANCE)) {
            if (choice == optionNumber) {
                System.out.println("Current Balance: $" + user.getBalance());
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.TOP_UP)) {
            if (choice == optionNumber) {
                System.out.print("Enter amount to top up: $");
                double amount = sc.nextDouble();
                sc.nextLine();
                if (amount > 0) {
                    user.setBalance(user.getBalance() + amount);
                    System.out.println("Balance updated: $" + user.getBalance());
                } else {
                    System.out.println("Invalid amount!");
                }
                return;
            }
            optionNumber++;
        }
        
        if (user.can(VendingMachine.REDEEM_POINTS)) {
            if (choice == optionNumber) {
                System.out.println("Current Points: " + user.getLoyaltyPoints());
                System.out.print("Enter points to redeem: ");
                int points = sc.nextInt();
                sc.nextLine();
                if (points > 0 && points <= user.getLoyaltyPoints()) {
                    user.setLoyaltyPoints(user.getLoyaltyPoints() - points);
                    System.out.println("Points redeemed. Remaining: " + user.getLoyaltyPoints());
                } else {
                    System.out.println("Invalid or insufficient points!");
                }
                return;
            }
            optionNumber++;
        }
        
        // Logout option (always last before exit)
        if (choice == optionNumber) {
            vm.logout();
            return;
        }
        
        if (choice == 0) {
            System.out.println("Goodbye!");
            return;
        }
        
        System.out.println("Invalid choice. Try again.");
    }

    // ===== Menu printing methods =====
    private static void printMainMenu() {
        System.out.println("\n=== MAIN MENU (Not Logged In) ===");
        System.out.println("1) User Login");
        System.out.println("2) View Revenue");
        System.out.println("3) Guest Purchase (Quick Buy)");
        System.out.println("0) Exit");
    }

    private static void printUserMenu(VendingMachine vm) {
        System.out.println("\n=== USER MENU (Logged In) ===");
        System.out.println("Logged in as: " + vm.getLoggedInUser().getRole() + " (" + vm.getLoggedInUser().getUsername() + ")");
        
        User user = vm.getLoggedInUser();
        int optionNumber = 1;
        
        if (user.can(VendingMachine.VIEW_MENU)) {
            System.out.println(optionNumber + ") View Products");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.PURCHASE)) {
            System.out.println(optionNumber + ") Purchase Item");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.RESTOCK)) {
            System.out.println(optionNumber + ") Restock Products");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_REVENUE)) {
            System.out.println(optionNumber + ") View Revenue");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_TRANSACTIONS)) {
            System.out.println(optionNumber + ") View Transactions");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_INVENTORY)) {
            System.out.println(optionNumber + ") View Inventory");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.VIEW_BALANCE)) {
            System.out.println(optionNumber + ") View Balance");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.TOP_UP)) {
            System.out.println(optionNumber + ") Top Up Balance");
            optionNumber++;
        }
        
        if (user.can(VendingMachine.REDEEM_POINTS)) {
            System.out.println(optionNumber + ") Redeem Points");
            optionNumber++;
        }
        
        System.out.println(optionNumber + ") Logout");
        System.out.println("0) Exit");
    }
}
