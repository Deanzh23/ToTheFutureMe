package com.dean.tothefutureme.main;

import android.content.Intent;

import com.dean.android.framework.convenient.activity.ConvenientMainActivity;
import com.dean.android.framework.convenient.file.download.listener.FileDownloadListener;
import com.dean.android.framework.convenient.version.VersionUpdate;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.auth.activity.LoginActivity;
import com.dean.tothefutureme.databinding.ActivityMainBinding;

@ContentView(R.layout.activity_main)
public class MainActivity extends ConvenientMainActivity<ActivityMainBinding> {

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
        startActivity(new Intent(this, LoginActivity.class));
        MainActivity.this.finish();
    }
}
