package ir.amin.networkmonitor.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import ir.amin.networkmonitor.utils.RetryHelper.RetryHelper
import ir.amin.networkmonitor.Observers.NetworkObserverHandler
import ir.amin.networkmonitor.Observers.NetworkStateLiveData
import ir.amin.networkmonitor.utils.Utils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 */
class NetworkChangeReceiver : BroadcastReceiver() {
    companion object {
        var PingBeforeInform = false
        var pingHost = "www.google.com"
        var pingTimeOut = 2000
        private var lastState: Boolean? = null

        var pingMechanism: ((host: String, timeout: Int) -> Boolean)? = null

        //        private var future: Future<*>? = null
        private var threadExecutor: ExecutorService? = null

        fun checkNetworkState(context: Context) {
            val currentState = Utils.isNetworkAvailable(context)
            //to prevent multiple call from device
            if (lastState == null || lastState != currentState) {

                lastState = currentState
                informNetworkChange(currentState)

            }
        }

        fun refreshCurrentState(context: Context?) {
            val currentState = Utils.isNetworkAvailable(context)
            informNetworkChange(currentState)
        }

        fun informNetworkChange(connectionState: Boolean) {
            if (PingBeforeInform) checkNetworkStateByPing() else informObservers(connectionState)
        }

        private fun checkNetworkStateByPing() {
            val handler = Handler()
            threadExecutor = threadExecutor ?: Executors.newSingleThreadExecutor()

//            future =
            threadExecutor?.submit {
                val isReachable = pingMechanism?.run { pingMechanism?.invoke(pingHost, pingTimeOut) }
                        ?: Utils.isConnectedToThisServer(pingHost, pingTimeOut)

                handler.post { informObservers(isReachable) }
            }
        }

        private fun informObservers(currentState: Boolean) {
            NetworkObserverHandler.getInstance().informObservers(currentState)
            NetworkStateLiveData.getInstance().postValue(
                NetworkStateLiveData.NetworkState(
                    currentState
                )
            )

            if(currentState)// unlock showing network error if network become available
                RetryHelper.unLockDismissedDialog()
        }
    }

    var isRegistered = AtomicBoolean(false)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.extras != null) {
            checkNetworkState(context)
        }
    }

    fun registerService(context: Context) {
        if (!isRegistered.get()) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            if (getObserversCount() == 1)//for the first time
            try {
                context.registerReceiver(this,
                        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
                isRegistered.set(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun unRegisterService(context: Context) {
        if (isRegistered.get()) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            if (getObserversCount() == 0 && lastCont == 1)//for the last time
            try {
                context.unregisterReceiver(this)
                isRegistered.set(false)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }
}