package data.dao;

import data.util.DatabaseUtils;
import entity.*;
import enums.ACCTYPE;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class AccountDao implements Dao<Account,Integer> {
    private static final Logger  LOGGER = Logger.getLogger(AccountDao.class.getName());
    private static final String  GET_ALL = "select acc_no,national_id,name,balance,account_type,created_at from public.account where account_type IN ('SAVINGS'::\"ACCTYPE\", \n" +
            "        'CURRENT'::\"ACCTYPE\",'FIXED'::\"ACCTYPE\")";
    private static final String  CREATE = "insert into public.account (acc_no,name,national_id, account_type, balance) select ? as acc_no,?,?, ?::\"ACCTYPE\" as account_type,? as balance from public.user u where u.national_id = ?";
    private static final String  GET_ONE = "select acc_no,national_id,name,balance,account_type,created_at from public.account where acc_no =?";
    private static final String  UPDATE = "update public.account set balance = ? where acc_no = ?";
    private static final String  DELETE = "delete from public.account where acc_no = ?";
    private static final String  DEPENDENT_ACC   = "SELECT u.name as g_name,a.national_id,a.name as c_name,a.acc_no as acc_no,a.balance,a.account_type as account_type,a.created_at FROM public.user AS u  \n" +
            "INNER JOIN account AS a ON u.national_id = a.national_id where a.account_type ='CHILD'::\"ACCTYPE\"";//doubtful about the using joining cause result set has an issue then
    private static final String HASHING = "insert into public.account hash,salt values(?,?) where acc_no = ?";

    @Override
    public Map<Integer, Account> getAll() {
        Map<Integer, Account> accounts = new HashMap<>();
        try(Statement statement = data.util.DatabaseUtils.getConnection().createStatement() ; Statement statement2 = data.util.DatabaseUtils.getConnection().createStatement()){
            ResultSet rs = statement.executeQuery(GET_ALL);
            accounts = this.processResultset(rs);
            ResultSet rs2 = statement2.executeQuery(DEPENDENT_ACC);
            accounts.putAll(this.processResultset(rs2));

        } catch (SQLException e) {
            DatabaseUtils.handleSQLexception("AccountDao.getAll",e,LOGGER);
        }
        return accounts;
    }

    @Override
    public Account create(Account entity) {
        Connection connection = data.util.DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setInt(1, entity.getAccNo());
            statement.setString(2,entity.getName());
            statement.setLong(3, entity.getNationalId());
            statement.setString(4, entity.getAccountType());
            statement.setDouble(5, entity.balance());
            statement.setLong(6, entity.getNationalId());
            statement.execute();
            connection.commit();
            statement.close();
            System.out.println("Account successfully created");

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSQLexception("AccountDao.create.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSQLexception("AccountDao.create", e, LOGGER);
        }
        return null;
    }


    @Override
    public Optional<Account> getOne(Integer accNo) {
        Map<Integer,Account> accounts = new HashMap<>();

        try( PreparedStatement statement = data.util.DatabaseUtils.getConnection().prepareStatement(GET_ONE)){
            statement.setInt(1,accNo);
            ResultSet rs = statement.executeQuery();
            accounts = this.processResultset(rs);
            if (accounts.isEmpty()){
                return Optional.empty();
            }
            return  Optional.of(accounts.get(accNo));
        } catch (SQLException e) {
            DatabaseUtils.handleSQLexception("AccountDao.getOne",e,LOGGER);
        }
        return Optional.empty();
    }

    @Override
    public Account update(Account entity) {
        Connection connection = data.util.DatabaseUtils.getConnection();

        try(PreparedStatement statement  = connection.prepareStatement(UPDATE)){
            connection.setAutoCommit(false);
            statement.setDouble(1,entity.balance());
            statement.setInt(2,entity.getAccNo());
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSQLexception("AccountDao.update.rollback",sqle,LOGGER);

            }
            DatabaseUtils.handleSQLexception("AccountDao.update",e,LOGGER);
        }
        return this.getOne(entity.getAccNo()).get();

    }

    @Override
    public void delete(Integer accNo) {
        Connection connection = data.util.DatabaseUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(DELETE)){
            connection.setAutoCommit(false);
            statement.setInt(1,accNo);
            statement.execute();
            connection.commit();
        }catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSQLexception("AccountDao.delete.rollback",sqle,LOGGER);

            }
            DatabaseUtils.handleSQLexception("AccountDao.delete",e,LOGGER);
        }
    }


    public void storePass( String hash , String salt ,Integer accNo){
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(HASHING);
            preparedStatement.setString(1,hash);
            preparedStatement.setString(2,salt);
            preparedStatement.setInt(3,accNo);
            preparedStatement.execute();
            connection.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                DatabaseUtils.handleSQLexception("AccountDao.storePass.rollback", ex, LOGGER);
            }
            DatabaseUtils.handleSQLexception("AccountDao.storePass", e, LOGGER);
        }
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
                        FixedAccount account = new FixedAccount(rs.getInt("acc_no"),rs.getString("name"),rs.getLong("national_id"),rs.getDouble("balance"));
                        account.setCreated_at(rs.getString("created_at"));
                        accounts.put(rs.getInt("acc_no"),account);

                        break;

                    }case ACCTYPE.CURRENT -> {
                        Account account = new CurrentAccount(rs.getInt("acc_no"),rs.getString("name"),rs.getLong("national_id"),rs.getDouble("balance"));
                        accounts.put(rs.getInt("acc_no"),account);
                        break;

                    }case ACCTYPE.CHILD -> {
                        ChildAccount account = new ChildAccount(rs.getInt("acc_no"),rs.getString("c_name"),rs.getString("g_name"),rs.getLong("national_id"),rs.getDouble("balance"));
                        account.setCreated_at(rs.getString("created_at"));
                        accounts.put(rs.getInt("acc_no"),account);
                        break;
                    }
                }
            }
        }
        return accounts;
    }
}
