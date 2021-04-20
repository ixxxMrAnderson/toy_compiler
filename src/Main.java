import AST.programNode;
import Backend.*;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import MIR.assign;
import MIR.block;
import MIR.statement;
import Parser.MxLexer;
import Parser.MxParser;
import Util.MxErrorListener;
import Util.error.error;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;


public class Main {
    public static void main(String[] args) throws Exception{

//        String file_name = "./testcases/codegen/sorting/merge_sort.mx";
//        String file_name = "./testcases/codegen/shortest_path/dijkstra.mx";
//        String file_name = "./testcases/codegen/t63.mx";
//        String file_name = "./testcases/optim-new/adce-adv.mx";
        String file_name = "./testcases/sema/misc-package/misc-34.mx";
//        InputStream input = new FileInputStream(file_name);
//        PrintStream o = new PrintStream(new File("test.s"));
//        System.setOut(o);
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
            HashMap<Integer, HashSet<Integer>> dom2sub = new HashMap<>();
            new SSA(blocks, dom2sub);
            Integer cnt = 0;
            for (statement s : blocks.get("main").stmts){
                if (s instanceof assign && ((assign) s).rhs == null) cnt++;
                else break;
            }
            HashMap<String, Integer> stackAlloc = new HashMap<>();
            if (cnt > 200){
                new RegAlloc(blocks, stackAlloc);
            } else {
                new constPropagation(blocks);
//                new IRPrinter(blocks);
                new CFGopt(blocks);
//                new IRPrinter(blocks);
                new ADCE(blocks);
//                new IRPrinter(blocks);
                new inline(blocks);
//                new IRPrinter(blocks);
                new CFGopt(blocks);
//                new IRPrinter(blocks);
                new Peephole(blocks);
//                new IRPrinter(blocks);
                HashMap<Integer, HashSet<String>> in = new HashMap<>();
                HashMap<Integer, HashSet<String>> out = new HashMap<>();
                new LivenessAnalysis(blocks, in, out);
//                new IRPrinter(blocks);
                new RegAllocate(blocks, in, out, stackAlloc);
            }
//            new IRPrinter(blocks);
            new AsmPrinter(blocks, stackAlloc, spillPara);
        } catch (error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}