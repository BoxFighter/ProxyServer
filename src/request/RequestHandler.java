package request;

import core.ProxyServer;
import exceptions.ProxyServerException;
import response.MyResponse;
import response.ResponseBuilder;
import util.SocketUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import static util.Messages.REPLY_CLIENT_ERR;

/**
 * 子线程
 * @author 刘宸睿
 * @since 14/12/17
 */
public class RequestHandler extends Thread {

    private Socket client;
    private ProxyServer proxyServer;
    private static Logger logger = Logger.getLogger(RequestHandler.class.getName());

    public RequestHandler(Socket client, ProxyServer proxyServer) {
        this.client = client;
        this.proxyServer = proxyServer;
    }

    @Override
    public void run() {
        RequestBuilder requestBuilder = new RequestBuilder();
        ResponseBuilder responseBuilder = new ResponseBuilder();
        MyRequest httpRequest = new MyRequest(requestBuilder, responseBuilder);
        httpRequest.buildRequest(SocketUtil.getInputStream(client));
        logger.info(String.format("Handle request: %s.", httpRequest.getUri()));
        if (httpRequest.isValid() && httpRequest.isGet()) {
            MyResponse httpResponse = getResponse(httpRequest);
            replyToClient(httpResponse);
        }
        SocketUtil.closeSocket(client);
    }

    /**
     * 从cache或server获取http response
     * @param httpRequest
     * @return HttpResponse
     */
    private MyResponse getResponse(MyRequest httpRequest) {
        MyResponse httpResponse;
        if (proxyServer.isCached(httpRequest)) {
            logger.warning(String.format("Served request %s from cache.", httpRequest.getUri()));
            httpResponse = proxyServer.getCache(httpRequest);
            if(!httpRequest.isModified(httpResponse)){
            	httpResponse = httpRequest.execute();
                proxyServer.addCache(httpRequest, httpResponse);
            }
        } else {
            httpResponse = httpRequest.execute();
            proxyServer.addCache(httpRequest, httpResponse);
        }
        return httpResponse;
    }

    /**
     * 返回http response给浏览器
     * @param httpResponse
     */
    private void replyToClient(MyResponse httpResponse) {
        try {
            DataOutputStream outputStream = new DataOutputStream(SocketUtil.getOutputStream
                    (client));
            outputStream.write(httpResponse.getResponse());
        } catch (IOException ex) {
            throw new ProxyServerException(REPLY_CLIENT_ERR, ex);
        }
    }
}
