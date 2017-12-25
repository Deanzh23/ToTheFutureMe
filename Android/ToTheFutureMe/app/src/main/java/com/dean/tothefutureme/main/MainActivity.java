package com.dean.tothefutureme.main;

import android.content.Intent;
import android.os.Handler;

import com.dean.android.framework.convenient.activity.ConvenientMainActivity;
import com.dean.android.framework.convenient.file.download.listener.FileDownloadListener;
import com.dean.android.framework.convenient.util.TextUtils;
import com.dean.android.framework.convenient.version.VersionUpdate;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.auth.activity.LoginActivity;
import com.dean.tothefutureme.databinding.ActivityMainBinding;
import com.dean.tothefutureme.home.HomeActivity;
import com.umeng.analytics.MobclickAgent;

@ContentView(R.layout.activity_main)
public class MainActivity extends ConvenientMainActivity<ActivityMainBinding> {

    private Handler handler = new Handler();

    @Override
    protected boolean isUseDefaultDownloadDialog() {
        return false;
    }

    @Override
    protected FileDownloadListener getFileDownloadListener() {
        return null;
    }

    @Override
    protected void showUpdateDownload(VersionUpdate versionUpdate) {
    }

    @Override
    protected void closeMainToHomeActivity() {
        String token = TTFMApplication.getAuthModel().getToken();

        handler.post(() -> {
            MobclickAgent.openActivityDurationTrack(false);

            startActivity(new Intent(MainActivity.this, TextUtils.isEmpty(token) ? LoginActivity.class : HomeActivity.class));
            MainActivity.this.finish();
        });
    }
}
