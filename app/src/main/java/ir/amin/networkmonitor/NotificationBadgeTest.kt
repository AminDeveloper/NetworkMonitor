package ir.amin.networkmonitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import ir.amin.networkmonitor.Activities.BaseActivity
import ir.amin.networkmonitor.utils.NotificationHelper
import ir.amin.networkmonitor.utils.SmartLogger
import ir.amin.networkmonitor.utils.Utils

/**
 *
 */
class NotificationBadgeTest : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_badge_test)
        findViewById<Button>(R.id.button).setOnClickListener { showNotification() }
        checkIntent(intent)
        NotificationHelper.refreshnotificationCount(this)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntent(intent)
    }

    private fun checkIntent(intent: Intent?) {
        SmartLogger.logDebug()
    }


    private fun showNotification() {
        Utils.createNotification(
            this,
            NotificationBadgeTest::class.java,
            R.drawable.ic_launcher_foreground,
            "title",
            "hello",
            "type"
        )
    }
}