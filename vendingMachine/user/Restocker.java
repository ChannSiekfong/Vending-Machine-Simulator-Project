package user;

import controller.VendingMachine;

public class Restocker extends User {

    private float salary;

    @Override
    public boolean can(String action) {
        if (VendingMachine.RESTOCK.equals(action) ||
            VendingMachine.VIEW_MENU.equals(action) ||
            VendingMachine.VIEW_INVENTORY.equals(action) ||
            VendingMachine.PURCHASE.equals(action) ||
            VendingMachine.VIEW_BALANCE.equals(action) ||
            VendingMachine.TOP_UP.equals(action) ||
            VendingMachine.REDEEM_POINTS.equals(action) ||
            VendingMachine.MANAGE_PRODUCT.equals(action)) {
            return true;
        }
        return false;
    }

    // ====== Constructor ======
    public Restocker(String userId, String fullName, String phone, String username, String password, float salary) {
        super(userId, fullName, phone, username, password);
        this.setSalary(salary);
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        if(salary < 0) {
            System.out.println("Salary can not be less than zero!");
        } else {
            this.salary = salary;
        }
    }

    @Override
    public String toString() {
        return super.toString() + 
                "Restocker [\"Position: Staff salary=" + salary +
                ']';
    }
    
    @Override
    public String getRole() {
        return "Restocker";
    }

    @Override 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Restocker)) return false;
        Restocker other = (Restocker) obj;
        return super.equals(other) && Float.floatToIntBits(salary) == Float.floatToIntBits(other.salary);
    }
}