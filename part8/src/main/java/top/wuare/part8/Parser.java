package top.wuare.part8;

import top.wuare.part8.ast.AST;
import top.wuare.part8.ast.BinOp;
import top.wuare.part8.ast.Num;
import top.wuare.part8.ast.UnaryOp;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Parser {
    private final Lexer lexer;
    private Token curToken;
    public Parser(Lexer lexer) {
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

    public AST factor() {
        // factor : (PLUS | MINUS) factor | INTEGER | LPAREN expr RPAREN
        Token token = curToken;
        if (token.getType() == Token.PLUS) {
            eat(Token.PLUS);
            return new UnaryOp(token, factor());
        } else if (token.getType() == Token.MINUS) {
            eat(Token.MINUS);
            return new UnaryOp(token, factor());
        } else if (token.getType() == Token.INTEGER) {
            eat(Token.INTEGER);
            return new Num(token);
        } else if (token.getType() == Token.LPAREN) {
            eat(Token.LPAREN);
            AST node = expr();
            eat(Token.RPAREN);
            return node;
        }
        error();
        return null;
    }

    public AST term() {
        // term : factor ((MUL | DIV) factor)*
        AST node = factor();
        while (curToken.getType() == Token.MUL || curToken.getType() == Token.DIV) {
            Token token = curToken;
            if (token.getType() == Token.MUL) {
                eat(Token.MUL);
            }
            if (token.getType() == Token.DIV) {
                eat(Token.DIV);
            }
            node = new BinOp(node, token, factor());
        }
        return node;
    }

    public AST expr() {
        // Arithmetic expression parser / interpreter
        // expr   : term ((PLUS | MINUS) term)*
        // term   : factor ((MUL | DIV) factor)*
        // factor : INTEGER | LPAREN expr RPAREN

        AST node = term();
        while (curToken.getType() == Token.PLUS || curToken.getType() == Token.MINUS) {
            Token token = curToken;
            if (token.getType() == Token.PLUS) {
                eat(Token.PLUS);
            } else if (token.getType() == Token.MINUS) {
                eat(Token.MINUS);
            }
            node = new BinOp(node, token, term());
        }
        return node;
    }

    public AST parse() {
        return expr();
    }
}
