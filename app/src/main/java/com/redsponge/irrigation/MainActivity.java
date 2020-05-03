package com.redsponge.irrigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeToList(View view) {
        Intent i = new Intent(this, ProgramListActivity.class);
        startActivity(i);
    }
}
