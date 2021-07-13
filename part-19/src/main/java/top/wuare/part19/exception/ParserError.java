package top.wuare.part19.exception;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ParserError extends RuntimeException {

    public ParserError() {
    }

    public ParserError(String message) {
        super(message);
    }
}
