package com.example.justgo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.justgo.customClass.MD5Utils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextP, editSMS, editTExtCT,editpswAgain;//手机号，验证码，密码,确认密码
    private Button button, SMSBtn;//注册按钮,获取验证码按钮
    private TextView enterText;//登录按钮
    private ImageView returnImage;//返回<

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.login_register );
        init ();
    }

    private void init () {
        editTextP = findViewById ( R.id.et_phone_num );
        editSMS = findViewById ( R.id.et_sms_code );
        editTExtCT = findViewById ( R.id.et_password );
        button = findViewById ( R.id.bn_immediateRegistration );
        button.setOnClickListener ( this );
        enterText = findViewById ( R.id.tv_enter );
        returnImage = findViewById ( R.id.iv_return );
        returnImage.setOnClickListener ( this );
        SMSBtn = findViewById ( R.id.bn_sms_code );
        SMSBtn.setOnClickListener ( this );
        editpswAgain = findViewById ( R.id.et_password_again );
        editpswAgain.setOnClickListener ( this );
    }

    @Override
    public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.bn_immediateRegistration:
                register ( view );
                break;
            case R.id.tv_enter:
                returnEnter ();
                break;
            case R.id.iv_return:
                returnEnter ();
                break;
            case R.id.bn_sms_code:
                final String usrename = editTextP.getText ().toString ().trim ();
                if (TextUtils.isEmpty ( usrename )) {
                    Toast.makeText ( this, "手机号不能为空", Toast.LENGTH_SHORT ).show ();
                    editTextP.requestFocus ();
                } else {
                    Toast.makeText ( this, "验证码获取成功", Toast.LENGTH_SHORT ).show ();
                }
                break;
        }
    }

    private void returnEnter () {
        Intent intent = new Intent ( this, EnterActivity.class );
        startActivity ( intent );
        finish ();
    }

    public void register (View view) {
        final String userName = editTextP.getText ().toString ().trim ();
        final String passwordSMS = editSMS.getText ().toString ().trim ();//验证码
        final String passwordFirst = editTExtCT.getText ().toString ().trim ();//首次密码
        final String passwordAgain = editpswAgain.getText ().toString ().trim ();//再次密码

        //判断输入框内容

        if (TextUtils.isEmpty ( userName )) {
            Toast.makeText ( RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT ).show ();
        } else if (TextUtils.isEmpty ( passwordFirst )) {
            Toast.makeText ( RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT ).show ();
        } else if (TextUtils.isEmpty ( passwordSMS )) {
            Toast.makeText ( RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT ).show ();
        }
        else if (TextUtils.isEmpty ( passwordAgain )) {
            Toast.makeText ( RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT ).show ();
        } else if (!passwordFirst.equals ( passwordAgain )) {
            Toast.makeText ( RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT ).show ();

            /**
             *从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
             */
        } else if (isExistUserName ( userName )) {
            Toast.makeText ( RegisterActivity.this, "此账户名已经存在", Toast.LENGTH_SHORT ).show ();

        } else {
            Toast.makeText ( RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT ).show ();
            //把账号、密码和账号标识保存到sp里面
            /**
             * 保存账号和密码到SharedPreferences中
             */
            saveRegisterInfo ( userName, passwordFirst );
            //注册成功后把账号传递到LoginActivity.java中
            // 返回值到loginActivity显示
            Intent data = new Intent ();
            data.putExtra ( "userName", userName );
            setResult ( RESULT_OK, data );
            //RESULT_OK为Activity系统常量，状态码为-1，
            // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
           // RegisterActivity.this.finish ();
            startActivity ( new Intent ( RegisterActivity.this,EnterActivity.class ) );
        }
    }
    /**
     * 从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
     */
    private boolean isExistUserName (String userName) {
        boolean has_userName = false;
        //mode_private SharedPreferences sp = getSharedPreferences( );
        // "loginInfo", MODE_PRIVATE
        SharedPreferences sp = getSharedPreferences ( "loginInfo", MODE_PRIVATE );
        //获取密码
        String spPsw = sp.getString ( userName, "" );//传入用户名获取密码
        //如果密码不为空则确实保存过这个用户名
        if (!TextUtils.isEmpty ( spPsw )) {
            has_userName = true;
        }
        return has_userName;
    }

    /**
     * 保存账号和密码到SharedPreferences中SharedPreferences
     */
    private void saveRegisterInfo (String userName, String psw) {
        String md5Psw = MD5Utils.MD5 ( psw );//把密码用MD5加密
        //loginInfo表示文件名, mode_private SharedPreferences sp = getSharedPreferences( );
        SharedPreferences sp = getSharedPreferences ( "loginInfo", MODE_PRIVATE );

        //获取编辑器， SharedPreferences.Editor  editor -> sp.edit();
        SharedPreferences.Editor editor = sp.edit ();
        //以用户名为key，密码为value保存在SharedPreferences中
        //key,value,如键值对，editor.putString(用户名，密码）;
        editor.putString ( userName, md5Psw );
        //提交修改 editor.commit();
        editor.apply ();
    }
}
