package src;
public class VendingMachine {

    String[] names;         // item names
    int[] prices;           // item prices (cents)
    int[] stock;            // item stock

    Card currentCard;       // stores the card that is currently inserted in this machine
    CardSystem cardSystem;  // card database
    TransactionSystem recordSystem; // transaction record system

    public VendingMachine(String[] names, int[] prices, int[] stock, Card currentCard, CardSystem cardSystem, TransactionSystem recordSystem) {

        this.names = names;
        this.prices = prices;
        this.stock = stock;

        this.currentCard = currentCard;
        this.cardSystem = cardSystem;
        this.recordSystem = recordSystem;
    }

    public void showItems() {
        System.out.println("\n===== ITEMS =====");
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ") " + names[i]
                    + " - $" + (prices[i] / 100.0)
                    + " | Stock: " + stock[i]);
        }

        System.out.println("=================");

        if (currentCard.inserted == true) {
            System.out.println("Card Inserted: YES (" + currentCard.id + " | " + currentCard.type + ")");
            System.out.println("Balance: $" + (currentCard.balance / 100.0));
        } else {
            System.out.println("Card Inserted: NO");
        }
    }

    public void insertCard(String id, int pin) {
        cardSystem.insertCard(currentCard, id, pin);
    }

    public void removeCard() {
        cardSystem.removeCard(currentCard);
    }

    public void buyItem(int choice) {
        int index = choice - 1;

        if (index < 0 || index >= names.length) {
            System.out.println("Invalid item selection!");
            return;
        }

        if (currentCard.inserted == false) {
            System.out.println("Please insert card first!");
            return;
        }

        if (stock[index] <= 0) {
            System.out.println("Out of stock!");
            return;
        }

        int discount = currentCard.getDiscountPercent();
        int finalPrice = prices[index] - (prices[index] * discount / 100);

        if (currentCard.balance < finalPrice) {
            System.out.println("Not enough money!");
            return;
        }

        currentCard.balance = currentCard.balance - finalPrice;
        stock[index] = stock[index] - 1;

        cardSystem.updateBalance(currentCard);

        System.out.println("Dispensed: " + names[index]);
        System.out.println("Paid: $" + (finalPrice / 100.0));
        System.out.println("Remaining Balance: $" + (currentCard.balance / 100.0));

        recordSystem.addLog("BUY | Card=" + currentCard.id
                + " | Item=" + names[index]
                + " | Paid=$" + (finalPrice / 100.0)
                + " | Type=" + currentCard.type);
    }
}
