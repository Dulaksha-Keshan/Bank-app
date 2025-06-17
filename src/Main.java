import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    Scanner input = new Scanner(System.in);
    Map <Integer,User> Users = new HashMap<>();
    Map<Integer,Account> Accounts = new HashMap<>();


    public static void main(String[] args) {

    }

    public void registration (){
        System.out.println("Enter the Full Name :");
        String name = input.nextLine();

        System.out.println("Enter the Age :");
        int age = input.nextInt();
        input.nextLine();

        System.out.println("Enter the National ID No :");
        Integer id = input.nextInt();
        input.nextLine();

        System.out.println("Enter the Sex(MALE/FEMALE) :");
        GENDER sex = GENDER.valueOf(input.nextLine().toUpperCase());

        User newUser  = new User(name,id,age,sex);
        Users.put(id,newUser);
        accountCreation(name,id);
    }

     public void accountCreation (String name , Integer id ){
         System.out.println("Chose the account type to create (Savings/Current/Fixed/Child) : ");
         String ans = input.nextLine();

         switch (ans.toLowerCase()){
             case "savings" ->{
                 while(true){
                     System.out.println("Initial deposit amount : ");
                     double amount = input.nextDouble();
                     if (amount>0){
                         Account newAcc = new SavingsAccount(name,id,amount);
                         Accounts.put(newAcc.getAccNo(), newAcc);
                         Users.get(id).addAcount(newAcc);
                         newAcc.details();
                         break;
                     }
                 }

             }
             case "current"->{}//to be implemented after current account class
             case "fixed"->{}//to be implemented after fixed account class
             case "child"->{}//to be implemented after child account class
             default -> System.out.println("Invalid input");
         }
     }


}