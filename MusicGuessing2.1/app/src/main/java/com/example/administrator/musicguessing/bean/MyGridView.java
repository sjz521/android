package com.example.administrator.musicguessing.bean;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.administrator.musicguessing.R;
import com.example.administrator.musicguessing.listener.WordButtonClickListener;
import com.example.administrator.musicguessing.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */

public class MyGridView extends GridView {

    //按钮个数
    public final static int COUNTS_WORDS = 16;
    public static final int WORD_SELECT_COUNTS = 4;
    //数据源
    private List<WordButton> mArrayList = new ArrayList<WordButton>();

    //内部自定义适配器
    private MyGridAdapter mAdapter;
    //上下文
    private Context mContext;
    //动画
    private Animation mScaleAnimation;

    //wordbutton点击监听事件
    private WordButtonClickListener mWordButtonListener;


    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //上下文
        mContext = context;
        mAdapter = new MyGridAdapter();
        setAdapter(mAdapter);
    }

    //更新数据
    public void updateDate(List<WordButton> list){
        mArrayList = list;
        setAdapter(mAdapter);
    }

    class MyGridAdapter extends BaseAdapter{

        private LayoutInflater mInflater;
        public MyGridAdapter(){
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final WordButton holder;
            /*先判断是否为空，这个convertView是每个小方格的布局文件*/
            if (convertView == null){
                //定义每个按钮的布局文件
                //convertView = mInflater.inflate(R.layout.self_ui_gridview_item,parent,false);
                convertView = GameUtil.getView(mContext, R.layout.self_ui_gridview_item);
                holder = mArrayList.get(position);
                //加载动画
                mScaleAnimation = AnimationUtils.loadAnimation(mContext,R.anim.scale);
                //延迟时间
                mScaleAnimation.setStartOffset(position*100);
                holder.setIndex(position);
                holder.setViewButton((Button) convertView.findViewById(R.id.item_btn));
                holder.getViewButton().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWordButtonListener.onWordButtonClick(holder);
                    }
                });
                //将convertView和holder关联起来
                convertView.setTag(holder);
            }else{
                holder = (WordButton) convertView.getTag();
            }
            //文字设置
            holder.getViewButton().setText(holder.getWordString());

            //播放动画
            convertView.startAnimation(mScaleAnimation);

            return convertView;
        }
    }
    /*
    Scale动画代码使用方式：
    1.创建Scale动画实例
        Animation mScaleAnimation = AnimationUtils.loadAnimation(AnimDemoActivity.this,R.anim.scale);
    2.设置相关属性

    3.执行动画
        view.startAnimation(mScaleAnimation);
     */

    public void registOnWordButtonClick(WordButtonClickListener listener){
        mWordButtonListener = listener;
    }

}