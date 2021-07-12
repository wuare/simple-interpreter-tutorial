package top.wuare.part14.sematic;

import top.wuare.part14.Lexer;
import top.wuare.part14.NodeVisitor;
import top.wuare.part14.Parser;
import top.wuare.part14.ast.AST;
import top.wuare.part14.ast.Assign;
import top.wuare.part14.ast.BinOp;
import top.wuare.part14.ast.Block;
import top.wuare.part14.ast.Compound;
import top.wuare.part14.ast.NoOp;
import top.wuare.part14.ast.Param;
import top.wuare.part14.ast.ProcedureDecl;
import top.wuare.part14.ast.Program;
import top.wuare.part14.ast.Var;
import top.wuare.part14.ast.VarDecl;
import top.wuare.part14.symbol.ProcedureSymbol;
import top.wuare.part14.symbol.Symbol;
import top.wuare.part14.symbol.ScopedSymbolTable;
import top.wuare.part14.symbol.VarSymbol;

/**
 * @author wuare
 * @date 2021/7/12
 */
public class SemanticAnalyzer extends NodeVisitor {

    private ScopedSymbolTable currentScope;

    public void visitBlock(Block node) {
        for(AST decl : node.getDeclarations()) {
            visit(decl);
        }
        visit(node.getCompound());
    }

    public void visitProgram(Program node) {
        System.out.println("ENTER scope: global");
        ScopedSymbolTable global = new ScopedSymbolTable("global", 1, currentScope);
        currentScope = global;
        visit(node.getBlock());

        System.out.println(global);
        currentScope = currentScope.getEnclosingScope();
        System.out.println("LEAVE scope: global");
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
        Symbol typeSymbol = currentScope.lookup(typeName);

        // We have all the information we need to create a variable symbol.
        // Create the symbol and insert it into the symbol table.
        String varName = node.getVarNode().getValue();
        VarSymbol varSymbol = new VarSymbol(varName, typeSymbol);

        currentScope.insert(varSymbol);
    }

    public void visitVar(Var node) {
        String varName = node.getValue();
        Symbol varSymbol = currentScope.lookup(varName);
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

    public void visitProcedureDecl(ProcedureDecl node) {
        String procName = node.getProcName();
        ProcedureSymbol procedureSymbol = new ProcedureSymbol(procName, null);
        currentScope.insert(procedureSymbol);

        System.out.println("ENTER scope: " + procName);
        ScopedSymbolTable scopedSymbolTable = new ScopedSymbolTable(procName, currentScope.getScopeLevel() + 1, currentScope);
        currentScope = scopedSymbolTable;

        // Insert parameters into the procedure scope
        for (Param param : node.getParams()) {
            Symbol paramType = currentScope.lookup(param.getTypeNode().getValue());
            String paramValue = param.getVarNode().getValue();
            VarSymbol varSymbol = new VarSymbol(paramValue, paramType);
            currentScope.insert(varSymbol);
            procedureSymbol.getParams().add(varSymbol);
        }

        visit(node.getBlockNode());
        System.out.println(scopedSymbolTable);
        currentScope = currentScope.getEnclosingScope();
        System.out.println("LEAVE scope: " + procName);
    }

    public static void main(String[] args) {
        String text = "PROGRAM Main;\n" +
                "   VAR x, y: REAL;\n" +
                "\n" +
                "   PROCEDURE Alpha(a : INTEGER);\n" +
                "      VAR y : INTEGER;\n" +
                "   BEGIN\n" +
                "      x := a + x + y;\n" +
                "   END;\n" +
                "\n" +
                "BEGIN { Main }\n" +
                "\n" +
                "END.  { Main }\n";
        AST tree = new Parser(new Lexer(text)).parse();
        new SemanticAnalyzer().visit(tree);
    }
}
