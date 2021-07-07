package top.wuare.part1;

import java.util.Scanner;

/**
 * @author wuare
 * @date 2021/7/7
 */
public class Interpreter {
    private String text;
    private int pos;
    private Token curToken;
    public Interpreter(String text) {
        this.text = text;
        this.pos = 0;
        this.curToken = null;
    }


    public void error() {
        throw new RuntimeException("Error parsing input");
    }

    public Token getNextToken() {
        String t = this.text;
        if (this.pos > len(t) - 1) {
            return new Token(Token.EOF, null);
        }
        char ch = text.charAt(pos);
        if (Character.isDigit(ch)) {
            this.pos++;
            return new Token(Token.INTEGER, String.valueOf(ch));
        }
        if (ch == '+') {
            this.pos++;
            return new Token(Token.PLUS, String.valueOf(ch));
        }
        error();
        return null;
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
        // set current token to the first token taken from the input
        curToken = getNextToken();
        // we expect the current token to be a single-digit integer
        Token left = curToken;
        eat(Token.INTEGER);

        // we expect the current token to be a '+' token
        Token op = curToken;
        eat(Token.PLUS);

        // we expect the current token to be a single-digit integer
        Token right = curToken;
        eat(Token.INTEGER);

        // after the above call the self.current_token is set to
        // EOF token

        // at this point INTEGER PLUS INTEGER sequence of tokens
        // has been successfully found and the method can just
        // return the result of adding two integers, thus
        // effectively interpreting client input

        return Integer.parseInt(left.getValue()) + Integer.parseInt(right.getValue());
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
