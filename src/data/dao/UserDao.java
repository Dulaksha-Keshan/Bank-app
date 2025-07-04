package data.dao;

import data.util.DatabaseUtils;
import entity.User;
import enums.GENDER;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class UserDao implements Dao<User,Long>{

    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());
    private static final String GET_ALL = "select national_id,name,age,gender from public.user";
    private static final String GET_ONE = "select national_id,name,age,gender from public.user where national_id = ?";
    private static final String CREATE = "insert into public.user (national_id,name,age,gender) values(?,?,?,?::\"GENDER\")";
    private static final String DELETE = "delete from public.user where national_id = ?";
    private static final String UPDATE = "update public.user set name=? where national_id = ?";

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
        Map<Long,User> users = new HashMap<>();

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
        Connection connection = data.util.DatabaseUtils.getConnection();
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

