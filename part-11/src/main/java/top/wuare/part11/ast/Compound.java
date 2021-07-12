package top.wuare.part11.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuare
 * @date 2021/7/9
 */
public class Compound extends AST {

    private List<AST> children = new ArrayList<>();

    public List<AST> getChildren() {
        return children;
    }

    public void setChildren(List<AST> children) {
        this.children = children;
    }
}
