package top.wuare.part9;

import top.wuare.part9.ast.AST;
import top.wuare.part9.ast.Assign;
import top.wuare.part9.ast.BinOp;
import top.wuare.part9.ast.Compound;
import top.wuare.part9.ast.NoOp;
import top.wuare.part9.ast.Num;
import top.wuare.part9.ast.UnaryOp;
import top.wuare.part9.ast.Var;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Interpreter extends NodeVisitor {
    private final Parser parser;
    private final Map<String, Integer> GLOBAL_SCOPE = new HashMap<>();

    public Interpreter(Parser parser) {
        this.parser = parser;
    }

    public int visitBinOp(BinOp node) {
        if (node.getOp().getType() == Token.PLUS) {
            return (int) visit(node.getLeft()) + (int) visit(node.getRight());
        }
        if (node.getOp().getType() == Token.MINUS) {
            return (int) visit(node.getLeft()) - (int) visit(node.getRight());
        }
        if (node.getOp().getType() == Token.MUL) {
            return (int) visit(node.getLeft()) * (int) visit(node.getRight());
        }
        if (node.getOp().getType() == Token.DIV) {
            return (int) visit(node.getLeft()) / (int) visit(node.getRight());
        }
        throw new RuntimeException("visit binOp error");
    }

    public int visitNum(Num node) {
        return node.getValue();
    }

    public int visitUnaryOp(UnaryOp node) {
        int type = node.getOp().getType();
        if (type == Token.PLUS) {
            return +(int) visit(node.getExpr());
        } else if (type == Token.MINUS) {
            return -(int) visit(node.getExpr());
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
        GLOBAL_SCOPE.put(varName, (int) visit(node.getRight()));
    }

    public int visitVar(Var node) {
        String varName = node.getValue();
        Integer val = GLOBAL_SCOPE.get(varName);
        if (val == null) {
            throw new RuntimeException("name error: [" + varName + "]");
        } else {
            return val;
        }
    }

    public Object interpret() {
        AST tree = parser.parse();
        if (tree == null) {
            return null;
        }
        return visit(tree);
    }


    public static void main(String[] args) {
        String text = "BEGIN\n" +
                "\n" +
                "     BEGIN\n" +
                "         number := 2;\n" +
                "         a := number;\n" +
                "         b := 10 * a + 10 * number / 4;\n" +
                "         c := a - - b\n" +
                "     END;\n" +
                "\n" +
                "     x := 11;\n" +
                " END.";
        Interpreter interpreter = new Interpreter(new Parser(new Lexer(text)));
        Object result = interpreter.interpret();
        System.out.println(interpreter.GLOBAL_SCOPE);
    }
}
