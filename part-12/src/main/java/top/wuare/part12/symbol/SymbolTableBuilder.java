package top.wuare.part12.symbol;

import top.wuare.part12.Lexer;
import top.wuare.part12.NodeVisitor;
import top.wuare.part12.Parser;
import top.wuare.part12.ast.AST;
import top.wuare.part12.ast.Assign;
import top.wuare.part12.ast.BinOp;
import top.wuare.part12.ast.Block;
import top.wuare.part12.ast.Compound;
import top.wuare.part12.ast.NoOp;
import top.wuare.part12.ast.Num;
import top.wuare.part12.ast.ProcedureDecl;
import top.wuare.part12.ast.Program;
import top.wuare.part12.ast.UnaryOp;
import top.wuare.part12.ast.Var;
import top.wuare.part12.ast.VarDecl;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class SymbolTableBuilder extends NodeVisitor {
    private final SymbolTable symbolTable = new SymbolTable();

    public void visitBlock(Block node) {
        for(AST decl : node.getDeclarations()) {
            visit(decl);
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

    public void visitProcedureDecl(ProcedureDecl node) {
        // pass
    }

    public static void main(String[] args) {
        String text = "PROGRAM Part12;\n" +
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
                "BEGIN {Part12}\n" +
                "   a := 10;\n" +
                "END.  {Part12}";
        AST tree = new Parser(new Lexer(text)).parse();
        new SymbolTableBuilder().visit(tree);
    }
}
