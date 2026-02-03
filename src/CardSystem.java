package src;

class CardSystem {

    Card[] cards;       // array of Card objects
    int cardCount;      // how many cards stored
    int maxSize;        // max cards allowed
    String systemName;  // extra field (Week 3: 4 fields)

    CardSystem(int maxSize) {
        this.maxSize = maxSize;
        cards = new Card[maxSize];
        cardCount = 0;
        systemName = "Card Database";
    }

    String normalizeId(String id) {
        return id.toUpperCase();
    }

    String getTypeFromId(String id) {
        if (id.startsWith("A")) {
            return "premium";
        } else if (id.startsWith("B")) {
            return "vip";
        }
        return "normal";
    }

    void addTestCards() {
        registerCard("1234", 1111, 500);
        registerCard("A1234", 2222, 700);
        registerCard("B1234", 3333, 1000);
    }

    Card findCardById(String inputId) {
        inputId = normalizeId(inputId);

        for (int i = 0; i < cardCount; i++) {
            if (cards[i].id.equals(inputId)) {
                return cards[i];
            }
        }
        return null;
    }

    boolean registerCard(String newId, int newPin, int newBalance) {

        newId = normalizeId(newId);

        if (cardCount >= maxSize) {
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

        Card found = findCardById(newId);
        if (found != null) {
            System.out.println("This Card ID already exists!");
            return false;
        }

        String newType = getTypeFromId(newId);

        cards[cardCount] = new Card(newId, newType, newPin, newBalance);
        cardCount = cardCount + 1;

        System.out.println("Card registered successfully!");
        System.out.println("Auto Type: " + newType);
        return true;
    }

    // ADMIN: show all cards (DO NOT show PIN)
    void adminShowAllCards() {
        System.out.println("\n===== ALL REGISTERED CARDS =====");
        if (cardCount == 0) {
            System.out.println("No cards in system.");
            return;
        }

        for (int i = 0; i < cardCount; i++) {
            System.out.println((i + 1) + ") ID: " + cards[i].id
                    + " | Type: " + cards[i].type
                    + " | Balance: $" + (cards[i].balance / 100.0));
        }
    }

    // ADMIN: top up card
    void adminTopUp(String id, int amount) {
        Card found = findCardById(id);

        if (found == null) {
            System.out.println("Card not found.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Top up amount must be > 0");
            return;
        }

        found.balance = found.balance + amount;
        System.out.println("Top up successful. New Balance: $" + (found.balance / 100.0));
    }

    // ADMIN: delete card
    void adminDeleteCard(String id) {

        id = normalizeId(id);

        int index = -1;

        for (int i = 0; i < cardCount; i++) {
            if (cards[i].id.equals(id)) {
                index = i;
            }
        }

        if (index == -1) {
            System.out.println("Card not found.");
            return;
        }

        for (int i = index; i < cardCount - 1; i++) {
            cards[i] = cards[i + 1];
        }

        cardCount = cardCount - 1;
        System.out.println("Card deleted successfully.");
    }
}
