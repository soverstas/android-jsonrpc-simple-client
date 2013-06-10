package com.example.lesson;

import android.os.AsyncTask;
import android.os.ParcelFormatException;

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
    private class RequestAsync extends AsyncTask<String, Void, String> {
        private IAsyncListener mOnResultListener;
        public RequestAsync(IAsyncListener onResultListener) {
            mOnResultListener = onResultListener;
        }

        @Override
        protected String doInBackground(String... strings) {
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
                    default:
                        return "can't identify method";
                }
            } catch (ParcelFormatException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mOnResultListener.onStartProcess();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mOnResultListener.onGetResult(s);
        }
    }
}
