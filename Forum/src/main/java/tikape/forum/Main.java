/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.forum;
import static spark.Spark.*;
/**
 *
 * @author Joonas <>
 */
public class Main {
     public static void main(String[] args) throws Exception {
        get("*", (req, res) -> "Hello World");
//        get("/salaisuus", (req, res) -> "Kryptos");
//        get("/path", (req, res) -> "Polku (path)");
//        get("/route", (req, res) -> "Polku (route)");
//        get("/trail", (req, res) -> "Polku (trail)");
    }
}
