package ir.amin.networkmonitor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import ir.amin.networkmonitor.BroadCastReceiver.NetworkChangeReceiver
import ir.amin.networkmonitor.Observers.ObserverList
import ir.amin.networkmonitor.Observers.TestObserver
import ir.amin.networkmonitor.utils.RetryHelper.DefaultNetworkErrorDialog
import ir.amin.networkmonitor.utils.RetryHelper.RetryHelper
import ir.amin.networkmonitor.utils.SmartLogger
import ir.amin.networkmonitor.utils.Utils
import ir.amin.networkmonitor.utils.doAsync
import ir.amin.networkmonitor.utils.uiThread

/**
 *
 */

class MainActivity : Activity(), TestObserver.ObserverTest {
    var retryHelper: RetryHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkChangeReceiver.PingBeforeInform = true
        NetworkChangeReceiver.pingMechanism = { host, timeout -> Utils.isConnectedToThisServer(host,timeout) }

        DefaultNetworkErrorDialog.startShowingDefaultNetworkErrorDialog(this)// Caused by: java.lang.SecurityException: ConnectivityService: Neither user 10082 nor current process has android.permission.ACCESS_NETWORK_STATE.
        SmartLogger.initLogger(applicationContext)
        setContentView(R.layout.main_activity)
        testMethod()
        findViewById<Button>(R.id.showNotificationTest).setOnClickListener {
            startNotificationBadgeTest()
        }

        findViewById<Button>(R.id.ObserverTest).setOnClickListener {
            ObserverList.getTestObserver().informObservers(listOf("abc", "cde"))
        }
        ObserverList.getTestObserver().addObserver(this)

        findViewById<Button>(R.id. RetryHelperTest).setOnClickListener {
            retryHelper = RetryHelper.getInstanceAndCall(this, {
                SmartLogger.logDebug("doing ...")
                Thread.sleep(500)
                retry()
            })
        }

    }

    private fun testMethod() {

        testmethod2()
    }

    private fun testmethod2() {
        SmartLogger.logDebug("hello ")
        SmartLogger.logError("Error! ")

    }

    private fun retry() {
        doAsync {
            uiThread {
                retryHelper!!.retry()

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        ObserverList.getTestObserver().removeObserver(this)
        DefaultNetworkErrorDialog.stopShowingDefaultNetworkErrorDialog(this)


    }

    override fun observeChanges(list: MutableList<String>?) {
        Toast.makeText(this, list!!.size.toString(), Toast.LENGTH_LONG).show()
    }


    private fun startNotificationBadgeTest() {
        val myIntent = Intent(this, NotificationBadgeTest::class.java)
//        myIntent.putExtra("key", value) //Optional parameters
        this.startActivity(myIntent)
    }


//    private fun startPage(NextActivity: Class<*>) {
//        val myIntent = Intent(this, NextActivity::class.java)
////        myIntent.putExtra("key", value) //Optional parameters
//        this.startActivity(myIntent)
//    }
}