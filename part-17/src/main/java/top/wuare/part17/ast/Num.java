package top.wuare.part17.ast;
import top.wuare.part17.Token;

import java.math.BigDecimal;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Num extends AST {
    private Token token;
    private BigDecimal value;

    public Num(Token token) {
        this.token = token;
        this.value = new BigDecimal(token.getValue());
    }

    public Token getToken() {
        return token;
    }

    public BigDecimal getValue() {
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
