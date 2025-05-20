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

    public int getAccNo() {
        return accNo;
    }

    @Override
    public void details() {
        System.out.printf("Account No: %d%nAccount Name : %s%nNational ID No : %s%nAccount Type : %s%nBalance : %f",this.accNo,this.name,this.nationalId,this.acctype,this.balance);
    }

    @Override
    public void balance() {
        System.out.printf("Your current available balance is : Rs %f",this.balance);
    }

    @Override
    public void deposit(double amount) {
        try {
            if (amount >= 0) {
                balance += amount;
                System.out.printf("Successfully deposited Rs %f%n",amount);
                System.out.printf("Your current available balance is : Rs %f",this.balance);
            }else{
                System.out.println("Invalid amount");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void withdraw(double amount) {
        try {
            if (amount >= 0 && amount<= this.balance) {
                balance -= amount;
                System.out.printf("Successfully withdrew Rs %f%n",amount);
                System.out.printf("Your current available balance is : Rs %f",this.balance);

            } else if (amount>this.balance) {
                System.out.println("Insufficient Account balance");

            }else{
                System.out.println("Invalid amount");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean transferable(double amount) {
        try {
            switch (acctype){
                case SAVINGS, CURRENT -> {
                    return true;
                }
                case CHILD ,FIXED-> {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void receive(String name, double amount) {
        this.balance += amount;
        System.out.printf("%s successfully received Rs %f from %s ",this.name,amount,name);
    }

    @Override
    public void transfer(int accNo, double amount, Account[] accounts) {

        try {
            if (java.util.Arrays.stream(accounts).noneMatch(acc -> acc.getAccNo() == accNo)){
                System.out.println("Invalid account No");

            } else if (amount > 0 && amount <= this.balance ){
                for (Account account : accounts) {
                    if (account.getAccNo() != accNo) {
                        continue;
                    }
                    this.balance -= amount;
                    account.receive(this.name, amount);
                }
            }else {
                System.out.println("Transaction failed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }




}
