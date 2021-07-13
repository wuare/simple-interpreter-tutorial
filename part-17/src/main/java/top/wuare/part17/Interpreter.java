package top.wuare.part17;

import top.wuare.part17.ast.AST;
import top.wuare.part17.ast.Assign;
import top.wuare.part17.ast.BinOp;
import top.wuare.part17.ast.Block;
import top.wuare.part17.ast.Compound;
import top.wuare.part17.ast.NoOp;
import top.wuare.part17.ast.Num;
import top.wuare.part17.ast.ProcedureCall;
import top.wuare.part17.ast.ProcedureDecl;
import top.wuare.part17.ast.Program;
import top.wuare.part17.ast.Type;
import top.wuare.part17.ast.UnaryOp;
import top.wuare.part17.ast.Var;
import top.wuare.part17.ast.VarDecl;
import top.wuare.part17.runtime.ARType;
import top.wuare.part17.runtime.ActivationRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Interpreter extends NodeVisitor {
    private final Parser parser;
    private final Stack<ActivationRecord> callStack = new Stack<>();

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
        BigDecimal varValue = (BigDecimal) visit(node.getRight());
        ActivationRecord peek = callStack.peek();
        peek.setItem(varName, varValue);
    }

    public BigDecimal visitVar(Var node) {
        String varName = node.getValue();
        ActivationRecord peek = callStack.peek();
        Object varValue = peek.get(varName);
        return (BigDecimal) varValue;
    }

    public void visitProgram(Program node) {
        String name = node.getName();
        ActivationRecord ar = new ActivationRecord(name, ARType.PROGRAM, 1);
        callStack.push(ar);
        visit(node.getBlock());
        System.out.println(callStack);
        callStack.pop();
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

    public void visitProcedureCall(ProcedureCall node) {
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
        String text = "PROGRAM Main;\n" +
                "VAR x, y : INTEGER;\n" +
                "BEGIN { Main }\n" +
                "   y := 7;\n" +
                "   x := (y + 3) * 3;\n" +
                "END.  { Main }";
        Interpreter interpreter = new Interpreter(new Parser(new Lexer(text)));
        Object result = interpreter.interpret();
    }
}
