package src;
public class CardSystem {

    String[] ids;        // store card IDs
    String[] types;      // store card types
    int[] pins;          // store card PINs (private)
    int[] balances;      // store card balances

    int count;           // current number of cards stored
    int maxSize;         // max cards allowed

    public CardSystem(int maxSize) {
        this.maxSize = maxSize;

        ids = new String[maxSize];
        types = new String[maxSize];
        pins = new int[maxSize];
        balances = new int[maxSize];

        count = 0;
    }

    public void addTestCards() {
        registerCard("A1234", "premium", 1111, 700);
        registerCard("B1234", "vip", 2222, 1000);
        registerCard("1234", "normal", 3333, 500);
    }

    public int findCardIndexById(String inputId) {
        for (int i = 0; i < count; i++) {
            if (ids[i].equals(inputId)) {
                return i;
            }
        }
        return -1;
    }

    public boolean registerCard(String newId, String newType, int newPin, int newBalance) {
        if (count >= maxSize) {
            System.out.println("Card system full! Cannot add more cards.");
            return false;
        }

        if (newId.length() == 0) {
            System.out.println("Invalid ID!");
            return false;
        }

        if (newPin < 1000 || newPin > 9999) {
            System.out.println("PIN must be 4 digits (1000-9999).");
            return false;
        }

        if (newBalance < 0) {
            System.out.println("Balance cannot be negative.");
            return false;
        }

        if (newType.equals("normal") == false &&
            newType.equals("premium") == false &&
            newType.equals("vip") == false) {

            System.out.println("Invalid type! Use normal/premium/vip");
            return false;
        }

        if (findCardIndexById(newId) != -1) {
            System.out.println("This Card ID already exists!");
            return false;
        }

        ids[count] = newId;
        types[count] = newType;
        pins[count] = newPin;
        balances[count] = newBalance;

        count = count + 1;

        System.out.println("Card registered successfully!");
        return true;
    }

    public boolean verifyCard(String inputId, int inputPin) {
        int index = findCardIndexById(inputId);
        if (index == -1) {
            return false;
        }
        if (pins[index] != inputPin) {
            return false;
        }
        return true;
    }

    public boolean insertCard(Card card, String inputId, int inputPin) {
        if (card.inserted == true) {
            System.out.println("A card is already inserted!");
            return false;
        }

        boolean ok = verifyCard(inputId, inputPin);
        if (ok == false) {
            System.out.println("Card ID or PIN is incorrect!");
            return false;
        }

        int index = findCardIndexById(inputId);

        card.id = ids[index];
        card.type = types[index];
        card.pin = pins[index];
        card.balance = balances[index];
        card.inserted = true;

        System.out.println("Card inserted successfully.");
        System.out.println("Card Type: " + card.type);
        System.out.println("Balance: $" + (card.balance / 100.0));

        return true;
    }

    public void updateBalance(Card card) {
        int index = findCardIndexById(card.id);
        if (index != -1) {
            balances[index] = card.balance;
        }
    }

    public void removeCard(Card card) {
        if (card.inserted == false) {
            System.out.println("No card inserted.");
            return;
        }

        updateBalance(card);
        card.clearCard();
        System.out.println("Card removed.");
    }

    // ADMIN: show all cards (DO NOT show PIN)
    public void adminShowAllCards() {
        System.out.println("\n===== ALL REGISTERED CARDS =====");
        if (count == 0) {
            System.out.println("No cards in system.");
            return;
        }

        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ") ID: " + ids[i]
                    + " | Type: " + types[i]
                    + " | Balance: $" + (balances[i] / 100.0));
        }
    }

    // ADMIN: top up card
    public void adminTopUp(String id, int amount) {
        int index = findCardIndexById(id);

        if (index == -1) {
            System.out.println("Card not found.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Top up amount must be > 0");
            return;
        }

        balances[index] = balances[index] + amount;
        System.out.println("Top up successful. New Balance: $" + (balances[index] / 100.0));
    }

    // ADMIN: block/delete card
    public void adminDeleteCard(String id) {
        int index = findCardIndexById(id);

        if (index == -1) {
            System.out.println("Card not found.");
            return;
        }

        // Shift left to remove
        for (int i = index; i < count - 1; i++) {
            ids[i] = ids[i + 1];
            types[i] = types[i + 1];
            pins[i] = pins[i + 1];
            balances[i] = balances[i + 1];
        }

        count = count - 1;
        System.out.println("Card deleted successfully.");
    }
}
