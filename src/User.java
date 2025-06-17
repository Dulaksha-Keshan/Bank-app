import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String name ;
    private int nationalId ;
    private int age ;
    private GENDER sex ;
    private List<Account> accounts = new ArrayList<>();


    public User(String name, Integer nationalId, int age, GENDER sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;

            if (age>=18){
                if(nationalId == null){
                    throw new IllegalArgumentException("National ID required For the User ");
                }
                this.nationalId = nationalId;
                }
    }

    public void addAcount(Account acc) {
        accounts.add(acc);
    }

    public void showAccounts (){
        if(accounts.isEmpty()){
            System.out.println("No accounts under this user");
        }
        for(Account a:accounts){
            if(a!= null){
                a.details();
            }
        }
    }

}


