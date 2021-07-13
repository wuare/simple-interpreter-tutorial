package top.wuare.part19.sematic;

import top.wuare.part19.Lexer;
import top.wuare.part19.NodeVisitor;
import top.wuare.part19.Parser;
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
import top.wuare.part19.ast.Var;
import top.wuare.part19.ast.VarDecl;
import top.wuare.part19.exception.SemanticError;
import top.wuare.part19.symbol.ProcedureSymbol;
import top.wuare.part19.symbol.ScopedSymbolTable;
import top.wuare.part19.symbol.Symbol;
import top.wuare.part19.symbol.VarSymbol;

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

        // Signal an error if the table already has a symbol with the same name
        // TODO

        currentScope.insert(varSymbol);
    }

    public void visitVar(Var node) {
        String varName = node.getValue();
        Symbol varSymbol = currentScope.lookup(varName);
        if (varSymbol == null) {
            throw new SemanticError("Identifier not found -> " + node.getToken());
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

    public void visitProcedureCall(ProcedureCall node) {
        for (AST ast : node.getActualParams()) {
            visit(ast);
        }

        ProcedureSymbol procSymbol = (ProcedureSymbol) currentScope.lookup(node.getProcName());
        node.setProcSymbol(procSymbol);
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

        procedureSymbol.setBlockAst(node.getBlockNode());
    }

    public void visitNum(Num node) {
        // pass
    }

    public static void main(String[] args) {
        String text = "PROGRAM Main;\n" +
                "\n" +
                "PROCEDURE Alpha(a : INTEGER; b : INTEGER);\n" +
                "VAR x : INTEGER;\n" +
                "BEGIN\n" +
                "   x := (a + b ) * 2;\n" +
                "END;\n" +
                "\n" +
                "BEGIN { Main }\n" +
                "\n" +
                "   Alpha(3 + 5, 7);  { procedure call }\n" +
                "\n" +
                "END.  { Main }";
        AST tree = new Parser(new Lexer(text)).parse();
        new SemanticAnalyzer().visit(tree);
    }
}
