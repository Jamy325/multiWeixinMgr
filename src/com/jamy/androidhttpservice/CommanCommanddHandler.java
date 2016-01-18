package com.jamy.androidhttpservice;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import android.util.Log;

public class CommanCommanddHandler implements HttpRequestHandler {

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext arg2)
			throws HttpException, IOException {
		RequestLine requestLine =  request.getRequestLine();
		String str = requestLine.getUri();
		String strparam = str.substring(str.indexOf("?")+1);
		String[] params = strparam.split("&");
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		for(int i = 0; i < params.length; ++i){
			Log.d("http", params[i]);
			String[] data = params[i].split("=");
			if (data.length == 2){
				paramMap.put(data[0], data[1]);
			}		
		}
		
		final String strData = paramMap.get("cmd");
		HttpEntity httpEntity = new EntityTemplate(
				new ContentProducer() {

					public void writeTo(final OutputStream outstream)
							throws IOException {
															
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outstream, "UTF-8");
						String result = "{code:200, data:"+strData+"}";
						outputStreamWriter.write(result);
						outputStreamWriter.flush();
					}
				});
		
		response.setHeader("Content-Type", "text/html");
		response.setEntity(httpEntity);
	}
}
