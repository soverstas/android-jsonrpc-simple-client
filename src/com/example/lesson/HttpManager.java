package com.example.lesson;

import android.util.Log;
import com.example.lesson.Exeptions.ServerException;
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
    private static HttpManager sInstance;
    private DefaultHttpClient mDefaultHttpClient;
    private static final String URL = "http://shagren.stagingmonster.com/calc/";

    private HttpManager() {
        mDefaultHttpClient = new DefaultHttpClient();
    }

    public static HttpManager getInstance() {
        if (sInstance == null) {
            sInstance = new HttpManager();
        }
        return sInstance;
    }

    /**
     * form JSON and process server
     * response, conversion in JSON object
     *
     * @param method
     * @param params
     * @return
     * @throws IOException
     * @throws JSONException
     */
    protected JSONObject call(String method, JSONArray params) throws IOException, JSONException {
        HttpPost httpPost = new HttpPost(URL);
        JSONObject options = new JSONObject();
        options.put("jsonrpc", "2.0");
        options.put("id", "123");
        options.put("method", method);
        options.put("params", params);
        Log.d(TAG, "call api request: " + options.toString());
        StringEntity entity = new StringEntity(options.toString(), HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        String jsonstring = processHttpPost(httpPost);
        return new JSONObject(jsonstring);
    }

    /**
     * send request data to server,
     * get result
     *
     * @param httpPost
     * @return
     * @throws IOException
     */

    protected String processHttpPost(HttpPost httpPost) throws IOException {
        HttpResponse response;
        response = mDefaultHttpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        Log.d(TAG, "response: " + result);
        return result;
    }

    public static Double add(double a, double b) throws ServerException, JSONException, IOException {
        return operation("add", a, b);
    }

    public static Double sub(double a, double b) throws ServerException, JSONException, IOException {
        return operation("sub", a, b);
    }

    public static Double mul(double a, double b) throws ServerException, JSONException, IOException {
        return operation("mul", a, b);
    }

    public static Double div(double a, double b) throws ServerException, JSONException, IOException {
        return operation("div", a, b);
    }

    /**
     * method for call server API
     * and process server error if
     * get or return answer
     *
     * @param operation
     * @param a
     * @param b
     * @return
     * @throws ServerException
     * @throws JSONException
     * @throws IOException
     */

    private static Double operation(String operation, double a, double b)
            throws ServerException, JSONException, IOException {
        JSONArray array = new JSONArray();
        array.put(a);
        array.put(b);
        JSONObject result = HttpManager.getInstance().call(operation, array);
        if (result.has("error")) {
            throw new ServerException(result.getJSONObject("error").getString("message"));
        } else {
            return result.getDouble("result");
        }
    }
}