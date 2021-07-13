package top.wuare.part18.ast;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class Param extends AST {
    private final Var varNode;
    private final Type typeNode;

    public Param(Var varNode, Type typeNode) {
        this.varNode = varNode;
        this.typeNode = typeNode;
    }

    public Var getVarNode() {
        return varNode;
    }

    public Type getTypeNode() {
        return typeNode;
    }
}
