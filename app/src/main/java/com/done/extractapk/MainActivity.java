package com.done.extractapk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.done.extractapk.adapter.AppListAdapter;
import com.done.extractapk.entity.AppInfo;
import com.done.extractapk.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    ArrayList<AppInfo> appList; //用来存储获取的应用信息数据
    List<PackageInfo> packages;
    RecyclerView mRecyclerView;
    AppListAdapter appListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.app_list);
        getApplications();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        appListAdapter = new AppListAdapter();
        appListAdapter.addList(appList);
        appListAdapter.setOnItemClickListener(new AppListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, AppInfo data) {

                String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + data.appName + ".apk";
                if (FileUtils.copyFile(data.path,newPath)) {
                    //打开文件管理器
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    File file = new File(newPath);
                    intent.setDataAndType(Uri.fromFile(file), "file/*");
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(appListAdapter);
/*

        if (FileUtils.copyApkFromAssets(this, "base.apk")){
            AlertDialog.Builder m = new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher).setMessage("是否安装？")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.apk"),
                                    "application/vnd.android.package-archive");
                            startActivity(intent);
                        }
                    });
            m.show();
        }
*/

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


}
