/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 *
 * @author Joonas
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        
        Database database = new Database("jdbc:sqlite:./lol2.db");
        database.init();
        UserDao userDao = new UserDao(database);
        CategoryDao catDao = new CategoryDao(database);
        TopicDao toDao = new TopicDao(database);
        PostDao poDao = new PostDao(database);
        ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();
        
        post("/", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                HashMap<String, String> data = new HashMap();
                String action1 = req.queryParams("login");
                String action2 = req.queryParams("newuser");
                
                if (action2 == null == false) {
                    res.redirect("/createuser");
                    return new ModelAndView(data, "createuser");
                } else if (action1 == null == false) {
                    res.redirect("/login");
                    return new ModelAndView(data, "login");
                }
                
                return new ModelAndView(data, "index");
            }
        });
        
        get("/", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            return new ModelAndView(data, "index");
        }, engine);
        
        get("/createuser", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            return new ModelAndView(data, "createuser");
        }, engine);
        
        post("/createuser", (req, res) -> {
            
            HashMap<String, String> data = new HashMap();
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            
            User i = userDao.findOneByName(username);
            
            if (i != null) {
                return "User already exists!! HAAHAHAHA NOW U GOTTA WRITE MORE HAHAHAHAHAAH!!!! NOOB!! HAHAHHAAA 8DDDDDDD";
            } else {
                userDao.addUser(username, password);
                res.redirect("/login");
            }
            
            return "";
        });
        
        get("/login", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            return new ModelAndView(data, "login");
        }, engine);
        
        post("/login", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            
            User i = userDao.findOneByName(username);
            
            if (i != null) {
                if (password.equals(i.getPassword())) {
                    data.put("string", i.getName());
                    res.cookie("name", username);
                    res.redirect("/main");
                } else {
                    return "Wrong password HAHAHAHAHAHAHAHAHAHAHA!!!!!!!!!!!!!!!!!!!!!!!111111111111";
                }
            } else {
                return "Lol noob you're a nobody, piss off!! HAHAHAHAHAHAHAHA!!!!!";
            }
            
            return new ModelAndView(data, "main");
        });
        
        get("/main", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            
            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                if (!name.equals("") || name != null) {
                }
            } else {
                res.redirect("/login");
            }
            
            return new ModelAndView(data, "main");
        }, engine);
        
        post("/main", (req, res) -> {
            
            HashMap<String, String> data = new HashMap();
            String catz = req.queryParams("catz");
            
            if (catz == null == false) {
                res.redirect("/categories");
                return new ModelAndView(data, "categories");
            }
            
            return "";
        });
        
        get("/categories", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            
            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                if (!name.equals("") || name != null) {
                    List<Category> list = new ArrayList<>();
                    
                    list.addAll(catDao.findAll());

                    HashMap<String, Object> catz = new HashMap<>();
                    catz.put("categories", list);
                    return new ModelAndView(catz, "categories");
                }
            } else {
                res.redirect("/login");
            }
            return new ModelAndView(data, "categories");
        }, engine);
        
        post("/categories", (req, res) -> {
            
            HashMap<String, String> data = new HashMap();
            String catz = req.queryParams("category");
            
            if (catz == null == false) {
                data.put("category", catz);
                res.redirect("/topics/" + catz);
                return new ModelAndView(data, "topics");
            }
            
            return "";
        });
        
        get("/topics/:cid", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            HashMap<String, String> cat = new HashMap<>();
            
            cat.put("category", req.queryParams("category"));
            
            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                
                if (!name.equals("") || name != null) {
                    List<Topic> list = new ArrayList<>();
                    list.addAll(toDao.findAllFromCategory(Integer.parseInt(req.params(":cid"))));
                    HashMap<String, Object> topz = new HashMap<>();
                    topz.put("topics", list);
                    return new ModelAndView(topz, "topics");
                }
            } else {
                res.redirect("/login");
            }
            return new ModelAndView(data, "topics");
        }, engine);
        
        post("/topics/:cid", (req, res) -> {
            
            HashMap<String, String> data = new HashMap();
            String tpz = req.queryParams("topic");
            String catId = req.params(":cid");
            
            if (tpz == null && req.queryParams("newtopic") != null) {

                String subject = req.queryParams("topictext");
                data.putAll(req.cookies());
                
                if (data.containsKey("name")) {
                    User user = userDao.findOneByName(data.get("name"));
                    Category category = catDao.findOne(Integer.parseInt(catId));
                    toDao.addTopic(subject, user, category);
                    res.redirect("/topics/" + catId);
                }
                return "";
            }
            
            if (tpz == null == false) {
                data.put("topics", tpz);
                res.redirect("/topics/" + catId + "/posts/" + tpz);
                return new ModelAndView(data, "posts");
            }
            
            return "";
        });
        
        get("topics/:cid/posts/:tid", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            HashMap<String, String> topic = new HashMap<>();
            topic.put("topic", req.queryParams("topic"));

            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                
                if (!name.equals("") || name != null) {
                    List<Post> list = new ArrayList<>();
                    list.addAll(poDao.findAllFromTopic(Integer.parseInt(req.params(":tid"))));
                    HashMap<String, Object> pstz = new HashMap<>();
                    pstz.put("posts", list);
                    return new ModelAndView(pstz, "posts");
                }
            } else {
                res.redirect("/login");
            }
            return new ModelAndView(data, "topics");
        }, engine);
        
        post("topics/:cid/posts/:tid", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            
            data.putAll(req.cookies());
            
            String catId = req.params(":cid");
            String topicId = req.params(":tid");
            
            if (catId == null || topicId == null) {
                res.redirect("createtopic/:cid");
                return "";
            }
            
            String message = req.queryParams("posttext");
            
            if (data.containsKey("name") && message != null) {
                String name = data.get("name");
                User user = userDao.findOneByName(name);
                Topic topic = toDao.findOne(Integer.parseInt(topicId));
                poDao.addPost(message, user, topic);
                res.redirect("/topics/" + catId + "/posts/" + topicId);
            }
            
            return "";
        });
    }
}