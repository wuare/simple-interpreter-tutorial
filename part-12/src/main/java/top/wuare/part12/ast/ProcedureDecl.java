package top.wuare.part12.ast;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ProcedureDecl extends AST {
    private String procName;
    private Block blockNode;

    public ProcedureDecl(String procName, Block blockNode) {
        this.procName = procName;
        this.blockNode = blockNode;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public Block getBlockNode() {
        return blockNode;
    }

    public void setBlockNode(Block blockNode) {
        this.blockNode = blockNode;
    }
}
