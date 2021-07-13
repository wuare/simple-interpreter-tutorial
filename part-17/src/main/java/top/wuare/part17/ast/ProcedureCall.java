package top.wuare.part17.ast;

import top.wuare.part17.Token;

import java.util.List;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ProcedureCall extends AST {
    private String procName;
    private List<AST> actualParams;
    private Token token;

    public ProcedureCall(String procName, List<AST> actualParams, Token token) {
        this.procName = procName;
        this.actualParams = actualParams;
        this.token = token;
    }

    public String getProcName() {
        return procName;
    }

    public List<AST> getActualParams() {
        return actualParams;
    }

    public Token getToken() {
        return token;
    }
}
