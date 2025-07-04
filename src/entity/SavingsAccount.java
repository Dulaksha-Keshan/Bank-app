package entity;

import enums.ACCTYPE;

import java.util.Map;
import java.util.Random;

public class SavingsAccount implements Account {
    private int accNo;
    private String name;
    private long nationalId;
    private double balance;
    private final ACCTYPE acctype = ACCTYPE.SAVINGS;

    public SavingsAccount(int accNo ,String name, long nationalId, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.nationalId = nationalId;
        this.balance = balance;

    }

    public SavingsAccount(String name, long nationalId, double balance) {
        this.name = name;
        this.nationalId = nationalId;
        this.balance = balance;

        Random random = new Random();
        this.accNo = random.nextInt(100000,300000);
    }

    public int getAccNo() {
        return accNo;
    }

    public long getNationalId() {return nationalId;}

    public String getAccountType() {return this.acctype.name();
    }
    public String getName(){return this.name;}

    @Override
    public void details() {
        System.out.printf("Account No: %d%nAccount Name : %s%nNational ID No : %s%nAccount Type : %s%nBalance : %.2f %n",this.accNo,this.name,this.nationalId,this.acctype,this.balance);
    }

    @Override
    public double balance() {return this.balance;}

    @Override
    public void deposit(double amount) {
        try {
            if (amount >= 0) {
                balance += amount;
                System.out.printf("Successfully deposited Rs %.2f%n",amount);
                System.out.printf("Your current available balance is : Rs %.2f",this.balance);
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
                System.out.printf("Successfully withdrew Rs %.2f%n",amount);
                System.out.printf("Your current available balance is : Rs %2f",this.balance);

            } else if (amount>this.balance) {
                System.out.println("Insufficient entity.Account balance");

            }else{
                System.out.println("Invalid amount");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean transferable() {
        try {
            switch (this.acctype){
                case ACCTYPE.SAVINGS, ACCTYPE.CURRENT -> {
                    return true;
                }
                case ACCTYPE.CHILD , ACCTYPE.FIXED-> {
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
        System.out.printf("%s successfully received Rs %.2f from %s%n ",this.name,amount,name);
    }

    @Override
    public void transfer(int accNo, double amount, Map<Integer, Account> accounts) {

        try {
            if (!accounts.containsKey(accNo)){
                System.out.println("Invalid account No");
                return;
            } else if (amount <= 0 || amount > this.balance ){
                System.out.println("Transaction failed: Invalid amount or insufficient balance.");
                return;
            } else {
                Account targetAccount = accounts.get(accNo);

                this.balance -= amount;
                targetAccount.receive(this.name, amount);
                System.out.println("Transfer successful! ");
            }
        } catch (Exception e) {
            System.err.println("An error occurred during transfer: " + e.getMessage());
            throw new RuntimeException("Transfer operation failed unexpectedly.", e);
        }

    }


}
