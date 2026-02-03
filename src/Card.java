package src;

class Card {

    String id;       // card id (example: A1234)
    String type;     // normal / premium / vip
    int pin;         // secret pin
    int balance;     // money in cents

    Card(String id, String type, int pin, int balance) {
        this.id = id;
        this.type = type;
        this.pin = pin;
        this.balance = balance;
    }

    int getDiscountPercent() {
        if (type.equals("vip")) {
            return 10;
        } else if (type.equals("premium")) {
            return 20;
        }
        return 0;
    }
}
