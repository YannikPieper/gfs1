package com.eastereggdevelopment.json;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class Loadscreen extends Activity {
    private static int time = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Loadscreen.this, MainActivity.class);
                startActivity(intent);
            }
        }, time);
    }
}
