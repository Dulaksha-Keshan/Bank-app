package entity;

import enums.ACCTYPE;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

public class ChildAccount implements Account{
    private int accNo;
    private String name;
    private long guardianNationalId;
    private String guardianName;
    private double balance;
    private final ACCTYPE acctype = ACCTYPE.CHILD;
    private LocalDateTime created_at;
    private double addedInterest;


    public ChildAccount(int accNo ,String name,String guardianName, long nationalId, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.guardianNationalId = nationalId;
        this.guardianName =guardianName;
        this.balance = balance;

    }

    public ChildAccount(String name,String guardianName, long nationalId, double balance) {
        this.name = name;
        this.guardianNationalId = nationalId;
        this.guardianName =guardianName;
        this.balance = balance;

        Random random = new Random();
        this.accNo = random.nextInt(700000,899999);
    }


    public void setCreated_at(String created_at) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.created_at = LocalDateTime.parse(created_at,formatter);
    }
    public void addingInterest(){
        if(created_at != null){
            Duration duration = Duration.between(created_at, LocalDateTime.now());
            float interestRate = 3f;
            double hourlyRate = (interestRate / 7 / 100) / 24.0;

            this.addedInterest = this.balance * hourlyRate * duration.toHours();
        }
    }

    @Override
    public void details() {
        addingInterest();
        System.out.printf("Account No: %d%nChild Name : %s%nGuardian name : %s%nNational ID No : %s%nAccount Type : %s%nBalance : %.2f %n",
                this.accNo,this.name,this.guardianName,this.guardianNationalId,this.acctype,this.balance);
        if(created_at !=null){
            System.out.printf("Account Created At : %tY-%tm-%td%n",
                    this.created_at, this.created_at, this.created_at);
        }
    }

    @Override
    public int getAccNo() {
        return this.accNo;
    }

    @Override
    public long getNationalId() {
        return this.guardianNationalId;
    }

    @Override
    public String getAccountType() {
        return this.acctype.name();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double balance() {
        addingInterest();
        return this.balance + this.addedInterest;
    }

    @Override
    public void deposit(double amount) {
        System.out.println("Sorry this account does not supports depositing");
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Sorry this account does not supports withdrawing");
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
    public void transfer(int accNo, double amount, Map<Integer, Account> accounts) {
        System.out.println("Sorry this account does not supports transferring");
    }

    @Override
    public void receive(String name, double amount) {
        this.balance += amount;
        System.out.printf("%s successfully received Rs %.2f from %s%n ",this.name,amount,name);
    }
}
