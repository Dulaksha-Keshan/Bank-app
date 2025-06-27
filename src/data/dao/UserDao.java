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
    private static final String CREATE = "insert into public.user (national_id,name,age,gender) values(?,?,?,?)";

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

        try(PreparedStatement statement = connection.prepareStatement(GET_ONE)){
            statement.setLong(1,nationalId);
            ResultSet rs = statement.executeQuery();
            Map<Long,User> users = new HashMap<>(this.processResultset(rs));
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
        return null;
    }

    @Override
    public void delete(Long nationalId) {

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
