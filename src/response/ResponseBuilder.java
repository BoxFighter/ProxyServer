package response;

import exceptions.ProxyServerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static util.Constants.BUFFER_SIZE;
import static util.Constants.MAX_OBJECT_SIZE;
import static util.Messages.READ_BODY_ERR;

/**
 * ����http response
 * @author ����
 * @since 15/12/17
 */
public class ResponseBuilder {
    private byte[] response;
    private int responseSize;

    public ResponseBuilder() {
        response = new byte[MAX_OBJECT_SIZE];
    }

    /**
     * ����http response
     * @param inputStream
     */
    public void build(InputStream inputStream) {
        int totalBytesRead = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            int bytesRead = inputStream.read(buffer);
            while (bytesRead != -1) {
                for (int i=0; i<bytesRead && totalBytesRead+i < MAX_OBJECT_SIZE; i++) {
                    response[i+totalBytesRead] = buffer[i];
                }
                totalBytesRead += bytesRead;
                bytesRead = inputStream.read(buffer);
            }
        } catch (IOException ex) {
            throw new ProxyServerException(READ_BODY_ERR, ex);
        }
        responseSize = totalBytesRead;
    }

    public byte[] getResponse() {
        return Arrays.copyOf(response, responseSize);
    }
}
