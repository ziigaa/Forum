/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.daos;

import tikape.objects.Category;
import tikape.objects.Topic;
import tikape.objects.Database;
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
public class CategoryDao {

    private Database database;

    public CategoryDao(Database database) {
        this.database = database;
    }

    public Category findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Categories WHERE cat_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("cat_id");
        String name = rs.getString("name");
        String desc = rs.getString("description");

        Category o = new Category(id, name, desc);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    public List<Category> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Categories");

        ResultSet rs = stmt.executeQuery();
        List<Category> cats = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("cat_id");
            String name = rs.getString("name");
            String desc = rs.getString("description");

            cats.add(new Category(id, name, desc));
        }

        rs.close();
        stmt.close();
        connection.close();

        return cats;
    }
    

    public void delete(Integer key) throws SQLException {
        database.update("DELETE FROM Categories WHERE cat_id = ?", key);
    }
    public Integer countAmountOfTopicsInCat(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Topics WHERE category_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        List<Topic> topics = new ArrayList<>();
        int o = 0;
        while (rs.next()) {
            o++;            
        }

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }
    public String getRecentPostDateFromCategory(int catId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT MAX(time) FROM (SELECT * FROM posts, topics WHERE category_id = ? AND Topics.topic_id = posts.topic_id);");
        stmt.setObject(1, catId);

        ResultSet rs = stmt.executeQuery();

        String o = "No posts";
        while (rs.next()) {
            o = rs.getString("MAX(time)");
        }

        

     //   String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }
    public Integer countAllWihinCat(int catId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("select post_id from posts, topics where category_id = ? AND Topics.topic_id = posts.topic_id;");
        stmt.setObject(1, catId);
        ResultSet rs = stmt.executeQuery();
        List<Integer> posts = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("post_id");
            

            posts.add(id);
        }
        int o = posts.size();
        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

}
