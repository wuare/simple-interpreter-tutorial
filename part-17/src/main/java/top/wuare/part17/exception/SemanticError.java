package top.wuare.part17.exception;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class SemanticError extends RuntimeException {

    public SemanticError() {
    }

    public SemanticError(String message) {
        super(message);
    }
}