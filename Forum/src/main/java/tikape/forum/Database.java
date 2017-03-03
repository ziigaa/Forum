/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.forum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();
        System.out.println(lauseet);
        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Categories(cat_id INTEGER PRIMARY KEY NOT NULL, name text NOT NULL, description text NOT NULL);");
        lista.add("CREATE TABLE Users (user_id INTEGER PRIMARY KEY NOT NULL, name text NOT NULL, password text NOT NULL);");
        lista.add("CREATE TABLE Topics (topic_id INTEGER PRIMARY KEY NOT NULL, subject text NOT NULL, time date NOT NULL, category_id INTEGER NOT NULL, user_id INTEGER NOT NULL, FOREIGN KEY (category_id) REFERENCES Categories(cat_id), FOREIGN KEY (user) REFERENCES Users(user_id));");
        lista.add("CREATE TABLE Posts(post_id INTEGER PRIMARY KEY NOT NULL, message text NOT NULL, time date NOT NULL, topic INTEGER NOT NULL, user_id INTEGER NOT NULL, FOREIGN KEY (topic) REFERENCES Topics(topic_id), FOREIGN KEY (user_id) REFERENCES Users(user_id));");

        lista.add("INSERT INTO \"Categories\" VALUES(1,'LOL NOOB!','Trolls in Training');");
        lista.add("INSERT INTO \"Categories\" VALUES(2,'Ronald Rump','Troll Rookies');");
        lista.add("INSERT INTO \"Categories\" VALUES(3,'TRIGGERED','Troll Journeymen');");
        lista.add("INSERT INTO \"Categories\" VALUES(4,'Chernoby kids','Troll Expert');");
        lista.add("INSERT INTO \"Users\" VALUES(1,'Admin','lol');");
        lista.add("INSERT INTO \"Topics\" VALUES(1,'TROLL-O-LOL','2004-08-19 18:51:06',1,1);");
        lista.add("INSERT INTO \"Posts\" VALUES(1,'hahahaha gg ez nubz lol','2017-02-27 21:54:33',1,1);");

        return lista;
    }

    public int update(String updateQuery, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(updateQuery);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        int changes = stmt.executeUpdate();

        stmt.close();
        conn.close();

        return changes;
    }
}
