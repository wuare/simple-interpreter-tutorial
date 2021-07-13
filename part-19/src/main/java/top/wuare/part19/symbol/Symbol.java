package top.wuare.part19.symbol;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class Symbol {
    private String name;
    private Symbol type;
    private int scopeLevel;

    public Symbol() {
    }

    public Symbol(String name, Symbol type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Symbol getType() {
        return type;
    }

    public void setType(Symbol type) {
        this.type = type;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    public void setScopeLevel(int scopeLevel) {
        this.scopeLevel = scopeLevel;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", scopeLevel=" + scopeLevel +
                '}';
    }
}
