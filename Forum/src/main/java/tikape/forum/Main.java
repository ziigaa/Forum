/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.forum;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 *
 * @author Joonas <>
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();
        
        post("/", (req, res) -> {
            HashMap<String, String> data = new HashMap();
           

            return new ModelAndView(data, "login");
        });

        get("/", (req, res) -> {
            HashMap<String, String> data = new HashMap();

            return new ModelAndView(data, "index");
        }, engine);

        get("/login", (req, res) -> {
            HashMap<String, String> data = new HashMap();

            return new ModelAndView(data, "login");
        }, engine);

        post("/login", (req, res) -> {
            //System.out.println(req.toString());
            HashMap<String, String> data = new HashMap();
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            System.out.println(username + " " + password);
            //System.out.println(request.body());
            //System.out.println(userResponse.location);
            //response.redirect("/locationAccepted");
            return new ModelAndView(data, "login");
        });

//        get("/salaisuus", (req, res) -> "Kryptos");
//        get("/path", (req, res) -> "Polku (path)");
//        get("/route", (req, res) -> "Polku (route)");
//        get("/trail", (req, res) -> "Polku (trail)");
    }

}
