package ir.amin.networkmonitor.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Locale;

import ir.amin.networkmonitor.Activities.BaseActivity;
import ir.amin.networkmonitor.R;
import ir.amin.networkmonitor.utils.Permision.PermisionUtils;


/**
 *
 */

public class Utils {

    public static final String NOTIFICATION_COMMON_DATA = "notification_common_data";

    public static ContextWrapper wrap(Context context, String language) {
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }
        if (!language.equals("") && !sysLocale.getLanguage().equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config);
            } else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }
        return new ContextWrapper(context);
    }

    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    public static void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    public static void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }

    /**
     * Kill the app either safely or quickly. The app is killed safely by
     * killing the virtual machine that the app runs in after finalizing all
     * {@link Object}s created by the app. The app is killed quickly by abruptly
     * killing the process that the virtual machine that runs the app runs in
     * without finalizing all {@link Object}s created by the app. Whether the
     * app is killed safely or quickly the app will be completely created as a
     * new app in a new virtual machine running in a new process if the user
     * starts the app again.
     * <p>
     * <p>
     * <B>NOTE:</B> The app will not be killed until all of its threads have
     * closed if it is killed safely.
     * </P>
     * <p>
     * <p>
     * <B>NOTE:</B> All threads running under the process will be abruptly
     * killed when the app is killed quickly. This can lead to various issues
     * related to threading. For example, if one of those threads was making
     * multiple related changes to the database, then it may have committed some
     * of those changes but not all of those changes when it was abruptly
     * killed.
     * </P>
     *
     * @param killSafely Primitive boolean which indicates whether the app should be
     *                   killed safely or quickly. If true then the app will be killed
     *                   safely. Otherwise it will be killed quickly.
     */
    public static void killApp(boolean killSafely) {
        if (killSafely) {
            /*
             * Notify the system to finalize and collect all objects of the app
             * on exit so that the virtual machine running the app can be killed
             * by the system without causing issues. NOTE: If this is set to
             * true then the virtual machine will not be killed until all of its
             * threads have closed.
             */
            System.runFinalizersOnExit(true);

            /*
             * Force the system to close the app down completely instead of
             * retaining it in the background. The virtual machine that runs the
             * app will be killed. The app will be completely created as a new
             * app in a new virtual machine running in a new process if the user
             * starts the app again.
             */
            System.exit(0);
        } else {
            /*
             * Alternatively the process that runs the virtual machine could be
             * abruptly killed. This is the quickest way to remove the app from
             * the device but it could cause problems since resources will not
             * be finalized first. For example, all threads running under the
             * process will be abruptly killed when the process is abruptly
             * killed. If one of those threads was making multiple related
             * changes to the database, then it may have committed some of those
             * changes but not all of those changes when it was abruptly killed.
             */
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }

    public static void restartApp(Context context, int delay) {

        if (delay == 0) {
            delay = 200;
        }
        Log.e("", "restarting app");
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        @SuppressLint("WrongConstant") PendingIntent intent = PendingIntent.getActivity(
                context, 0,
                restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP | PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(0);

    }

    public static float pxFromDP(float dp, Context context) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();// Caused by: java.lang.SecurityException: ConnectivityService: Neither user 10082 nor current process has android.permission.ACCESS_NETWORK_STATE.
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isConnectedToThisServer(String host, int timeout) {
        return checkIsReachable(host, timeout);

//        try {
//            return InetAddress.getByName(host).isReachable(timeout);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        Runtime runtime = Runtime.getRuntime();
//        try {
//
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + host);
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return false;
    }

    public static boolean checkIsReachable(String url, int timeOut) {
//    SmartLogger.logDebug("ping call ")

        if (url == "#" || url == "")
            return false;
//    SmartLogger.logDebug("url = $url")

        String urlToCheck = url.replace("https://", "");
        urlToCheck = urlToCheck.replace("http://", "");
//    SmartLogger.logDebug("urlToCheck = $urlToCheck")
        boolean isReachable =
                isReachable(urlToCheck, timeOut);
//    SmartLogger.logDebug("isReachable = $isReachable")
        return isReachable;
    }

    public static boolean isReachable(String addr, int timeOutMillis) {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        int openPort = 80;
        try {
            Socket socket = new Socket();

            socket.connect(
                    new InetSocketAddress(addr, openPort),
                    timeOutMillis
            );
            return true;
        } catch (IOException ex) {
            return false;
        }
    }


    public static Point getDisplayDimention(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getDisplayDimention(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Point outSize = new Point();

        outSize.x = width;
        outSize.y = height;
        return outSize;
    }

    public static String localeNumber(String string) {
        if (string == null)
            return "";

        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                int index = (int) (string.charAt(i)) - 48;
                if (index >= 0 && index <= 9)
                    builder.append(arabicChars[(int) (string.charAt(i)) - 48]);
                else
                    builder.append(string.charAt(i));

            } else {
                builder.append(string.charAt(i));
            }
        }
        return builder.toString();


//        Locale loc = new Locale("fa");
//        return String.format(loc, "%s", string);
    }

    public static String getPathFromURI(Context context, Uri data) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(data, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;


    }

    public static String getExtention(File file) {
        int index = file.getName().lastIndexOf('.') + 1;
        return file.getName().substring(index).toLowerCase();
    }

    public static void runOnMainThread(Runnable runnable, View view) {
        view.post(runnable);

    }

    public static void runOnMainThread(Runnable runnable, Activity activity) {
        activity.runOnUiThread(runnable);
    }

    public static void chageProgressbarColor(ProgressBar progressBar, Context context, int color) {
// fixes pre-Lollipop progressBar indeterminateDrawable tinting

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
        }
    }

    public static Dialog showLoadingDialog(Context context) {
        Dialog loadingDialog = new Dialog(context);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater mInflater = LayoutInflater.from(loadingDialog.getContext());
        View layout = mInflater.inflate(R.layout.progress_bar_dialog, null);
        loadingDialog.setContentView(layout);
        ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        Utils.chageProgressbarColor(progressBar, context, R.color.blue);

//            TextView mTextView = (TextView) layout.findViewById(R.id.text);
//            if (text.equals(""))
//                mTextView.setVisibility(View.GONE);
//            else
//                mTextView.setText(text);

        loadingDialog.setCancelable(false);
        // aiImage.post(new Starter(activityIndicator));
        loadingDialog.show();
        return loadingDialog;
    }

    public static void openBrowser(Activity activity, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void createNotification(Context context, Class<?> cls, int iconRes, String title, String text, String type) {
        NotificationHelper.createNotification(context, cls, iconRes, title, text, type);

    }


}
