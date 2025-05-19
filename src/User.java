import java.util.Objects;

public class User {
    private String name ;
    private int nationalId ;
    private int age ;
    private GENDER sex ;
    private Account [] accounts;


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
            else {
                this.nationalId = nationalId != null ? nationalId : 0;
            }
            accounts = new Account[5];
    }

    public void showAccounts (){
        if(java.util.Arrays.stream(accounts).allMatch(Objects::isNull)){
            System.out.println("No accounts under this user");
        }
        for(Account a:accounts){
            if(a!= null){
                a.details();
            }
        }
    }

}


