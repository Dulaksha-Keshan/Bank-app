import com.password4j.Argon2Function;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Argon2;
import data.dao.AccountDao;
import data.dao.Dao;
import data.dao.UserDao;
import entity.*;
import entity.records.PasswordRecord;
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
            Accounts = accountDao.getAll();
            Users = userDao.getAll();
            while (true) {
                System.out.printf("%n************************%nWelcome to the Banking app%n************************%n");
                System.out.println("1.Register to a new account ");
                System.out.println("2.Log in if an existing user ");
                System.out.println("3.Log in to existing account directly");
                System.out.println("0.Exit");
                String ans = input.nextLine().strip();
                switch (ans){
                    case "1":{
                        registration();
                        break;
                    }
                    case "2":{
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

                        if (input.nextLine().strip().equalsIgnoreCase("yes")){
                            System.out.printf("%n%nWelcome Back %5s%n",name);

                            userLoggedIn(id,name);

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

        while (true){
            System.out.println("Enter a password");
            String rawPass = input.nextLine().strip();
            System.out.println("Confirm the password");
            String conf = input.nextLine().strip();
            if (rawPass.equals(conf)&& !rawPass.isBlank()){
                PasswordRecord passwordRecord = hashCred(rawPass);
                userDao.storePass(passwordRecord.hash(),passwordRecord.salt(),id);
                break;
            }else {
                System.out.println("Invalid Password try enter a different one");
            }
        }

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
                         System.out.printf("Amount is %.2f Confirm ? (yes/no)",amount);
                         if (input.nextLine().strip().equalsIgnoreCase("no")){
                             System.out.println("Re-enter the amount...");
                             continue;
                         }else if(input.nextLine().strip().equalsIgnoreCase("yes")){
                             Account newAcc = new SavingsAccount(name,id,amount);
                             accountDao.create(newAcc);
//                             Users.get(id).addAccount(newAcc);
                             newAcc.details();
                             break;
                         }else {
                             System.out.println("Invalid Input try Again");
                             continue;
                         }

                     }else {
                         System.out.println("Invalid  amount....");
                     }
                     break;
                 }
                 return;

             }
             case "current":{
                 while (true){
                     System.out.println("Initial deposit amount : ");
                     double amount = input.nextDouble();
                     input.nextLine();
                     if (amount>49999){
                         System.out.printf("Amount is %.2f Confirm ? (yes/no)",amount);
                         if (input.nextLine().strip().equalsIgnoreCase("no")){
                             System.out.println("Re-enter the amount...");
                             continue;
                         }else if(input.nextLine().strip().equalsIgnoreCase("yes")){
                             Account newAcc = new CurrentAccount(name,id,amount);
                             accountDao.create(newAcc);
//                             Users.get(id).addAccount(newAcc);
                             newAcc.details();
                             break;
                         }else {
                             System.out.println("Invalid Input try Again");
                             continue;
                         }
                     }else {
                         System.out.println("Minimum deposit for a current account is 50000.00");
                     }
                     break;
                 }return;

             }
             case "fixed":{
                 System.out.println("Current Interest rate : 12.5%");
                 while(true){
                     System.out.println("Initial deposit amount : ");
                     double amount = input.nextDouble();
                     input.nextLine();
                     if (amount>99999){
                         System.out.printf("Amount is %.2f Confirm ? (yes/no)",amount);
                         if (input.nextLine().strip().equalsIgnoreCase("no")){
                             System.out.println("Re-enter the amount...");
                             continue;
                         }else if(input.nextLine().strip().equalsIgnoreCase("yes")){
                             Account newAcc = new FixedAccount(name,id,amount);
                             accountDao.create(newAcc);
//                             Users.get(id).addAccount(newAcc);
                             newAcc.details();
                             break;
                         }else {
                             System.out.println("Invalid Input try Again");
                             continue;
                         }
                     }else {
                         System.out.println("Minimum deposit for a Fixed account is 100000.00");
                     }
                     break;
                 }return;

             }
             case "child":{
                 System.out.println("Enter the child Name :");
                 String c_name = input.nextLine();
                 while(true){
                     System.out.println("Initial deposit amount : ");
                     double amount = input.nextDouble();
                     input.nextLine();
                     if (amount>0){
                         System.out.printf("Amount is %.2f Confirm ? (yes/no)",amount);
                         if (input.nextLine().strip().equalsIgnoreCase("no")){
                             System.out.println("Re-enter the amount...");
                             continue;
                         }else if(input.nextLine().strip().equalsIgnoreCase("yes")){
                             Account newAcc = new ChildAccount(c_name,name,id,amount);
                             accountDao.create(newAcc);
//                             Users.get(id).addAccount(newAcc);
                             newAcc.details();
                             break;
                         }else {
                             System.out.println("Invalid Input try Again");
                             continue;
                         }
                     }else {
                         System.out.println("Invalid  amount....");
                     }
                     break;
                 }return;
             }
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

    public static  void userLoggedIn(Long id , String name){
        do {

            Users.get(id).setAccounts(userDao.getUsersAccounts(id));
            System.out.printf("%n1.Check your accounts%n2.Create and account%n3.Update Name%n4.Delete an account%n5.Delete user%n0.Exit%n");
            int choice = input.nextInt();
            input.nextLine();


            switch (choice) {
                case 1: {
                    Users.get(id).showAccounts();
                    System.out.println("Do you wish to perform another service ? (Y/N)");
                    String cont = input.nextLine().strip().toLowerCase();
                    if (cont.equals("n")) {
                        System.out.println("Logging you out!! Thank you see you again!");
                        return;
                    }
                    break;
                }
                case 2: {
                    accountCreation(name,id);
                    System.out.println("Do you wish to perform another service ? (Y/N)");
                    String cont = input.nextLine().strip().toLowerCase();
                    if (cont.equals("n")) {
                        System.out.println("Logging you out!! Thank you see you again!");
                        return;
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter the new name of the account");
                    name = input.nextLine();
                    if(name!=null){
                        Users.get(id).setName(name);
                        userDao.update(Users.get(id));
                        System.out.println("Name Successfully changed ");

                    }
                    System.out.println("Do you wish to perform another service ? (Y/N)");
                    String cont = input.nextLine().strip().toLowerCase();
                    if (cont.equals("n")) {
                        System.out.println("Logging you out!! Thank you see you again!");
                        return;
                    }
                    break;
                }
                case 4: {
                    try{
                        System.out.println("Enter the Account number");
                        Account temp = Accounts.get(input.nextInt());
                        input.nextLine();
                        temp.details();
                        System.out.println("Do you wish to delete this account? (yes/no)");

                        String dConf = input.nextLine().strip();

                        if( dConf.equalsIgnoreCase("yes") && (temp.balance() == 0.0)){
                            accountDao.delete(temp.getAccNo());
                            System.out.println("Account successfully deleted");
                        }else if (dConf.equalsIgnoreCase("no")){
                            break;
                        }else {
                            System.out.println("Invalid Account balance or invalid input");
                        }
                        System.out.println("Do you wish to perform another service ? (Y/N)");
                        String cont = input.nextLine().strip().toLowerCase();
                        if (cont.equals("n")) {
                            System.out.println("Logging you out!! Thank you see you again!");
                            return;
                        }
                        break;

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case 5: {
                    System.out.println("Do you wish to delete this user (Yes/No) NOTE : ALl the funds will be lost if the accounts are not handled before deleting ");
                    System.out.println(Users.get(id));
                    String dConf = input.nextLine().strip();
                    if (dConf.equalsIgnoreCase("yes")){
                        try {
                            userDao.delete(id);
                            System.out.println("Thank you for using our service");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }else if(dConf.equalsIgnoreCase("no")) {
                        System.out.println("Do you wish to perform another service ? (Y/N)");
                        String cont = input.nextLine().strip().toLowerCase();
                        if (cont.equals("n")) {
                            System.out.println("Logging you out!! Thank you see you again!");
                            return;
                        }
                        break;
                    }else {
                        System.out.println("Invalid Input");
                    }
                }
                default: {
                    return;
                }
            }

        }while (true);
    }

    public static PasswordRecord hashCred(String rawPass ){
        String hashed;
        String salt;

        Argon2Function argon2Function = Argon2Function.getInstance(8,1,1,32, Argon2.ID);

        Hash hash = Password.hash(rawPass).with(argon2Function);

        hashed = hash.getResult();
        salt = hash.getSalt();
        return new PasswordRecord(hashed,salt);

    }
}
