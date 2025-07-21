package data.dao;

import data.util.DatabaseUtils;
import entity.User;
import entity.records.AccountRecord;
import entity.records.PasswordRecord;
import enums.ACCTYPE;
import enums.GENDER;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class UserDao implements Dao<User,Long>{

    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());
    private static final String GET_ALL = "select national_id,name,age,gender from public.user";
    private static final String GET_ONE = "select national_id,name,age,gender from public.user where national_id = ?";
    private static final String CREATE = "insert into public.user (national_id,name,age,gender) values(?,?,?,?::\"GENDER\")";
    private static final String DELETE = "delete from public.user where national_id = ?";
    private static final String UPDATE = "update public.user set name=? where national_id = ?";
    private static final String GET_USERS_ACCOUNTS = "select u.national_id , u.name , a.acc_no , a.account_type , a.name as acc_name from public.user as u  \n" +
            "inner join public.account as a on  u.national_id = a.national_id where u.national_id = ? ";
    private static final String HASHING = "insert into public.user hash,salt values(?,?) where national_id = ?";
    private static final String RETRIEVE = "select from public.user hash,salt  where national_id = ?";

    @Override
    public Map<Long, User> getAll() {
        Map<Long,User> users = new HashMap<>();
        Connection connection = DatabaseUtils.getConnection();

        try(Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(GET_ALL);
            users = this.processResultset(rs);
        } catch (SQLException e) {
            DatabaseUtils.handleSQLexception("UserDao.getAll",e,LOGGER);
        }

        return users;
    }

    @Override
    public User create(User entity) {
        Connection connection = DatabaseUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setLong(1,entity.getNationalId());
            statement.setString(2, entity.getName());
            statement.setInt(3,entity.getAge());
            statement.setString(4,entity.getSex());
            statement.execute();
            connection.commit();
            statement.close();
            System.out.println("User successfully Created");
        }catch (SQLException e){
            try {
                connection.rollback();
            }catch (SQLException sqle){
                DatabaseUtils.handleSQLexception("UserDao.create.rollback",sqle,LOGGER);
            }
            DatabaseUtils.handleSQLexception("UserDao.create",e,LOGGER);
        }
        return null;
    }

    @Override
    public Optional<User> getOne(Long nationalId) {

        Connection connection = DatabaseUtils.getConnection();
        Map<Long,User> users;

        try(PreparedStatement statement = connection.prepareStatement(GET_ONE)){
            statement.setLong(1,nationalId);
            ResultSet rs = statement.executeQuery();

            users = this.processResultset(rs);
            if(users.isEmpty()){
                return Optional.empty();
            }
            return Optional.of(users.get(nationalId));

        } catch (SQLException e) {
            DatabaseUtils.handleSQLexception("UserDao.getOne",e,LOGGER);
        }
        return Optional.empty();
    }

    @Override
    public User update(User entity) {
        Connection connection = DatabaseUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1,entity.getName());
            statement.setLong(2,entity.getNationalId());
            statement.execute();
            connection.commit();
            statement.close();

        }catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSQLexception("UserDao.update.rollback",sqle,LOGGER);

            }
            DatabaseUtils.handleSQLexception("UserDao.update",e,LOGGER);
        }
        return this.getOne(entity.getNationalId()).get();
    }

    @Override
    public void delete(Long nationalId) {
        Connection connection = DatabaseUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1,nationalId);
            statement.execute();
            connection.commit();
            statement.close();
        }catch (SQLException e){
            try {
               connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSQLexception("UserDao.delete.rollback",sqle,LOGGER);

            }
            DatabaseUtils.handleSQLexception("UserDao.delete",e,LOGGER);
        }
    }


    public List<AccountRecord> getUsersAccounts(Long id){
        List<AccountRecord> accountRecords = new ArrayList<>();
        Connection connection = DatabaseUtils.getConnection();

        try(PreparedStatement statement = connection.prepareStatement(GET_USERS_ACCOUNTS)){
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            accountRecords = this.processAccountSet(rs);
        } catch (SQLException e) {
            DatabaseUtils.handleSQLexception("UserDao.getAll",e,LOGGER);
        }

        return accountRecords;
    }

    public void storePass( String hash , String salt ,Long id){
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(HASHING);
            preparedStatement.setString(1,hash);
            preparedStatement.setString(2,salt);
            preparedStatement.setLong(3,id);
            preparedStatement.execute();
            connection.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                DatabaseUtils.handleSQLexception("UserDao.storePass.rollback", ex, LOGGER);
            }
            DatabaseUtils.handleSQLexception("UserDao.storePass", e, LOGGER);
        }
    }

    public PasswordRecord retrieveHash(Long id ) {
        PasswordRecord passwordRecord = null;
        try (PreparedStatement preparedStatement = DatabaseUtils.getConnection().prepareStatement(RETRIEVE)) {
            preparedStatement.setLong(1,id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                passwordRecord = new PasswordRecord(rs.getString("hash"), rs.getString("salt"));
            }

        } catch (SQLException e) {
            DatabaseUtils.handleSQLexception("UserDao.retrieveHash", e, LOGGER);
        }
        return passwordRecord;
    }

    private List<AccountRecord> processAccountSet(ResultSet rs) throws SQLException {
        List<AccountRecord> accountRecords = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong("national_id");
            if (!rs.wasNull()) {
                AccountRecord account = new AccountRecord(rs.getInt("acc_no"), id, rs.getString("name"), rs.getString("acc_name"), ACCTYPE.valueOf(rs.getString("account_type")));
                accountRecords.add(account);
            }
        }
        return accountRecords;
    }

    private Map<Long,User>processResultset(ResultSet rs) throws SQLException{
        Map<Long,User> users = new HashMap<>();
        while (rs.next()){
            long id = rs.getLong("national_id");
            if(!rs.wasNull()){
            User user = new User(rs.getString("name"),rs.getLong("national_id"),rs.getInt("age"), GENDER.valueOf(rs.getString("gender")));
                users.put(id,user);
            }
        }
        return users;
    }
}

