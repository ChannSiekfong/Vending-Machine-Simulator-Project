package user;

import controller.VendingMachine;

/**
 * Represents a Customer user in the vending machine system.
 * Extends the base {@link User} class and defines the actions a customer is allowed to perform.
 */
public class Customer extends User {

    /**
     * Determines if the customer can perform the specified action.
     *
     * @param action the action to check (e.g., PURCHASE, VIEW_MENU, etc.)
     * @return {@code true} if the customer can perform the action, {@code false} otherwise
     */
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

    // ====== Constructor ======
    /**
     * Constructs a new Customer with the given details.
     *
     * @param userId the user ID
     * @param fullName the full name
     * @param phone the phone number
     * @param username the username
     * @param password the password
     */
    public Customer(String userId, String fullName, String phone, String username, String password) {
        super(userId, fullName, phone, username, password);
    }

    /**
     * Returns a string representation of the Customer, including the role.
     *
     * @return a string with user details and role
     */
    @Override
    public String toString() {
        return super.toString() + 
                ", role=Customer" +
                '}';
    }
    
    /**
     * Gets the role of the user.
     *
     * @return the role as a string ("Customer")
     */
    @Override
    public String getRole() {
        return "Customer";
    }

    /**
     * Checks if this Customer is equal to another object.
     * Equality is based on the base User class implementation.
     *
     * @param obj the object to compare with
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        Customer other = (Customer) obj;
        return super.equals(other);
    }
}