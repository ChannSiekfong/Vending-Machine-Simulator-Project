package user;

import controller.VendingMachine;

public class Customer extends User {

    @Override
    public boolean can(String action) {
        if (VendingMachine.PURCHASE.equals(action) ||
            VendingMachine.VIEW_MENU.equals(action) ||
            VendingMachine.VIEW_BALANCE.equals(action) ||
            VendingMachine.TOP_UP.equals(action) ||
            VendingMachine.REDEEM_POINTS.equals(action)) {
            return true;
        }
        return false;
    }

    public Customer(String userId, String fullName, String phone, String username, String password) {
        super(userId, fullName, phone, username, password);
    }

    @Override
    public String toString() {
        return super.toString() + 
                ", role=Customer" +
                '}';
    }
    
    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        Customer other = (Customer) obj;
        return super.equals(other);
    }
}