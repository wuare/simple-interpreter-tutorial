package top.wuare.part19;

import top.wuare.part19.ast.AST;
import top.wuare.part19.ast.Assign;
import top.wuare.part19.ast.BinOp;
import top.wuare.part19.ast.Block;
import top.wuare.part19.ast.Compound;
import top.wuare.part19.ast.NoOp;
import top.wuare.part19.ast.Num;
import top.wuare.part19.ast.Param;
import top.wuare.part19.ast.ProcedureCall;
import top.wuare.part19.ast.ProcedureDecl;
import top.wuare.part19.ast.Program;
import top.wuare.part19.ast.Type;
import top.wuare.part19.ast.UnaryOp;
import top.wuare.part19.ast.Var;
import top.wuare.part19.ast.VarDecl;
import top.wuare.part19.exception.ParserError;

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

    public void error(Token token) {
        throw new ParserError("unexpected token -> " + (token != null ? token.toString(): null));
    }

    public void eat(int tokenType) {
        // compare the current token type with the passed token
        // type and if they match then "eat" the current token
        // and assign the next token to the self.current_token,
        // otherwise raise an exception.
        if (curToken.getType() == tokenType) {
            curToken = lexer.getNextToken();
        } else {
            error(curToken);
        }
    }

    public Program program() {
        // program : PROGRAM variable SEMI block DOT
        eat(Token.PROGRAM);
        Var variable = variable();
        String programName = variable.getValue();
        eat(Token.SEMI);
        Block block = block();
        Program programNode = new Program(programName, block);
        eat(Token.DOT);
        return programNode;
    }

    public Compound compoundStatement() {
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
            error(curToken);
        }
        return results;
    }

    private AST statement() {
        // statement : compound_statement
        //              | procCall_statement
        //              | assignment_statement
        //              | empty
        if (curToken.getType() == Token.BEGIN) {
            return compoundStatement();
        } else if (curToken.getType() == Token.ID && lexer.getCurCh() == '(') {
            return procCallStatement();
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

    private Block block() {
        // block : declarations compound_statement
        List<AST> declarationNodes = declarations();
        Compound compound = compoundStatement();
        return new Block(declarationNodes, compound);
    }

    private ProcedureCall procCallStatement() {
        // procCall_statement : ID LPAREN (expr (COMMA expr)*)? RPAREN
        Token token = curToken;
        String procName = token.getValue();
        eat(Token.ID);
        eat(Token.LPAREN);
        List<AST> actualParams = new ArrayList<>();
        if (curToken.getType() != Token.RPAREN) {
            AST expr = expr();
            actualParams.add(expr);
        }
        while (curToken.getType() == Token.COMMA) {
            eat(Token.COMMA);
            AST expr = expr();
            actualParams.add(expr);
        }
        eat(Token.RPAREN);
        return new ProcedureCall(procName, actualParams, token);
    }

    private List<AST> declarations() {
        // declarations : (VAR (variable_declaration SEMI)+)*
        //              | empty
        List<AST> declarations = new ArrayList<>();
        while (curToken.getType() == Token.VAR) {
            eat(Token.VAR);
            while (curToken.getType() == Token.ID) {
                List<VarDecl> varDecls = variableDeclaration();
                declarations.addAll(varDecls);
                eat(Token.SEMI);
            }
        }

        while (curToken.getType() == Token.PROCEDURE) {
            ProcedureDecl procedureDecl = procedureDeclaration();
            declarations.add(procedureDecl);
        }
        return declarations;
    }

    public ProcedureDecl procedureDeclaration() {
        // (PROCEDURE ID (LPAREN formal_parameter_list RPAREN)? SEMI block SEMI)*
        eat(Token.PROCEDURE);
        String procName = curToken.getValue();
        eat(Token.ID);
        List<Param> params = new ArrayList<>();
        if (curToken.getType() == Token.LPAREN) {
            eat(Token.LPAREN);
            params = formalParameterList();
            eat(Token.RPAREN);
        }
        eat(Token.SEMI);
        Block blockNode = block();
        ProcedureDecl procedureDecl = new ProcedureDecl(procName, params, blockNode);
        eat(Token.SEMI);
        return procedureDecl;
    }

    private List<VarDecl> variableDeclaration() {
        // variable_declaration : ID (COMMA ID)* COLON type_spec
        List<Var> varNodes = new ArrayList<>();
        varNodes.add(new Var(curToken));
        eat(Token.ID);

        while (curToken.getType() == Token.COMMA) {
            eat(Token.COMMA);
            varNodes.add(new Var(curToken));
            eat(Token.ID);
        }
        eat(Token.COLON);

        Type typeNode = typeSpec();
        List<VarDecl> varDecls = new ArrayList<>();
        for (Var var : varNodes) {
            varDecls.add(new VarDecl(var, typeNode));
        }
        return varDecls;
    }

    private List<Param> formalParameterList() {
        // formal_parameter_list : formal_parameters
        //                       | formal_parameters SEMI formal_parameter_list

        if (curToken.getType() != Token.ID) {
            return new ArrayList<>();
        }
        List<Param> paramNodes = formalParameters();
        while (curToken.getType() == Token.SEMI) {
            eat(Token.SEMI);
            paramNodes.addAll(formalParameters());
        }
        return paramNodes;
    }

    private List<Param> formalParameters() {
        // formal_parameters : ID (COMMA ID)* COLON type_spec
        List<Param> params = new ArrayList<>();
        List<Token> paramTokens = new ArrayList<>();
        paramTokens.add(curToken);
        eat(Token.ID);
        while (curToken.getType() == Token.COMMA) {
            eat(Token.COMMA);
            paramTokens.add(curToken);
            eat(Token.ID);
        }
        eat(Token.COLON);
        Type type = typeSpec();
        for (Token token : paramTokens) {
            params.add(new Param(new Var(token), type));
        }
        return params;
    }

    private Type typeSpec() {
        // type_spec : INTEGER
        //           | REAL
        Token token = curToken;
        if (token.getType() == Token.INTEGER) {
            eat(Token.INTEGER);
        } else {
            eat(Token.REAL);
        }
        return new Type(token);
    }


    public AST factor() {
        // factor : PLUS factor
        //        | MINUS factor
        //        | INTEGER_CONST
        //        | REAL_CONST
        //        | LPAREN expr RPAREN
        //        | variable
        Token token = curToken;
        if (token.getType() == Token.PLUS) {
            eat(Token.PLUS);
            return new UnaryOp(token, factor());
        } else if (token.getType() == Token.MINUS) {
            eat(Token.MINUS);
            return new UnaryOp(token, factor());
        } else if (token.getType() == Token.INTEGER_CONST) {
            eat(Token.INTEGER_CONST);
            return new Num(token);
        } else if (token.getType() == Token.REAL_CONST) {
            eat(Token.REAL_CONST);
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
        // term : factor ((MUL | INTEGER_DIV | FLOAT_DIV) factor)*
        AST node = factor();
        while (curToken.getType() == Token.MUL
                || curToken.getType() == Token.INTEGER_DIV
                || curToken.getType() == Token.FLOAT_DIV) {
            Token token = curToken;
            if (token.getType() == Token.MUL) {
                eat(Token.MUL);
            }
            if (token.getType() == Token.INTEGER_DIV) {
                eat(Token.INTEGER_DIV);
            }
            if (token.getType() == Token.FLOAT_DIV) {
                eat(Token.FLOAT_DIV);
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
            error(curToken);
        }
        return node;
    }
}
