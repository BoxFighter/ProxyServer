package cache;

import exceptions.ProxyServerException;
import request.MyRequest;
import response.MyResponse;
import response.ResponseBuilder;
import util.HashUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static util.Constants.SHA_256;
import static util.Constants.UTF_8;
import static util.Messages.CREATE_DIR_ERR;
import static util.Messages.NO_ALGORITHM_ERR;
import static util.Messages.NO_FILE_ERR;
import static util.Messages.UNSUPPORTED_ENCODING_ERR;
import static util.Messages.WRITE_RESPONSE_ERR;

/**
 * Disk supported cache.
 * @author 刘宸睿
 * @since 14/12/17
 */
public class DiskCache implements Cache {

    private String cacheRoot;
    private ResponseBuilder responseBuilder;

    public DiskCache(String cacheRoot, ResponseBuilder responseBuilder) {
        this.cacheRoot = cacheRoot;
        validateCacheRoot();
        this.responseBuilder = responseBuilder;
    }

    private void validateCacheRoot() {
        if (!cacheRoot.endsWith("/")) {
            cacheRoot += "/";
        }
        if (!pathExists(this.cacheRoot)) {
            if (!createPath(this.cacheRoot)) {
                throw new ProxyServerException(String.format(CREATE_DIR_ERR, this.cacheRoot));
            }
        }
    }

    private boolean createPath(String path) {
        File rootPath = new File(path);
        return rootPath.mkdirs();
    }

    private boolean pathExists(String path) {
        return new File(path).exists();
    }

    public boolean isCached(MyRequest httpRequest) {
        String filePath = getFilePath(httpRequest);
        return pathExists(filePath);
    }

    /**
     * 根据http request获得hash encoded file path
     * @param httpRequest
     * @return String
     */
    private String getFilePath(MyRequest httpRequest) {
        String uri = httpRequest.getUri();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
            byte[] hash = messageDigest.digest(uri.getBytes(UTF_8));
            String fileName = HashUtil.bytesToHex(hash);
            return cacheRoot + fileName;
        } catch (NoSuchAlgorithmException ex) {
            throw new ProxyServerException(String.format(NO_ALGORITHM_ERR, SHA_256));
        } catch (UnsupportedEncodingException ex) {
            throw new ProxyServerException(String.format(UNSUPPORTED_ENCODING_ERR, UTF_8));
        }
    }

    public MyResponse get(MyRequest httpRequest) {
        String filePath = getFilePath(httpRequest);
        File file = new File(filePath);
        MyResponse httpResponse = new MyResponse(responseBuilder);
        try {
            httpResponse.buildResponse(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            throw new ProxyServerException(String.format(NO_FILE_ERR),ex);
        }
        return httpResponse;
    }

    public void add(MyRequest httpRequest, MyResponse httpResponse) {
        String filePath = getFilePath(httpRequest);
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(httpResponse.getResponse());
        } catch (IOException ex) {
            throw new ProxyServerException(String.format(WRITE_RESPONSE_ERR, filePath), ex);
        }
    }
}
