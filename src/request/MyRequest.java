package request;

import exceptions.ProxyServerException;
import response.MyResponse;
import response.ResponseBuilder;
import util.SocketUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static util.Constants.HTTP_GET;
import static util.Constants.LOCAL_HOST;
import static util.Messages.SEND_TO_HOST_ERR;

/**
 * 处理http request的类.
 * @author 刘宸睿
 * @since 13/12/17
 */
public class MyRequest {
    private RequestBuilder requestBuilder;
    private ResponseBuilder responseBuilder;

    public MyRequest(RequestBuilder requestBuilder, ResponseBuilder responseBuilder) {
        this.requestBuilder = requestBuilder;
        this.responseBuilder = responseBuilder;
    }

    /**
     * 建立http requst from socket输入流.
     * @param inputStream
     */
    public void buildRequest(InputStream inputStream){
        requestBuilder.build(inputStream);
    }

    /**
     * 向server发送http request
     * @return HttpResponse
     */
    public MyResponse execute() {
        Socket client = SocketUtil.createSocket(requestBuilder.getHost(), requestBuilder.getPort());
        DataOutputStream outputStream = new DataOutputStream(SocketUtil.getOutputStream(client));
        try {
            outputStream.writeBytes(requestBuilder.getHeadersAsString());
            outputStream.flush();
        } catch (IOException ex) {
            throw new ProxyServerException(String.format(SEND_TO_HOST_ERR, requestBuilder.getHost()), ex);
        }
        MyResponse response = new MyResponse(responseBuilder);
        response.buildResponse(SocketUtil.getInputStream(client));
        SocketUtil.closeSocket(client);
        return response;
    }

    /**
     * 获取request id
     * @return String
     */
    public String getRequestId() {
        return requestBuilder.getHeadId();
    }

    /**
     * 获取http request的uri(host+path)
     * @return String
     */
    public String getUri() {
        return requestBuilder.getUri();
    }

    /**
     * 此http request 是否是GET request.
     * @return booolean
     */
    public boolean isGet() {
        return requestBuilder.getMethod().equals(HTTP_GET);
    }

    @Override
    public int hashCode() {
        return getRequestId().hashCode();
    }

    @Override
    public boolean equals(Object httpRequest) {
        if (!(httpRequest instanceof MyRequest)) {
            return false;
        } else if (httpRequest == this) {
            return true;
        } else {
            return getRequestId().equals(((MyRequest) httpRequest).getRequestId());
        }
    }

    public boolean isValid() {
        return requestBuilder.getHost().length() != 0 && !requestBuilder.getHost().equals
                (LOCAL_HOST);
    }
    
    public boolean isModified(MyResponse myResponse) {
    	String modifyTime;
        modifyTime=findModifyTime(myResponse.getResponse());//提取modifytime
    	Socket client = SocketUtil.createSocket(requestBuilder.getHost(), requestBuilder.getPort());
        DataOutputStream outputStream = new DataOutputStream(SocketUtil.getOutputStream(client));
        try {
        	String str1 = "Host: " + requestBuilder.getHost() + "\r\n";
            outputStream.writeBytes(str1);
            String str = "If-modified-since: " + modifyTime
                    + "\r\n";
            outputStream.writeBytes(str);
            outputStream.writeBytes("\r\n");
            outputStream.flush();
        } catch (IOException ex) {
            throw new ProxyServerException(String.format(SEND_TO_HOST_ERR, requestBuilder.getHost()), ex);
        }
        MyResponse response = new MyResponse(responseBuilder);
        response.buildResponse(SocketUtil.getInputStream(client));
        SocketUtil.closeSocket(client);
        if (new String(response.getResponse()).contains("Not Modified")){
        	return true;
        }
		return false;
	}

	private String findModifyTime(byte[] response) {
		String res;
		String LastModifiTime=null;
		res=new String(response);

        if (res.contains("Last-Modified:")){
        	LastModifiTime=res.substring(res.indexOf("Last-Modified:"));
        	return LastModifiTime;
        }
       
        return LastModifiTime;
	}
}
