package top.wuare.part12;

/**
 * token
 *
 * @author wuare
 * @date 2021/7/7
 */
public class Token {

    public static final int EOF = -1;
    public static final int INTEGER = 1;
    public static final int PLUS = 2;
    public static final int MINUS = 3;
    public static final int MUL = 4;
    public static final int LPAREN = 6;
    public static final int RPAREN = 7;


    public static final int BEGIN = 8;
    public static final int END = 9;
    public static final int DOT = 10;
    public static final int ASSIGN = 11;
    public static final int SEMI = 12;
    public static final int ID = 13;

    public static final int PROGRAM = 14;
    public static final int VAR = 15;
    public static final int COLON = 16;
    public static final int COMMA = 17;
    public static final int REAL = 18;
    public static final int INTEGER_CONST = 19;
    public static final int REAL_CONST = 20;
    public static final int INTEGER_DIV = 21;
    public static final int FLOAT_DIV = 22;

    public static final int PROCEDURE = 23;


    private int type;
    private String value;

    public Token(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
