package top.wuare.part18.exception;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class LexerError extends RuntimeException {

    public LexerError() {
    }

    public LexerError(String message) {
        super(message);
    }

}
