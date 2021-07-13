package top.wuare.part19.ast;

import top.wuare.part19.Token;

/**
 * @author wuare
 * @date 2021/7/9
 */
public class Assign extends AST {

    private Var left;
    private Token token;
    private Token op;
    private AST right;

    public Assign(Var left, Token op, AST right) {
        this.left = left;
        this.token = this.op = op;
        this.right = right;
    }

    public Var getLeft() {
        return left;
    }

    public Token getToken() {
        return token;
    }

    public Token getOp() {
        return op;
    }

    public AST getRight() {
        return right;
    }
}
