package util;

import exceptions.ProxyServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static util.Messages.ACCEPT_SOCK_ERR;
import static util.Messages.CLOSE_SOCK_ERR;
import static util.Messages.CREATE_SERVER_SOCK_ERR;
import static util.Messages.GET_INPUT_STREAM_ERR;
import static util.Messages.GET_OUTPUT_STEAM_ERR;
import static util.Messages.UNKNOWN_HOST_ERR;

/**
 * socketπ§æﬂ¿‡.
 * @author ¡ıÂ∑Ó£
 * @since 16/12/17
 */
public class SocketUtil {

    public static InputStream getInputStream(Socket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException ex) {
            throw new ProxyServerException(GET_INPUT_STREAM_ERR, ex);
        }
    }

    public static OutputStream getOutputStream(Socket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException ex) {
            throw new ProxyServerException(GET_OUTPUT_STEAM_ERR, ex);
        }
    }

    public static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            throw new ProxyServerException(CLOSE_SOCK_ERR, ex);
        }
    }

    public static Socket createSocket(String host, int port) {
        try {
            return new Socket(host, port);
        } catch (UnknownHostException ex) {
            throw new ProxyServerException(String.format(UNKNOWN_HOST_ERR, host), ex);
        } catch (IOException ex) {
            throw new ProxyServerException(ex);
        }
    }

    public static ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException ex) {
            throw new ProxyServerException(String.format(CREATE_SERVER_SOCK_ERR, port), ex);
        }
    }

    public static Socket acceptClient(ServerSocket serverSocket) {
        try {
            return serverSocket.accept();
        } catch (IOException ex) {
            throw new ProxyServerException(ACCEPT_SOCK_ERR, ex);
        }
    }
}
