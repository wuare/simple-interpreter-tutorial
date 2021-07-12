package top.wuare.part13;

import top.wuare.part13.ast.AST;
import top.wuare.part13.ast.Assign;
import top.wuare.part13.ast.BinOp;
import top.wuare.part13.ast.Block;
import top.wuare.part13.ast.Compound;
import top.wuare.part13.ast.NoOp;
import top.wuare.part13.ast.Num;
import top.wuare.part13.ast.ProcedureDecl;
import top.wuare.part13.ast.Program;
import top.wuare.part13.ast.Type;
import top.wuare.part13.ast.UnaryOp;
import top.wuare.part13.ast.Var;
import top.wuare.part13.ast.VarDecl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Interpreter extends NodeVisitor {
    private final Parser parser;
    private final Map<String, BigDecimal> GLOBAL_SCOPE = new HashMap<>();

    public Interpreter(Parser parser) {
        this.parser = parser;
    }

    public BigDecimal visitBinOp(BinOp node) {
        if (node.getOp().getType() == Token.PLUS) {
            return ((BigDecimal) visit(node.getLeft())).add((BigDecimal) visit(node.getRight()));
        }
        if (node.getOp().getType() == Token.MINUS) {
            return ((BigDecimal) visit(node.getLeft())).subtract((BigDecimal) visit(node.getRight()));
        }
        if (node.getOp().getType() == Token.MUL) {
            return ((BigDecimal) visit(node.getLeft())).multiply((BigDecimal) visit(node.getRight()));
        }
        if (node.getOp().getType() == Token.INTEGER_DIV) {
            return new BigDecimal(((BigDecimal) visit(node.getLeft())).intValue() / ((BigDecimal) visit(node.getRight())).intValue());
        }
        if (node.getOp().getType() == Token.FLOAT_DIV) {
            return ((BigDecimal) visit(node.getLeft())).divide((BigDecimal) visit(node.getRight()), 11, RoundingMode.DOWN);
        }
        throw new RuntimeException("visit binOp error");
    }

    public BigDecimal visitNum(Num node) {
        return node.getValue();
    }

    public BigDecimal visitUnaryOp(UnaryOp node) {
        int type = node.getOp().getType();
        if (type == Token.PLUS) {
            return (BigDecimal) visit(node.getExpr());
        } else if (type == Token.MINUS) {
            return ((BigDecimal) visit(node.getExpr())).multiply(new BigDecimal("-1"));
        }
        throw new RuntimeException("visit unaryOp error");
    }

    public void visitCompound(Compound node) {
        for (AST child: node.getChildren()) {
            visit(child);
        }
    }

    public void visitNoOp(NoOp node) {
        // pass
    }

    public void visitAssign(Assign node) {
        String varName = node.getLeft().getValue();
        GLOBAL_SCOPE.put(varName, (BigDecimal) visit(node.getRight()));
    }

    public BigDecimal visitVar(Var node) {
        String varName = node.getValue();
        BigDecimal val = GLOBAL_SCOPE.get(varName);
        if (val == null) {
            throw new RuntimeException("name error: [" + varName + "]");
        } else {
            return val;
        }
    }

    public void visitProgram(Program node) {
        visit(node.getBlock());
    }

    public void visitBlock(Block node) {
        for(AST decl : node.getDeclarations()) {
            visit(decl);
        }
        visit(node.getCompound());
    }

    public void visitVarDecl(VarDecl node) {
        // pass
    }

    public void visitType(Type node) {
        // pass
    }

    public void visitProcedureDecl(ProcedureDecl node) {
        // pass
    }

    public Object interpret() {
        AST tree = parser.parse();
        if (tree == null) {
            return null;
        }
        return visit(tree);
    }


    public static void main(String[] args) {
        String text = "PROGRAM part13;\n" +
                "VAR\n" +
                "   a : INTEGER;\n" +
                "\n" +
                "PROCEDURE P1;\n" +
                "VAR\n" +
                "   a : REAL;\n" +
                "   k : INTEGER;\n" +
                "\n" +
                "   PROCEDURE P2;\n" +
                "   VAR\n" +
                "      a, z : INTEGER;\n" +
                "   BEGIN {P2}\n" +
                "      z := 777;\n" +
                "   END;  {P2}\n" +
                "\n" +
                "BEGIN {P1}\n" +
                "\n" +
                "END;  {P1}\n" +
                "\n" +
                "BEGIN {part13}\n" +
                "   a := 10;\n" +
                "END.  {part13}";
        Interpreter interpreter = new Interpreter(new Parser(new Lexer(text)));
        Object result = interpreter.interpret();
        System.out.println(interpreter.GLOBAL_SCOPE);
    }
}
