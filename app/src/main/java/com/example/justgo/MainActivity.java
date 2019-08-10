package com.example.justgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    final int NOTIFYID =0x123;     //通知ID
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        //获取通知管理服务
        NotificationManager notificationManager = (NotificationManager) getSystemService ( NOTIFICATION_SERVICE );
        //创建消息通知类
        NotificationCompat.Builder notification =new NotificationCompat.Builder ( MainActivity.this );
        //消息通知时默认发出的声音
        notification.setDefaults ( Notification.DEFAULT_SOUND );
        //设置打开消息通知后该通知自动消失
        notification.setAutoCancel ( true );
        //设置标题栏消息通知小图标
        notification.setSmallIcon (  R.mipmap.xuan_ku_icon);
        //设置下拉列表中的大图标
        notification.setLargeIcon ( BitmapFactory.decodeResource ( getResources (),R.mipmap.xuan_ku_icon ) );
        //设置通知内容的主题
        notification.setContentTitle ( "Android入门第一季" );
        //设置通知内容
        notification.setContentText ( "点击查看详情" );
        //设置通知时间
        notification.setWhen ( System.currentTimeMillis () );
        //发送通知
        notificationManager.notify ( NOTIFYID,notification.build () );
        //创建一个启动其他界面的intent
        Intent intent = new Intent ( MainActivity.this, MessageActivity.class);
        PendingIntent pi =  PendingIntent.getActivity ( MainActivity.this,0,intent,0 );
        //点击通知栏进行界面跳转
        notification.setContentIntent ( pi );
    }
}
