package top.wuare.part16.ast;

import top.wuare.part16.Token;

/**
 * @author wuare
 * @date 2021/7/9
 */
public class Type extends AST {
    private Token token;
    private String value;

    public Type(Token token) {
        this.token = token;
        this.value = token.getValue();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
