package src;

class VendingMachine {

    String[] names;
    int[] prices;
    int[] stock;

    Card currentCard;                 // current session card (null means no card)
    CardSystem cardSystem;
    TransactionSystem recordSystem;

    VendingMachine(String[] names, int[] prices, int[] stock, Card currentCard, CardSystem cardSystem, TransactionSystem recordSystem) {
        this.names = names;
        this.prices = prices;
        this.stock = stock;

        this.currentCard = currentCard;
        this.cardSystem = cardSystem;
        this.recordSystem = recordSystem;
    }

    void showItems() {
        System.out.println("\n===== ITEMS =====");
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ") " + names[i]
                    + " - $" + (prices[i] / 100.0)
                    + " | Stock: " + stock[i]);
        }

        System.out.println("=================");

        if (currentCard != null) {
            System.out.println("Card Inserted: YES (" + currentCard.id + " | " + currentCard.type + ")");
            System.out.println("Balance: $" + (currentCard.balance / 100.0));
        } else {
            System.out.println("Card Inserted: NO");
        }
    }

    void insertCard(String id, int pin) {

        if (currentCard != null) {
            System.out.println("A card is already inserted!");
            return;
        }

        id = id.toUpperCase();

        Card found = cardSystem.findCardById(id);

        if (found == null) {
            System.out.println("Card not found!");
            return;
        }

        if (found.pin != pin) {
            System.out.println("Wrong PIN!");
            return;
        }

        currentCard = found;
        System.out.println("Card inserted successfully.");
        System.out.println("Card Type: " + currentCard.type);
        System.out.println("Balance: $" + (currentCard.balance / 100.0));
    }

    void removeCard() {
        if (currentCard == null) {
            System.out.println("No card inserted.");
            return;
        }

        System.out.println("Card removed: " + currentCard.id);
        currentCard = null;
    }

    void buyItem(int choice) {

        int index = choice - 1;

        if (index < 0 || index >= names.length) {
            System.out.println("Invalid item selection!");
            return;
        }

        if (currentCard == null) {
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

        System.out.println("Dispensed: " + names[index]);
        System.out.println("Paid: $" + (finalPrice / 100.0));
        System.out.println("Remaining Balance: $" + (currentCard.balance / 100.0));

        recordSystem.addLog("BUY | Card=" + currentCard.id
                + " | Item=" + names[index]
                + " | Paid=$" + (finalPrice / 100.0)
                + " | Type=" + currentCard.type);
    }
}
