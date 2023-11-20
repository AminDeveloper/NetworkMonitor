package ir.amin.networkmonitor.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Locale;

import ir.amin.networkmonitor.Observers.NetworkObserverHandler;
import ir.amin.networkmonitor.utils.Permision.PermisionUtils;
import ir.amin.networkmonitor.utils.Utils;


/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity implements NetworkObserverHandler.NetworkChangeObserver {
//    private boolean showMessagesFromTop = false;


    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
    };

    PermisionUtils permisionUtils = new PermisionUtils(this);
    public OnBackPressedListener onBackPressedListener;
    public OnActivityResultListener onActivityResultListener;
    public lifecycleListener lifecycleListener;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
//        super.attachBaseContext( wrap(newBase, "fa"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lifecycleListener!=null)
            lifecycleListener.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(lifecycleListener!=null)
            lifecycleListener.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkObserverHandler.getInstance().addObserver(this);
//        chageConfiguration();
        if(lifecycleListener!=null)
            lifecycleListener.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        NetworkObserverHandler.getInstance().removeObserver(this);
        if(lifecycleListener!=null)
            lifecycleListener.onStop();
    }

    @Override
    public void onBackPressed() {
        if(onBackPressedListener==null || onBackPressedListener.onBackPressed()){
            super.onBackPressed();
        }
    }



    /**
     * changes local to persian "fa"
     */
    private void chageConfigurationToFA() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale("fa"));
        createConfigurationContext(configuration);
    }


//    public boolean checkInternetConnection() {
//        return checkInternetConnection();
//    }

    public boolean checkInternetConnection() {
        if (!Utils.isNetworkAvailable(this)) {
            showNetworkDialog();
            return false;
        } else {
            dismissNetworkDialog();
            return true;
        }
    }

//    public static boolean checkInternetConnectionShowTop(BaseActivity activity) {
//        if (!Utils.isNetworkAvailable(activity)) {
//            activity.showTopNetworkSnakeBar();
//            return false;
//        } else {
//            activity.dismisTopNetworkSnakeBar();
//            return true;
//        }
//    }


    @Override
    public void onNetworkStateChange(Boolean connected) {
        if (!connected)
            showNetworkDialog();
        else
            dismissNetworkDialog();
    }

    /**
     * dismiss network error message
     */
    protected void dismissNetworkDialog() {

    }

    /**
     * shows network error message
     */
    protected void showNetworkDialog() {


    }


    @Override
    public Context getContextForNetworkObserver() {
        return getBaseContext();
    }


    public void showTopMessage(String message) {
//        View coordinatorLayout = null;
//        if (coordinatorLayout == null)
//            coordinatorLayout = getWindow().getDecorView().getRootView();
//
//        TSnackbar snackbar = TSnackbar
//                .make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
//        View sbView = snackbar.getView();
//        sbView.setAlpha(0.6f);
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
//        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.gray2));
//        snackbar.show();
    }

    private void showBottomMessage(String message) {
//        View coordinatorLayout = null;
//        if (coordinatorLayout == null)
//            coordinatorLayout = getWindow().getDecorView().getRootView();
//        Snackbar snackbar = Snackbar
//                .make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
//        View sbView = snackbar.getView();
//
//        sbView.setAlpha(0.6f);
//
//        snackbar.show();
    }

    public void showMessage(int res) {
        showMessage(this.getString(res));
    }

    /**
     * shows a message to user
     *
     * @param string
     */
    private void showMessage(String string) {

    }

//    LoadingDialogHelper dialogHelper=new LoadingDialogHelper();

    @Deprecated
    protected void changeLocale() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(new Locale("fa"));
        resources.updateConfiguration(configuration, displayMetrics);
    }


    public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener) {
        this.onActivityResultListener = onActivityResultListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (onActivityResultListener != null)
            onActivityResultListener.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public interface OnBackPressedListener {
        /**
         *
         * @return true to pass event to parent
         */
        boolean onBackPressed();
    }
    public interface lifecycleListener{
        void onPause();
        void onResume();
        void onStart();
        void onStop();

    }


    //permisions

    /**
     * request all permisions
     */
    public synchronized void requestPermisions(PermisionUtils.PermisionGrantListener permisionGrantListener) {
        requestPermisions(permissions, permisionGrantListener);
    }

    public void requestPermisions(final String[] permissions, final PermisionUtils.PermisionGrantListener permisionGrantListener) {
        permisionUtils.requestPermisions(permissions, permisionGrantListener);
    }

    public void requestPermisions(final String permission, final PermisionUtils.PermisionGrantListener permisionGrantListener) {

        String[] permissions = new String[]{permission};
        permisionUtils.requestPermisions(permissions, permisionGrantListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permisionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

