package com.jamy.androidhttpservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

public class HttpServiceThread extends Thread {


	ServerSocket serverSocket;
	Socket socket;
	HttpService httpService;
	BasicHttpContext basicHttpContext;
	static final int HttpServerPORT = 8080;
	boolean RUNNING = false;

	HttpServiceThread() {
		RUNNING = true;
		startHttpService();
	}

	@Override
	public void run() {

		try {
			serverSocket = new ServerSocket(HttpServerPORT);
			serverSocket.setReuseAddress(true);

			while (RUNNING) {
				socket = serverSocket.accept();
				DefaultHttpServerConnection httpServerConnection = new DefaultHttpServerConnection();
				httpServerConnection.bind(socket, new BasicHttpParams());
				httpService.handleRequest(httpServerConnection, basicHttpContext);
				httpServerConnection.shutdown();
			}
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void startHttpService() {
		BasicHttpProcessor basicHttpProcessor = new BasicHttpProcessor();
		basicHttpContext = new BasicHttpContext();

		basicHttpProcessor.addInterceptor(new ResponseDate());
		basicHttpProcessor.addInterceptor(new ResponseServer());
		basicHttpProcessor.addInterceptor(new ResponseContent());
		basicHttpProcessor.addInterceptor(new ResponseConnControl());

		httpService = new HttpService(basicHttpProcessor,
				new DefaultConnectionReuseStrategy(),
				new DefaultHttpResponseFactory());

		HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
		registry.register("/", new HomeCommandHandler(MainActivity.mainContext));
		registry.register("/cmd", new CommanCommanddHandler());
		httpService.setHandlerResolver(registry);
	}

	public synchronized void stopServer() {
		RUNNING = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
