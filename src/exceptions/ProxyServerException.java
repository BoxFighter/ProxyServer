package exceptions;

/**
 * ´¦ÀíexceptionµÄclass.
 * @author Áõå·î£
 * @since 12/12/17
 */
public class ProxyServerException extends RuntimeException {

    public ProxyServerException(String message) {
        super(message);
    }

    public ProxyServerException(String message, Exception ex) {
        super(message, ex);
    }

    public ProxyServerException(Exception ex) {
        super(ex);
    }
}
