/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.objects;

/**
 *
 * @author Joonas
 */
public class Post {

    private int id;
    private String message;
    private String time;
    private User user;
    private Topic topic;

    public Post(Integer id, String message, String time, User user, Topic topic) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.user = user;
        this.topic = topic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String dateTime) {
        this.time = dateTime;
    }
    
        public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

        public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }


}
