package src;
public class Card {

    String id;        // Card unique ID (example: A1234)
    String type;      // "normal", "premium", "vip"
    int pin;          // Secret PIN for verification
    int balance;      // Money stored in cents
    boolean inserted; // True if card is inserted in machine

    public Card(String id, String type, int pin, int balance, boolean inserted) {
        this.id = id;
        this.type = type;
        this.pin = pin;
        this.balance = balance;
        this.inserted = inserted;
    }

    public int getDiscountPercent() {
        if (type.equals("vip")) {
            return 20;
        } else if (type.equals("premium")) {
            return 10;
        }
        return 0;
    }

    public void clearCard() {
        id = "";
        type = "";
        pin = 0;
        balance = 0;
        inserted = false;
    }
}
