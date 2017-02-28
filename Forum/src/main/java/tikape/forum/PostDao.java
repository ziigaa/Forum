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

/**
 *
 * @author Joonas
 */
public class PostDao {

    private Database database;

    public PostDao(Database database) {
        this.database = database;
    }

    public Post findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Posts WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("post_id");
        String message = rs.getString("message");
        String dateTime = rs.getString("time");

        UserDao uTemp = new UserDao(database);
        User u = uTemp.findOne(Integer.parseInt(rs.getString("user_id")));

        TopicDao tTemp = new TopicDao(database);
        Topic t = tTemp.findOne(Integer.parseInt(rs.getString("topic_id")));

        Post o = new Post(id, message, dateTime, u, t);

        rs.close();

        stmt.close();

        connection.close();

        return o;
    }

    public List<Post> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Posts");

        ResultSet rs = stmt.executeQuery();
        List<Post> posts = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("post_id");
            String message = rs.getString("message");
            String dateTime = rs.getString("time");
            
            UserDao uTemp = new UserDao(database);
            User u = uTemp.findOne(Integer.parseInt(rs.getString("user_id")));

            TopicDao tTemp = new TopicDao(database);
            Topic t = tTemp.findOne(Integer.parseInt(rs.getString("topic_id")));

            posts.add(new Post(id, message, dateTime, u, t));
        }

        rs.close();
        stmt.close();
        connection.close();

        return posts;
    }

    public void delete(Integer key) throws SQLException {
        database.update("DELETE FROM Posts WHERE post_id = ?", key);
    }

}
