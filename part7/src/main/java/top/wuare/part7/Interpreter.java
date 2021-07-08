package top.wuare.part7;

import top.wuare.part7.ast.AST;
import top.wuare.part7.ast.BinOp;
import top.wuare.part7.ast.Num;

import java.util.Scanner;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class Interpreter extends NodeVisitor {
    private final Parser parser;

    public Interpreter(Parser parser) {
        this.parser = parser;
    }

    public int visitBinOp(BinOp node) {
        if (node.getOp().getType() == Token.PLUS) {
            return visit(node.getLeft()) + visit(node.getRight());
        }
        if (node.getOp().getType() == Token.MINUS) {
            return visit(node.getLeft()) - visit(node.getRight());
        }
        if (node.getOp().getType() == Token.MUL) {
            return visit(node.getLeft()) * visit(node.getRight());
        }
        if (node.getOp().getType() == Token.DIV) {
            return visit(node.getLeft()) / visit(node.getRight());
        }
        throw new RuntimeException("visit bin op error");
    }

    public int visitNum(Num node) {
        return node.getValue();
    }

    public int interpret() {
        AST tree = parser.parse();
        return visit(tree);
    }

    public static void main(String[] args) {
        while (true) {
            System.out.print("calc> ");
            Scanner scanner = new Scanner(System.in);
            String text = scanner.nextLine();
            if (text == null) {
                continue;
            }
            Interpreter interpreter = new Interpreter(new Parser(new Lexer(text)));
            int result = interpreter.interpret();
            System.out.println(result);
        }
    }
}
