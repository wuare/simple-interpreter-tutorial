package top.wuare.part5;

import java.util.Scanner;

/**
 * @author wuare
 * @date 2021/7/7
 */
public class Interpreter {
    private final Lexer lexer;
    private Token curToken;
    public Interpreter(Lexer lexer) {
        this.lexer = lexer;
        this.curToken = lexer.getNextToken();
    }

    public void error() {
        throw new RuntimeException("Invalid syntax");
    }

    public void eat(int tokenType) {
        // compare the current token type with the passed token
        // type and if they match then "eat" the current token
        // and assign the next token to the self.current_token,
        // otherwise raise an exception.
        if (curToken.getType() == tokenType) {
            curToken = lexer.getNextToken();
        } else {
            error();
        }
    }

    public int factor() {
        // factor : INTEGER
        // Return an INTEGER token value
        Token token = curToken;
        eat(Token.INTEGER);
        return Integer.parseInt(token.getValue());
    }

    public int term() {
        // term : factor ((MUL | DIV) factor)*
        int result = factor();
        while (curToken.getType() == Token.MUL || curToken.getType() == Token.DIV) {
            Token token = curToken;
            if (token.getType() == Token.MUL) {
                eat(Token.MUL);
                result = result * factor();
            }
            if (token.getType() == Token.DIV) {
                eat(Token.DIV);
                result = result / factor();
            }
        }
        return result;
    }

    public int expr() {
        // Arithmetic expression parser / interpreter
        // expr   : term ((PLUS | MINUS) term)*
        // term   : factor ((MUL | DIV) factor)*
        // factor : INTEGER

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
            Interpreter interpreter = new Interpreter(new Lexer(text));
            int result = interpreter.expr();
            System.out.println(result);
        }
    }
}
