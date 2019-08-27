package com.example.livingbusiness.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.example.livingbusiness.util.update.UpdateManager;
import com.superc.yf_lib.base.BaseActivity;
import com.superc.yf_lib.utils.ShareUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";
    private boolean mIsLogin;
    private UpdateManager mUpdateManager;
    /*
   //    TextView mMianTvFinish;
   //    @BindView(R.id.mian_tv_finish)
   //    LinearLayout mMainLl;
   //    @BindView(R.id.main_ll)
   //    ViewPager mMainMvp;
//    @BindView(R.id.main_mvp)
//    private List<View> mViews;
//    private List<ImageView> mImageViews;
//    private MvpAdapter mMvpAdapter;
//    private int[] mInts;
//    private boolean mIs_fist;
//    private int num = 0;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           /*
//        setContentView(R.layout.activity_welcome);
//        ButterKnife.bind(this);
*/
        init();
    }

    @Override
    public void init() {
        mIsLogin = (boolean) ShareUtil.getInstance(this).get("isLogin", false);
        rxPermission();

    }

    /*请求相机权限*/
    private void rxPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {
                    checkUpdate();
                } else {
                    ToastShow("需要该权限才能正常使用");
                    rxPermission();
                }
            }
        });
    }

    public void checkUpdate() {
        mUpdateManager = new UpdateManager(this, false);
        mUpdateManager.checkUpdate();
        mUpdateManager.setOnCancelClickListener(new UpdateManager.OnCancelClickListener() {
            @Override
            public void OnCancelClickListener(boolean isqiangz) {//弹窗后不进行更新
                if(isqiangz){
                    WelcomeActivity.this.finish();
                }else{
                    toGo();
                }
            }
        });
        mUpdateManager.setIsUpDateListener(new UpdateManager.IsUpDateListener() {
            @Override
            public void IsUpDateListener(boolean isUpdate) {
                if (!isUpdate) {//不需要更新
                    toGoWhat();
                }
            }
        });
    }

    private void toGoWhat() {
          /*
//        setUser_titleBar(false);
//        mIs_fist = (boolean) ShareUtil.getInstance(this).get("is_fist", true);
//        mInts = new int[]{R.drawable.load_o, R.drawable.load_two, R.drawable.load_three};
//        mViews = new ArrayList<>();
//        mImageViews = new ArrayList<>();
//
//        mMvpAdapter = new MvpAdapter(this, mViews, null);
//        mMainMvp.setAdapter(mMvpAdapter);
//        mMainMvp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position == mImageViews.size() - 1) {
//                    num++;
//                    Log.d(TAG, "onPageScrolled:positionOffset=" + positionOffset + "  positionOffsetPixels=" + positionOffsetPixels);
//                    Log.d(TAG, "onPageScrolled: num=" + num);
//                    if (num == 20) {
//                        stActivity(LoginActivity.class);
//                        WelcomeActivity.this.finish();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                for (int i = 0; i < mImageViews.size(); i++) {
//                    if (i == position) {
//                        mImageViews.get(i).setImageResource(R.drawable.indicator_select);
//                    } else {
//                        mImageViews.get(i).setImageResource(R.drawable.indicator_unselect);
//                    }
//                }
//                if (position == (mImageViews.size() - 1)) {
//                    num=0;
//                    mMianTvFinish.setVisibility(View.VISIBLE);
//                } else {
//                    mMianTvFinish.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        if (mIsLogin) {
//            stActivity(MainActivity.class);
//            WelcomeActivity.this.finish();
//        } else {
//            if (mIs_fist) {
//                ShareUtil.getInstance(this).put("is_fist", false);
//                setMsg(mInts);
//            } else {
//                stActivity(LoginActivity.class);
//                WelcomeActivity.this.finish();
//            }
//        }
*/
  /*设置数据
//    public void setMsg(int[] ints) {
//        mMainLl.removeAllViews();
//        mImageViews.clear();
//        for (int i = 0; i < ints.length; i++) {
//            ImageView imageView = new ImageView(this);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            Glide.with(this).load(ints[i]).into(imageView);
//            mViews.add(imageView);
//            ImageView img = new ImageView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
//            layoutParams.leftMargin = 15;
//            layoutParams.rightMargin = 15;
//            img.setLayoutParams(layoutParams);
//            if (i == 0) {
//                img.setImageResource(R.drawable.indicator_select);
//            } else {
//                img.setImageResource(R.drawable.indicator_unselect);
//            }
//            mImageViews.add(img);
//            mMainLl.addView(img);
//        }
//        mMvpAdapter.notifyDataSetChanged();
//    }
    */
        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                toGo();
            }
        }.start();
    }

    private void toGo(){
        if (mIsLogin) {
            stActivity(MainActivity.class);
        } else {
            stActivity(LoginActivity.class);
        }
        WelcomeActivity.this.finish();
    }


    /*    @OnClick({R.id.mian_tv_finish})*/
    public void onClick(View view) {
        /*
//        switch (view.getId()) {
//            case R.id.mian_tv_finish:
//                stActivity(LoginActivity.class);
//                WelcomeActivity.this.finish();
//                break;
//        }
*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mUpdateManager.isGents()){
            finish();
        }
    }
}
