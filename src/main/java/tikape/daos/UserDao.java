/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.daos;

import tikape.objects.Database;
import tikape.objects.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private Database database;

    public UserDao(Database database) {
        this.database = database;
    }

    public User findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Users WHERE user_id = ?");
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

        public User findOneByName(String key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Users WHERE name = ?");
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

    public void delete(Integer key) throws SQLException {
        database.update("DELETE FROM Users WHERE user_id = ?", key);
    }

    public void addUser(String name, String password) throws SQLException {
        database.update("INSERT INTO \"Users\" VALUES(null,'" + name + "','" + password + "');");
    }
}
