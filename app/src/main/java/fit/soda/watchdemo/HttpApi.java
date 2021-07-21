package fit.soda.watchdemo;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import java.util.Map;

public class HttpApi {
    Gson gson = new Gson();

    public String mock() {
        String response = "empty";
        try {
            response = HttpRequest.get("https://www.baidu.com", null, true).body();
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }
        Utils.log(response);
        return "";
    }

    private <T> T request(String path, Map<String, String> params, Class<T> clz) {
        String base = "http://192.168.8.1:8080";
        String response = null;
        try {
            response = HttpRequest.get(base + path, params, true).body();
            return gson.fromJson(response, clz);
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }
        return null;
    }
}
