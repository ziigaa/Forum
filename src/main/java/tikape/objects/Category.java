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
public class Category {
    private int postMaara;
    private String recentPost;
    private int topMaara;
    private int id;
    private String name;
    private String description;

    public Category(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.topMaara = 0;
        this.recentPost = "";
    }

    public void setPostMaara(int postMaara) {
        this.postMaara = postMaara;
    }

    public int getPostMaara() {
        return postMaara;
    }

    public void setRecentPost(String recentPost) {
        this.recentPost = recentPost;
    }

    public String getRecentPost() {
        return recentPost;
    }

    public int getTopMaara() {
        return topMaara;
    }

    public void setTopMaara(int topMaara) {
        this.topMaara = topMaara;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
