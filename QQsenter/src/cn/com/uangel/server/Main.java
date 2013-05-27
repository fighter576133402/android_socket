package cn.com.uangel.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main implements Runnable{

	
	int port = 54321;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread desktopServerThread = new Thread (new Main());
		desktopServerThread.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		try {
			ServerSocket server = new ServerSocket(port);
			
			while(true){
				//接收客户端请求
				Socket client = server.accept();
				System.out.println("accept");
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String str = in.readLine();
					System.out.println("read:"+str);
					//向服务器发送消息
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
					out.println("server message:"+str);
					in.close();
					out.close();
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					client.close();
					System.out.println("close server");
				}
				//接收客户端消息
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			
		}
	}

}
