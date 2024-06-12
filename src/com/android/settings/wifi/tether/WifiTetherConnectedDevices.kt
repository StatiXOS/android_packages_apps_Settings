package com.android.settings.wifi.tether

import android.app.settings.SettingsEnums
import android.content.Context
import android.os.Bundle
import android.net.wifi.SoftApInfo
import android.net.wifi.WifiClient
import android.net.wifi.WifiManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnLifecycleDestroyed
import com.android.settings.R
import com.android.settings.dashboard.DashboardFragment
import com.android.settingslib.spa.framework.theme.SettingsTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class WifiTetherConnectedDevices : DashboardFragment() {

    private val clientList = MutableStateFlow<List<WifiClient>>(listOf())

    private val capabilitySoftApCallback = object : WifiManager.SoftApCallback {
        override fun onConnectedClientsChanged(info: SoftApInfo, clients: List<WifiClient>) {
            clientList.update { clients }
        }
    }

    override fun getMetricsCategory(): Int {
        return SettingsEnums.WIFI_TETHER_SETTINGS
    }

    override fun getPreferenceScreenResId(): Int {
        return R.xml.wifi_hotspot_connected_devices
    }

    override fun getLogTag(): String {
        return TAG
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        val view = inflater.inflate(getPreferenceScreenResId(), container, false)
        val composeView = view.findViewById<ComposeView>(R.id.connected_devices_compose_view)!!
        composeView.apply {
                // Dispose the Composition when viewLifecycleOwner is destroyed
                setViewCompositionStrategy(
                    DisposeOnLifecycleDestroyed(viewLifecycleOwner)
                )
                setContent {
                    SettingsTheme {
                       val clients by clientList.collectAsState()
                       LazyColumn(modifier = Modifier.fillMaxWidth()) {
                           items(clients) { client ->
                               ConnectedDevice(client)
                           }
                       }
                    }
                }
        }
        return view
    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as? WifiManager ?: return
        wifiManager.registerSoftApCallback(requireContext().applicationContext.mainExecutor, capabilitySoftApCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as? WifiManager ?: return
        wifiManager.unregisterSoftApCallback(capabilitySoftApCallback)
    }

    @Composable
    fun ConnectedDevice(device: WifiClient) {
        Column {
           Text(device.macAddress.toString())
           Text("MAC Address")
        }
    }

    companion object {
        const val TAG: String = "WifiHotspotConnectedDevices"
    }
}
