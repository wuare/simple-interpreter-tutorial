package top.wuare.part11.ast;

import top.wuare.part11.Token;

/**
 * @author wuare
 * @date 2021/7/9
 */
public class Var extends AST {
    private Token token;
    private String value;

    public Var(Token token) {
        this.token = token;
        this.value = token.getValue();
    }

    public Token getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }
}
