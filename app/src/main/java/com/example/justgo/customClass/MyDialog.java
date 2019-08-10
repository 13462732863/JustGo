package com.example.justgo.customClass;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.justgo.R;

public class MyDialog extends Dialog {
    private Button Button_canael,Button_exit;      //定义取消与确定取消按钮
    private TextView TVTitle;                   //定义标题文字
    //自定义构造方法
    public MyDialog(Context context){
        super (context, R.style.mdilog);     //设置对话框样式
        //通过LayoutInflater获取布局
        LayoutInflater inflater = LayoutInflater.from ( context );
        View view= inflater.inflate ( R.layout.mdialog_layout,null );

       // View view =  LayoutInflater.from ( context ).inflate ( R.layout.mdialog_layout ,null);
        TVTitle = view.findViewById ( R.id.tv_exit_title );       //获取显示标题的文本框控件
        Button_canael = view.findViewById ( R.id.btn_cancel );        //获取取消按钮
        Button_exit = view.findViewById ( R.id.btn_exit );           //获取确定取消按钮
        setContentView ( view );                        //设置显示的视图
    }
    //设置显示的标题文字
    public void setTv(String content){
        TVTitle.setText ( content );
    }
    //取消按钮的监听事件
    public void setOnCancelistener(View.OnClickListener listener){
        Button_canael.setOnClickListener ( listener );
    }
    //退出按钮监听
    public void  setOnExitListener(View.OnClickListener listener){
        Button_exit.setOnClickListener ( listener );
    }
}
