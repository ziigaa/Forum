/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.forum;

import tikape.daos.TopicDao;
import tikape.daos.UserDao;
import tikape.daos.CategoryDao;
import tikape.daos.PostDao;
import tikape.objects.Post;
import tikape.objects.Category;
import tikape.objects.Topic;
import tikape.objects.Database;
import tikape.objects.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 *
 * @author Joonas
 */
public class Main {

    public static void main(String[] args) throws Exception {

        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        Spark.staticFileLocation("/public");

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

        HashMap<String, Object> createUserData = new HashMap<>();

        get("/createuser", (req, res) -> {
            HashMap<String, Object> data = new HashMap();

            try {
                data.put("eMessage", createUserData.get("eMessage"));
                createUserData.clear();
            } catch (Exception e) {
            }

            return new ModelAndView(data, "createuser");
        }, engine);

        post("/createuser", (req, res) -> {
            HashMap<String, Object> eData = new HashMap();
            ArrayList<String> erska = new ArrayList<>();
            HashMap<String, String> data = new HashMap();
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            eData.clear();
            erska.clear();
            
            User i = userDao.findOneByName(username);

            if (i != null) {
                erska.add("User already exists");
                createUserData.put("eMessage", erska);
                res.redirect("/createuser");
                //return "User already exists!! HAAHAHAHA NOW U GOTTA WRITE MORE HAHAHAHAHAAH!!!! NOOB!! HAHAHHAAA 8DDDDDDD";
            } else {
                userDao.addUser(username, password);
                res.redirect("/login");
            }

            return "";
        });

        HashMap<String, Object> loginData = new HashMap<>();

        get("/login", (req, res) -> {
            HashMap<String, Object> data = new HashMap<>();
            try {
                data.put("eMessage", loginData.get("eMessage"));
                loginData.clear();
            } catch (Exception e) {
            }
            return new ModelAndView(data, "login");
        }, engine);

        post("/login", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            HashMap<String, Object> eData = new HashMap();
            ArrayList<String> erska = new ArrayList<>();

            eData.clear();
            erska.clear();

            User i = userDao.findOneByName(username);

            if (i != null) {
                if (password.equals(i.getPassword())) {
                    data.put("string", i.getName());
                    res.cookie("name", username);
                    res.redirect("/main");
                } else {
                    erska.add("Wrong Password");
                    loginData.put("eMessage", erska);
                    res.redirect("/login");
                }
            } else {
                erska.add("User does not exist");
                loginData.put("eMessage", erska);
                res.redirect("/login");
            }

            return new ModelAndView(data, "main");
        });

        get("/main", (req, res) -> {
            HashMap<String, String> data = new HashMap();
            data.putAll(req.cookies());
            if (data.containsKey("name")) {
                String name = data.get("name");
                if (name.equals("") || name == null) {
                    res.redirect("/login");
                }
            }
            return new ModelAndView(data, "main");
        }, engine);

        post("/main", (req, res) -> {

            HashMap<String, String> data = new HashMap();
            String catz = req.queryParams("catz");
            String logout = req.queryParams("logout");

            if (catz != null) {
                res.redirect("/categories");
                return new ModelAndView(data, "categories");
            } else if (logout != null) {
                res.cookie("name", null, 0);
                res.redirect("/login");
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
                    //muutos
                    for (Category cat : list) {
                        cat.setTopMaara(catDao.countAmountOfTopicsInCat(cat.getId()));
                        System.out.println(cat.getTopMaara());

                        //täällä!
                        cat.setRecentPost(catDao.getRecentPostDateFromCategory(cat.getId()));
                        cat.setPostMaara(catDao.countAllWihinCat(cat.getId()));

                    }
                    //muustos

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
            String logout = req.queryParams("logout");

            if (catz == null == false) {
                data.put("category", catz);
                res.redirect("/topics/" + catz);
                return new ModelAndView(data, "topics");
            } else if (logout != null) {
                res.cookie("name", null, 0);
                res.redirect("/login");
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
                    for (Topic top : list) {
                        top.setPostMaara(toDao.countAmountOfPostsInTopic(top.getId()));

                        top.setRecentPost(toDao.getRecentPostDateFromTOpic(top.getId()));
                    }
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
            String logout = req.queryParams("logout");

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
            } else if (logout != null) {
                res.cookie("name", null, 0);
                res.redirect("/login");
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
            String logout = req.queryParams("logout");
            data.putAll(req.cookies());

            String catId = req.params(":cid");
            String topicId = req.params(":tid");

            if (catId == null || topicId == null) {
                res.redirect("createtopic/:cid");
                return "";
            } else if (logout != null) {
                res.cookie("name", null, 0);
                res.redirect("/login");
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
