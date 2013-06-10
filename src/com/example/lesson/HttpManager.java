package com.example.lesson;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * class singleton for manage Http requests
 * form and sed request, get data and parse to JSON
 */
public class HttpManager {
    private static final String TAG = HttpManager.class.getSimpleName();
    private static HttpManager mInstance;
    private DefaultHttpClient mDefaultHttpClient;
    private static final String URL = "http://shagren.stagingmonster.com/calc/";

    private HttpManager() {
        mDefaultHttpClient = new DefaultHttpClient();
    }

    public static HttpManager getInstance() {
        if(mInstance == null) {
            mInstance = new HttpManager();
        }
        return mInstance;
    }
    /**
     * form JSON and send to server
     * @param method
     * @param params
     * @return
     */
    protected JSONObject call(String method, JSONArray params) {
        HttpPost httpPost = new HttpPost(URL);
        JSONObject options = new JSONObject();
        try {
            options.put("jsonrpc", "2.0");
            options.put("id", "123");
            options.put("method", method);
            options.put("params", params);
            Log.d(TAG, "call api request: " + options.toString());
            StringEntity entity = new StringEntity(options.toString(),
                    HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        } catch (Exception e) {
            Log.e(TAG, "Can't add params", e);
        }
        String jsonstring = processHttpPost(httpPost);
        try {
            return new JSONObject(jsonstring);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String processHttpPost(HttpPost httpPost) {
        String result = null;
        try {
            HttpResponse response;
            response = mDefaultHttpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
            Log.d(TAG, "response: " + result);
        } catch (IOException e) {
            Log.e(TAG, "HTTP error", e);
        }
        return result;
    }

    protected String formCall(String method, double a, double b) {
        JSONArray array = new JSONArray();
        try {
            array.put(a);
            array.put(b);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject result = call(method, array);

        try {
            if(result.has("error")) {
                return result.getJSONObject("error").getString("message");
            } else {
                return result.getString("result");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "call method " + method + " error";
    }

    public static String add(double a, double b) {
        return HttpManager.getInstance().formCall("add", a, b);
    }

    public static String sub(double a, double b) {
        return HttpManager.getInstance().formCall("sub", a, b);
    }

    public static  String mul(double a, double b) {
        return HttpManager.getInstance().formCall("mul", a, b);

    }
    public static  String div(double a, double b) {
        return HttpManager.getInstance().formCall("div", a, b);
    }
}
