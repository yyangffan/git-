package com.superc.allihaveuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.superc.allihaveuse.adapter.MvpAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.mian_tv_finish)
    TextView mMianTvFinish;
    @BindView(R.id.main_ll)
    LinearLayout mMainLl;
    @BindView(R.id.main_mvp)
    ViewPager mMainMvp;
    private List<View> mViews;
    private List<ImageView> mImageViews;
    private MvpAdapter mMvpAdapter;
    private int num = 0;
    private int[] mInts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mViews=new ArrayList<>();
        mImageViews=new ArrayList<>();
        mMvpAdapter = new MvpAdapter(this, mViews, null);
        mMainMvp.setAdapter(mMvpAdapter);
        mInts = new int[]{R.drawable.bg, R.drawable.bg, R.drawable.bg};
        setMsg(mInts);
        mMainMvp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mImageViews.size() - 1) {
                    num++;
                    if (num == 20) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        WelcomeActivity.this.finish();
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mImageViews.size(); i++) {
                    if (i == position) {
                        mImageViews.get(i).setImageResource(R.drawable.indicator_select);
                    } else {
                        mImageViews.get(i).setImageResource(R.drawable.indicator_unselect);
                    }
                }
                if (position == (mImageViews.size() - 1)) {
                    num = 0;
                    mMianTvFinish.setVisibility(View.VISIBLE);
                } else {
                    mMianTvFinish.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*
        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                toGo();
            }
        }.start();
        */
    }

    /*设置数据*/
    public void setMsg(int[] ints) {
        mMainLl.removeAllViews();
        mImageViews.clear();
        for (int i = 0; i < ints.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(ints[i]).into(imageView);
            mViews.add(imageView);
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.leftMargin = 15;
            layoutParams.rightMargin = 15;
            img.setLayoutParams(layoutParams);
            if (i == 0) {
                img.setImageResource(R.drawable.indicator_select);
            } else {
                img.setImageResource(R.drawable.indicator_unselect);
            }
            mImageViews.add(img);
            mMainLl.addView(img);
        }
        mMvpAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.mian_tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mian_tv_finish:
                Toast.makeText(this, "跳转到登录", Toast.LENGTH_SHORT).show();
                WelcomeActivity.this.finish();
                break;
        }
    }

    private void toGo() {
//        if (mIsLogin) {
//            startActivity(new Intent(this,MainActivity.class));
//        } else {
//            Toast.makeText(this, "跳转到登录", Toast.LENGTH_SHORT).show();
//        }
        WelcomeActivity.this.finish();
    }

}
