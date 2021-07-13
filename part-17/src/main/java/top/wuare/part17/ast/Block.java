package top.wuare.part17.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuare
 * @date 2021/7/9
 */
public class Block extends AST {

    private List<AST> declarations = new ArrayList<>();
    private Compound compound;

    public Block(List<AST> declarations, Compound compound) {
        this.declarations = declarations;
        this.compound = compound;
    }

    public List<AST> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<AST> declarations) {
        this.declarations = declarations;
    }

    public Compound getCompound() {
        return compound;
    }

    public void setCompound(Compound compound) {
        this.compound = compound;
    }
}
