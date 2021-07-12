package top.wuare.part13.ast;

import top.wuare.part13.Token;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class UnaryOp extends AST {
    private Token token;
    private Token op;
    private AST expr;

    public UnaryOp(Token op, AST expr) {
        this.token = this.op = op;
        this.expr = expr;
    }

    public Token getOp() {
        return op;
    }

    public Token getToken() {
        return token;
    }

    public AST getExpr() {
        return expr;
    }
}
