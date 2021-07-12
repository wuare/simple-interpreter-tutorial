package top.wuare.part11.symbol;

import top.wuare.part11.Lexer;
import top.wuare.part11.NodeVisitor;
import top.wuare.part11.Parser;
import top.wuare.part11.Token;
import top.wuare.part11.ast.AST;
import top.wuare.part11.ast.Assign;
import top.wuare.part11.ast.BinOp;
import top.wuare.part11.ast.Block;
import top.wuare.part11.ast.Compound;
import top.wuare.part11.ast.NoOp;
import top.wuare.part11.ast.Num;
import top.wuare.part11.ast.Program;
import top.wuare.part11.ast.Type;
import top.wuare.part11.ast.UnaryOp;
import top.wuare.part11.ast.Var;
import top.wuare.part11.ast.VarDecl;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class SymbolTableBuilder extends NodeVisitor {
    private final SymbolTable symbolTable = new SymbolTable();

    public void visitBlock(Block node) {
        for(VarDecl varDecl : node.getDeclarations()) {
            visit(varDecl);
        }
        visit(node.getCompound());
    }

    public void visitProgram(Program node) {
        visit(node.getBlock());
    }

    public void visitBinOp(BinOp node) {
        visit(node.getLeft());
        visit(node.getRight());
    }

    public void visitNum(Num node) {
        // pass
    }

    public void visitUnaryOp(UnaryOp node) {
        visit(node.getExpr());
    }

    public void visitCompound(Compound node) {
        for (AST child: node.getChildren()) {
            visit(child);
        }
    }

    public void visitNoOp(NoOp node) {
        // pass
    }

    public void visitVarDecl(VarDecl node) {
        String typeName = node.getTypeNode().getValue();
        Symbol typeSymbol = symbolTable.lookup(typeName);
        String varName = node.getVarNode().getValue();
        symbolTable.define(new VarSymbol(varName, typeSymbol));
    }

    public void visitAssign(Assign node) {
        String varName = node.getLeft().getValue();
        Symbol varSymbol = symbolTable.lookup(varName);
        if (varSymbol == null) {
            throw new RuntimeException("name error: [" + varName + "]");
        }
        visit(node.getRight());
    }

    public void visitVar(Var node) {
        String varName = node.getValue();
        Symbol varSymbol = symbolTable.lookup(varName);
        if (varSymbol == null) {
            throw new RuntimeException("name error: [" + varName + "]");
        }
    }

    public static void main(String[] args) {
        String text = "PROGRAM NameError2;\n" +
                "VAR\n" +
                "   b : INTEGER;\n" +
                "\n" +
                "BEGIN\n" +
                "   b := 1;\n" +
                "   a := b + 2;\n" +
                "END.";
        AST tree = new Parser(new Lexer(text)).parse();
        new SymbolTableBuilder().visit(tree);
    }
}
