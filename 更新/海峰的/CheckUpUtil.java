package com.example.updata.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import com.example.updata.R;
import com.example.updata.http.Service;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.body.ProgressInfo;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.observer.DownloadObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.FileUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.File;

import io.reactivex.Observable;


public class CheckUpUtil {
    String TAG = "CheckUpUtil";
    private final static String BASEUrl = "http://47.105.214.89/";
    private static CheckUpUtil instance;
    //包名
    private static String pageName;
    //versionCode
    private static int versionCode;
    //versionName
    private static String versionName;
    //程序名称
    private static String appname;
    private AlertDialog customAlert;
    DownloadObserver mDownloadObserver;//下载请求的回调
    //上下文
    public JSONObject a = null;

    String directoryPath = "";
    File mFileSave;//下载内容将保存到此File中

    private CheckUpUtil() {


    }

    public static CheckUpUtil getInstance() {
        if (instance == null) {
            instance = new CheckUpUtil();
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Application app) {
        CrashHandler.getInstance().init(app);
        PackageManager manager = app.getPackageManager();
        PackageInfo packageInfo;
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = manager.getApplicationInfo(app.getPackageName(), 0);
            packageInfo = manager.getPackageInfo(app.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            appname = (String) manager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pageName = app.getPackageName();
        Log.d("CheckUpUtil", "pageName--->" + pageName);
        Log.d("CheckUpUtil", "versionCode--->" + versionCode);
        Log.d("CheckUpUtil", "versionName--->" + versionName);
        Log.d("CheckUpUtil", "appname--->" + appname);


        DevRing.init(app);
        DevRing.configureHttp().setBaseUrl(BASEUrl)
                .setConnectTimeout(15)//设置请求超时时长，单位秒
//                .setIsUseCookie(true)//是否开启Cookie，默认不开启
//                .setIsCookiePersistent(true)//设置Cookie是否为持久化类型
//                .setMapHeader(mapHeader)//设置全局的header信息
                .setIsUseCache(true)//设置是否启用缓存，默认不启用
//                .setCacheFolder(file)//设置缓存地址，传入的file需为文件夹，默认保存在/storage/emulated/0/Android/data/com.xxx.xxx/cache/retrofit_http_cache下
//                .setCacheSize(size)//设置缓存大小，单位byte，默认20M
//                .setCacheTimeWithNet(time)//设置有网络时缓存保留时长，单位秒，默认60秒
//                .setCacheTimeWithoutNet(time)//设置无网络时缓存保留时长，单位秒，默认一周
                .setIsUseRetryWhenError(true)//设置是否开启失败重试功能。默认不开启
                .setMaxRetryCount(2)//设置失败后重试的最大次数，默认3次
                .setTimeRetryDelay(5)//设置失败后重试的延迟时长，单位秒，默认3秒
                .setIsUseLog(true);//设置是否开启Log，默认不开启

        DevRing.create();
    }


    /**
     * 检查版本更新
     */
    public void Check(final Activity activity) {

        Observable observable = DevRing.httpManager().getService(Service.class).getDetail(BASEUrl+"api/edition/getDetail",pageName);

        DevRing.httpManager().commonRequest(observable, new CommonObserver<JSONObject>() {
            @Override
            public void onResult(JSONObject result) {
                a = result;
                if (a.getInteger("status") == 200) {
                    if (result.getJSONObject("data") != null) {
                        if (result.getJSONObject("data").getInteger("versionCode") > versionCode) {
                            dialog(activity, result.getJSONObject("data"));
                        }else{
                            Toast.makeText(activity,"暂无更新",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {

            }
        }, (LifecycleTransformer) null);


    }


    /**
     * 获取版本数据
     */
    public void GetCheck(CommonObserver listener) {

        Observable observable = DevRing.httpManager().getService(Service.class).getDetail(BASEUrl+"api/edition/getDetail",pageName);

        DevRing.httpManager().commonRequest(observable, listener, (LifecycleTransformer) null);

    }


    /**
     * 弹窗提示
     */
    public void dialog(final Activity activity, final JSONObject J) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        View v = View.inflate(activity, R.layout.dialog_check, null);
        dialog.setView(v);
        ImageView logo = v.findViewById(R.id.img_logo);
        TextView versionName = v.findViewById(R.id.versionName);
        TextView tv_size = v.findViewById(R.id.tv_size);
        TextView tv_contect = v.findViewById(R.id.tv_contect);
        final TextView tv_exit = v.findViewById(R.id.tv_exit);
        final TextView tv_ok = v.findViewById(R.id.tv_ok);
        final ProgressBar myprogress = v.findViewById(R.id.myprogress);
        Glide.with(activity).load(J.getString("logo")).into(logo);
        versionName.setText("版本：" + J.getString("versionName"));
        tv_size.setText("大小：" + J.getString("size"));
        tv_contect.setText(Html.fromHtml(J.getString("content")));
        //取消点击外部消失弹窗
        dialog.setCancelable(false);
        //创建AlertDiaLog
        dialog.create();
        //AlertDiaLog显示
        customAlert = dialog.show();

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlert.dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myprogress.setVisibility(View.VISIBLE);
                tv_ok.setEnabled(false);
                tv_exit.setEnabled(false);
                upload(myprogress, J.getString("url"), activity);
            }
        });

    }

    // 下载

    private void upload(final ProgressBar myprogress, String url, final Activity activity) {



        mFileSave = FileUtil.getFile(FileUtil.getExternalCacheDir(activity), appname+".apk");

        Observable download = DevRing.httpManager().getService(Service.class).download(url);

        if (mDownloadObserver == null) {
            mDownloadObserver = new DownloadObserver(url) {
                @Override
                public void onResult(boolean isSaveSuccess, String filePath) {
                    //请求成功回调
                    if (isSaveSuccess) {
                        Toast.makeText(activity, "下载成功，已保存至： " + filePath, Toast.LENGTH_SHORT).show();
                        openAPK(mFileSave, activity);
                    } else {
                        Toast.makeText(activity, "下载成功，保存失败", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(long progressInfoId, HttpThrowable httpThrowable) {
                    //请求失败回调
                    if (progressInfoId != 0) {
                        //下载文件过程中发生异常，一般时读写过程出错，重试即可
                        //手动终止未完成的下载请求也会回调这里
                        myprogress.setProgress(0);

                    } else {
                        //下载请求出错。
                        Toast.makeText(activity, "下载请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProgress(ProgressInfo progressInfo) {
                    myprogress.setProgress(progressInfo.getPercent());
                }
            };

            //发起新请求前，先手动终止之前的请求，避免发起多个相同的请求
            DevRing.httpManager().stopRequestByTag("download");

            //发起请求
            DevRing.httpManager().downloadRequest(mFileSave, download, mDownloadObserver, "download");
        }


    }


    private void openAPK(File file, final Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(activity, "com.gzairports.xxbjkb.gza_fids.fileprovider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);

    }

}