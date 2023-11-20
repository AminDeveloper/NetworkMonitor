package ir.amin.networkmonitor.utils

import ir.amin.networkmonitor.utils.doAsync
import ir.amin.networkmonitor.utils.uiThread


/**
 * controls frequency of doing an action by given delay
 */
class RequestRateController{
    private var pendingRequest = false
    private var lastRequest: Long = 0
    var rateMilli=500

    @Synchronized
     fun request(method:()->Any?) {
        if (System.currentTimeMillis() - lastRequest >= rateMilli) {
            lastRequest = System.currentTimeMillis()
            method.invoke()
            pendingRequest = false
        } else {
            if (!pendingRequest)
                doAsync {
                    Thread.sleep((rateMilli+1).toLong())
                    uiThread {
                        request(method)
                    }
                }
            pendingRequest = true
        }
    }
}
