package lxg.cjz.rpc.common.exception;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class SerializerException extends RuntimeException{
    private static final long serialVersionUID = -6783134254669118520L;
    public SerializerException(String msg){
        super(msg);
    }

    public SerializerException(final Throwable cause){
        super(cause);
    }

    public SerializerException(String msg, final Throwable cause){
        super(msg, cause);
    }
}
