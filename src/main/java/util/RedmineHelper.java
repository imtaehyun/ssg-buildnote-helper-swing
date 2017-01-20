package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import constant.GlobalEnv;
import model.Author;
import model.Issue;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by 140179 on 2015-06-09.
 */
public class RedmineHelper {
    private static String id = "";
    private static String pwd = "";

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        RedmineHelper.id = id;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(String pwd) {
        RedmineHelper.pwd = pwd;
    }

    public static User login(String id, String pwd) {
        User user = getCurrentUser(id, pwd);
        System.out.println(user.toString());

        if (user == null) {
            return null;
        }

        setId(id);
        setPwd(pwd);
        return user;
    }

    private static User getCurrentUser(String id, String pwd) {
        String url = "http://redmine.ssgadm.com/redmine/users/current.json";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .basicAuth(id, pwd)
                    .asJson();

            if (jsonResponse.getStatus() != 200) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(jsonResponse.getBody().getObject().getJSONObject("user").toString(), User.class);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User getCurrentUser() {
        return getCurrentUser(id, pwd);
    }

    /*public static ArrayList<Author> getAuthorList() {
        String urlForDeveloper = "http://redmine.ssgadm.com/redmine/groups/1102.json?include=users";
        String urlForPlanner = "http://redmine.ssgadm.com/redmine/groups/1591.json?include=users";

        ArrayList<Author> authorList = new ArrayList<Author>();
        try {
            // 개발자목록 가져오기
            HttpResponse<JsonNode> jsonResponse = Unirest.get(urlForDeveloper)
                    .basicAuth(id, pwd)
                    .asJson();

            JSONArray arr = jsonResponse.getBody().getObject().getJSONObject("group").getJSONArray("users");
            for (int i=0; i < arr.length(); i++) {
                authorList.add(new Author(Integer.parseInt(arr.getJSONObject(i).get("id").toString()), arr.getJSONObject(i).get("name").toString()));
            }

            // 기획자 목록 가져오기
            jsonResponse = Unirest.get(urlForPlanner)
                    .basicAuth(id, pwd)
                    .asJson();

            arr = jsonResponse.getBody().getObject().getJSONObject("group").getJSONArray("users");
            for (int i=0; i < arr.length(); i++) {
                authorList.add(new Author(Integer.parseInt(arr.getJSONObject(i).get("id").toString()), arr.getJSONObject(i).get("name").toString()));
            }

            return authorList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static ArrayList<Author> getAuthorList() {
        ArrayList<Author> authorList = new ArrayList<Author>();
        try {
            // 기획자 목록 가져오기
            JSONObject jsonObject = new JSONObject(GlobalEnv.authorPlannerJson);

            JSONArray arr = jsonObject.getJSONObject("group").getJSONArray("users");
            for (int i=0; i < arr.length(); i++) {
                authorList.add(new Author(Integer.parseInt(arr.getJSONObject(i).get("id").toString()), arr.getJSONObject(i).get("name").toString()));
            }

            // 개발자목록 가져오기
            jsonObject = new JSONObject(GlobalEnv.authorDeveloperJson);

            arr = jsonObject.getJSONObject("group").getJSONArray("users");
            for (int i=0; i < arr.length(); i++) {
                authorList.add(new Author(Integer.parseInt(arr.getJSONObject(i).get("id").toString()), arr.getJSONObject(i).get("name").toString()));
            }

            return authorList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Issue getIssue(String issueNo) {
        String url = "http://redmine.ssgadm.com/redmine/issues/" + issueNo + ".json";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .basicAuth(id, pwd)
                    .asJson();

//            System.out.println(jsonResponse.getStatus());
//            System.out.println(jsonResponse.getStatusText());
//            System.out.println(jsonResponse.getHeaders());
//            System.out.println(jsonResponse.getBody());
//            System.out.println(jsonResponse.getRawBody());

            if (jsonResponse.getStatus() != 200) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            Issue issue = mapper.readValue(jsonResponse.getBody().getObject().getJSONObject("issue").toString(), Issue.class);
            return issue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Issue registerBuildnote(String requestBody) {
        String url = "https://redmine.ssgadm.com/redmine/projects/project-0103/issues.json";

        try {

            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .basicAuth(id, pwd)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asJson();
            System.out.println(jsonResponse.getStatus());
            System.out.println(jsonResponse.getStatusText());
            System.out.println(jsonResponse.getHeaders());
            System.out.println(jsonResponse.getBody());
            System.out.println(jsonResponse.getRawBody());

            if (jsonResponse.getStatus() != 201) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            Issue issue = mapper.readValue(jsonResponse.getBody().getObject().getJSONObject("issue").toString(), Issue.class);
            return issue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
