package cache;

import request.MyRequest;
import response.MyResponse;

/**
 * �����ӿ� ��cache
 * @author ����
 * @since 14/12/17
 */
public interface Cache {

    /**
     * ���ָ��http request�Ƿ��Ѿ�����
     * @param httpRequest
     * @return boolean
     */
    public boolean isCached(MyRequest httpRequest);

    /**
     * ���ָ��http request��http response����cache.
     * @param httpRequest
     * @param httpResponse
     */
    public void add(MyRequest httpRequest, MyResponse httpResponse);

    /**
     * ��cache����һ�ȡָ��http request�Ļ���
     * @param httpRequest
     * @return HttpResponse
     */
    public MyResponse get(MyRequest httpRequest);
}
