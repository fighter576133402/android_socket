package cn.com.uangel.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	//服务器端口
	private static final int SERVERPORT = 54322;
	//客户端连接
	private static List<Socket> mClientList = new ArrayList<Socket>();
	//线程池
	private ExecutorService mExecutorService;
	//ServerSocket对象
	private ServerSocket msServerSocket ;
	/**
	 * @param 开启服务器
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Server();
	}

	public Server(){
		try {
			//设置服务器端口
			msServerSocket = new ServerSocket(SERVERPORT);
			//创建一个线程池
			mExecutorService = Executors.newCachedThreadPool();
			System.out.println("start...");
			//用来临时保存客户端连接的socket对象
			Socket client = null;
			while (true){
				//接收客户端连接并且添加到List中
				client = msServerSocket.accept();
				System.out.println("--------");
				mClientList.add(client);
				//开启一个客户端线程
				mExecutorService.execute(new ThreadServer(client));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	static class ThreadServer implements Runnable{

		private Socket msSocket ;
		private BufferedReader mBufferedReader;
		private PrintWriter mPrintWriter;
		private String mStrMSG;
		
		public ThreadServer(Socket socket) throws IOException{
			this.msSocket = socket;
			mBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			mStrMSG = "user:"+this.msSocket.getInetAddress()+" come total :" + mClientList.size();
			sendMessage();
		}
		//发送消息给所有客户端
		private void sendMessage() throws IOException{
			System.out.println(mStrMSG);
			for(Socket client : mClientList){
				mPrintWriter = new PrintWriter(client.getOutputStream(),true);
				mPrintWriter.println(mStrMSG);
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while((mStrMSG = mBufferedReader.readLine())!=null){
					if(mStrMSG.trim().equals("exit")){
						mStrMSG = "user:"+this.msSocket.getInetAddress()+" exit total :" + mClientList.size();
						sendMessage();
						mBufferedReader.close();
						mPrintWriter.close();
						msSocket.close();
						mClientList.remove(msSocket);
						break;
					}else {
						mStrMSG = msSocket.getInetAddress() + ":" +mStrMSG;
						sendMessage();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
}
