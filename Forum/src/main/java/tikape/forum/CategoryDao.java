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
public class CategoryDao {

    private Database database;

    public CategoryDao(Database database) {
        this.database = database;
    }

    public Category findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Categories WHERE id = ?");
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

}
