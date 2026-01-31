package src;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ITEMS
        String[] names = new String[]{"Coke", "Pepsi", "Water", "Chips", "Candy"};
        int[] prices = new int[]{150, 140, 100, 200, 120};
        int[] stock = new int[]{5, 5, 5, 5, 5};

        // SYSTEMS
        CardSystem cardSystem = new CardSystem(100);
        cardSystem.addTestCards();

        TransactionSystem recordSystem = new TransactionSystem(200);

        // Inserted card object (must use parameter constructor)
        Card currentCard = new Card("", "", 0, 0, false);

        VendingMachine vm = new VendingMachine(names, prices, stock, currentCard, cardSystem, recordSystem);

        // ADMIN PIN (secret)
        int ADMIN_PIN = 9999;

        int running = 1;

        while (running == 1) {

            System.out.println("\n===== START MENU =====");
            System.out.println("1) User Section");
            System.out.println("2) Admin Section");
            System.out.println("3) Exit");
            System.out.print("Choose: ");

            int startChoice = input.nextInt();
            input.nextLine();

            // USER SECTION
            if (startChoice == 1) {

                int userRunning = 1;
                while (userRunning == 1) {
                    System.out.println("\n===== USER MENU =====");
                    System.out.println("1) Show Items");
                    System.out.println("2) Insert Card (ID + PIN)");
                    System.out.println("3) Buy Item");
                    System.out.println("4) Remove Card");
                    System.out.println("5) Back");
                    System.out.print("Choose: ");

                    int option = input.nextInt();
                    input.nextLine();

                    if (option == 1) {
                        vm.showItems();

                    } else if (option == 2) {
                        System.out.print("Enter Card ID: ");
                        String id = input.nextLine();

                        System.out.print("Enter PIN (4 digits): ");
                        int pin = input.nextInt();
                        input.nextLine();

                        vm.insertCard(id, pin);

                    } else if (option == 3) {
                        vm.showItems();
                        System.out.print("Select item number: ");
                        int choice = input.nextInt();
                        input.nextLine();

                        vm.buyItem(choice);

                    } else if (option == 4) {
                        vm.removeCard();

                    } else if (option == 5) {
                        userRunning = 0;

                    } else {
                        System.out.println("Invalid option!");
                    }
                }

            // ADMIN SECTION
            } else if (startChoice == 2) {

                System.out.print("Enter Admin PIN: ");
                int pin = input.nextInt();
                input.nextLine();

                if (pin != ADMIN_PIN) {
                    System.out.println("Wrong Admin PIN! Access denied.");
                } else {

                    int adminRunning = 1;
                    while (adminRunning == 1) {

                        System.out.println("\n===== ADMIN MENU =====");
                        System.out.println("1) Show All Cards (No PIN shown)");
                        System.out.println("2) Register New Card");
                        System.out.println("3) Top Up Card");
                        System.out.println("4) Delete Card");
                        System.out.println("5) Show Transaction Records");
                        System.out.println("6) Back");
                        System.out.print("Choose: ");

                        int option = input.nextInt();
                        input.nextLine();

                        if (option == 1) {
                            cardSystem.adminShowAllCards();

                        } else if (option == 2) {
                            System.out.print("New Card ID: ");
                            String id = input.nextLine();

                            System.out.print("Type (normal/premium/vip): ");
                            String type = input.nextLine();

                            System.out.print("PIN (4 digits): ");
                            int newPin = input.nextInt();
                            input.nextLine();

                            System.out.print("Balance (cents): ");
                            int balance = input.nextInt();
                            input.nextLine();

                            cardSystem.registerCard(id, type, newPin, balance);

                        } else if (option == 3) {
                            System.out.print("Card ID to top up: ");
                            String id = input.nextLine();

                            System.out.print("Amount (cents): ");
                            int amount = input.nextInt();
                            input.nextLine();

                            cardSystem.adminTopUp(id, amount);

                        } else if (option == 4) {
                            System.out.print("Card ID to delete: ");
                            String id = input.nextLine();
                            cardSystem.adminDeleteCard(id);

                        } else if (option == 5) {
                            recordSystem.showLogs();

                        } else if (option == 6) {
                            adminRunning = 0;

                        } else {
                            System.out.println("Invalid option!");
                        }
                    }
                }

            } else if (startChoice == 3) {
                running = 0;
                System.out.println("Program ended.");

            } else {
                System.out.println("Invalid choice!");
            }
        }

        input.close();
    }
}
