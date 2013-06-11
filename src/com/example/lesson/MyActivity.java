package com.example.lesson;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends Activity implements IAsyncListener, View.OnClickListener {
    private EditText mParam1;
    private EditText mParam2;
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
        mResult = (TextView) findViewById(R.id.result);

        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.sub).setOnClickListener(this);
        findViewById(R.id.mul).setOnClickListener(this);
        findViewById(R.id.div).setOnClickListener(this);


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

    @Override
    public void onClick(View view) {
        String a = mParam1.getText().toString();
        String b = mParam2.getText().toString();
        if(a.isEmpty() || b.isEmpty()) {
            setTextToResultHolder("Can't send this request. Invalid parameters.");
        } else {
            //form parameters for call on server
            String params[] = null;
            switch (view.getId()) {
                case R.id.add:
                    params = new String[]{"+", a, b};
                    break;
                case R.id.sub:
                    params = new String[]{"-", a, b};
                    break;
                case R.id.mul:
                    params = new String[]{"*", a, b};
                    break;
                case R.id.div:
                    params = new String[]{"/", a, b};
                    break;
            }
            //request
            AsyncManager.getInstance().process(MyActivity.this, params);
        }
    }
}
