package com.smf.xxy.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity
{
    EditText userName;
    EditText Password;
    private View btnTest;
    private View btnClean;
    private TextView tvTestResult;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest=findViewById(R.id.btnTestSql);
        btnClean=findViewById(R.id.btnClean);
        tvTestResult = (TextView)findViewById(R.id.tvTestResult);
        userName=findViewById(R.id.userName);
        Password=findViewById(R.id.Password);
    }
    private long firstTime=0;  //记录第几次点击返回
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-firstTime>2000){
            Toast.makeText(MainActivity.this,"再次点击返回退出",Toast.LENGTH_SHORT).show();
            firstTime=System.currentTimeMillis();
        }else{
            finish();
            System.exit(0);
        }
    }
    public void login(View v) {
        if(v==btnTest){
            if(userName.getText().toString().equals(""))
            {
                Toast.makeText(MainActivity.this,"请输入用户名！",Toast.LENGTH_SHORT).show();
            }
            else if(Password.getText().toString().equals(""))
            {
                Toast.makeText(MainActivity.this,"密码不可为空！",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,"正在登陆,请稍后...",Toast.LENGTH_SHORT).show();
                test();
            }
        }
    }
    private void test()
    {
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                try{
                    Message msg = new Message();
                    String sql = "SELECT [Password] FROM [UserInfo] where UserName='" + userName.getText() + "'";
                    String ret = DBUtil.QuerySQL(sql);
                    ret=ret.trim();
                    String pw=Password.getText().toString();
                    pw=pw.trim();
                    if(pw.equals(ret))
                    {
                        msg.what=1001;
                    }
                    else
                    {
                        msg.what=1002;
                    }
                    Bundle data = new Bundle();
                    data.putString("result", ret);

                    msg.setData(data);
                    mHandler.sendMessage(msg);
                }
                catch (Exception e){
                    Message msg = new Message();
                    tvTestResult.setText(e.getMessage());
                    Bundle data = new Bundle();
                    data.putString("result", e.getMessage());
                    msg.setData(data);
                    mHandler.sendMessage(msg);
                }
                finally { }
            }
        };
        new Thread(run).start();
    }

    Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what)
            {
                case 1001:
                    Toast.makeText(MainActivity.this,"登陆成功~",Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(MainActivity.this,"密码不正确！",Toast.LENGTH_SHORT).show();
                    Password.setText("") ;
                    break;
            }
        };
    };
}
