package com.example.administrator.musicguessing;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.musicguessing.bean.Song;
import com.example.administrator.musicguessing.bean.User;
import com.example.administrator.musicguessing.bean.WordButton;
import com.example.administrator.musicguessing.constdata.Const;
import com.example.administrator.musicguessing.listener.OnDialogListener;
import com.example.administrator.musicguessing.listener.WordButtonClickListener;
import com.example.administrator.musicguessing.utils.DialogHelper;
import com.example.administrator.musicguessing.utils.GameUtil;
import com.example.administrator.musicguessing.bean.MyGridView;
import com.example.administrator.musicguessing.utils.UITools;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends Activity implements WordButtonClickListener {

    /*------------常量-------------*/
    private static final int SPARK_TIMES = 6;
    private static final int ID_DIALOG_DEL = 0x21;
    private static final int ID_DIALOG_TIP = 0x22;
    private static final int ID_DIALOG_LACK = 0x23;
    private static final String TAG = MainActivity.class.getSimpleName();

    /*----------------动画相关----------------*/
    //唱片相关动画
    private Animation mPanAnim;

    //运动速度即加速器
    private LinearInterpolator mPanLin;
    //拨杆往左动画
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLIn;

    //拨杆往右动画
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    /*----------------控件相关----------------*/
    private ImageButton mBtnPlayStart;//播放按钮
    private ImageView mImgPan;//盘
    private ImageView mImgBar;//盘杆
    private MyGridView myGridView;//文字布局
    private LinearLayout mViewSelectContainer;//文字容器

    /*----------------数据相关----------------*/
    private boolean isRunning ;//盘片是否在运行
    private Song mCurrentSong;//当前歌曲
    private int mCurrentIndex = -1;//当前文字索引
    private List<WordButton> mAllWords;//所有文字容器
    private List<WordButton> mSelectWords;//所选文字
    private int mCurrentCoins ;

    /*----------------通关相关----------------*/
    private View mViewPass;//通关视图
    private TextView mTVCurrentCoins;
    private ImageButton mbtnDelWord;//删除金币按钮
    private ImageButton mBtnTipAnswer;//问题提示按钮
    private TextView mTVStagePass;//通关弹出界面
    private TextView mTVStage;//主界面的状态
    private TextView mTVSong;//显示歌曲名称
    private ImageButton mBtnNext;//下一关按钮


    private ServiceConnection conn;
    private MusicService.MyBindle binder;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();
        initAnimation();
        initEvents();
        initCurrentStageDatas();

    }

    private void initDatas(){
        /*int[] datas = GameUtil.loadDate(this);
        //由于保存的是上一关的数据，所以要-2
        if (datas[GameUtil.DATA_INDEX] != 0 && datas[GameUtil.DATA_INDEX] != Const.SONG_INFO.size()-1){
            mCurrentIndex = datas[GameUtil.DATA_INDEX];
        }
        if(datas[GameUtil.DATA_COIN] != 0){
            mCurrentCoins = datas[GameUtil.DATA_COIN];
        }
        mTVCurrentCoins.setText(mCurrentCoins+"");*/

        Intent intent = getIntent();
        //int id = intent.getIntExtra("level_id", 0);
        int id = Integer.parseInt(intent.getStringExtra("level_id"));
        user = (User) intent.getSerializableExtra("user");
        //mCurrentSong = Const.SONG_INFO.get(id-1);
        mCurrentIndex = id-2;
        mCurrentCoins = user.getCoins();
        mTVCurrentCoins.setText(mCurrentCoins+"");
    }

    private void initViews(){
        myGridView = ((MyGridView) findViewById(R.id.gridview));
        mBtnPlayStart = ((ImageButton) findViewById(R.id.btn_play_start));
        mImgBar = ((ImageView) findViewById(R.id.img_pan_bar));
        mImgPan = ((ImageView) findViewById(R.id.img_pan));
        mViewSelectContainer = (LinearLayout)findViewById(R.id.word_select_container);
        //通关后视图
        mViewPass = findViewById(R.id.pass_view);
        //当前金币
        mTVCurrentCoins = ((TextView) findViewById(R.id.txt_bar_coins));
        //删除金币按钮
        mbtnDelWord = ((ImageButton) findViewById(R.id.btn_delete_word));
        mBtnTipAnswer = ((ImageButton) findViewById(R.id.btn_tip_answer));

        /*--------------------通关初始化--------------------*/
        mTVStagePass = ((TextView) findViewById(R.id.textView_current_stage));
        mTVStage = ((TextView) findViewById(R.id.text_current_stage));
        mTVSong = ((TextView) findViewById(R.id.textView_song_name));
        mBtnNext = ((ImageButton) findViewById(R.id.btn_next_stage));

    }

    //初始化当前关的数据，当前关的正确答案
    private void initCurrentStageDatas(){
        //1.加载歌曲信息，每过一关加1
        mCurrentSong = GameUtil.loadStageSongInfo(++mCurrentIndex);
        //2.获得数据
        mAllWords = GameUtil.initAllWords(mCurrentSong);
        //3.更新数据
        myGridView.updateDate(mAllWords);
        //4.播放音乐
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (MusicService.MyBindle) service;
                /*
                * 1.通过Service使页面刚开始就播放音乐
                * 2.将播放按钮隐藏
                * 3.开启唱片动画
                * */
                binder.play(MainActivity.this,mCurrentSong.getSongFileName());
                mBtnPlayStart.setVisibility(View.INVISIBLE);
                mImgPan.startAnimation(mPanAnim);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, MusicService.class), conn,BIND_AUTO_CREATE);

        //5.初始化选中的文字
        mSelectWords = GameUtil.initSelectWords(this,mCurrentSong,mAllWords);
        //6.清除原有的视图
        mViewSelectContainer.removeAllViews();
        //7.把自布局添加到linearLayout容器里
        addSelectViewToContainer();
        //8.更新界面上的关数
        mTVStage.setText((mCurrentIndex+1)+"");

    }

    /**
     * 动态添加已选择的button到容器里
     */
    private void addSelectViewToContainer(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UITools.px2dip(this,35),UITools.px2dip(this,35));
        for(int i=0;i<mSelectWords.size();i++){
            View child = mSelectWords.get(i).getViewButton();
            mViewSelectContainer.addView(child,params);
        }
    }


    /*======================事件处理=========================*/
    private void initEvents(){
        /*播放动画*/
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                binder.play(MainActivity.this,mCurrentSong.getSongFileName());
            }
        });

        /*删除不是答案的文字*/
        mbtnDelWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeleteWord();
            }
        });

        /*提示答案的文字*/
        mBtnTipAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTipAnswer();
            }
        });

        myGridView.registOnWordButtonClick(this);
    }

    /**
     * GridView按钮点击事件
     * @param wordButton
     */

    @Override
    public void onWordButtonClick(WordButton wordButton) {
        GameUtil.setSelectWord(wordButton,mSelectWords);
        //获得答案状态
        int state = GameUtil.checkTheAnswer(mSelectWords,mCurrentSong);
        switch (state){
            case Const.STATE_ANSWER_LACK:
                for(int i=0;i<mSelectWords.size();i++){
                    mSelectWords.get(i).getViewButton().setTextColor(Color.WHITE);
                }
                break;
            case Const.STATE_ANSWER_RIGHT:
                //处理通关时间
                handlePassEvent();
                break;
            case Const.STATE_ANSWER_WRONG:
                //上锁文字
                sparkWords();
                break;
            default:
                break;
        }
        GameUtil.setButtonVisiblity(wordButton,View.INVISIBLE);

    }



    //一闪文字
    protected void sparkWords(){
        TimerTask task = new TimerTask() {
            int sparkTimes = 0;
            boolean change = false;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(++sparkTimes>SPARK_TIMES)
                            return;
                        for(int i=0;i<mSelectWords.size();i++){
                            mSelectWords.get(i).getViewButton().setTextColor(change?Color.RED:Color.WHITE);
                        }
                        change = !change;
                    }
                });
            }
        };

        new Timer().schedule(task,1,150);
    }

    /*======================动画设置==========================*/
    private void startAnim(){
        /*
        *盘杆到左边后开始播放
        * 盘转了3圈之后盘杆到右边
        * */
        //1.先自行盘杆到左边
        if(mImgBar !=null){
            if(!isRunning){
                mImgBar.startAnimation(mBarInAnim);
                isRunning = true;
                mBtnPlayStart.setVisibility(View.INVISIBLE);
                binder.play(this,mCurrentSong.getSongFileName());
            }
        }
    }

    private void setAnimListener(){
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //2.播放
                mImgPan.startAnimation(mPanAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //播放结束后盘杆到右边恢复
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //3.回到右边
                mImgBar.startAnimation(mBarOutAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //盘杆回到原味后触发的动作...
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //全部播放完成后，重新运行
                isRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initAnimation(){
        //1.右杆向左到盘片
        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLIn = new LinearInterpolator();
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mBarInLIn);
        //2.右杆向右
        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setInterpolator(mBarOutLin);
        //3.盘旋转
        mPanAnim = AnimationUtils.loadAnimation(this,R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setFillAfter(true);
        mPanAnim.setInterpolator(mPanLin);
        //给动画设置监听
        setAnimListener();

    }

    @Override
    protected void onPause() {
        mImgPan.clearAnimation();
        mImgBar.clearAnimation();

        binder.stop();

        //保存数据
        //GameUtil.saveDate(this, mCurrentIndex -1,mCurrentCoins);
        update();

        super.onPause();
    }

    /*======================通关后事件处理==========================*/
    protected void handlePassEvent(){
        mViewPass.setVisibility(View.VISIBLE);
        //清除动画
        mImgPan.clearAnimation();
        mImgBar.clearAnimation();
        //停止播放音乐
        binder.stop();

        //播放音效
        binder.play(this,MusicService.INDEX_STONE_COIN);
        //更新索引
        if (mTVStagePass != null){
            mTVStagePass.setText((mCurrentIndex +1)+"");
        }
        if(mTVSong != null){
            mTVSong.setText(mCurrentSong.getSongName());
        }

        //下一关
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPass()){
                    //跳转到通关视图
                   // GameUtil.startActivity(MainActivity.this,PassActivity.class);
                    Toast.makeText(getApplicationContext(),"恭喜你成功了！",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MyDate.class);
                    mCurrentIndex = mCurrentIndex+1;
                    mCurrentCoins = mCurrentCoins+100;
                    user.setIndex(mCurrentIndex);
                    user.setCoins(mCurrentCoins);
                    intent.putExtra("user", (Serializable) user);
                    startActivity(intent);
                    update();
                }else{
                    //下一关
                    initCurrentStageDatas();
                    //过关添加金币
                    handleCoins(100);
                    //关闭过关页面
                    mViewPass.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 全部过关
     * @return
     */
    protected boolean hasPass(){
        return (mCurrentIndex+1) ==Const.SONG_INFO.size();
    }

    //处理待选文字事件
    private void handleDeleteWord(){
        showConfirmDialog(ID_DIALOG_DEL);
    }

    //处理提示事件
    private  void handleTipAnswer(){
        showConfirmDialog(ID_DIALOG_TIP);
    }
    //删除一个文字
    private void deleteOneWord(){
        //减少30个金币
        if(!handleCoins(-getDelConis())){
            showConfirmDialog(ID_DIALOG_LACK);
            return;
        }
        GameUtil.setButtonVisiblity(findNotAnswerWordButton(),View.INVISIBLE);
    }

    //提示一个文字
    private void tipOnWord(){
        boolean isTip = false;
        for(int i=0;i<mSelectWords.size();i++){
            if(mSelectWords.get(i).getWordString().length()==0){
                if(!handleCoins(-getTipConis())){
                    showConfirmDialog(ID_DIALOG_LACK);
                    return;
                }
                onWordButtonClick(findIsTheAnswer(i));
                isTip = true;
                break;
            }
        }
        if(!isTip){
            sparkWords();
        }
    }

    //随机获得
    private WordButton findNotAnswerWordButton(){
        Random random = new Random();
        WordButton wb = null;
        while (true){
            int index = random.nextInt(MyGridView.COUNTS_WORDS);
            wb = mAllWords.get(index);
            if(wb.isVisiable() && ! isTheAnswer(wb)){
                return wb;
            }
        }
    }

    private WordButton findIsTheAnswer(int index){
        WordButton wb = null;
        for(int i=0;i<MyGridView.COUNTS_WORDS;i++){
            wb = mAllWords.get(i);
            if(wb.getWordString().equals(""+mCurrentSong.getNameChar()[index])){
                return wb;
            }
        }
        return  null;
    }

    //判断是否被选择的文字
    private  boolean isTheAnswer(WordButton wb){
        boolean result = false;
        for(int i = 0;i<mCurrentSong.getNameLength();i++){
            if(wb.getWordString().equals(mCurrentSong.getNameChar()[i]+"")){
                result = true;
                break;
            }
        }
        return result;
    }

    //从配置文件中获得删除的金币数目
    private int getDelConis(){
        return getResources().getInteger(R.integer.pay_delete_word);
    }

    //从配置文件中获得是提示信息要的金币数目
    private int getTipConis(){
        return getResources().getInteger(R.integer.pay_tip_answer);
    }

    //处理增加金币或减少金币事件
    private boolean handleCoins(int data){
        if(mCurrentCoins+data>=0){
            mCurrentCoins+=data;
            mTVCurrentCoins.setText(mCurrentCoins+"");
            return  true;
        }
        else{
            return false;
        }
    }


    /*======================对话框设置==========================*/
    private void showConfirmDialog(int index){
        switch (index){
            case ID_DIALOG_DEL:
                DialogHelper.showDialog(this,"您确认花"+getDelConis()+"删除一个错误答案吗？",delListener);
                break;
            case ID_DIALOG_TIP:
                DialogHelper.showDialog(this, "您确认花" + getTipConis() + "得到一个提示答案吗?",tipListener);
                break;
            case ID_DIALOG_LACK:
                DialogHelper.showDialog(this, "您的金币不足,请及时充值!", lackListener);
                break;
            default:
                break;
        }
    }

    private OnDialogListener delListener = new OnDialogListener() {
        @Override
        public void onClick() {
            deleteOneWord();
        }
    };

    private OnDialogListener tipListener = new OnDialogListener() {
        @Override
        public void onClick() {
            tipOnWord();;
        }
    };

    private OnDialogListener lackListener = new OnDialogListener() {
        @Override
        public void onClick() {

        }
    };



    /*======================菜单设置==========================*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //返回按钮
    public void returnLevel(View view) {

        Intent intent = new Intent(this,LevelActivity.class);
        if(user.getIndex()<mCurrentIndex){
            user.setIndex(mCurrentIndex);
        }
        user.setCoins(mCurrentCoins);
        intent.putExtra("user", (Serializable) user);
        startActivity(intent);
        update();
    }

    public void update(){
        if(user.getIndex()<mCurrentIndex){
            user.setIndex(mCurrentIndex);
        }
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://cn.itcast.db.myProvider/update");
        ContentValues values = new ContentValues();
        values.put("money",mCurrentCoins);
        values.put("checkpoint",user.getIndex());
        resolver.update(uri,values,"username=?",new String[]{user.getUsername()});
    }
}
