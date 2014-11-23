package com.infosecurity.mac_celllocator;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phillip on 10/21/2014.
 */
public class WifiAccessPoint {

    public String sBSSID;
    public List<WifiConnectionUpdate> lConnectionUpdates;

    public float fLatitude;
    public float fLongitude;

    public WifiAccessPoint(String BSSID)
    {
        sBSSID = BSSID;
        lConnectionUpdates = new ArrayList<WifiConnectionUpdate>(4096);

        fLatitude = 0f;
        fLongitude = 0f;
    }

    public void AddConnectionUpdate(WifiConnectionUpdate connectionUpdate)
    {
        lConnectionUpdates.add(connectionUpdate);
    }

    public void DetermineCoordinates()
    {
        int maxSignalStrength = -100;
        int count = 0;
        for ( WifiConnectionUpdate connectionUpdate : lConnectionUpdates)
        {
            if(connectionUpdate.iSignalStrength>maxSignalStrength)
            {
                fLatitude = connectionUpdate.fLatitude;
                fLongitude = connectionUpdate.fLongitude;
                count = 1;
            }
            if(connectionUpdate.iSignalStrength==maxSignalStrength)
            {
                fLatitude = (count * fLatitude + connectionUpdate.fLatitude) / (count +1);
                fLongitude = (count * fLongitude + connectionUpdate.fLongitude) / (count +1);
                count ++;
            }
        }
    }
}