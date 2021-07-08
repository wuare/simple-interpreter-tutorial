package top.wuare.part7.ast;

import top.wuare.part7.Token;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Num extends AST {
    private Token token;
    private int value;

    public Num(Token token) {
        this.token = token;
        this.value = Integer.parseInt(token.getValue());
    }

    public Token getToken() {
        return token;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Num{" +
                "token=" + token +
                ", value=" + value +
                '}';
    }
}
