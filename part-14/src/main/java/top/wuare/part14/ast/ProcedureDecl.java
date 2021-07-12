package top.wuare.part14.ast;

import java.util.List;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ProcedureDecl extends AST {
    private String procName;
    private List<Param> params;
    private Block blockNode;

    public ProcedureDecl(String procName, List<Param> params, Block blockNode) {
        this.procName = procName;
        this.params = params;
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

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
