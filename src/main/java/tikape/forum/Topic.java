/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.forum;

/**
 *
 * @author Joonas
 */
public class Topic {
    private String recentPost;
    private int postMaara;
    private int id;
    private String subject;
    private String dateTime;
    private User user;
    private Category category;

    public Topic(Integer id, String subject, String dateTime, User user, Category category) {
        this.id = id;
        this.subject = subject;
        this.dateTime = dateTime;
        this.user = user;
        this.category = category;
        this.postMaara = 0;
        this.recentPost = "No post";
    }

    public Integer getId() {
        return id;
    }

    public void setRecentPost(String recentPost) {
        this.recentPost = recentPost;
    }

    public void setPostMaara(int postMaara) {
        this.postMaara = postMaara;
    }

    public String getRecentPost() {
        return recentPost;
    }

    public int getPostMaara() {
        return postMaara;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
