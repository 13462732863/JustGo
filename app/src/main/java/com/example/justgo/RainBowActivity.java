package com.example.justgo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;

import com.example.justgo.customClass.MyAnimation;
import com.example.justgo.customClass.MyDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RainBowActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener{

    private ImageButton imageButton_a1, imageButton_b2;                //获取触发菜单的按钮
    private RelativeLayout l2, l3;                                     //二级与三级菜单布局
    private Boolean isl2Show = true;                                  //判断二级菜单是否显示
    private Boolean isl3Show = true;                                 //判断三级菜单是否显示

   // public PieChart mChart;                                         //爱的魔力转圈圈
            //获取本地音乐的相关按钮
    private String name[]=new String[1024];
    private String[] artical = new String[1024];
    private String url[]=new String[1024];
    private int id[]=new int[1024];
    private SimpleAdapter adapter;
    private List<Map<String, Object>> list;
    private ListView listView;
    private MediaPlayer mediaPlayer;
    private ImageButton button1,button2,button3,button4,button5;
    private int index=0;//当前播放音乐索引
    private boolean isPause=false;
    private Context base;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_rain_bow);
//        mChart = findViewById ( R.id.pie_chart );
//        PieData mPieData = getPieData ( 2,0 );
//        showChart ( mChart, mPieData );

        //获取本地音乐
        listView=  findViewById(R.id.listview);
        list = new ArrayList<> ();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        init();//按钮初始化
        /**
         * IOS退出对话框的设置
         * */

        //不显示系统的标题栏
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        /**
         * 彩虹菜单的设置
         * */

        //获取触发菜单的按钮
        imageButton_a1 = findViewById ( R.id.a_1 );                  //获取用于触发二级菜单的按钮
        imageButton_b2 = findViewById ( R.id.b_2 );                  //获取用于触发三级菜单的按钮
        //获取二级与三级菜单布局
        l2 = findViewById ( R.id.level_2 );
        l3 = findViewById ( R.id.level_3 );
        //单机该按钮显示或隐藏按钮
        imageButton_b2.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (isl3Show) {
                    //隐藏三级菜单导航栏
                    MyAnimation.animationOut ( l3, 500, 0 );
                } else {
                    //显示三级菜单导航栏
                    MyAnimation.animationIn ( l3, 500 );
                }
                isl3Show = !isl3Show;           //修改三级菜单显示状态
            }
        } );
        imageButton_a1.setOnClickListener ( new View.OnClickListener () {

            @Override
            public void onClick (View view) {
                if (!isl2Show) {
                    //显示二级菜单
                    MyAnimation.animationIn ( l2, 500 );
                } else {
                    if (isl3Show) {
                        //隐藏三级菜单
                        MyAnimation.animationOut ( l3, 500, 0 );
                        //隐藏二级菜单
                        MyAnimation.animationOut ( l2, 500, 500 );
                        isl3Show = !isl3Show;                   //修改三级菜单显示状态
                    } else {
                        //隐藏二级菜单
                        MyAnimation.animationOut ( l2, 500, 0 );
                    }
                }
                isl2Show = !isl2Show;           //修改二级菜单显示状态
            }
        } );
    }

    /**
     * IOS退出对话框的设置
     */

    //设置退出账号按钮对话框
    public void OnExitNumber (View view) {
         final MyDialog myDialog = new MyDialog ( RainBowActivity.this);         //实例化自定义对话框
        myDialog.setOnExitListener ( new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                //如果对话框处于显示状态
                if (myDialog.isShowing ()) {
                    myDialog.dismiss ();           //关闭对话框
                    finish ();                     //关闭当前界面
                }
            }
        } );
        myDialog.setOnCancelistener ( new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (myDialog != null && myDialog.isShowing ()) {
                    myDialog.dismiss ();            //关闭对话框
                }
            }
        } );
        myDialog.show ();            //显示对话框
    }

    //按钮初始化(本地音乐)
    private void init(){
        button1=  findViewById(R.id.up);
        button2=  findViewById(R.id.stop);
        button3=  findViewById(R.id.next);
        button4=  findViewById(R.id.start);
        button5= findViewById ( R.id.huoqu);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                huoqu ( view );
            }
        } );
        ActivityCompat.requestPermissions(RainBowActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

    }
    public void huoqu(View v){
        ContentResolver contentResolver= getContentResolver();
        Cursor c=contentResolver.query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);          //获取本地音乐
        if (c!=null){
            int i=0;
            c.moveToFirst();
            while(c.moveToNext()){
                Map<String,Object> map= new HashMap<> ();
                //歌曲
                name[i]=c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                id[i]=c.getInt(c.getColumnIndex(MediaStore.Audio.Media._ID));
                //作者
                artical[i]=c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                //路径
                url[i]=c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                map.put("SongName", name[i]);
                map.put("id", id[i]);
                map.put("Artical", artical[i]);
                map.put("url", url[i]);
                list.add(map);
                i++;
            }
            adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.music_content,
                    new String[] { "SongName","Artical" }, new int[] { R.id.name,R.id.artical});
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //从头开始播放音乐

                    if (i<list.size()){
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        Uri conuri= ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id[i]);
                        grantUriPermission(null, conuri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            mediaPlayer.setDataSource(getApplicationContext(),conuri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        index=i;
                        isPause=false;
                        mediaPlayer.prepareAsync();
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"本地没有音乐文件",Toast.LENGTH_LONG).show();
            c.close ();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    //监听事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.up:
                try {
                    up();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.start:
                try {
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stop:
                pause();
                break;
            case R.id.next:
                try {
                    next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
    //上一首
    private void up() throws IOException {
        if (index-1>=0){
            index--;
        }else{
            index=list.size()-1;
        }
        ccstat();

    }

    //下一首
    private void next() throws IOException {
        if (isPause){
            mediaPlayer.stop();
            mediaPlayer.reset();
            isPause=false;
        }
        if (index+1<list.size()){
            index++;
        }else{
            index=0;
        }
        ccstat();
    }

    //暂停
    private void pause() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause=true;
        }
    }

    //   播放
    private void start() throws IOException {
        //恢复播放或者从头播放
        if (isPause){
            mediaPlayer.start();
            isPause=false;
        }else{
            ccstat();
        }

    }

    //从头开始播放音乐
    private void ccstat() throws IOException {
        if (index<list.size()){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            AtomicReference<Uri> conuri= new AtomicReference<> ( ContentUris.withAppendedId ( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id[index] ) );
            mediaPlayer.setDataSource(getApplicationContext(), conuri.get () );
            mediaPlayer.prepareAsync();
            isPause=false;

        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /**
     *再按一次退出程序的实现
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
    /**
     * 爱的魔力转圈圈
     * */

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void showChart (PieChart pieChart, PieData pieData) {
//        pieChart.setHoleColorTransparent ( true );
//        //半径
//        pieChart.setHoleRadius ( 40f );
//        //半透明圈
//        pieChart.setTransparentCircleRadius ( 64f );
//        //pieChart.setHoleRadius(0); //实心圆
//        pieChart.setDescription ( "" );
//        //饼状图中间可以添加文字
//        pieChart.setDrawCenterText ( true );
//        pieChart.setDrawHoleEnabled ( true );
//        //初始旋转角度
//        pieChart.setRotationAngle ( 90 );
//        //可以手动旋转
//        pieChart.setRotationEnabled ( true );
//        //显示成百分比
//        pieChart.setUsePercentValues ( true );
//        //饼状图中间的文字
//        pieChart.setCenterText ( "袁" );
//        pieChart.setClickable ( true );
//        pieChart.setCenterTextColor ( Color.BLUE );
//        pieChart.setCenterTextSize ( 16f );
//        pieChart.setCenterTextSizePixels ( 100f );
//        //设置数据
//        pieChart.setData ( pieData );
//        //设置比例图
//
//        Legend mLegend = pieChart.getLegend ();
//        //最右边显示
//        mLegend.setEnabled ( false );
//        mLegend.setPosition ( Legend.LegendPosition.RIGHT_OF_CHART );
//        mLegend.setXEntrySpace ( 8f );
//        mLegend.setYEntrySpace ( 8f );
//        //设置动画
//        pieChart.animateXY ( 1000, 1000 );
//    }
//
//    //分成几部分
//    private PieData getPieData (int count, float range) {
//        //xVals用来表示每个饼块上的内容
//        ArrayList<String> xValues = new ArrayList<> ();
//        //饼块上显示文字
//        xValues.add ( "爱" );
//        xValues.add ( "的" );
//        xValues.add ( "磨" );
//        xValues.add ( "力" );
//        xValues.add ( "转" );
//        xValues.add ( "圈" );
//        xValues.add ( "圈" );
//        //yVals用来表示封装每个饼块的实际数据
//        ArrayList<Entry> yValues = new ArrayList<> ();
//        // 饼图数据
//        //将一个饼形图分成五个部分
//        //所以 40代表的百分比就是40%
//        float quarterly1 = 100 / 7;
//        float quarterly2 = 100 / 7;
//        float quarterly3 = 100 / 7;
//        float quarterly4 = 100 / 7;
//        float quarterly5 = 100 / 7;
//        float quarterly6 = 100 / 7;
//        float quarterly7 = 100 / 7;
//        //添加数据到数组
//        yValues.add ( new Entry ( quarterly1, 0 ) );
//        yValues.add ( new Entry ( quarterly2, 1 ) );
//        yValues.add ( new Entry ( quarterly3, 2 ) );
//        yValues.add ( new Entry ( quarterly4, 3 ) );
//        yValues.add ( new Entry ( quarterly5, 4 ) );
//        yValues.add ( new Entry ( quarterly6, 5 ) );
//        yValues.add ( new Entry ( quarterly7, 6 ) );
//        //y轴的集合/*显示在比例图上*/
//        PieDataSet pieDataSet = new PieDataSet ( yValues, "" );
//        //设置个饼状图之间的距离
//        pieDataSet.setSliceSpace (1f);
//        ArrayList<Integer> colors = new ArrayList<> ();
//        // 饼图颜色
//        colors.add ( Color.GRAY );
//        colors.add ( Color.rgb ( 255,0,0  ) );
//        colors.add ( Color.rgb (  255,165,0) );
//        colors.add ( Color.rgb ( 255,255,0 ) );
//        colors.add ( Color.rgb ( 0, 255, 0) );
//        colors.add ( Color.rgb ( 0, 127, 255 ) );
//        colors.add ( Color.rgb ( 0, 0, 255) );
//        colors.add ( Color.rgb ( 139, 0, 255) );
//        pieDataSet.setColors ( colors );
//        //设置圆盘文字颜色
//        pieDataSet.setValueTextColor ( Color.WHITE );
//        pieDataSet.setValueTextSize ( 30f );
//        //设置是否显示区域百分比的值
//        //设置数据样式
//        pieDataSet.setValueFormatter(new ValueFormatter ()
//        { @Override
//        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler){
//            return "";
//        }
//        //{ return ""+(int)value+"%"; }
//        });
//        DisplayMetrics metrics = getResources ().getDisplayMetrics ();
//        float px = 5 * (metrics.densityDpi / 160f);
//        // 选中态多出的长度
//        pieDataSet.setSelectionShift ( px );
//        PieData pieData = new PieData ( xValues, pieDataSet );
//        return pieData;
//    }
}
