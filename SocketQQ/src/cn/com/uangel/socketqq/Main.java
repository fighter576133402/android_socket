package cn.com.uangel.socketqq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends Activity {
	
	String ip = "192.168.0.107";
	int port = 54321;
	private TextView mTextView = null;
	private Button mButton = null;
	private EditText mEditText = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mTextView = (TextView)findViewById(R.id.serverMessage);
        mButton = (Button)findViewById(R.id.send);
        mEditText = (EditText)findViewById(R.id.message);
        
        //登录
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Socket socket = null;
				String message = mEditText.getText().toString()+"\r\n";
				try {
					//创建socket
					socket = new Socket(ip,port);
					//向服务器发送信息
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
					out.println(message);
					
					//接收来自服务器的消息
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String msg = in.readLine();
					if(msg !=null){
						mTextView.setText(msg);
					}else {
						mTextView.setText("数据错误！");
						
					}
					out.close();
					in.close();
					socket.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
        
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
