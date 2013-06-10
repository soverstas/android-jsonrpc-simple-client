package com.example.lesson;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends Activity implements IAsyncListener {
    private EditText mParam1;
    private EditText mParam2;
    private EditText mOperation;
    private TextView mResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mParam1 = (EditText) findViewById(R.id.param1);
        mParam2 = (EditText) findViewById(R.id.param2);
        mOperation = (EditText) findViewById(R.id.operation);
        mResult = (TextView) findViewById(R.id.result);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String method = mOperation.getText().toString();
                String a = mParam1.getText().toString();
                String b = mParam2.getText().toString();
                if(method.isEmpty() || a.isEmpty() || b.isEmpty()) {
                    setTextToResultHolder("Can't send this request. Invalid parameters.");
                } else {
                    //form parameters for call on server
                    String params[] = new String[]{
                            mOperation.getText().toString(),
                            mParam1.getText().toString(),
                            mParam2.getText().toString()
                    };
                    //request
                    AsyncManager.getInstance().process(MyActivity.this, params);
                }
            }
        });

    }
    //set result text
    protected void setTextToResultHolder(String text) {
        mResult.setText(text);
    }


    //callback from AsyncManager, call when start process
    @Override
    public void onStartProcess() {
        setTextToResultHolder("try process...");
    }

    //callback from AsyncManager, call when get result
    @Override
    public void onGetResult(String result) {
        setTextToResultHolder(result);
    }
}
