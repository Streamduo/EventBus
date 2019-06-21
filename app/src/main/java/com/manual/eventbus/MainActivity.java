package com.manual.eventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDeafault().register(this);
        Intent intent = new Intent();
        intent.setClass(this,SecondActivity.class);
        startActivity(intent);
    }

    @Subscrbile(threadMode = ThreadMode.MAIN)
    public void getMessage(NeBean neBean) {
        Log.e("MainActivity",neBean.getName() + "==" + neBean.getContent());
        Log.e("MainActivity",Thread.currentThread().getName());
    }
}
