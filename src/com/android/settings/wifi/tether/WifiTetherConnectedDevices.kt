package com.android.settings.wifi.tether

import android.app.settings.SettingsEnums
import android.net.Wifi.WifiClient
import android.net.Wifi.WifiManager

import com.android.settings.dashboard.DashboardFragment

class WifiTetherConnectedDevices : DashboardFragment() {

    private val capabilitySoftApCallback = object : WifiManager.SoftApCallback {
        override fun onConnectedClientsChanged(info: SoftApInfo, clients: List<WifiClient>) {
            
        }
    }

    override fun getMetricsCategory(): Int {
        return SettingsEnums.WIFI_TETHER_SETTINGS
    }

    override fun getPreferenceScreenResId() {

    }



    companion object {
        const val TAG: String = "WifiHotspotConnectedDevices"
    }

}
