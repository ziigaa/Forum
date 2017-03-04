/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.daos;

import tikape.objects.Category;
import tikape.objects.Topic;
import tikape.objects.Database;
import tikape.objects.User;
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
public class TopicDao {

    private Database database;

    public TopicDao(Database database) {
        this.database = database;
    }

    public Topic findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Topics WHERE topic_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("topic_id");
        String subject = rs.getString("subject");
        String dateTime = rs.getString("time");

        UserDao uTemp = new UserDao(database);
        User u = uTemp.findOne(Integer.parseInt(rs.getString("user_id")));

        CategoryDao cTemp = new CategoryDao(database);
        Category c = cTemp.findOne(Integer.parseInt(rs.getString("category_id")));

        Topic o = new Topic(id, subject, dateTime, u, c);

        rs.close();

        stmt.close();

        connection.close();

        return o;
    }

    public List<Topic> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Topics");

        ResultSet rs = stmt.executeQuery();
        List<Topic> topics = new ArrayList<>();
        
        while (rs.next()) {
            Integer id = rs.getInt("topic_id");
            String subject = rs.getString("subject");
            String dateTime = rs.getString("time");

            UserDao uTemp = new UserDao(database);
            User u = uTemp.findOne(Integer.parseInt(rs.getString("user_id")));

            CategoryDao cTemp = new CategoryDao(database);
            Category c = cTemp.findOne(Integer.parseInt(rs.getString("category_id")));

            topics.add(new Topic(id, subject, dateTime, u, c));
        }

        rs.close();
        stmt.close();
        connection.close();

        return topics;
    }
    
        public List<Topic> findAllFromCategory(int category) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Topics WHERE category_id = ?");
        stmt.setObject(1, category);
        
        ResultSet rs = stmt.executeQuery();
        List<Topic> topics = new ArrayList<>();
        
        while (rs.next()) {
            Integer id = rs.getInt("topic_id");
            String subject = rs.getString("subject");
            String dateTime = rs.getString("time");

            UserDao uTemp = new UserDao(database);
            User u = uTemp.findOne(Integer.parseInt(rs.getString("user_id")));

            CategoryDao cTemp = new CategoryDao(database);
            Category c = cTemp.findOne(Integer.parseInt(rs.getString("category_id")));

            topics.add(new Topic(id, subject, dateTime, u, c));
        }

        rs.close();
        stmt.close();
        connection.close();

        return topics;
    }

    public void delete(Integer key) throws SQLException {
        database.update("DELETE FROM Topics WHERE topic_id = ?", key);
    }
    
    public void addTopic(String subject, User user, Category category) throws SQLException {
        Integer catId = category.getId();
        Integer userId = user.getId();
        database.update("INSERT INTO \"Topics\" VALUES(null,'" + subject + "', datetime('now'),'" + catId + "','+" + userId + "');");
    }
    public Integer countAmountOfPostsInTopic(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Posts WHERE topic_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();

        int o = 0;
        while (rs.next()) {
            o++;
        }

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }
    public String getRecentPostDateFromTOpic(int topicId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM posts WHERE post_id = (SELECT MAX(post_id) FROM posts WHERE topic_id = ?);");
        stmt.setObject(1, topicId);

        ResultSet rs = stmt.executeQuery();

        String o = "No posts";
        while (rs.next()) {
            o = rs.getString("time");
        }

        

     //   String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }
    
}
