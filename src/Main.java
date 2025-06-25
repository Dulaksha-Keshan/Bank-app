import entity.Account;
import entity.SavingsAccount;
import entity.User;
import enums.GENDER;

import java.util.*;

public class Main {
    static Scanner input = new Scanner(System.in);
    static Map <Long, User> Users = new HashMap<>();
    static Map<Integer, Account> Accounts = new HashMap<>();


    public static void main(String[] args) {

        try {
            while (true) {
                System.out.printf("%n************************%nWelcome to the Banking app%n************************%n");
                System.out.println("1.Register to a new account ");
                System.out.println("2.Log in to existing account ");
                System.out.println("0.Exit");
                String ans = input.nextLine().strip();
                switch (ans){
                    case "1":{
                        registration();
                        break;
                    }
                    case "2":{
                        loggingIn();
                        break;
                    }
                    case "0":
                        System.out.println("<<<<<<<Exiting>>>>>>>");
                        return;
                    default:{
                        System.out.println("Invalid Input try again");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error at main menu " + e);
        }
    }

    public static void registration (){
        System.out.println("Enter the Full Name :");
        String name = input.nextLine();

        System.out.println("Enter the Age :");
        int age = input.nextInt();
        input.nextLine();

        System.out.println("Enter the National ID No :");
        Long id = input.nextLong();
        input.nextLine();

        System.out.println("Enter the Sex(MALE/FEMALE) :");
        GENDER sex = GENDER.valueOf(input.nextLine().strip().toUpperCase());

        User newUser  = new User(name,id,age,sex);
        Users.put(id,newUser);
        accountCreation(name,id);
    }

    public static void accountCreation (String name , Long id ){
         System.out.println("Chose the account type to create (Savings/Current/Fixed/Child) : ");
         String ans = input.nextLine();

         switch (ans.strip().toLowerCase()){
             case "savings" :{
                 while(true){
                     System.out.println("Initial deposit amount : ");
                     double amount = input.nextDouble();
                     if (amount>0){
                         Account newAcc = new SavingsAccount(name,id,amount);
                         Accounts.put(newAcc.getAccNo(), newAcc);
                         Users.get(id).addAccount(newAcc);
                         newAcc.details();
                         break;
                     }
                 }

             }
             case "current":{}//to be implemented after current account class
             case "fixed":{}//to be implemented after fixed account class
             case "child":{}//to be implemented after child account class
             default : System.out.println("Invalid input");
         }
     }

    public static void loggingIn() {
        try {
            int accNO ;
            System.out.println("Enter the account No");
            accNO = input.nextInt();
            input.nextLine();
            if (Accounts.isEmpty() || !Accounts.containsKey(accNO)){
                throw new IllegalArgumentException("Invalid account Number try again");
            } else{
                Account acc = Accounts.get(accNO);
                loggedIn(acc);
            }
            }catch (IllegalArgumentException e) {
               System.out.println(e.getLocalizedMessage());
            }catch (InputMismatchException e) {
            input.nextLine();
            System.out.println("Invalid Input");
        }catch (Exception e) {
            input.nextLine();
            System.out.println("Error at Logging Menu "+e);
        }
    }

    public static void loggedIn(Account acc){
        double amount;
        int accNo;
        int ans;
        do {
            try {
                System.out.printf("%n1.Check details%n2.Check balance%n3.deposit%n4.Withdraw%n5.Transfer%n0.Exit%n");
                ans = input.nextInt();
                input.nextLine();
                switch (ans){
                    case 1:{
                        acc.details();
                        break;
                    }
                    case 2:{
                        acc.balance();
                        break;
                    }
                    case 3:{
                        System.out.println("Enter the amount");
                        amount = input.nextDouble();
                        input.nextLine();
                        acc.deposit(amount);
                        break;
                    }
                    case 4:{
                        System.out.println("Enter the amount");
                        amount = input.nextDouble();
                        input.nextLine();
                        acc.withdraw(amount);
                        break;
                    }
                    case 5:{
                        if (acc.transferable()) {
                            System.out.println("Enter the entity.Account No");
                            accNo = input.nextInt();
                            input.nextLine();

                            System.out.println("Enter the amount");
                            amount = input.nextDouble();
                            input.nextLine();

                            acc.transfer(accNo,amount,Accounts);
                        }else{
                            System.out.println("Sorry this account cannot transfer money");
                        }
                        break;
                    } default:{
                        return;
                    }


                }
            } catch (Exception e) {
                System.out.println("Error at Logged menu" + e.getMessage());
            }
        }while (true);
    }
}