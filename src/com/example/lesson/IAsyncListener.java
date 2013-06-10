package com.example.lesson;

/**
 * callback interface for AsyncManager
 */
public interface IAsyncListener {
    /**
     * call in PreExecute in AsyncTask
     */
    void onStartProcess();

    /**
     * call in postExecute in AsyncTask
     * @param result
     */
    void onGetResult(String result);
}
