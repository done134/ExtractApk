package com.done.extractapk;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.done.extractapk.adapter.AppListAdapter;
import com.done.extractapk.entity.AppInfo;
import com.done.extractapk.utils.AppUtils;
import com.done.extractapk.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int READ_AND_WRITE = 1;

    ArrayList<AppInfo> appList; //用来存储获取的应用信息数据
    List<PackageInfo> packages;
    RecyclerView mRecyclerView;
    AppListAdapter appListAdapter;
    AppInfo appInfo;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.app_list);
//        checkFilePermissions();
        getApplications();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        appListAdapter = new AppListAdapter();
        appListAdapter.addList(appList);
        appListAdapter.setOnItemClickListener(new AppListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, AppInfo data) {
                if (appInfo != null)
                    appInfo = null;
                appInfo = data;
                showBottomSheet();
            }
        });
        mRecyclerView.setAdapter(appListAdapter);

    }

    @AfterPermissionGranted(READ_AND_WRITE)
    private boolean checkFilePermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        }else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera),
                    READ_AND_WRITE, perms);
        }
        return false;
    }

    /**
     * 获取应用列表
     */
    private void getApplications() {
        appList = new ArrayList<>();
        packages = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo =new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.path = packageInfo.applicationInfo.sourceDir;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
            tmpInfo.flag = packageInfo.applicationInfo.flags;
            //Only display the non-system app info
            if ((tmpInfo.flag & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);//如果非系统应用，则添加至appList
            }

        }
        Log.e(TAG, "appCount:" + appList.size());
//        for (AppInfo appInfo : appList) {
//            Log.e(TAG, appInfo.toString());
//        }
        //好啦 这下手机上安装的应用数据都存在appList里了。


    }

    /**
     * 弹出底部菜单
     */
    private void showBottomSheet() {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setTitle("操作");
//        bottomSheetDialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.mipmap.ic_launcher);
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                appInfo = null;
            }
        });
        bottomSheetDialog.show();
    }

    public void onClick(View view) {
        if (!checkFilePermissions()) {
            return;
        }
        switch (view.getId()) {

            case R.id.extract_apk:
                String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + appInfo.appName + ".apk";
                if (FileUtils.copyFile(appInfo.path,newPath)) {
                    Toast.makeText(this, "安装包已复制到：" + newPath, Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "安装包提取失败", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.share_apk:
                AppUtils.shareMsg(this, "test", "test", "test", appInfo.path);
                break;
            case R.id.uninstall_app:
                Uri packageURI = Uri.parse("package:"+appInfo.packageName);
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
                break;
            case R.id.open_app:
                PackageManager packageManager = getPackageManager();
                Intent intent;
                intent =packageManager.getLaunchIntentForPackage(appInfo.packageName);
                if(intent==null){
                    System.out.println("APP not found!");
                }
                startActivity(intent);

                break;
        }
        bottomSheetDialog.dismiss();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "没有被授予权限", Toast.LENGTH_LONG).show();
    }
}
