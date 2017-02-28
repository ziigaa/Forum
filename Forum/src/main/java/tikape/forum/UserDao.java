/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.forum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.forum.User;

public class UserDao implements Dao<User, Integer> {

    private Database database;

    public UserDao(Database database) {
        this.database = database;
    }

    @Override
    public User findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Users WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("user_id");
        String name = rs.getString("name");
        String password = rs.getString("password");

        User o = new User(id, name, password);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<User> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Users");

        ResultSet rs = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("user_id");
            String name = rs.getString("name");
            String password = rs.getString("password");
            
            users.add(new User(id, name, password));
        }

        rs.close();
        stmt.close();
        connection.close();

        return users;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
}
