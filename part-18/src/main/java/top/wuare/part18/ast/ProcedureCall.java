package top.wuare.part18.ast;

import top.wuare.part18.Token;
import top.wuare.part18.symbol.ProcedureSymbol;

import java.util.List;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ProcedureCall extends AST {
    private String procName;
    private List<AST> actualParams;
    private Token token;
    private ProcedureSymbol procSymbol;

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

    public ProcedureSymbol getProcSymbol() {
        return procSymbol;
    }

    public void setProcSymbol(ProcedureSymbol procSymbol) {
        this.procSymbol = procSymbol;
    }
}
