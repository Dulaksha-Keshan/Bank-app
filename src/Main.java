import data.dao.AccountDao;
import data.dao.UserDao;
import entity.*;
import enums.GENDER;

import java.util.*;

public class Main {
    static Scanner input = new Scanner(System.in);
    static Map <Long, User> Users = new HashMap<>();
    static Map<Integer, Account> Accounts = new HashMap<>();
    static UserDao userDao = new UserDao();
    static AccountDao accountDao = new AccountDao();

    public static void main(String[] args) {

        try {
            while (true) {
                System.out.printf("%n************************%nWelcome to the Banking app%n************************%n");
                System.out.println("1.Register to a new account ");
                System.out.println("2.Create an new account if an existing user ");
                System.out.println("3.Log in to existing account ");
                System.out.println("0.Exit");
                String ans = input.nextLine().strip();
                switch (ans){
                    case "1":{
                        registration();
                        break;
                    } case "2":{
                        Users = userDao.getAll();
                        System.out.println("Enter the National Id ");
                        Long id = input.nextLong();
                        input.nextLine();
                        if(Users.isEmpty() || !Users.containsKey(id)){
                            System.out.println("User does not exist or invalid National ID");
                            break;
                        }
                        System.out.println("Confirm Name(Yes?No)");
                        String name = Users.get(id).getName();
                        System.out.println(name);
                        String confirm = input.nextLine().strip().toLowerCase();
                        if (confirm.equals("yes")){
                            accountCreation(name,id);
                        } else{
                            System.out.println("User is not found or invalid input");
                        }
                        break;
                    }
                    case "3":{
                        loggingIn();
                        break;
                    } case "0":
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
        userDao.create(newUser);
        Users = userDao.getAll();
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
                     input.nextLine();
                     if (amount>0){
                         Account newAcc = new SavingsAccount(name,id,amount);
                         accountDao.create(newAcc);
                         Users.get(id).addAccount(newAcc);
                         newAcc.details();
                         break;
                     }
                 }
                 return;

             }
             case "current":{
                 System.out.println("Initial deposit amount : ");
                 double amount = input.nextDouble();
                 input.nextLine();
                 if (amount>49999){
                     Account newAcc = new CurrentAccount(name,id,amount);
                     accountDao.create(newAcc);
                     Users.get(id).addAccount(newAcc);
                     newAcc.details();
                 }else {
                     System.out.println("Minimum deposit for a current account is 50000.00");
                 }
                 break;
             }
             case "fixed":{
                 System.out.println("Current Interest rate : 12.5%");
                 System.out.println("Initial deposit amount : ");
                 double amount = input.nextDouble();
                 input.nextLine();
                 if (amount>99999){
                     Account newAcc = new FixedAccount(name,id,amount);
                     accountDao.create(newAcc);
                     Users.get(id).addAccount(newAcc);
                     newAcc.details();
                 }else {
                     System.out.println("Minimum deposit for a Fixed account is 100000.00");
                 }
                 break;
             }
             case "child":{
                 System.out.println("Enter the child Name :");
                 String c_name = input.nextLine();
                 while(true){
                     System.out.println("Initial deposit amount : ");
                     double amount = input.nextDouble();
                     input.nextLine();
                     if (amount>0){
                         Account newAcc = new ChildAccount(c_name,name,id,amount);
                         accountDao.create(newAcc);
                         Users.get(id).addAccount(newAcc);
                         newAcc.details();
                         break;
                     }
                 }
             }
             default : System.out.println("Invalid input");
         }
     }

    public static void loggingIn() {
        try {
            int accNO ;
            Accounts = accountDao.getAll();
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
                System.out.printf("%n%nWelcome Back %5s%n",acc.getName());
                System.out.printf("%n1.Check details%n2.Check balance%n3.deposit%n4.Withdraw%n5.Transfer%n0.Exit%n");
                ans = input.nextInt();
                input.nextLine();
                switch (ans){
                    case 1:{
                        acc.details();
                        System.out.println("Do you wish to perform another service ? (Y/N)");
                        String cont = input.nextLine().strip().toLowerCase();
                        if (cont.equals("n")) {
                            System.out.println("Thank you see you again!");
                            return;
                        }
                        break;
                    }
                    case 2:{
                        System.out.printf("Your current account balance is %.2f %n",acc.balance());
                        System.out.println("Do you wish to perform another service ? (Y/N)");
                        String cont = input.nextLine().strip().toLowerCase();
                        if (cont.equals("n")) {
                            System.out.println("Thank you see you again!");
                            return;
                        }
                        break;
                    }
                    case 3:{
                        if (acc.transferable()) {
                            System.out.println("Enter the amount");
                            amount = input.nextDouble();
                            input.nextLine();
                            acc.deposit(amount);
                            accountDao.update(acc);
                        }else {
                            acc.deposit(0);
                        }
                        System.out.println("Do you wish to perform another service ? (Y/N)");
                        String cont = input.nextLine().strip().toLowerCase();
                        if (cont.equals("n")) {
                            System.out.println("Thank you see you again!");
                            return;
                        }
                        break;
                    }
                    case 4:{
                        if (acc.transferable()) {
                            System.out.println("Enter the amount");
                            amount = input.nextDouble();
                            input.nextLine();
                            acc.withdraw(amount);
                            accountDao.update(acc);
                        }else {
                            acc.withdraw(0);
                        }
                        System.out.println("Do you wish to perform another service ? (Y/N)");
                        String cont = input.nextLine().strip().toLowerCase();
                        if (cont.equals("n")) {
                            System.out.println("Thank you see you again!");
                            return;
                        }
                        break;
                    }
                    case 5:{
                        if (acc.transferable()) {
                            System.out.println("Enter the Account No");
                            accNo = input.nextInt();
                            input.nextLine();

                            System.out.println("Enter the amount");
                            amount = input.nextDouble();
                            input.nextLine();

                            acc.transfer(accNo, amount, Accounts);
                            accountDao.update(acc);
                            accountDao.update(Accounts.get(accNo));

                        }else{
                            System.out.println("Sorry Transfer option is not available for this account ");
                        }
                        System.out.println("Do you wish to perform another service ? (Y/N)");
                        String cont = input.nextLine().strip().toLowerCase();
                        if (cont.equals("n")) {
                            System.out.println("Thank you see you again!");
                            return;
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
