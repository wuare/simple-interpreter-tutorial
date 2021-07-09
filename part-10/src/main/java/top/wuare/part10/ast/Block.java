package top.wuare.part10.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuare
 * @date 2021/7/9
 */
public class Block extends AST {

    private List<VarDecl> declarations = new ArrayList<>();
    private Compound compound;

    public Block(List<VarDecl> declarations, Compound compound) {
        this.declarations = declarations;
        this.compound = compound;
    }

    public List<VarDecl> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<VarDecl> declarations) {
        this.declarations = declarations;
    }

    public Compound getCompound() {
        return compound;
    }

    public void setCompound(Compound compound) {
        this.compound = compound;
    }
}
