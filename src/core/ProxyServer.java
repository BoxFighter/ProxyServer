package core;

import cache.Cache;
import cache.DiskCache;
import exceptions.ProxyServerException;
import request.MyRequest;
import request.RequestHandler;
import response.MyResponse;
import response.ResponseBuilder;
import util.SocketUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

import static util.Messages.NEED_PORT_ARG_INFO;
import static util.Messages.PORT_NUMBER_FMT_ERR;

/**
 * ������������߳�
 * @author ����
 * @since 12/12/17
 */
public class ProxyServer {
    private final int port;
    private ServerSocket serverSocket;
    private final Cache cache;
    private static Logger logger = Logger.getLogger(ProxyServer.class.getName());

    public ProxyServer(int port, Cache cache) {
        this.port = port;
        this.cache = cache;
        serverSocket = SocketUtil.createServerSocket(port);
    }

    public synchronized boolean isCached(MyRequest httpRequest) {
        return cache.isCached(httpRequest);
    }

    public synchronized MyResponse getCache(MyRequest httpRequest) {
        return cache.get(httpRequest);
    }

    public synchronized void addCache(MyRequest httpRequest, MyResponse httpResponse) {
        cache.add(httpRequest, httpResponse);
    }

    /**
     * ���������������http request
     * @param client
     */
    private void handle(Socket client) {
        RequestHandler requestHandler = new RequestHandler(client, this);
        requestHandler.start();
    }

    public void run() {
        logger.info("������������� \n �˿�:" + port);
        while (true) {
            Socket client = SocketUtil.acceptClient(serverSocket);
            handle(client);
        }
    }

    public static void main(String[] args) {
        try {
            int port = 8888;
            System.out.println("�����뻺��Ĵ洢Ŀ¼������t������ΪĬ��Ŀ¼������ͬһĿ¼�£�");
            Scanner scanner=new Scanner(System.in);
            String cachePath = scanner.nextLine();
            if(cachePath.equals("t")){
                cachePath="tmp/";
            }
            System.out.println("����Ŀ¼���óɹ�");
            scanner.close();
            Cache cache = new DiskCache(cachePath, new ResponseBuilder());
            ProxyServer proxyServer = new ProxyServer(port, cache);
            proxyServer.run();
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ProxyServerException(NEED_PORT_ARG_INFO, ex);
        } catch (NumberFormatException ex) {
            throw new ProxyServerException(PORT_NUMBER_FMT_ERR, ex);
        }
    }
}
