package com.superc.allihaveuse.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by user on 2018/2/8.
 */

public class MvpAdapter extends PagerAdapter {
    private Context mContext;
    private List<View> mViewList;
    private OnClickListener mOnClickListener;

    public MvpAdapter(Context context, List<View> viewList, OnClickListener onClickListener) {
        mContext = context;
        mViewList = viewList;
        this.mOnClickListener=onClickListener;
    }

    @Override
    public int getCount() {
        return mViewList==null?0:mViewList.size();
    }

    // 初始化显示的条目对象
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        mViewList.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnClickListener!=null){
                    mOnClickListener.OnClickListener();
                }
            }
        });


        return mViewList.get(position);
    }

    // 销毁条目对象
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public interface  OnClickListener{
        void OnClickListener();
    }

}
