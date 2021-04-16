import AST.programNode;
import Backend.*;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import MIR.block;
import Parser.MxLexer;
import Parser.MxParser;
import Util.MxErrorListener;
import Util.error.error;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Main {
    public static void main(String[] args) throws Exception{

        String file_name = "./testcases/codegen/t27.mx";
//        InputStream input = new FileInputStream(file_name);
        InputStream input = System.in;

        try {
            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxErrorListener());
            ParseTree parse_tree_root = parser.program();
            ASTBuilder AST_builder = new ASTBuilder();
            AST_builder.visit(parse_tree_root);
            programNode AST_root = AST_builder.program_node;
            AST_root = new SemanticChecker().visit(AST_root);
            HashMap<String, block> blocks = new HashMap<>();
            HashMap<String, Integer> spillPara = new HashMap<>();
            new IRBuilder(blocks, spillPara).visit(AST_root);
//            new IRPrinter(blocks);
            HashMap<Integer, HashSet<Integer>> dom2sub = new HashMap<>();
            new SSA(blocks, dom2sub);
//            new IRPrinter(blocks);
            HashMap<String, HashMap<String, Integer>> stackAlloc = new HashMap<>();
            new RegAlloc(blocks, stackAlloc);
//            new LinearScan(blocks, stackAlloc, dom2sub);
//            new IRPrinter(blocks);
            new AsmPrinter(blocks, stackAlloc, spillPara);
        } catch (error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}