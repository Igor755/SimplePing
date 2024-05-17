package com.simple.ping.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsounjan.icmp4a.Icmp
import com.marsounjan.icmp4a.Icmp4a
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PingViewModel : ViewModel() {

    var text = mutableStateOf("Please press button start ping - 8.8.8.8")
    var job: Job? = null
    var interval = mutableStateOf("500")
    var enabledStart = mutableStateOf(true)
    var enabledStop = mutableStateOf(false)

    fun startPing() {
        val host = "google.com"
        val icmp = Icmp4a()
        if (interval.value.isEmpty()){
            interval.value = "500"
        }
        try {
            job = icmp.pingInterval("8.8.8.8", count = null, intervalMillis = interval.value.toLong())
                .onEach { status ->
                    when (val result = status.result) {
                        is Icmp.PingResult.Success -> Log.d(
                            "ICMP",
                            "$host(${status.ip.hostAddress}) ${result.packetSize} bytes - ${result.ms} ms"
                        )

                        is Icmp.PingResult.Failed -> Log.d(
                            "ICMP",
                            "$host(${status.ip.hostAddress}) Failed: ${result.message}"
                        )
                    }
                }
                .launchIn(viewModelScope)
        } catch (error: Icmp.Error.UnknownHost) {
            Log.d("ICMP", "Unknown host $host")
        }
    }
}