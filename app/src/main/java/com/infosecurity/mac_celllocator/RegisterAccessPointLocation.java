package com.infosecurity.mac_celllocator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;


import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.content.BroadcastReceiver;


public class RegisterAccessPointLocation extends Activity {

    final Context context = this;
    public WifiManager mWifiManager;
    public IntentFilter mIntentFilter;
    public String mApStr;
    public Map<String, WifiAccessPoint> mAccessPoints;
    public List<ScanResult> scanResults;
    public WifiAccessPoint[] lAccessPoints;
    public double uX, uY, uZ;
    public double uLat, uLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_access_point_location);

        // Set up WifiManager.
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);


        registerReceiver(mReceiver, mIntentFilter);

        uX = 0.0;
        uY = 0.0;
        uZ = 0.0;
        uLat = 40.002162;
        uLong = -83.015655;



        lAccessPoints = new WifiAccessPoint[5];
        for (WifiAccessPoint  accessPoint : lAccessPoints) {
            accessPoint = null;
        }

        mAccessPoints = new HashMap<String, WifiAccessPoint>(2048);

        loadAccessPoints();

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
                Intent mac = new Intent(context, RegisterAccessPointLocation.class);
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

    /*
 * ***********************************************************************
* onResume() is called when an Activity returns to the foreground. It's
* safe to assume that graphical widgets have been drawn.
*
* Register the broadcast receiver with the intent values to be matched.
* ***********************************************************************
*/
    @Override
    protected void onResume()
    {
        super.onResume();

        //registerReceiver(mReceiver, mIntentFilter);
    }

    /*
     * ************************************************************************
     * onPause() is called when an Activity leaves the foreground, e.g., when
     * the user presses 'Home' or 'Back'. You'll want to save state in this
     * method.
     *
     * Unregister the broadcast receiver to save memory. Otherwise the receiver
     * will be leaked. (I've seen this happen many times; it encumbers debugging
     * efforts.)
     * ************************************************************************
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        //unregisterReceiver(mReceiver);
    }

    public void refreshAccessPoints(View view)
    {
        mWifiManager.startScan();

        System.out.println("refreshAccessPoints");
        try {
            if (scanResults.size() > 0) {
                lAccessPoints[0] = mAccessPoints.get(scanResults.listIterator(0).next().BSSID);
                if (scanResults.size() > 1) {
                    lAccessPoints[1] = mAccessPoints.get(scanResults.listIterator(1).next().BSSID);
                    if (scanResults.size() > 2) {
                        lAccessPoints[2] = mAccessPoints.get(scanResults.listIterator(2).next().BSSID);
                        if (scanResults.size() > 3) {
                            lAccessPoints[3] = mAccessPoints.get(scanResults.listIterator(3).next().BSSID);
                            if (scanResults.size() > 4) {
                                lAccessPoints[4] = mAccessPoints.get(scanResults.listIterator(4).next().BSSID);
                            } else {
                                lAccessPoints[4] = null;
                            }
                        } else {
                            lAccessPoints[3] = null;
                            lAccessPoints[4] = null;
                        }
                    } else {
                        lAccessPoints[2] = null;
                        lAccessPoints[3] = null;
                        lAccessPoints[4] = null;
                    }
                } else {
                    lAccessPoints[1] = null;
                    lAccessPoints[2] = null;
                    lAccessPoints[3] = null;
                    lAccessPoints[4] = null;
                }
            } else {
                lAccessPoints[0] = null;
                lAccessPoints[1] = null;
                lAccessPoints[2] = null;
                lAccessPoints[3] = null;
                lAccessPoints[4] = null;
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void activate0(View view)
    {
        System.out.println("activate0");
        try {
            int iX = Integer.valueOf(((EditText) findViewById(R.id.apX0)).getText().toString());
            int iY = Integer.valueOf(((EditText) findViewById(R.id.apY0)).getText().toString());
            int iZ = Integer.valueOf(((EditText) findViewById(R.id.apZ0)).getText().toString());


            if (lAccessPoints[0] != null) {
                lAccessPoints[0].SaveCoordinates(iX, iY, iZ, true);
            }
            saveAccessPoints();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void activate1(View view)
    {
        System.out.println("activate1");
        try {
            int iX =  Integer.valueOf(((EditText)findViewById(R.id.apX1)).getText().toString());
            int iY = Integer.valueOf(((EditText)findViewById(R.id.apY1)).getText().toString());
            int iZ = Integer.valueOf(((EditText)findViewById(R.id.apZ1)).getText().toString());


            if(lAccessPoints[1]!=null) {
                lAccessPoints[1].SaveCoordinates(iX, iY, iZ, true);
            }
            saveAccessPoints();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void activate2(View view)
    {
        System.out.println("activate2");
        try {
            int iX =  Integer.valueOf(((EditText)findViewById(R.id.apX2)).getText().toString());
            int iY = Integer.valueOf(((EditText)findViewById(R.id.apY2)).getText().toString());
            int iZ = Integer.valueOf(((EditText)findViewById(R.id.apZ2)).getText().toString());


            if(lAccessPoints[2]!=null) {
                lAccessPoints[2].SaveCoordinates(iX, iY, iZ, true);
            }
            saveAccessPoints();
        }catch(Exception e)
        {
        System.out.println(e.getMessage());
        }
    }
    public void activate3(View view)
    {
        System.out.println("activate3");
        try {
            int iX =  Integer.valueOf(((EditText)findViewById(R.id.apX3)).getText().toString());
            int iY = Integer.valueOf(((EditText)findViewById(R.id.apY3)).getText().toString());
            int iZ = Integer.valueOf(((EditText)findViewById(R.id.apZ3)).getText().toString());


            if(lAccessPoints[3]!=null) {
                lAccessPoints[3].SaveCoordinates(iX, iY, iZ, true);
            }
            saveAccessPoints();
        }catch(Exception e)
        {
        System.out.println(e.getMessage());
        }
    }
    public void activate4(View view)
    {
        System.out.println("activate4");
        try {
            int iX =  Integer.valueOf(((EditText) findViewById(R.id.apX4)).getText().toString());
            int iY = Integer.valueOf(((EditText)findViewById(R.id.apY4)).getText().toString());
            int iZ = Integer.valueOf(((EditText)findViewById(R.id.apZ4)).getText().toString());


            if(lAccessPoints[4]!=null) {
                lAccessPoints[4].SaveCoordinates(iX, iY, iZ, true);
            }
            saveAccessPoints();
        }catch(Exception e)
        {
        System.out.println(e.getMessage());
        }
    }

    private void loadAccessPoints()
    {
        String filename = "mAccessPoints";
        String string = "";
        FileInputStream inputStream;

        try {
            inputStream = openFileInput(filename);
            boolean bGoodInput = true;
            if(inputStream.available()<2)  // There would probably be a better way to do this, but this was quick and easy.  This pre-loads many of the Access Points from Dreese Labs.
            {
                String defaultAccessPoints = "WiFi@OSU\n6c:f3:7f:52:53:63\n62\n32\n38\nWiFi@OSU\n6c:f3:7f:52:50:b1\n29\n21\n38\nWiFi@OSU\n6c:f3:7f:52:51:23\n54\n15\n38\neduroam\n6c:f3:7f:52:51:20\n54\n15\n38\neduroam\n6c:f3:7f:52:50:a0\n29\n21\n38\nosuwireless\n6c:f3:7f:52:52:61\n63\n25\n38\nosuwireless\n6c:f3:7f:52:52:50\n30\n40\n38\nosuwireless\n6c:f3:7f:52:53:61\n62\n32\n38\nWiFi@OSU\n6c:f3:7f:52:53:03\n48\n21\n38\nWiFi@OSU\n6c:f3:7f:52:52:63\n63\n25\n38\neduroam\n6c:f3:7f:52:53:f2\n78\n8\n38\nosuwireless\n6c:f3:7f:52:53:01\n48\n21\n38\nosuwireless\n6c:f3:7f:52:4d:a1\n91\n24\n38\nosuwireless\n6c:f3:7f:52:53:f0\n78\n8\n38\neduroam\n6c:f3:7f:52:53:00\n48\n21\n38\nWiFi@OSU\n6c:f3:7f:52:53:f1\n78\n8\n38\neduroam\n6c:f3:7f:52:52:52\n30\n40\n38\nWiFi@OSU\n6c:f3:7f:52:51:83\n84\n50\n38\nosuwireless\n6c:f3:7f:52:51:81\n84\n50\n38\neduroam\n6c:f3:7f:52:51:80\n84\n50\n38\nWiFi@OSU\n6c:f3:7f:52:4d:a3\n91\n24\n38\nWiFi@OSU\n6c:f3:7f:52:50:a3\n29\n21\n38\neduroam\n6c:f3:7f:52:4d:a0\n91\n24\n38\neduroam\n6c:f3:7f:52:50:b2\n29\n21\n38\nosuwireless\n6c:f3:7f:52:51:30\n54\n15\n38\neduroam\n6c:f3:7f:52:52:60\n63\n25\n38\neduroam\n6c:f3:7f:52:51:32\n54\n15\n38\nosuwireless\n6c:f3:7f:52:50:b0\n29\n21\n38\nWiFi@OSU\n6c:f3:7f:52:52:51\n30\n40\n38\neduroam\n6c:f3:7f:52:53:72\n62\n32\n38\nosuwireless\n6c:f3:7f:52:50:a1\n29\n21\n38\nosuwireless\n6c:f3:7f:52:51:21\n54\n15\n38\neduroam\n6c:f3:7f:52:53:60\n62\n32\n38\n";


                FileOutputStream outputStream;
                outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
                outputStream.write(defaultAccessPoints.getBytes());
                outputStream.close();

                inputStream = openFileInput(filename);
            }
            while(inputStream.available()>0 && bGoodInput) {
                char nextChar='\n';
                String sSSID="";
                String sBSSID="";
                String sX = "";
                String sY = "";
                String sZ = "";
                int iX=0;
                int iY=0;
                int iZ=0;

                do {
                    nextChar = (char) inputStream.read();
                    if(nextChar!='\n') {
                        sSSID = sSSID + nextChar;
                    }
                } while (nextChar!='\n');

                do {
                    nextChar = (char) inputStream.read();
                    if(nextChar!='\n') {
                        sBSSID = sBSSID + nextChar;
                    }
                } while (nextChar!='\n');

                do {
                    nextChar = (char) inputStream.read();
                    if(nextChar!='\n') {
                        sX = sX + nextChar;
                    }
                } while (nextChar!='\n');
                do {
                    nextChar = (char) inputStream.read();
                    if(nextChar!='\n') {
                        sY = sY + nextChar;
                    }
                } while (nextChar!='\n');
                do {
                    nextChar = (char) inputStream.read();
                    if(nextChar!='\n') {
                        sZ = sZ + nextChar;
                    }
                } while (nextChar!='\n');

                iX = Integer.parseInt(sX);
                iY = Integer.parseInt(sY);
                iZ = Integer.parseInt(sZ);

                if(!mAccessPoints.containsKey(sBSSID))
                {
                    mAccessPoints.put(sBSSID,new WifiAccessPoint(sSSID,sBSSID));
                    mAccessPoints.get(sBSSID).SaveCoordinates(iX,iY,iZ,true);
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void saveAccessPoints()
    {
        String filename = "mAccessPoints";
        String string = "";

        for(WifiAccessPoint accessPoint : mAccessPoints.values())
        {
            if(accessPoint.bInit)
            {
                string = string + accessPoint.sSSID + "\n" +
                        accessPoint.sBSSID + "\n" +
                        accessPoint.iX + "\n" +
                        accessPoint.iY + "\n" +
                        accessPoint.iZ + "\n";
            }
        }

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
            outputStream.write(string.getBytes());
            outputStream.close();

            if(isExternalStorageReadable()&&isExternalStorageWritable())
            {
                File mAccessPointsFile = getFileStorageDir("mAccessPoints.txt");
                mAccessPointsFile.setWritable(true);
                mAccessPointsFile.setReadable(true);

                FileWriter fileWriter = new FileWriter(mAccessPointsFile);
                fileWriter.write(string);
                fileWriter.close();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",string);
                clipboard.setPrimaryClip(clip);

            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getFileStorageDir(String fileName) {
        // Get the directory for the user's public documents directory.
        File file = new File(context.getFilesDir(), fileName);
        file.delete();
        return file;
    }

    public void updateUserCoordinates()
    {
        mWifiManager.startScan();


        try {
            double t = 0;
            double tx = 0;
            double ty = 0;
            double tz = 0;

            for(ScanResult scanResult : scanResults)
            {
                if(mAccessPoints.containsKey(scanResult.BSSID))
                {
                    if(mAccessPoints.get(scanResult.BSSID).bInit)
                    {
                        t += Math.log(scanResult.level + 100);
                        tx += Math.log(scanResult.level + 100) * mAccessPoints.get(scanResult.BSSID).iX;
                        ty += Math.log(scanResult.level + 100) * mAccessPoints.get(scanResult.BSSID).iY;
                        tz += Math.log(scanResult.level + 100) * mAccessPoints.get(scanResult.BSSID).iZ;
                    }
                }
            }

            uX = tx/t;
            uY = ty/t;
            uZ = tz/t;

            if(t==0)
            {
                uX = 0;
                uY = 0;
                uZ = 0;
            }

            uLat = 40.002162 + (2.6981e-6 * uX) - (4.66e-7 * uY);
            uLong = -83.015655 - (6.08e-7 * uX) - (3.522e-6 * uY);

            TextView disXval = (TextView) findViewById(R.id.disXval);
            TextView disYval = (TextView) findViewById(R.id.disYval);
            TextView disZval = (TextView) findViewById(R.id.disZval);

            TextView disLatval = (TextView) findViewById(R.id.disLatval);
            TextView disLongval = (TextView) findViewById(R.id.disLongval);

            disXval.setText(""+uX);
            disYval.setText(""+uY);
            disZval.setText(""+uZ);

            disLatval.setText(""+uLat);
            disLongval.setText(""+uLong);
        }catch(Exception e)// Sometimes gets Divide By Zero... try-catch is an easy, but uninspired fix
        {
            System.out.println(e.getMessage());
        }
    }

    /*
     * ************************************************************************
     * Declare a Broadcast Receiver that "responds" to Android system Intents.
     * In our case, we only want to display the results of a WiFi scan, which
     * are made available when the SCAN_RESULTS_AVAILABLE_ACTION fires (after
     * the scan is completed).
     * ************************************************************************
     */
    public final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        // Override onReceive() method to implement our custom logic.
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action))
            {
                System.out.println("Scan Refresh");

                scanResults = mWifiManager.getScanResults();
                Collections.sort(scanResults, new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult scanResult, ScanResult scanResult2) {
                        return scanResult2.level - scanResult.level;
                    }
                });

                mApStr = "";

                for(ScanResult scanResult : scanResults)
                {
                    if(!mAccessPoints.containsKey(scanResult.BSSID))
                    {
                        mAccessPoints.put(scanResult.BSSID,new WifiAccessPoint(scanResult.SSID,scanResult.BSSID));
                    }
                }

                TextView apBssidVal0 = (TextView) findViewById(R.id.apBssidVal0);
                TextView apSignalVal0 = (TextView) findViewById(R.id.apSignalVal0);
                TextView apActiveVal0 = (TextView) findViewById(R.id.apActiveVal0);
                TextView apBssidVal1 = (TextView) findViewById(R.id.apBssidVal1);
                TextView apSignalVal1 = (TextView) findViewById(R.id.apSignalVal1);
                TextView apActiveVal1 = (TextView) findViewById(R.id.apActiveVal1);
                TextView apBssidVal2 = (TextView) findViewById(R.id.apBssidVal2);
                TextView apSignalVal2 = (TextView) findViewById(R.id.apSignalVal2);
                TextView apActiveVal2 = (TextView) findViewById(R.id.apActiveVal2);
                TextView apBssidVal3 = (TextView) findViewById(R.id.apBssidVal3);
                TextView apSignalVal3 = (TextView) findViewById(R.id.apSignalVal3);
                TextView apActiveVal3 = (TextView) findViewById(R.id.apActiveVal3);
                TextView apBssidVal4 = (TextView) findViewById(R.id.apBssidVal4);
                TextView apSignalVal4 = (TextView) findViewById(R.id.apSignalVal4);
                TextView apActiveVal4 = (TextView) findViewById(R.id.apActiveVal4);

                if(lAccessPoints[0]!= null)
                {
                    apBssidVal0.setText(lAccessPoints[0].sBSSID);

                    for(ScanResult scanResult : scanResults)
                    {
                        if(scanResult.BSSID.equals(lAccessPoints[0].sBSSID))
                        {
                            apSignalVal0.setText(""+scanResult.level);
                        }
                    }

                    if(lAccessPoints[0].bInit)
                    {
                        apActiveVal0.setText("True");
                    }
                    else
                    {
                        apActiveVal0.setText("False");
                    }


                    if(lAccessPoints[1]!= null)
                    {

                        apBssidVal1.setText(lAccessPoints[1].sBSSID);

                        for(ScanResult scanResult : scanResults)
                        {
                            if(scanResult.BSSID.equals(lAccessPoints[1].sBSSID))
                            {
                                apSignalVal1.setText(""+scanResult.level);
                            }
                        }

                        if(lAccessPoints[1].bInit)
                        {
                            apActiveVal1.setText("True");
                        }
                        else
                        {
                            apActiveVal1.setText("False");
                        }


                        if(lAccessPoints[2]!= null)
                        {

                            apBssidVal2.setText(lAccessPoints[2].sBSSID);

                            for(ScanResult scanResult : scanResults)
                            {
                                if(scanResult.BSSID.equals(lAccessPoints[2].sBSSID))
                                {
                                    apSignalVal2.setText(""+scanResult.level);
                                }
                            }

                            if(lAccessPoints[2].bInit)
                            {
                                apActiveVal2.setText("True");
                            }
                            else
                            {
                                apActiveVal2.setText("False");
                            }


                            if(lAccessPoints[3]!= null)
                            {

                                apBssidVal3.setText(lAccessPoints[3].sBSSID);

                                for(ScanResult scanResult : scanResults)
                                {
                                    if(scanResult.BSSID.equals(lAccessPoints[3].sBSSID))
                                    {
                                        apSignalVal3.setText(""+scanResult.level);
                                    }
                                }

                                if(lAccessPoints[3].bInit)
                                {
                                    apActiveVal3.setText("True");
                                }
                                else
                                {
                                    apActiveVal3.setText("False");
                                }


                                if(lAccessPoints[4]!= null)
                                {

                                    apBssidVal4.setText(lAccessPoints[4].sBSSID);

                                    for(ScanResult scanResult : scanResults)
                                    {
                                        if(scanResult.BSSID.equals(lAccessPoints[4].sBSSID))
                                        {
                                            apSignalVal4.setText(""+scanResult.level);
                                        }
                                    }

                                    if(lAccessPoints[4].bInit)
                                    {
                                        apActiveVal4.setText("True");
                                    }
                                    else
                                    {
                                        apActiveVal4.setText("False");
                                    }



                                }
                            }
                        }
                    }
                }

                updateUserCoordinates();

            }
        }
    };
}
