package top.wuare.part15.exception;

import top.wuare.part15.Token;

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
