package cn.com.uangel.socketqq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityMain extends Activity implements OnClickListener{

	// 服务器IP、端口
	private static final String SERVERIP = "192.168.0.116";
	//游戏的FPS
	public static final int FPS =40 ;
	private static final int SERVERPORT = 54322;
	private Thread mThread = null;
	private Socket mSocket = null;
	private Button mButton_In = null;
	private Button mButton_Out = null;
	private Button mButton_Send = null;
	private EditText mEditTextAll = null;
	private EditText mEditTextMess = null;
	private BufferedReader mBufferedReader = null;
	private PrintWriter mPrintWriter = null;
	private String mStrMSG = "";
	private boolean stopThread = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mButton_In = (Button) findViewById(R.id.login);
		mButton_Out = (Button) findViewById(R.id.login_out);
		
		mButton_Send = (Button) findViewById(R.id.send);
		mEditTextAll = (EditText) findViewById(R.id.message_all);
		mEditTextAll.setKeyListener(null);
		mEditTextMess = (EditText) findViewById(R.id.message);
		
		mButton_In.setOnClickListener(this);
		mButton_Out.setOnClickListener(this);
		mButton_Send.setOnClickListener(this);
		
		mThread = new Thread(mRunnable);
		mThread.start();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			mButton_In.setText(getResources().getString(R.string.login_out));
			Message msg = mHandler.obtainMessage();
			msg.what = 0;
			mHandler.sendMessage(msg);
			break;
		case R.id.login_out:
			logonOut();
			break;
		case R.id.send:
			try {
				String str = mEditTextMess.getText().toString() + "\n";
				mPrintWriter.print(str);
				mPrintWriter.flush();
				mEditTextMess.setText("");
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
	
		
		}
	}
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true && stopThread) {
				try {
					if ((mStrMSG = mBufferedReader.readLine()) != null) {
						mStrMSG += "\n";
						mHandler.sendMessage(mHandler.obtainMessage(1));
					}
					Thread.sleep(1000/FPS);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	};
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				if (mSocket != null) {
					Toast.makeText(ActivityMain.this,
							getResources().getString(R.string.login_error),
							Toast.LENGTH_SHORT).show();
				} else {
					Runnable startLogin = new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {
								// 连接服务器
								System.out.println("login");
								mSocket = new Socket(SERVERIP, SERVERPORT);
								// 取得输入输出流
								mBufferedReader = new BufferedReader(
										new InputStreamReader(
												mSocket.getInputStream()));
								mPrintWriter = new PrintWriter(
										mSocket.getOutputStream(), true);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					};
					mThread = new Thread(startLogin);
					mThread.start();
				}
				

				break;

			case 1:
				try {
					mEditTextAll.append(mStrMSG);

				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 2:
				Runnable loginOut = new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
							logonOut();
					}
				};
				mThread = new Thread(loginOut);
				mThread.start();
				break;
			}
		}

	};
	private void logonOut(){
		
		try {
			if (mSocket != null) {
				mPrintWriter.print("exit");
				mPrintWriter.flush();
				mSocket.close();
				System.out.println("关闭socket："+mSocket);
				mSocket = null;
			}
			


		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		logonOut();
	}
	

}
