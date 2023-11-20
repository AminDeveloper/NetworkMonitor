package ir.amin.networkmonitor.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import java.util.concurrent.atomic.AtomicInteger;
import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by Amin on 18/11/2017.
 */

public class NotificationHelper {
    private static Object lock = new Object();

    private static final String NOTIFICATION_DELETED_ACTION = "NOTIFICATION_DELETED";
    public static final String NOTIFICATION_TYPE = "notification_intent";
    public static final String NOTIFICATION_DELETE = "notification_intent";
    private static final String NOTIFICATION_ID = "notification_id";
    public static final String PENDING_INTENT = "pending_intent";
    private static final String NOTIFICATION_OPEN = "notification_open";
    private static final String NOTIFICATION_CLOSE = "notification_close";
    private static final String ACTION_TYPE = "action_type";

    private final static AtomicInteger notificationID = new AtomicInteger(0);

    public static void createNotification(Context context, Class<?> cls, int iconRes, String title, String text, String type) {
        // Prepare intent which is triggered if the
        // notification is selected
        context = context.getApplicationContext();
        int id = notificationID.incrementAndGet();

        Intent intent = new Intent(context, cls);
        intent.putExtra(NOTIFICATION_TYPE, type);
        intent.putExtra(NOTIFICATION_ID, id);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        // Build notification
        // Actions are just fake
        Notification noti = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            noti = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(text).setSmallIcon(iconRes)
                    .setContentIntent(getOpenServiceIPIntent(context, id, type, pIntent))
                    .setDeleteIntent(getcloseServiceIPIntent(context, id, type))
                    .setSound(soundURI)
                    .setChannelId(getnotificationChannelID(context))

                    .build();
        }else
            noti = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(text).setSmallIcon(iconRes)
                    .setContentIntent(getOpenServiceIPIntent(context, id, type, pIntent))
                    .setDeleteIntent(getcloseServiceIPIntent(context, id, type))
                    .setSound(soundURI)
                    .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, noti);

        showBadge(context, increaseAndGetCount(context));
//        SmartLogger.Companion.logDebug(String.valueOf(notCount));


    }
    public static void createNotification(Context context, Intent intent, int iconRes, String title, String text) {
        // Prepare intent which is triggered if the
        // notification is selected
        context = context.getApplicationContext();
        int id = notificationID.incrementAndGet();

        intent.putExtra(NOTIFICATION_ID, id);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        // Build notification
        // Actions are just fake
        Notification noti = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            noti = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(text).setSmallIcon(iconRes)
                    .setContentIntent(getOpenServiceIPIntent(context, id, "", pIntent))
                    .setDeleteIntent(getcloseServiceIPIntent(context, id, ""))
                    .setSound(soundURI)
                    .setChannelId(getnotificationChannelID(context))
                    .build();
        }else
            noti = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(text).setSmallIcon(iconRes)
                    .setContentIntent(getOpenServiceIPIntent(context, id, "", pIntent))
                    .setDeleteIntent(getcloseServiceIPIntent(context, id, ""))
                    .setSound(soundURI)
                    .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, noti);

        showBadge(context, increaseAndGetCount(context));
//        SmartLogger.Companion.logDebug(String.valueOf(notCount));


    }


    private static String getnotificationChannelID(Context context)  {
        String channelId = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =new NotificationChannel(channelId, "name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.createNotificationChannel(notificationChannel);

        } else {
        }
        return channelId;

    }

//    public static PendingIntent getDeletePendingIntent(Context context, int id, String type) {
//
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // Do what you want here
//                notCount--;
//                showBadge(context, notCount);
//
//                context.unregisterReceiver(this);
//            }
//        };
//        Intent intent = new Intent(NOTIFICATION_DELETED_ACTION + "close" + notificationID);
//        intent.putExtra(NOTIFICATION_ID, id);
//        intent.putExtra(NOTIFICATION_TYPE, type);
//
//        PendingIntent pendintIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        context.registerReceiver(receiver, new IntentFilter(NOTIFICATION_DELETED_ACTION +"close" + notificationID));
//        return pendintIntent;
//    }
//
//    public static PendingIntent getOpenPendingIntent(Context context, int id, String type, final PendingIntent pIntent) {
//
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // Do what you want here
//                try {
//                    pIntent.send();
//                } catch (PendingIntent.CanceledException e) {
//                    e.printStackTrace();
//                }
//                notCount--;
//                showBadge(context, notCount);
//                context.unregisterReceiver(this);
//            }
//        };
//        Intent intent = new Intent(NOTIFICATION_DELETED_ACTION + "open" + notificationID);
//        intent.putExtra(NOTIFICATION_ID, id);
//        intent.putExtra(NOTIFICATION_TYPE, type);
//
//        PendingIntent pendintIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        context.registerReceiver(receiver, new IntentFilter(NOTIFICATION_DELETED_ACTION + "open" + notificationID));
//        return pendintIntent;
//    }

    private static PendingIntent getOpenServiceIPIntent(Context context, int id, String type, final PendingIntent pIntent) {
        Intent intent = new Intent(context, NotificationService.class);

        intent.putExtra(NOTIFICATION_ID, id);
        intent.putExtra(NOTIFICATION_TYPE, type);
        intent.putExtra(ACTION_TYPE, NOTIFICATION_OPEN);

        intent.putExtra(PENDING_INTENT, pIntent);

        return PendingIntent.getService(context, (int) System.currentTimeMillis(), intent, 0);
    }

    private static PendingIntent getcloseServiceIPIntent(Context context, int id, String type) {
        Intent intent = new Intent(context, NotificationService.class);

        intent.putExtra(NOTIFICATION_ID, id);
        intent.putExtra(NOTIFICATION_TYPE, type);
        intent.putExtra(ACTION_TYPE, NOTIFICATION_CLOSE);

        intent.removeExtra(PENDING_INTENT);

        return PendingIntent.getService(context, id, intent, 0);
    }


    public static int decreaseAndGetCount(Context context) {
        int count;
//        count = getNotificationCountWithAPI(context);
//        if (count == -1)
        synchronized (lock) {
            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(context);
            count = sharedPreferenceHelper.getNotificationCount();
            count--;
            if (count < 0)
                count = 0;
            sharedPreferenceHelper.setNotificationCount(count);
        }
        return count;
    }

    private static int getNotificationCountWithAPI(Context context) {
//        this will cause delay
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            StatusBarNotification[] notifications = notificationManager.getActiveNotifications();
            return notifications.length;
        } else
            return -1;
    }

    public static int increaseAndGetCount(Context context) {
        int count;
//        int count = getNotificationCountWithAPI(context);
//        if (count == -1)
        synchronized (lock) {
            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(context);
            count = sharedPreferenceHelper.getNotificationCount();
            count++;
            sharedPreferenceHelper.setNotificationCount(count);
        }
        return count;
    }

    public static void showBadge(Context context, int badgeCount) {
//        ShortcutBadger.applyCount(context, badgeCount);
    }

    public static void refreshnotificationCount(Context context) {
        int count = getNotificationCountWithAPI(context);
        if (count == -1)
            count = 0;
        new SharedPreferenceHelper(context).setNotificationCount(count);
        showBadge(context, count);
    }


}