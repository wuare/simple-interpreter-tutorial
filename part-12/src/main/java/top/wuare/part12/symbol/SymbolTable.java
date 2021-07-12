package top.wuare.part12.symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class SymbolTable {
    private final Map<String, Symbol> symbols = new HashMap<>();

    public SymbolTable() {
        initBuiltins();
    }

    private void initBuiltins() {
        define(new BuiltinTypeSymbol("INTEGER"));
        define(new BuiltinTypeSymbol("REAL"));
    }

    public void define(Symbol symbol) {
        System.out.println("Define: " + symbol);
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol lookup(String name) {
        System.out.println("Lookup: " + name);
        return symbols.get(name);
    }
}
