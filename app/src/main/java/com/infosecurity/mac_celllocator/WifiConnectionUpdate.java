package com.infosecurity.mac_celllocator;

/**
 * Created by Phillip on 10/21/2014.
 */
public class WifiConnectionUpdate {

    public float fLatitude;
    public float fLongitude;
    public int iSignalStrength;

    public WifiConnectionUpdate(float latitude, float longitude, int signalStrength)
    {
        fLatitude = latitude;
        fLongitude = longitude;
        iSignalStrength = signalStrength;
    }
}
