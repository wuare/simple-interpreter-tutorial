package top.wuare.part9;

import top.wuare.part9.ast.AST;
import top.wuare.part9.ast.Assign;
import top.wuare.part9.ast.BinOp;
import top.wuare.part9.ast.Compound;
import top.wuare.part9.ast.NoOp;
import top.wuare.part9.ast.Num;
import top.wuare.part9.ast.UnaryOp;
import top.wuare.part9.ast.Var;

import java.util.ArrayList;
import java.util.List;

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

    public AST program() {
        // program : compound_statement DOT
        AST node = compoundStatement();
        eat(Token.DOT);
        return node;
    }

    public AST compoundStatement() {
        // compound_statement: BEGIN statement_list END
        eat(Token.BEGIN);
        List<AST> nodes = statementList();
        eat(Token.END);

        Compound root = new Compound();
        root.getChildren().addAll(nodes);
        return root;
    }

    private List<AST> statementList() {
        // statement_list : statement
        //                   | statement SEMI statement_list
        AST node = statement();
        List<AST> results = new ArrayList<>();
        results.add(node);
        while (curToken.getType() == Token.SEMI) {
            eat(Token.SEMI);
            results.add(statement());
        }
        if (curToken.getType() == Token.ID) {
            error();
        }
        return results;
    }

    private AST statement() {
        // statement : compound_statement
        //              | assignment_statement
        //              | empty
        if (curToken.getType() == Token.BEGIN) {
            return compoundStatement();
        } else if (curToken.getType() == Token.ID) {
            return assignmentStatement();
        } else {
            return empty();
        }
    }

    private AST assignmentStatement() {
        // assignment_statement : variable ASSIGN expr
        Var left = variable();
        Token token = curToken;
        eat(Token.ASSIGN);
        AST right = expr();
        return new Assign(left, token, right);
    }

    private Var variable() {
        // variable : ID
        Var node = new Var(curToken);
        eat(Token.ID);
        return node;
    }

    private AST empty() {
        // An empty production
        return new NoOp();
    }


    public AST factor() {
        // factor : PLUS  factor
        //        | MINUS factor
        //        | INTEGER
        //        | LPAREN expr RPAREN
        //        | variable
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
        } else {
            return variable();
        }
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
        // program : compound_statement DOT
        //        compound_statement : BEGIN statement_list END
        //        statement_list : statement
        //                       | statement SEMI statement_list
        //        statement : compound_statement
        //                  | assignment_statement
        //                  | empty
        //        assignment_statement : variable ASSIGN expr
        //        empty :
        //        expr: term ((PLUS | MINUS) term)*
        //        term: factor ((MUL | DIV) factor)*
        //        factor : PLUS factor
        //               | MINUS factor
        //               | INTEGER
        //               | LPAREN expr RPAREN
        //               | variable
        //        variable: ID
        AST node = program();
        if (curToken.getType() != Token.EOF) {
            error();
        }
        return node;
    }
}
