package com.infosecurity.mac_celllocator;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MAC extends Activity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.activity_mac:
                Intent mac = new Intent(context, MAC.class);
                startActivity(mac);
                break;
            case R.id.activity_map:
                Intent map = new Intent(context, map.class);
                startActivity(map);
                break;
            case R.id.activity_triangle:
                Intent triangle = new Intent(context, triangle.class);
                startActivity(triangle);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        //Return false to allow normal menu processing to proceed,
        //true to consume it here.
        return false;
    }
}
