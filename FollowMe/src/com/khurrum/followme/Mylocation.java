package com.khurrum.followme;

import android.os.Bundle;
import android.view.Menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public class Mylocation extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mylocation, menu);
        return true;
    }
    
}
