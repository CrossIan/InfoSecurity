package com.infosecurity.mac_celllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phillip on 10/21/2014.
 */
public class WifiNetwork {

    public String sSSID;
    public Map<String,WifiAccessPoint> mAccessPoints;

    public WifiNetwork(String SSID)
    {
        sSSID = SSID;
        mAccessPoints = new HashMap<String,WifiAccessPoint>(256);
    }

    public void AddConnectionUpdate(String BSSID, float latitude, float longitude, int signalStrength)
    {
        WifiConnectionUpdate connectionUpdate = new WifiConnectionUpdate(latitude, longitude, signalStrength);

        if(mAccessPoints.containsKey(BSSID))
        {
            mAccessPoints.get(BSSID).AddConnectionUpdate(connectionUpdate);
        }
        else
        {
            mAccessPoints.put(BSSID,new WifiAccessPoint(BSSID));
            mAccessPoints.get(BSSID).AddConnectionUpdate(connectionUpdate);
        }
    }

}
