package entity;

import entity.records.AccountRecord;
import enums.GENDER;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name ;
    private long nationalId ;
    private int age ;
    private GENDER sex ;
    private List<AccountRecord> accounts = new ArrayList<>();


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

    public void setName(String name) {
        this.name = name;
    }

    public List<AccountRecord> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountRecord> accounts) {
        this.accounts = accounts;
    }

    public void showAccounts (){
        if(accounts.isEmpty()){
            System.out.println("No accounts under this user");
        }
        for(AccountRecord a:accounts){
            if(a!= null){
                System.out.println(a);
            }
        }
    }

    public String getName() {
        return name;
    }

    public long getNationalId() {
        return this.nationalId;
    }

    public int getAge() {
        return this.age;
    }

    public String getSex() {
        return this.sex.name();
    }

    @Override
    public String toString() {
        return "User : " +
                "name='" + name +
                ", nationalId=" + nationalId +
                ", age=" + age +
                ", sex=" + sex +
                ", accounts=" + accounts
                ;
    }
}


