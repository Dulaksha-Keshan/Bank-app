public interface Account {
    void details ();
    void balance();
    void deposit(double amount);
    void withdraw(double amount);
    boolean transferable (double amount);
    void transfer (double amount);
}
