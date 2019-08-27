package com.example.livingbusiness.util.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.livingbusiness.BuildConfig;
import com.example.livingbusiness.R;
import com.example.livingbusiness.base.ApiService;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yf_lib.utils.ShareUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by user on 2017/11/23.
 */

public class UpdateManager {

    private Context context;
    private boolean isInterceptDownload;
    private String apkName;
    private final int COMMIT_WHAT = 0x001;
    private String apkUrl;
    private boolean isUpdate;
    private String urlString = "https://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk";

    // 提示信息对话框
    AlertDialog updateDialog;

    // 下载对话框
    AlertDialog downloadDialog;
    private View viewUpdate;
    private TextView tvProgress;
    private TextView tvTotal;

    private boolean isToast = false;

    private int appVersionCode;
    private ProgressBar progressBar;
    private int progress;
    private String versionName;
    private OnCancelClickListener mOnCancelClickListener;
    private static final String TAG = "UpdateManager";
    private IsUpDateListener mIsUpDateListener;
    private boolean isqiangz = false;
    private boolean isGents=false;
    /**
     * 声明一个handler来跟进进度条
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新进度情况
                    progressBar.setProgress(progress);
                    tvProgress.setText(progress + "");
                    break;
                case 0:
                    progressBar.setVisibility(View.INVISIBLE);
                    downloadDialog.dismiss();
                    // 安装apk文件
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Context context, boolean isToast) {
        super();
        this.context = context;
        this.isToast = isToast;
    }

    /**
     * 调用这个方法来进行更新
     */
    public void checkUpdate() {
        apkName = "ruijilingshoubang.apk";
        // 获取当前版本信息
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionName = packageInfo.versionName;
            appVersionCode = packageInfo.versionCode;
//            showUpdateDialog("需要更新才能使用");
            request();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void showUpdateDialog(String msg) {
        Log.e(TAG, "showUpdateDialog: show dialog");
        isInterceptDownload = true;
        updateDialog = new AlertDialog.Builder(context).create();
        updateDialog.show();
        viewUpdate = LayoutInflater.from(context).inflate(R.layout.dialog_version, null);
//        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int screenHeight = manager.getDefaultDisplay().getHeight();
//        Window dialogWindow = updateDialog.getWindow();
//        WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
//        wlp.height = screenHeight / 3;
//        dialogWindow.setAttributes(wlp);
        updateDialog.setContentView(viewUpdate);
        TextView tvTip = (TextView) viewUpdate.findViewById(R.id.tv_tips);
        Button btnCancel = (Button) viewUpdate.findViewById(R.id.btn_cancle);
        Button btnDelete = (Button) viewUpdate.findViewById(R.id.btn_delete);
        Button btnSure = (Button) viewUpdate.findViewById(R.id.btn_sure);
        tvTip.setText(msg);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SPUtils.put(context, "UpdateTip", version);
                updateDialog.dismiss();
                if (mOnCancelClickListener != null) {
                    mOnCancelClickListener.OnCancelClickListener(isqiangz);
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.getInstance(context).put("DeleteTip", false);
                updateDialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInterceptDownload = false;
                updateDialog.dismiss();
                showDownLoadDialog();
            }
        });
        updateDialog.setCancelable(false);

    }

    public interface OnCancelClickListener {
        void OnCancelClickListener(boolean is_qiangz);
    }

    /**
     * 弹出下载框
     */
    private void showDownLoadDialog() {
        downloadDialog = new AlertDialog.Builder(context).create();
        downloadDialog.show();
        downloadDialog.setTitle("版本更新中...");
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_progress, null);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
        Button btnPause = (Button) v.findViewById(R.id.pause);
        tvProgress = (TextView) v.findViewById(R.id.tv_progress);
        tvTotal = (TextView) v.findViewById(R.id.tv_total);
        downloadDialog.setContentView(v);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 终止下载
                isInterceptDownload = true;
                downloadDialog.dismiss();
                Button btn = (Button) viewUpdate.findViewById(R.id.btn_sure);
                btn.setText("继续");
                updateDialog.show();
            }
        });

        downloadDialog.setCancelable(false);

        // 下载apk
        downLoadApk();
    }

    /**
     * 开启新线程下载apk
     */
    public void downLoadApk() {
        // 执行下载的方法
        Log.i(TAG, "downLoadApk: 版本更新，执行下载的方法");
        Thread downloadThread = new Thread(downloadRunnable);
        downloadThread.start();
    }

    /**
     * 下载apk的线程
     */
    private Runnable downloadRunnable = new Runnable() {

        @Override
        public void run() {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 如果没有SD卡
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("当前设备无SD卡，数据无法下载");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                return;
            } else {
                try {
                    Log.i("UPdateManager", "版本更新，已经在执行下载的方法");
                    // 服务器上新版apk地址,这应该是从服务器上下载的json解析下来的地址 apkUrl
                    // "http://gdown.baidu.com/data/wisegame/3a60158cef157a6d/QQ_372.apk"
                    URL url = new URL(urlString);
                    HttpURLConnection connection;
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Accept-Encoding", "identity");
                    connection.connect();
                    int length = connection.getContentLength();
                    InputStream is = connection.getInputStream();
                    File file = new File(Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/updateApkFile/");
                    if (!file.exists()) {
                        // 如果文件夹不存在,则创建
                        file.mkdir();
                    }
                    // 下载服务器中新版本软件（写文件）
                    String apkFileName = Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/updateApkFile/" + apkName;
                    File apkFile = new File(apkFileName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    byte[] buf = new byte[1024];

                    while (true) {
                        // 当点击暂停时，则暂停下载
                        if (!isInterceptDownload) {
                            int numRead = is.read(buf);
                            count += numRead;
                            progress = (int) (((float) count / length) * 100);
                            Log.d("安装包大小", "安装包大小：" + length + "  下载大小：" + count + "  下载百分比：" + progress);
                            handler.sendEmptyMessage(1);
                            if (numRead <= 0) {
                                // 下载完成通知安装
                                handler.sendEmptyMessage(0);
                                break;
                            }
                            fos.write(buf, 0, numRead);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * 安装apk
     */
    private void installApk() {
        // 获取当前sdcard存储路径
        File apkfile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/updateApkFile/" + apkName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        // 安装，如果签名不一致，可能出现程序未安装提示
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            i.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider",new File(apkfile.getAbsolutePath())),"application/vnd.android.package-archive");
        }else {
            i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(i);
    }

    /**
     * 进行接口查询获取新版本app网络路径
     * UPDATE="接口访问地址"
     * client 2-安卓  1-ios
     */
    private void request() {
        Map<String, Object> map = new HashMap<>();
        map.put("client", "2");
        map.put("version", versionName);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), com.alibaba.fastjson.JSONObject.toJSONString(map));
        Observable<com.alibaba.fastjson.JSONObject> jsonObjectObservable = DevRing.httpManager().getService(ApiService.class).getByClient(requestBody);
        DevRing.httpManager().commonRequest(jsonObjectObservable, new CommonObserver<JSONObject>() {
            @Override
            public void onResult(JSONObject result) {
                String status = result.getString("status");
                if (status.equals("1.0")) {
                    JSONObject data = result.getJSONObject("data");
                    urlString = data.getString("updateurl");//更新地址
                    String updatecontent = data.getString("updatecontent");//更新说明
                    isGents = data.getBoolean("consistent");//是否更新
                    isqiangz = data.getInteger("forcedupdates") == 0 ? false : true;//是否强制更新 0否 1是
//                    JSONObject data = result.getJSONObject("data");
//                    urlString ="https://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk";//更新地址
//                    String updatecontent = "更新后才能使用，请进行更新";//更新说明
//                    isGents = true;//是否更新
//                    isqiangz = data.getInteger("forcedupdates") == 0 ? true : false;//是否强制更新 0否 1是
                    if (isGents) {
                        toSetListener(true);
                        showUpdateDialog(updatecontent);
                    } else {
                        toSetListener(false);
                    }
                } else {
                    toSetListener(false);
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                toSetListener(false);
                Log.e(TAG, "onError: " + httpThrowable.toString());
            }
        }, (LifecycleTransformer) null);
    }

    private void toSetListener(boolean isWhat) {
        if (mIsUpDateListener != null) {
            mIsUpDateListener.IsUpDateListener(isWhat);
        }
    }


    public boolean isGents() {
        return isGents;
    }

    /**
     * 设置要下载的apk的url地址，在个人中心版本更新页面要用到此方法
     */
    public void setUrl(String urlString) {
        this.urlString = urlString;
    }

    public void setTextView(TextView tvProgress) {
        this.tvProgress = tvProgress;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setDownloadDialog(AlertDialog downloadDialog) {
        this.downloadDialog = downloadDialog;
    }

    public interface IsUpDateListener {
        void IsUpDateListener(boolean isUpdate);
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
    }

    public void setIsUpDateListener(IsUpDateListener isUpDateListener) {
        mIsUpDateListener = isUpDateListener;
    }

}
