package ir.amin.networkmonitor.utils.Permision.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import ir.amin.networkmonitor.R;
import ir.amin.networkmonitor.utils.Permision.PermisionUtils;


/**
 * Created by Amin on 9/20/2017.
 */

public class PermisionActivity extends Activity {

    public static PermisionUtils permisionUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity);
        Intent intent = getIntent();

        String[] permissions= intent.getStringArrayExtra("permissions");
        int permissionRequestCode=intent.getIntExtra("permissionRequestCode",0);
        ActivityCompat.requestPermissions(this,
                permissions,
                permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permisionUtils!=null)
            permisionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        permisionUtils=null;

    }
}
