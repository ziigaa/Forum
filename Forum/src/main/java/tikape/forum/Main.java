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

        //Class.forName("org.sqlite.JDBC");
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
                    System.out.println("createuser!");
                    res.redirect("/createuser");
                    return new ModelAndView(data, "createuser");
                } else if (action1 == null == false) {
                    System.out.println("login!");
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
            System.out.println(username + " " + password);
            
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
            System.out.println(username + " " + password);
            
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
                    System.out.println("login success");
                    //return "Welcome " + name + ", u look especially noobish today, haha.";
                }
            } else {
                System.out.println("no login");
                res.redirect("/login");
            }
            
            return new ModelAndView(data, "main");
        }, engine);
        
        post("/main", (req, res) -> {
            
            HashMap<String, String> data = new HashMap();
            String catz = req.queryParams("catz");
            
            if (catz == null == false) {
                System.out.println("catz!");
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
                    System.out.println("still logged in @categories");
                    
                    List<Category> list = new ArrayList<>();
                    
                    list.addAll(catDao.findAll());
                    
                    for (Category c : list) {
                        System.out.println(c.getName());
                    }
                    
                    HashMap<String, Object> catz = new HashMap<>();
                    catz.put("categories", list);
                    System.out.println("right place");
                    return new ModelAndView(catz, "categories");
                }
            } else {
                System.out.println("no login");
                res.redirect("/login");
            }
            System.out.println("goeeesss herelol");
            return new ModelAndView(data, "categories");
        }, engine);
        
        post("/categories", (req, res) -> {
            
            HashMap<String, String> data = new HashMap();
            System.out.println("goes to post");
            
            String catz = req.queryParams("category");
            
            System.out.println("catz string: " + catz);
            
            if (catz == null == false) {
                System.out.println(catz);
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
            
            System.out.println(req.params(":cid"));
            
            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                if (!name.equals("") || name != null) {
                    System.out.println("still logged in @topics");
                    
                    List<Topic> list = new ArrayList<>();
                    
                    list.addAll(toDao.findAllFromCategory(Integer.parseInt(req.params(":cid"))));
                    
                    for (Topic t : list) {
                        System.out.println(t.getSubject());
                    }
                    //ArrayList<String> namesList = new ArrayList<>();
                    HashMap<String, Object> topz = new HashMap<>();
                    topz.put("topics", list);
                    System.out.println("right place@topics");
                    return new ModelAndView(topz, "topics");
                }
            } else {
                System.out.println("no login");
                res.redirect("/login");
            }
            System.out.println("goeeesss herelol");
            return new ModelAndView(data, "topics");
        }, engine);
        
        post("/topics/:cid", (req, res) -> {
            
            HashMap<String, String> data = new HashMap();
            System.out.println("goes to post@topicsID");
            
            String tpz = req.queryParams("topic");
            
            String catId = req.params(":cid");
            
            if (tpz == null && req.queryParams("newtopic") != null) {
                //res.redirect("createtopic/:cid");
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
            
            System.out.println("tpz string: " + tpz);
            
            if (tpz == null == false) {
                System.out.println(tpz);
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
            
            System.out.println(req.params(":cid"));
            
            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                if (!name.equals("") || name != null) {
                    System.out.println("still logged in @posts");
                    
                    List<Post> list = new ArrayList<>();
                    
                    list.addAll(poDao.findAllFromTopic(Integer.parseInt(req.params(":cid"))));
                    
                    for (Post p : list) {
                        System.out.println(p.getMessage());
                    }
                    //ArrayList<String> namesList = new ArrayList<>();
                    HashMap<String, Object> pstz = new HashMap<>();
                    pstz.put("posts", list);
                    System.out.println("right place@posts");
                    return new ModelAndView(pstz, "posts");
                }
            } else {
                System.out.println("no login");
                res.redirect("/login");
            }
            System.out.println("goeeesss herelol");
            return new ModelAndView(data, "topics");
        }, engine);
        
        post("topics/:cid/posts/:tid", (req, res) -> {
            //System.out.println(req.toString());
            HashMap<String, String> data = new HashMap();
            System.out.println("goes to post@createtopic");
            
            data.putAll(req.cookies());
            
            String catId = req.params(":cid");
            String topicId = req.params(":tid");
            
            if (catId == null || topicId == null) {
                res.redirect("createtopic/:cid");
                return "";
            }
            
            System.out.println("catId string: " + catId);
            System.out.println("topicId string: " + topicId);
            
            if (data.containsKey("name")) {
                String name = data.get("name");
                int cateId = Integer.parseInt(req.params(":cid"));
                User user = userDao.findOneByName(name);
                Category category = catDao.findOne(cateId);
                //toDao.addTopic(subject, user, category);
            }
            
            return "";
        });
        
        get("/createtopic/:cid", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            
            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                if (!name.equals("") || name != null) {
                    System.out.println("logged in@createtopic");
                    
                }
            } else {
                System.out.println("no login");
                res.redirect("/login");
            }
            
            return new ModelAndView(data, "createtopic");
        }, engine);
        
        post("/createtopic/:cid", (req, res) -> {
            //System.out.println(req.toString());
            HashMap<String, String> data = new HashMap();
            System.out.println("goes to post@createtopic");
            
            data.putAll(req.cookies());
            
            String subject = req.queryParams("subject");
            String message = req.queryParams("post");
            
            if (message == null || subject == null) {
                res.redirect("createtopic/:cid");
                return "";
            }
            
            System.out.println("message string: " + message);
            System.out.println("subject string: " + subject);
            
            if (data.containsKey("name")) {
                String name = data.get("name");
                int catId = Integer.parseInt(req.params(":cid"));
                User user = userDao.findOneByName(name);
                Category category = catDao.findOne(catId);
                toDao.addTopic(subject, user, category);
            }
            
            return "";
        });
    }
}
