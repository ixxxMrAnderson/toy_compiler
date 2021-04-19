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
import java.util.HashMap;
import java.util.HashSet;


public class Main {
    public static void main(String[] args) throws Exception{

        String file_name = "./testcases/codegen/t56.mx";
//        InputStream input = new FileInputStream(file_name);
        InputStream input = System.in;
//

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
            if (blocks.containsKey("main")) {
//            new IRPrinter(blocks);
                HashMap<Integer, HashSet<Integer>> dom2sub = new HashMap<>();
                new SSA(blocks, dom2sub);
                new constPropagation(blocks);
                new CFGopt(blocks);
//            new IRPrinter(blocks);
                new ADCE(blocks);
                new inline(blocks);
                new CFGopt(blocks);
//                new IRPrinter(blocks);
                HashMap<Integer, HashSet<String>> in = new HashMap<>();
                HashMap<Integer, HashSet<String>> out = new HashMap<>();
                new LivenessAnalysis(blocks, in, out);
//            System.out.println("IN");
//            System.out.println(in);
//            System.out.println("OUT");
//            System.out.println(out);
//            new IRPrinter(blocks);
                HashMap<String, Integer> stackAlloc = new HashMap<>();
                new RegAllocate(blocks, in, out, stackAlloc);
//            new IRPrinter(blocks);
                new AsmPrinter(blocks, stackAlloc, spillPara);
            }
        } catch (error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}