package top.wuare.part7;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * token
 *
 * @author wuare
 * @date 2021/7/7
 */
@Data
@AllArgsConstructor
public class Token {

    public static final int EOF = -1;
    public static final int INTEGER = 1;
    public static final int PLUS = 2;
    public static final int MINUS = 3;
    public static final int MUL = 4;
    public static final int DIV = 5;
    public static final int LPAREN = 6;
    public static final int RPAREN = 7;

    private int type;
    private String value;
}
