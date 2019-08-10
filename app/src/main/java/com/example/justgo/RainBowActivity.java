package com.example.justgo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.justgo.customClass.MyAnimation;
import com.example.justgo.customClass.MyDialog;

public class RainBowActivity extends AppCompatActivity {
    private ImageButton imageButton_a1, imageButton_b2;                //获取触发菜单的按钮
    private RelativeLayout l2, l3;                                     //二级与三级菜单布局
    private Boolean isl2Show = true;                                  //判断二级菜单是否显示
    private Boolean isl3Show = true;                                 //判断三级菜单是否显示

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_rain_bow );

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

}
