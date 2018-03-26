package response;

import java.io.InputStream;

/**
 * ����http response
 * @author ����
 * @since 15/12/17
 */
public class MyResponse {
    private ResponseBuilder responseBuilder;

    public MyResponse(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    /**
     * ����http response
     * @param inputStream
     */
    public void buildResponse(InputStream inputStream) {
        responseBuilder.build(inputStream);
    }

    public byte[] getResponse() {
        return responseBuilder.getResponse();
    }
}
