package data.dao;

import entity.Account;
import entity.SavingsAccount;
import entity.User;
import enums.ACCTYPE;
import enums.GENDER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountDao implements Dao<Account,Integer> {
    @Override
    public Map<Integer, Account> getAll() {
        return Map.of();
    }

    @Override
    public Account create(Account entity) {
        return null;
    }

    @Override
    public Optional<Account> getOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Account update(Account entity) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    private Map<Integer,Account>processResultset(ResultSet rs) throws SQLException {
        Map<Integer,Account> accounts = new HashMap<>();
        while (rs.next()){
            ACCTYPE acctype = ACCTYPE.valueOf(rs.getString("account_type"));
            if(!rs.wasNull()){
                switch (acctype){
                    case ACCTYPE.SAVINGS -> {
                        Account account = new SavingsAccount(rs.getInt("acc_no"),rs.getString("name"),rs.getLong("national_id"),rs.getDouble("balance"));
                        accounts.put(rs.getInt("acc_no"),account);
                        break;

                    }case ACCTYPE.FIXED -> {
                        break;

                    }case ACCTYPE.CURRENT -> {
                        break;

                    }case ACCTYPE.CHILD -> {
                        break;
//TODO the other cases of the switch after creating the sub classes of the accounts
                    }
                }
            }
        }
        return accounts;
    }
}
