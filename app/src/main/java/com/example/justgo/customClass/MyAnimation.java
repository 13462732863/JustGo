package com.example.justgo.customClass;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class MyAnimation {
    //菜单进入的动画
    public static void animationIn (ViewGroup viewGroup, int duration) {
        //遍历布局中的按钮控件
        for (int i = 0; i < viewGroup.getChildCount (); i++) {
            viewGroup.getChildAt ( i ).setVisibility ( View.VISIBLE );            //设置控件显示
            viewGroup.getChildAt ( i ).setFocusable ( true );                     //设置控件获取焦点
            viewGroup.getChildAt ( i ).setClickable ( true );                     //设置控件可以点击
        }
        Animation animation;                                    //定义动画对象
        animation = new RotateAnimation ( -180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f );   //创建旋转动画
        animation.setFillAfter ( true );                        //停留在动画结束的位置
        animation.setDuration ( duration );                     //设置动画经过的时间
        viewGroup.startAnimation ( animation );                    //启动动画
    }

    //菜单退出时动画
    public static void animationOut (final ViewGroup viewGroup, int duration, int startOffSet) {
        Animation animation;                              //创建动画对象
        animation = new RotateAnimation ( 0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f );   //创建退出旋转动画
        animation.setFillAfter ( true );                       //设置动画停留在结束位置
        animation.setDuration ( duration );                   //设置动画经过时间
        animation.setStartOffset ( startOffSet );              //设置此动画多久后执行
        //设置动画的监听事件
        animation.setAnimationListener ( new Animation.AnimationListener () {
            @Override
            public void onAnimationStart (Animation animation) {

            }

            //动画结束后的事件处理
            @Override
            public void onAnimationEnd (Animation animation) {
                for (int i = 0; i < viewGroup.getChildCount (); i++) {
                    viewGroup.getChildAt ( i ).setVisibility ( View.GONE );           //设置隐藏菜单中的按钮
                    viewGroup.getChildAt ( i ).setFocusable ( false );                //设置按钮失去焦点
                    viewGroup.getChildAt ( i ).setClickable ( false );                //设置按钮不可单机
                }
            }

            @Override
            public void onAnimationRepeat (Animation animation) {

            }
        } );
        viewGroup.startAnimation ( animation );                //启动退出动画
    }
}
