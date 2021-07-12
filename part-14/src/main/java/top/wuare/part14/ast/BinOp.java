package top.wuare.part14.ast;

import top.wuare.part14.Token;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class BinOp extends AST {
    private AST left;
    private AST right;
    private Token token;
    private Token op;

    public BinOp(AST left, Token op, AST right) {
        this.left = left;
        this.token = this.op = op;
        this.right = right;
    }

    public AST getLeft() {
        return left;
    }

    public AST getRight() {
        return right;
    }

    public Token getOp() {
        return op;
    }

    @Override
    public String toString() {
        return "BinOp{" +
                "left=" + left +
                ", right=" + right +
                ", token=" + token +
                ", op=" + op +
                '}';
    }
}
