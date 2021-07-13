package top.wuare.part18.symbol;

import top.wuare.part18.ast.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ProcedureSymbol extends Symbol {
    private List<VarSymbol> params;
    private Block blockAst;

    public ProcedureSymbol(String name, List<VarSymbol> params) {
        super(name, null);
        this.params = params == null ? new ArrayList<>() : params;
    }

    public List<VarSymbol> getParams() {
        return params;
    }

    public Block getBlockAst() {
        return blockAst;
    }

    public void setBlockAst(Block blockAst) {
        this.blockAst = blockAst;
    }
}
