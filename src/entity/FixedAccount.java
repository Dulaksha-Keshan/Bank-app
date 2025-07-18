package entity;

import enums.ACCTYPE;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

public class FixedAccount implements Account{

    private int accNo;
    private String name;
    private long nationalId;
    private final double balance;
    private final ACCTYPE acctype = ACCTYPE.FIXED;
    private final float interestRate = 12.5F;
    private double addedInterest ;
    private LocalDateTime created_at;





    public FixedAccount(int accNo , String name, long nationalId, double balance) {
        this.balance = balance;
        this.nationalId = nationalId;
        this.name = name;
        this.accNo = accNo;
    }

    public FixedAccount(String name, long nationalId, double balance) {
        this.name = name;
        this.nationalId = nationalId;
        this.balance = balance;

        Random random = new Random();
        this.accNo = random.nextInt(500000,699999);
    }


    public void setCreated_at(String created_at) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.created_at = LocalDateTime.parse(created_at,formatter);
    }

    @Override
    public void details() {
        addingInterest();
        System.out.printf("Account No: %d%nAccount Name : %s%nNational ID No : %s%nAccount Type : %s%nBalance : %.2f %nInitial deposit  : %.2f%nIntrest Rate(weekly) : %.1f%n",
                this.accNo,this.name,this.nationalId,this.acctype,(this.balance +this.addedInterest),this.balance,this.interestRate);
        if(created_at !=null){
            System.out.printf("Account Created At : %tY-%tm-%td %tH:%tM:%tS%n",
                    this.created_at, this.created_at, this.created_at,
                    this.created_at, this.created_at, this.created_at);
        }

    }

    public void addingInterest(){

        Duration duration = Duration.between(created_at,LocalDateTime.now());
        double hourlyRate = (this.interestRate/7/100)/24.0;

        this.addedInterest = this.balance *hourlyRate*duration.toHours();
    }

    @Override
    public int getAccNo() {
        return this.accNo;
    }

    @Override
    public long getNationalId() {
        return this.nationalId;
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

    }

    @Override
    public boolean transferable() {
        return false;
    }

    @Override
    public void transfer(int accNo, double amount, Map<Integer, Account> accounts) {

    }

    @Override
    public void receive(String name, double amount) {

    }
}
