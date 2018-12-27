package com.example.administrator.musicguessing.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.musicguessing.R;
import com.example.administrator.musicguessing.bean.MyGridView;
import com.example.administrator.musicguessing.bean.Song;
import com.example.administrator.musicguessing.bean.WordButton;
import com.example.administrator.musicguessing.constdata.Const;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 2018/11/22.
 */

public class GameUtil {
    private static final String TAG = GameUtil.class.getSimpleName();

    /**
     * 保存加密数据到data.dat文件
     *
     * @param context
     * @param index
     * @param coins

    public static void saveDate(Context context,int index,int coins)  {
        FileOutputStream fos = null;
        try {
            fos= context.openFileOutput(Const.FILE_NAME_DATA,Context.MODE_PRIVATE);
            //使用base64加密的关卡索引和金币数
            DataOutputStream dos = new DataOutputStream(fos);
            String indexCode = encodeUseBase64(index+"");
            String coinsCode = encodeUseBase64(coins+"");
            Log.i(TAG, "saveDate: coins = "+coins);
            dos.writeBytes(indexCode);
            dos.writeBytes(GAME_DATA_SEPARATOR);
            dos.writeBytes(coinsCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/


    /**
     * 获取签名
     * @param context
     * @param packgeName
     * @param view
     * @return
     */
    public String getSignature(Context context, String packgeName, View view){
        StringBuilder sb  = new StringBuilder();
        boolean isEmpty = TextUtils.isEmpty(packgeName);
        PackageInfo packageInfo;
        PackageManager manager = context.getPackageManager();
        Signature signatures[];
        if (isEmpty){
            Toast.makeText(context,"应用程序的包名不能为空",Toast.LENGTH_SHORT).show();

        }else{
            try {
                packageInfo = manager.getPackageInfo(packgeName,PackageManager.GET_SIGNATURES);
                signatures = packageInfo.signatures;
                for(Signature temp:signatures){
                    sb.append(temp.toCharsString());
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 使用Base64加密数据
     * @param data
     * @return
     */
    private static String encodeUseBase64(String data) {
        String result = "";
        if(TextUtils.isEmpty(data)){
            return null;
        }
        try {
             result= new String(Base64.encode(data.getBytes("utf-8"),Base64.NO_WRAP),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(result);
    }

    /**
     * 使用Base64将字符解码
     * @param data
     * @return
     */
    public static String decodeUseBase64(String data){
        String result = "";
        if(TextUtils.isEmpty(data)){
            return  null;
        }
        try {
            result = new String(Base64.decode(data,Base64.NO_WRAP),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取数据
     * @param context
     * @return

    public static int[] loadDate(Context context) {
        int[] dataArray = null;
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            dataArray = new int[]{0,Const.TOTAL_COINS};
            fileInputStream = context.openFileInput(Const.FILE_NAME_DATA);
            dataInputStream = new DataInputStream(fileInputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = dataInputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer,0,len);
            }
            byteArrayOutputStream.flush();

            String data = byteArrayOutputStream.toString("utf-8");
            Log.i(TAG, "loadDate: "+data);

            String indexStr = data.split(GAME_DATA_SEPARATOR)[DATA_INDEX];
            String coidStr = data.split(GAME_DATA_SEPARATOR)[DATA_COIN];

            dataArray[DATA_INDEX] = Integer.parseInt(decodeUseBase64(indexStr));
            dataArray[DATA_COIN] = Integer.parseInt(decodeUseBase64(coidStr));

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                    if(byteArrayOutputStream != null){
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  dataArray;
    }*/

    /**
     * 生成随机文字
     * @return
     */
    public static char getRandomChar(){
        String str = "";
        int highPos = 0;
        int lowPos = 0;
        Random random = new Random();
        highPos = (176+Math.abs(random.nextInt(39)));
        lowPos = (161+Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = Integer.valueOf(highPos).byteValue();
        b[1] = Integer.valueOf(lowPos).byteValue();
        try {
            str = new String(b,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  str.charAt(0);
    }

    /**
     * 生成16个随机汉字
     * @param mCurrentSong
     * @return
     */
    public static String[] generateWords2(Song mCurrentSong){
        String[] words = new String[MyGridView.COUNTS_WORDS];
        //1.存入歌曲名称
        for(int i=0;i<mCurrentSong.getNameLength();i++){
            words[i] = mCurrentSong.getNameChar()[i]+"";
        }

        //2.存入随机字
        for(int i= mCurrentSong.getNameLength();i<MyGridView.COUNTS_WORDS;i++){
            words[i] = getRandomChar()+"";
        }
        //3.打乱字的排列顺序
        Random random = new Random();
        for(int i= MyGridView.COUNTS_WORDS-1;i>=0;i--){
            int index = random.nextInt(i+1);
            String temp = words[index];
            words[index] = words[i];
            words[i] = temp;
        }
        /*原理：首先从所有元素中随机选取一个与第一个元素进行交换，
        * 然后在第二个之后选择一个元素与第二个交换，直到最后一个元素
        * 这样能够确保每个元素在每个位置的概率都是1/n
        * */
        return words;
    }

    /**
     * 生成所有的待选文字
     * @param mCurrentSong
     * @return
     */
    private static String[] generateWords(Song mCurrentSong){
        String[] words = new String[MyGridView.COUNTS_WORDS];
        for(int i=0;i<mCurrentSong.getNameLength();i++){
            words[i] = mCurrentSong.getNameChar()[i]+"";
        }
        for(int i= mCurrentSong.getNameLength();i<MyGridView.COUNTS_WORDS;i++){
            words[i] = getRandomChar()+"";
        }
        List<String> wordList = Arrays.asList(words);
        Collections.shuffle(wordList);
        for(int i=0;i<words.length;i++){
            words[i] = wordList.get(i);
        }
        return  words;
    }

    /**
     * 点击后设置选中文字状态
     * @param wordButton
     * @param mSelectWords
     */
    public static void setSelectWord(WordButton wordButton,List<WordButton> mSelectWords){
        for(int i=0;i<mSelectWords.size();i++){
            //1.判断是否有文字
            WordButton selectedBtn = mSelectWords.get(i);
            if(selectedBtn.getWordString().length()==0){
                //设置过滤动画
                setAnimationToSelectedWord(selectedBtn,wordButton);
                //设置选择框内容
                selectedBtn.getViewButton().setText(wordButton.getWordString());
                selectedBtn.setVisiable(true);
                selectedBtn.setWordString(wordButton.getWordString());
                //设置索引
                selectedBtn.setIndex(wordButton.getIndex());
                //设置待选框可见性
                setButtonVisiblity(wordButton,View.INVISIBLE);
                break;
            }
        }
    }

    /**
     * 设置文字按钮的平移动画效果
     * @param selectedWord
     * @param wordButton
     */
    public static void setAnimationToSelectedWord(final WordButton selectedWord,
                                                  final WordButton wordButton){
        TranslateAnimation animation = (TranslateAnimation) getAnimationFromViewToAnother(
                wordButton.getViewButton(), selectedWord.getViewButton());
        wordButton.getViewButton().startAnimation(animation);
    }




    /**
     * 将控件从一个位置移动到另一个位置
     * @param from
     * @param to
     * @return
     */
    public static Animation getAnimationFromViewToAnother(View from, View to){
        int[] fromPosition = getViewLocation(from);
        int[] toPosition = getViewLocation(to);
        TranslateAnimation animation = new TranslateAnimation(
                0, toPosition[0] - fromPosition[0], 0, toPosition[1] - fromPosition[1]);
        animation.setDuration(500);
        animation.setFillAfter(false);
        return animation;
    }

    /**
     * 获取一个控件在屏幕中的显示位置
     * @param view
     * @return
     */
    public static int[] getViewLocation(View view){
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position;
    }

    /**
     * 设置字体按钮的状态信息
     * @param holder
     * @param visible
     */
    public static void setButtonVisiblity(WordButton holder,int visible){
        if(holder != null){
            //Log.i(TAG, "holder.WordString=: "+holder.getWordString());
            //Log.i(TAG, "holder.visible=: "+holder.isVisiable());
            //Log.i(TAG, "holder.index=: "+holder.getIndex());
            holder.getViewButton().setVisibility(visible);
            holder.setVisiable(visible==View.VISIBLE?true:false);
        }
    }

    /**
     * 清除被选中的答案
     * @param holder
     * @param mAllWords
     */
    public static void clearTheAnswer(WordButton holder,List<WordButton> mAllWords){
        if (holder != null){
            holder.getViewButton().setText("");
            holder.setWordString("");
            holder.setVisiable(false);
            if(mAllWords != null && mAllWords.size()>0){
                setButtonVisiblity(mAllWords.get(holder.getIndex()), View.VISIBLE);
            }
        }
    }

    /**
     * 加载歌曲信息
     * @param index
     * @return
     */
    public static Song loadStageSongInfo(int index){
        Song song = Const.SONG_INFO.get(index);
        return song;
    }

    /**
     * 初始化文字选择框
     * @param context
     * @param mCurrentSong
     * @param mAllWords
     * @return
     */
    public static List<WordButton> initSelectWords(Context context,Song mCurrentSong,
                                                   final List<WordButton> mAllWords){
        List<WordButton> datas = new ArrayList<WordButton>();
        for(int i=0;i<mCurrentSong.getNameLength();i++){
            View view = GameUtil.getView(context, R.layout.self_ui_gridview_item);
            final WordButton holder = new WordButton();
            holder.setViewButton(((Button) view.findViewById(R.id.item_btn)));
            holder.getViewButton().setText("");
            holder.getViewButton().setBackgroundResource(R.drawable.game_wordblank);
            holder.setVisiable(false);
            holder.getViewButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清除被点击的已选文字框的文字
                    if(!TextUtils.isEmpty(holder.getWordString())){
                        clearTheAnswer(holder,mAllWords);
                    }
                }
            });
            datas.add(holder);
        }
        return datas;
    }

    /**
     * 初始化所有的文字
     * @param mCurrentSong
     * @return
     */
    public static List<WordButton> initAllWords(Song mCurrentSong){
        List<WordButton> list = new ArrayList<WordButton>();
        String[] words = generateWords(mCurrentSong);
        for(int i= 0;i<MyGridView.COUNTS_WORDS;i++){
            WordButton wb = new WordButton();
            wb.setWordString(words[i]);
            //wb.setIndex(i);
            list.add(wb);
        }
        return list;
    }

    /**
     * 检查选择的信息是否正确
     * @param mSelectedWords
     * @param mCurrentSong
     * @return
     */
    public static int checkTheAnswer(List<WordButton> mSelectedWords,Song mCurrentSong){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<mSelectedWords.size();i++){
            if(mSelectedWords.get(i).getWordString().length()==0){
                return Const.STATE_ANSWER_LACK;
            }
            sb.append(mSelectedWords.get(i).getWordString());
        }
        return sb.toString().equals(mCurrentSong.getSongName())?Const.STATE_ANSWER_RIGHT:Const.STATE_ANSWER_WRONG;
    }

    /**
     * 加载View
     * @param context
     * @param layoutId
     * @return
     */
    public static View getView(Context context,int layoutId){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId,null);
        return layout;
    }

}
