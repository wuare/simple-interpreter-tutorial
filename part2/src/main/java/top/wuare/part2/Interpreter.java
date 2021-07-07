package top.wuare.part2;

import java.util.Scanner;

/**
 * @author wuare
 * @date 2021/7/7
 */
public class Interpreter {
    private String text;
    private int pos;
    private Character curCh;
    private Token curToken;
    public Interpreter(String text) {
        this.text = text;
        this.pos = 0;
        this.curToken = null;
        this.curCh = text.charAt(this.pos);
    }


    public void error() {
        throw new RuntimeException("Error parsing input");
    }

    public void advance() {
        // Advance the 'pos' pointer and set the 'current_char' variable
        this.pos++;
        if (pos > len(text) - 1) {
            curCh = null;
        } else {
            curCh = text.charAt(pos);
        }
    }

    public void skipWhiteSpace() {
        while (curCh != null && Character.isWhitespace(curCh)) {
            advance();
        }
    }

    public int integer() {
        // Return a (multi-digit) integer consumed from the input
        StringBuilder builder = new StringBuilder();
        while (curCh != null && Character.isDigit(curCh)) {
            builder.append(curCh);
            advance();
        }
        return Integer.parseInt(builder.toString());
    }

    public Token getNextToken() {
        while (curCh != null) {
            if (Character.isWhitespace(curCh)) {
                skipWhiteSpace();
                continue;
            }
            if (Character.isDigit(curCh)) {
                return new Token(Token.INTEGER, String.valueOf(integer()));
            }
            if (curCh == '+') {
                advance();
                return new Token(Token.PLUS, "+");
            }
            if (curCh == '-') {
                advance();
                return new Token(Token.MINUS, "-");
            }
            error();
        }
        return new Token(Token.EOF, null);
    }

    public int len(String text) {
        return text != null ? text.length() : 0;
    }

    public void eat(int tokenType) {
        // compare the current token type with the passed token
        // type and if they match then "eat" the current token
        // and assign the next token to the self.current_token,
        // otherwise raise an exception.
        if (curToken.getType() == tokenType) {
            curToken = getNextToken();
        } else {
            error();
        }
    }

    public int expr() {
        // expr -> INTEGER PLUS INTEGER
        // expr -> INTEGER MINUS INTEGER
        // set current token to the first token taken from the input
        curToken = getNextToken();
        // we expect the current token to be an integer
        Token left = curToken;
        eat(Token.INTEGER);

        // we expect the current token to be either a '+' or '-'
        Token op = curToken;
        if (op.getType() == Token.PLUS) {
            eat(Token.PLUS);
        } else {
            eat(Token.MINUS);
        }

        // we expect the current token to be an integer
        Token right = curToken;
        eat(Token.INTEGER);

        // after the above call the self.current_token is set to
        // EOF token

        // at this point either the INTEGER PLUS INTEGER or
        // the INTEGER MINUS INTEGER sequence of tokens
        // has been successfully found and the method can just
        // return the result of adding or subtracting two integers,
        // thus effectively interpreting client input
        if (op.getType() == Token.PLUS) {
            return Integer.parseInt(left.getValue()) + Integer.parseInt(right.getValue());
        } else {
            return Integer.parseInt(left.getValue()) - Integer.parseInt(right.getValue());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.print("calc> ");
            Scanner scanner = new Scanner(System.in);
            String text = scanner.nextLine();
            if (text == null) {
                continue;
            }
            Interpreter interpreter = new Interpreter(text);
            int result = interpreter.expr();
            System.out.println(result);
        }
    }
}
