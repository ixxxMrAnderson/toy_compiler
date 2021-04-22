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
//        String file_name = "./testcases/codegen/t2.mx";
        String file_name = "./testcases/optim-new/inline-adv.mx";
//        String file_name = "./testcases/sema/misc-package/misc-24.mx";
//        InputStream input = new FileInputStream(file_name);
        InputStream input = System.in;
//        PrintStream o = new PrintStream(new File("test.s"));
//        System.setOut(o);
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
            HashMap<Integer, HashSet<Integer>> dom2sub = new HashMap<>();
            HashMap<String, Integer> stackAlloc = new HashMap<>();
            new IRBuilder(blocks, spillPara).visit(AST_root);
            new SSA(blocks, dom2sub);
            Integer cnt = 0;
            for (int i = 0; i < blocks.get("main").stmts.size(); ++i){
                if (i == 1 || i == 0) continue;
                statement s = blocks.get("main").stmts.get(i);
                if (s instanceof assign && ((assign) s).rhs == null) cnt++;
                else break;
            }
            if (cnt > 200){
                new RegAlloc(blocks, stackAlloc);
            } else {
//                new IRPrinter(blocks);
//                System.out.println("___________________CFGopt________________");
                new CFGopt(blocks);
//                new IRPrinter(blocks);
//                System.out.println("___________________ADCE________________");
                new ADCE(blocks);
//                new CFGopt(blocks);
//                new IRPrinter(blocks);
//                System.out.println("___________________inline________________");
                new inline(blocks);
//                new IRPrinter(blocks);
//                System.out.println("___________________const________________");
//                new constPropagation(blocks);
//                new IRPrinter(blocks);
//                System.out.println("___________________CFG________________");
                new CFGopt(blocks);
//                new IRPrinter(blocks);
//                System.out.println("___________________Peephole________________");
                new Peephole(blocks);
//                System.out.println("___________________Reg________________");
//                new IRPrinter(blocks);
                new RegAllocate(blocks, stackAlloc);
            }
//            new IRPrinter(blocks);
            new AsmPrinter(blocks, stackAlloc, spillPara);
        } catch (error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}