package response;

import java.io.InputStream;

/**
 * 处理http response
 * @author 刘宸睿
 * @since 15/12/17
 */
public class MyResponse {
    private ResponseBuilder responseBuilder;

    public MyResponse(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    /**
     * 构建http response
     * @param inputStream
     */
    public void buildResponse(InputStream inputStream) {
        responseBuilder.build(inputStream);
    }

    public byte[] getResponse() {
        return responseBuilder.getResponse();
    }
}
