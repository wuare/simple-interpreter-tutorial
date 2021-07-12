package top.wuare.part13.sematic;

import top.wuare.part13.Lexer;
import top.wuare.part13.NodeVisitor;
import top.wuare.part13.Parser;
import top.wuare.part13.ast.AST;
import top.wuare.part13.ast.Assign;
import top.wuare.part13.ast.BinOp;
import top.wuare.part13.ast.Block;
import top.wuare.part13.ast.Compound;
import top.wuare.part13.ast.NoOp;
import top.wuare.part13.ast.Program;
import top.wuare.part13.ast.Var;
import top.wuare.part13.ast.VarDecl;
import top.wuare.part13.symbol.Symbol;
import top.wuare.part13.symbol.SymbolTable;
import top.wuare.part13.symbol.VarSymbol;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class SemanticAnalyzer extends NodeVisitor {

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

        // We have all the information we need to create a variable symbol.
        // Create the symbol and insert it into the symbol table.
        String varName = node.getVarNode().getValue();

        if (symbolTable.lookup(varName) != null) {
            throw new RuntimeException("Duplicate identifier: [" + varName + "] found");
        }
        symbolTable.insert(new VarSymbol(varName, typeSymbol));
    }

    public void visitVar(Var node) {
        String varName = node.getValue();
        Symbol varSymbol = symbolTable.lookup(varName);
        if (varSymbol == null) {
            throw new RuntimeException("name error: [" + varName + "]");
        }
    }

    public void visitAssign(Assign node) {
        visit(node.getRight());
        visit(node.getLeft());
    }

    public void visitBinOp(BinOp node) {
        visit(node.getLeft());
        visit(node.getRight());
    }

    public static void main(String[] args) {
        String text = "PROGRAM SymTab6;\n" +
                "   VAR x, y : INTEGER;\n" +
                "   VAR y : REAL;\n" +
                "BEGIN\n" +
                "   x := x + y;\n" +
                "END.";
        AST tree = new Parser(new Lexer(text)).parse();
        new SemanticAnalyzer().visit(tree);
    }
}
