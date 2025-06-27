package entity;

import enums.GENDER;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name ;
    private long nationalId ;
    private int age ;
    private GENDER sex ;
    private List<Account> accounts = new ArrayList<>();


    public User(String name, Long nationalId, int age, GENDER sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;

            if (age>=18){
                if(nationalId == null){
                    throw new IllegalArgumentException("National ID required For the entity.User ");
                }
                this.nationalId = nationalId;
                }
    }

    public void addAccount(Account acc) {
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

    public String getName() {
        return name;
    }

    public long getNationalId() {
        return nationalId;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex.name();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", nationalId=" + nationalId +
                ", age=" + age +
                ", sex=" + sex +
                ", accounts=" + accounts +
                '}';
    }
}


