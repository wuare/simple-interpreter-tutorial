package top.wuare.part3;

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

    ///////////////////////////////////////////////////////////////////////////////
    //           Lexer code                                                      //
    ///////////////////////////////////////////////////////////////////////////////
    public void error() {
        throw new RuntimeException("Invalid syntax");
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
        // Lexical analyzer (also known as scanner or tokenizer)
        // This method is responsible for breaking a sentence apart into tokens. One token at a time
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

    ///////////////////////////////////////////////////////////////////////////////
    //           Parser / Interpreter code                                       //
    ///////////////////////////////////////////////////////////////////////////////
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

    public int term() {
        // Return an INTEGER token value
        Token token = curToken;
        eat(Token.INTEGER);
        return Integer.parseInt(token.getValue());
    }

    public int expr() {
        // Arithmetic expression parser / interpreter
        // set current token to the first token taken from the input
        curToken = getNextToken();

        int result = term();
        while (curToken.getType() == Token.PLUS || curToken.getType() == Token.MINUS) {
            Token token = curToken;
            if (token.getType() == Token.PLUS) {
                eat(Token.PLUS);
                result = result + term();
            } else if (token.getType() == Token.MINUS) {
                eat(Token.MINUS);
                result = result - term();
            }
        }
        return result;
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
