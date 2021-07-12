package top.wuare.part16.symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ProcedureSymbol extends Symbol {
    private List<VarSymbol> params;

    public ProcedureSymbol(String name, List<VarSymbol> params) {
        super(name, null);
        this.params = params == null ? new ArrayList<>() : params;
    }

    public List<VarSymbol> getParams() {
        return params;
    }
}
