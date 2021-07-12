package top.wuare.part14.symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class ScopedSymbolTable {
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final String scopeName;
    private final int scopeLevel;
    private final ScopedSymbolTable enclosingScope;

    public ScopedSymbolTable(String scopeName, int scopeLevel, ScopedSymbolTable enclosingScope) {
        this.scopeName = scopeName;
        this.scopeLevel = scopeLevel;
        this.enclosingScope = enclosingScope;
        initBuiltins();
    }

    private void initBuiltins() {
        insert(new BuiltinTypeSymbol("INTEGER"));
        insert(new BuiltinTypeSymbol("REAL"));
    }

    public void insert(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol lookup(String name) {
        System.out.println("Lookup: " + name);
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
            return symbol;
        }
        if (enclosingScope != null) {
            return enclosingScope.lookup(name);
        }
        return null;
    }

    public String getScopeName() {
        return scopeName;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    public ScopedSymbolTable getEnclosingScope() {
        return enclosingScope;
    }

    @Override
    public String toString() {
        return "ScopedSymbolTable{" +
                "symbols=" + symbols +
                ", scopeName='" + scopeName + '\'' +
                ", scopeLevel=" + scopeLevel +
                ", enclosingScope=" + (enclosingScope != null ? enclosingScope.getScopeName() : null) +
                '}';
    }
}
