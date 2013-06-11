package com.example.lesson;

import android.os.AsyncTask;
import com.example.lesson.Exeptions.ServerException;
import org.apache.http.ConnectionClosedException;
import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * class singleton for manage server requests.
 */

public class AsyncManager {
    private static AsyncManager mInstance;
    private AsyncManager() {}

    public static AsyncManager getInstance() {
        if(mInstance == null) {
            mInstance = new AsyncManager();
        }
        return mInstance;
    }
    //create new request
    public void process(IAsyncListener onResultListener, String... params) {
        new RequestAsync(onResultListener).execute(params);
    }

    /**
     * class for request on server
     */
    private class RequestAsync extends AsyncTask<String, Void, Double> {
        private IAsyncListener mOnResultListener;
        private String mError = null;
        public RequestAsync(IAsyncListener onResultListener) {
            mOnResultListener = onResultListener;
        }

        /**
         * call HttpManager methods for operations
         * catch all errors and for error message for user
         *
         * @param strings
         * @return
         */

        @Override
        protected Double doInBackground(String... strings) {
            try {
                String operation = strings[0];
                double a = Double.valueOf(strings[1]);
                double b = Double.valueOf(strings[2]);
                //get method for call
                switch (operation.charAt(0)) {
                    case '+':
                        return HttpManager.add(a, b);
                    case '-':
                        return HttpManager.sub(a, b);
                    case '*':
                        return HttpManager.mul(a, b);
                    case '/':
                        return HttpManager.div(a, b);
                }
            } catch (ServerException e) {
                mError = e.getMessage();
                e.printStackTrace();
            } catch (ConnectionClosedException e) {
                mError = e.getMessage();
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                mError = e.getMessage();
                e.printStackTrace();
            } catch (JSONException e) {
                mError = e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                mError = e.getMessage();
                e.printStackTrace();
            } catch (NumberFormatException e) {
                mError = e.getMessage();
                e.printStackTrace();
            }  catch (Exception e) {
                mError = e.getMessage();
                e.printStackTrace();
            }
            //if get errors - return null
            return null;
        };

        /**
         * start executing AsyncTask
         * work in IU thread
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mOnResultListener.onStartProcess();
        }

        /**
         * AsyncTask result
         * work in IU thread
         */

        @Override
        protected void onPostExecute(Double s) {
            super.onPostExecute(s);
            if(mError == null) {
                mOnResultListener.onGetResult(String.valueOf(s));
            } else {
                mOnResultListener.onGetResult(mError);
            }
        }
    }
}
