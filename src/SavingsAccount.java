import java.util.Random;

public class SavingsAccount implements Account{
    private int accNo;
    private String name;
    private int nationalId;
    private double balance;
    private final ACCTYPE  acctype = ACCTYPE.SAVINGS;

    public SavingsAccount(String name, int nationalId, double balance) {
        this.name = name;
        this.nationalId = nationalId;
        this.balance = balance;

        Random random = new Random();
        this.accNo = random.nextInt(100000,500000);
    }

    @Override
    public void details() {
        System.out.printf("Account No: %d%nAccount Name : %s%nNational ID No : %s%nAccount Type : %s%nBalance : %f",this.accNo,this.name,this.nationalId,this.acctype,this.balance);
    }

    @Override
    public void balance() {

    }

    @Override
    public void deposit(double amount) {

    }

    @Override
    public void withdraw(double amount) {

    }

    @Override
    public boolean transferable(double amount) {
        return false;
    }

    @Override
    public void transfer(double amount) {

    }
}
