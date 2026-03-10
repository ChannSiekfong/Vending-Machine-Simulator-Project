package user;

public interface IUser {
    String getUserId();
    String getUsername();
    boolean isActive();
    boolean checkPassword(String input);
    String getFullName();
    boolean isPremium();
    double getBalance();
    String getRole();
    boolean can(String action);
    // Added for completeness (though lesson recommends ArrayList<User> for now)
    int getItemsBought();
    int getLoyaltyPoints();
}