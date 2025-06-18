import java.util.Map;

public interface Account {
    void details ();
    int getAccNo ();
    void balance();
    void deposit(double amount);
    void withdraw(double amount);
    boolean transferable ();
    void transfer (int accNo , double amount , Map<Integer, Account> accounts);
    void receive(String name, double amount);
}
