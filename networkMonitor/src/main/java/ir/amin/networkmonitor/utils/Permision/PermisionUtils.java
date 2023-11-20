package ir.amin.networkmonitor.utils.Permision;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import ir.amin.networkmonitor.utils.Permision.Activity.PermisionActivity;

/**
 * Created by Amin on 9/20/2017.
 * should be used inside activity like baseActivity
 */

public class PermisionUtils {

    private Activity activity;
    Context context;

    public PermisionUtils(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public PermisionUtils(Context context) {
        this.context = context;

    }

    private final Semaphore permisionSemaphore = new Semaphore(1, true);

    public static final int MY_PERMISSIONS_REQUEST = 24;
    private final static AtomicInteger permitionCode = new AtomicInteger(MY_PERMISSIONS_REQUEST);

    private Map<Integer, PermisionGrantListener> permissionListeners = new HashMap<>();

    public void requestPermisions(final String[] permissions, final PermisionGrantListener permisionGrantListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    permisionSemaphore.acquire();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doRequestPermisions(permissions, permisionGrantListener);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private synchronized void doRequestPermisions(String[] permissions, PermisionGrantListener permisionGrantListener) {
        int permissionRequestCode = permitionCode.getAndIncrement();

//        int permissionRequestCode = MY_PERMISSIONS_REQUEST;
        if (permisionGrantListener != null) {
//            permissionRequestCode = MY_PERMISSIONS_REQUEST + permissionListeners.size();
            permissionListeners.put(permissionRequestCode, permisionGrantListener);
        }
        // Should we show an explanation?
        boolean requestNeeded = false;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context,
                    permission) != PackageManager.PERMISSION_GRANTED)
                requestNeeded = true;
        }
        if (requestNeeded) {
            // we can request the permission.
            if (activity == null) {
                startPermisionActivity(permissions, permissionRequestCode);
            } else {
                ActivityCompat.requestPermissions(activity,
                        permissions,
                        permissionRequestCode);
                // The callback method gets the
                // result of the request.
            }


        } else {
            permisionSemaphore.release();
            permisionGrantListener.onPermisionGranted();
        }
    }

    private void startPermisionActivity(String[] permissions, int permissionRequestCode) {
        PermisionActivity.permisionUtils=this;
        Intent intent = new Intent(context, PermisionActivity.class);
        intent.putExtra("permissions", permissions);
        intent.putExtra("permissionRequestCode", permissionRequestCode);
        context.startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        permisionSemaphore.release();
        PermisionGrantListener permisionGrantListener = permissionListeners.get(requestCode);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST: {
        // If request is cancelled, the result arrays are empty.
        if (permisionGrantListener != null)
            if (grantResults.length > 0) {
                boolean allGranted = true;
                for (int result : grantResults)
                    if (result != PackageManager.PERMISSION_GRANTED)
                        allGranted = false;
                // permission was granted, yay! Do the
                if (allGranted)
                    permisionGrantListener.onPermisionGranted();
                else
                    permisionGrantListener.onPermisionDenied();
            } else {
                permisionGrantListener.onPermisionDenied();

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        return;
//            }

        // other 'case' lines to check for other
        // permissions this app might request
//        }
    }

    public interface PermisionGrantListener {
        void onPermisionGranted();

        void onPermisionDenied();
    }
}
