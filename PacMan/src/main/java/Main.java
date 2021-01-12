import PacManDSL.ast.PacManChecker;
import PacManDSL.ast.PacManEvaluator;
import PacManDSL.ast.PacManParser;
import PacManDSL.ast.Program;
import PacManDSL.libs.SimpleTokenizer;
import PacManDSL.libs.Tokenizer;
import org.lwjgl.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        List<String> fixedLiterals = Arrays.asList("CREATEMAP", "(", ",", ")", "BUILDWALL", "LOOP", "{", "ENEMYSTART",
                "ENEMYMOVE", "LEFT", "RIGHT", "UP", "DOWN", "}", "ENEMYCOLOUR", "ENEMYSPEED", "WALLCOLOUR", "MAPCOLOUR",
                "CHARCOLOUR", "#", "END");
        Tokenizer tokenizer = SimpleTokenizer.createSimpleTokenizer("sample.txt",fixedLiterals);
        System.out.println("Done tokenizing");

        PacManParser p = PacManParser.getParser(tokenizer);
        Program program = p.parseProgram();
        System.out.println("Done parsing");

        PacManChecker c = new PacManChecker();
        String errors = program.accept(new HashMap<>(),c);
        if(!errors.isEmpty()) {
            // terminate evaluation
            throw new RuntimeException("Static Checks failed with the following errors: \n " + errors);
        }

        PacManEvaluator e = new PacManEvaluator();
        program.accept(null, e);
        System.out.println("Done evaluation");
    }
}
