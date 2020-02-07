package brelok.workshops;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MyBenefit {
    final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15";
    final static String LOGIN_FORM_URL = "https://system.mybenefit.pl/mybenefit/login.html";
    final static String LOGIN_ACTION_URL = "https://system.mybenefit.pl/mybenefit/login/by_email";
    final static String USERNAME = "xxx";
    final static String PASSWORD = "xxx";

    public static void main(String[] args) {


        List<String> headers = new ArrayList<>();
        Path path = Paths.get("/Users/brelok/workspace/myBenefit/headers.txt");

        try {
            Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                    .method(Connection.Method.GET)
                    .userAgent(USER_AGENT)
                    .execute();

            HashMap<String, String> cookies = new HashMap<>(loginForm.cookies());

            HashMap<String, String> formData = new HashMap<>();
            formData.put("captcha", "");
            formData.put("email", USERNAME);
            formData.put("lang", "pl");
            formData.put("password", PASSWORD);

            Connection.Response homePage = Jsoup.connect(LOGIN_ACTION_URL)
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .execute();

            System.out.println(homePage.statusCode());
            System.out.println(homePage.parse());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
