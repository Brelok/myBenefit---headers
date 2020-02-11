package brelok.workshops;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.json.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;




public class MyBenefit {
    final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15";
    final static String LOGIN_FORM_URL = "https://system.mybenefit.pl/mybenefit/login.html";
    final static String LOGIN_ACTION_URL = "https://system.mybenefit.pl/mybenefit/login/by_email";
    final static String URL = "https://system.mybenefit.pl/mybenefit/benefit-login-dispatcher.html";
    final static String URLQUERY = "https://system.mybenefit.pl/mybenefit/global-search.html?query=";
    final static String USERNAME = "xxx";
    final static String PASSWORD = "xxx";

    public static void main(String[] args) {

        try {
            Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                    .method(Connection.Method.GET)
                    .userAgent(USER_AGENT)
                    .execute();

            JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(Collections.EMPTY_MAP);
            JsonObject jsonObject = jsonBuilderFactory.createObjectBuilder()
                    .add("password", PASSWORD)
                    .add("captcha", "")
                    .add("lang", "pl")
                    .add("email", USERNAME).build();


            HashMap<String, String> cookies = new HashMap<>(loginForm.cookies());

            Connection.Response homePage = Jsoup.connect(LOGIN_ACTION_URL)
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .requestBody(jsonObject.toString())
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .execute();

            //here I get cookie after logg
            String sessionID = homePage.cookie("BENEFIT_SESSIONID");

            //this connection is working
            Connection.Response afterLogg = Jsoup.connect(URL)
                    .cookie("BENEFIT_SESSIONID", sessionID)
                    .execute();

            Document document = afterLogg.parse();

            //this connection is NOT workingg good, I am not logged on website
            Connection.Response test = Jsoup.connect(URLQUERY)
                    .cookie("BENEFIT_SESSIONID", sessionID)
                    .execute();

            System.out.println(test.parse());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
