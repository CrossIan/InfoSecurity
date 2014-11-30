package com.infosecurity.mac_celllocator;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phillip on 10/21/2014.
 */
public class WifiAccessPoint {

    public String sSSID;
    public String sBSSID;

    public float fLatitude;
    public float fLongitude;


    //This is the offset from the bottom southeast corner of Dreese Labs (in feet) (40.002162, -83.015655) (coordinates are rotated off by 9.8 degrees)
    public int iX; //North
    public int iY; //West
    public int iZ; //Up

    public boolean bInit; //Have the coordinates been set yet

    public WifiAccessPoint(String SSID, String BSSID)
    {
        sSSID = SSID;
        sBSSID = BSSID;

        fLatitude = 0f;
        fLongitude = 0f;

        iX = 0;
        iY = 0;
        iZ = 0;

        bInit = false;
    }


    public void SaveCoordinates(int iX, int iY, int iZ, boolean bInit)
    {
        this.iX = iX;
        this.iY = iY;
        this.iZ = iZ;

        this.bInit = bInit;
    }
}