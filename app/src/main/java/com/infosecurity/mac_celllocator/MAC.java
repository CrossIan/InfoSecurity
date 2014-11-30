package com.infosecurity.mac_celllocator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.nio.channels.Channel;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.Channel;


public class MAC extends Activity {

    final Context context = this;

    IntentFilter mIntentFilter;

    private String ipstring;

    public String[] compIPs;

    public double userLatitude, userLongitude, otherLatitude, otherLongitude;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;

    WifiP2pManager.PeerListListener myPeerListListener;

    public String GetUserIPAddress() {
        WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();

        return (( mWifiInfo.getIpAddress() & 0xFF)+ "."+((mWifiInfo.getIpAddress() >> 8 ) & 0xFF)+
                "."+((mWifiInfo.getIpAddress() >> 16 ) & 0xFF)+"."+((mWifiInfo.getIpAddress() >> 24 ) & 0xFF));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac);
        compIPs = new String[3];

        compIPs[0]="User1-IPAddress";
        compIPs[1]="User2-IPAddress";
        compIPs[2]="User3-IPAddress";

        ipstring = GetUserIPAddress();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        otherLatitude = 39.0995;
        otherLongitude = -84.5172;
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
                map.putExtra("otherlatitudeMAC",otherLatitude);
                map.putExtra("otherlongitudeMAC",otherLongitude);
                startActivity(map);
                break;
            case R.id.activity_triangle:
                Intent triangle = new Intent(context, triangle.class);
                startActivity(triangle);
                break;
            case R.id.activity_register_access_point_location:
                Intent registerAccessPointLocation = new Intent(context, RegisterAccessPointLocation.class);
                startActivity(registerAccessPointLocation);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        //Return false to allow normal menu processing to proceed,
        //true to consume it here.
        return false;
    }
    public void sendSelfIP(View view){
        /*
        TextView text=(TextView)findViewById(R.id.useriptext);
        text.setText(ipstring);
        */
    }

    public void sendCompIPs(View view){
        /*
        TextView text=(TextView)findViewById(R.id.otheriptext1);
        TextView text2=(TextView)findViewById(R.id.otheriptext2);
        TextView text3=(TextView)findViewById(R.id.otheriptext3);
        text.setText(compIPs[0]);
        text2.setText(compIPs[1]);
        text3.setText(compIPs[2]);
        */
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void connectToPeers(View view)
    {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Searching for Devices", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(context, "Connection-Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void connectToIndividual(WifiP2pDevice device)
    {
        //obtain a peer from the WifiP2pDeviceList
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //success logic
            }

            @Override
            public void onFailure(int reason) {
                //failure logic
            }
        });
    }
}
