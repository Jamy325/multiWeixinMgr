package com.jamy.androidhttpservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.content.res.AssetManager;

public class HomeCommandHandler implements HttpRequestHandler {
	private Context	_context;
	 public static final String ENCODING = "UTF-8";  
	public HomeCommandHandler(Context mainContext){
		_context = mainContext;
	}
	
	 //从assets 文件夹中获取文件并读取数据  
    public String getFromAssets(String fileName){  
        String result = "";  
            try {  
                InputStream in = _context.getResources().getAssets().open(fileName);  
                //获取文件的字节数  
                int lenght = in.available();  
                //创建byte数组  
                byte[]  buffer = new byte[lenght];  
                //将文件中的数据读到byte数组中  
                in.read(buffer);  
                result = EncodingUtils.getString(buffer, ENCODING);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return result;  
    }  
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {
		
		HttpEntity httpEntity = new EntityTemplate(
				new ContentProducer() {

					public void writeTo(final OutputStream outstream)
							throws IOException {

						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outstream, "UTF-8");
						String buffer = getFromAssets("index.html");
						outputStreamWriter.write(buffer);
						outputStreamWriter.flush();
					}
				});
		
		response.setHeader("Content-Type", "text/html");
		response.setEntity(httpEntity);
	}
}
