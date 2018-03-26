package cache;

import request.MyRequest;
import response.MyResponse;

/**
 * 公共接口 ：cache
 * @author 刘宸睿
 * @since 14/12/17
 */
public interface Cache {

    /**
     * 检查指定http request是否已经缓存
     * @param httpRequest
     * @return boolean
     */
    public boolean isCached(MyRequest httpRequest);

    /**
     * 添加指定http request的http response进入cache.
     * @param httpRequest
     * @param httpResponse
     */
    public void add(MyRequest httpRequest, MyResponse httpResponse);

    /**
     * 从cache里查找获取指定http request的缓存
     * @param httpRequest
     * @return HttpResponse
     */
    public MyResponse get(MyRequest httpRequest);
}
