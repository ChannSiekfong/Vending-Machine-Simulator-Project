package user;

public class Manager extends User {

    private float salary;

    @Override
    public boolean can(String action) {
        return true; 
    }

    // ====== Constructor ====== 
    public Manager(String userId, String fullName, String phone, String username, String password, float salary) {
        super(userId, fullName, phone, username, password);
        this.setSalary(salary);
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        if(salary < 1000) {
            System.out.println("Salary can not be less than zero!");
        } else {
            this.salary = salary;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "ManagerStaff [\"Position: Manager salary=" + salary + "]";
    }
    
    @Override
    public String getRole() {
        return "Manager";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Manager)) return false;
        Manager other = (Manager) obj;
        return super.equals(other) && Float.floatToIntBits(salary) == Float.floatToIntBits(other.salary);
    }
}