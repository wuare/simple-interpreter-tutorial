package top.wuare.part15.exception;

import top.wuare.part15.Token;

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
