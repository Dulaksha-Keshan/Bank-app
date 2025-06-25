package data.dao;

import entity.User;
import enums.GENDER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDao implements Dao<User,Long>{
    @Override
    public Map<Long, User> getAll() {
        return Map.of();
    }

    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public Optional<User> getOne(Long nationalId) {
        return Optional.empty();
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void delete(Long nationalId) {

    }

    public Map<Long,User>processResultset(ResultSet rs) throws SQLException{
        Map<Long,User> users = new HashMap<>();
        while (rs.next()){
            long id = rs.getLong("national_Id");
            if(!rs.wasNull()){
            User user = new User(rs.getString("name"),rs.getLong("national_Id"),rs.getInt("age"), GENDER.valueOf(rs.getString("gender")));
                users.put(id,user);
            }
        }
        return users;
    }
}
