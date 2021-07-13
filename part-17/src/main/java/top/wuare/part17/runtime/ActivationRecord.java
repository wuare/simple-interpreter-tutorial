package top.wuare.part17.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuare
 * @date 2021/7/13
 */
public class ActivationRecord {
    private final String name;
    private final int type;
    private final int nestingLevel;
    private final Map<String, Object> members = new HashMap<>();

    public ActivationRecord(String name, int type, int nestingLevel) {
        this.name = name;
        this.type = type;
        this.nestingLevel = nestingLevel;
    }

    public void setItem(String key, Object value) {
        members.put(key, value);
    }

    public Object getItem(String key) {
        return members.get(key);
    }

    public Object get(String key) {
        return members.get(key);
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    @Override
    public String toString() {
        return "ActivationRecord{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", nestingLevel=" + nestingLevel +
                ", members=" + members +
                '}';
    }
}
