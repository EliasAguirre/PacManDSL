package PacManDSL.ast;

import PacManDSL.libs.Pair;
import PacManDSL.libs.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class PacManParser {
    private Tokenizer tokenizer;

    public static PacManParser getParser(Tokenizer tokenizer) {
        return new PacManParser(tokenizer);
    }

    private PacManParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }
    private int w, h;

    //PROGRAM  ::= CREATEMAP WALLS+ ENEMIES+ WALLCOLOUR MAPCOLOUR CHARCOLOUR END
    public Program parseProgram() {
        List<Statement> statements = new ArrayList<>();
        while(!tokenizer.checkToken("END")){
            if (tokenizer.checkToken("CREATEMAP")) {
                statements.add(parseMapSize());
            }
            else if (tokenizer.checkToken("BUILDWALL")) {
                statements.add(parseBuildWall());
            }
            else if (tokenizer.checkToken("LOOP")) {
                statements.add(parseEnemy());
            }
            else if (tokenizer.checkToken("WALLCOLOUR")) {
                statements.add(parseWallColour());
            }
            else if (tokenizer.checkToken("MAPCOLOUR")) {
                statements.add(parseMapColour());
            }
            else if (tokenizer.checkToken("CHARCOLOUR")) {
                statements.add(parseCharacterColour());
            }
            else {
                throw new RuntimeException("Unexpected next token for Parsing!");
            }
        }
        tokenizer.getNext();
        return new Program(statements);
    }

    public Map parseMapSize(){
        tokenizer.getAndCheckNext("CREATEMAP");
        tokenizer.getAndCheckNext("\\(");
        w = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext(",");
        h = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext("\\)");
        return new Map(w, h);
    }

    // WALL ::== “BUILDWALL(“  NUMBER  “,”   NUMBER   “,”   NUMBER  “,”  NUMBER  “) “\n”
    public BuildWall parseBuildWall(){
        int start_x, start_y, width, height;
        tokenizer.getAndCheckNext("BUILDWALL");
        tokenizer.getAndCheckNext("\\(");
        start_x = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext(",");
        start_y = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext(",");
        width = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext(",");
        height = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext("\\)");

        return new BuildWall(start_x, start_y, width, height);
    }

    // ENEMY::== LOOP “\n” ENEMYCOLOUR “\n” ENEMYSPEED
    public Enemy parseEnemy(){
        int start_x, start_y;
        tokenizer.getAndCheckNext("LOOP");
        tokenizer.getAndCheckNext("\\{");
        tokenizer.getAndCheckNext("ENEMYSTART");
        tokenizer.getAndCheckNext("\\(");
        start_x = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext(",");
        start_y = Integer.valueOf(tokenizer.getNext());
        tokenizer.getAndCheckNext("\\)");
        Enemy e = new Enemy(start_x, start_y);

        // Movement to loop here.
        while (tokenizer.checkToken("ENEMYMOVE")){
            tokenizer.getAndCheckNext("ENEMYMOVE");
            tokenizer.getAndCheckNext("\\(");
            Enemy.DIRECTION d = Enemy.DIRECTION.valueOf(tokenizer.getNext());
            tokenizer.getAndCheckNext(",");
            int m = Integer.valueOf(tokenizer.getNext());
            tokenizer.getAndCheckNext("\\)");
            e.setMoveList(new Pair<>(d, m));
        }

        tokenizer.getAndCheckNext("\\}");
        tokenizer.getAndCheckNext("ENEMYCOLOUR");
        tokenizer.getAndCheckNext("#");
        String c = tokenizer.getNext();
        e.setColour(c);

        tokenizer.getAndCheckNext("ENEMYSPEED");
        int s = Integer.valueOf(tokenizer.getNext());
        e.setSpeed(s);

        return e;
    }

    // WALLCOLOUR ::== “WALLCOLOUR #” HEXVALUE{6}
    public WallColour parseWallColour(){
        tokenizer.getAndCheckNext("WALLCOLOUR");
        tokenizer.getAndCheckNext("#");
        return new WallColour(tokenizer.getNext());
    }

    // MAPCOLOUR :== “MAPCOLOUR #” HEXVALUE{6}
    public MapColour parseMapColour(){
        tokenizer.getAndCheckNext("MAPCOLOUR");
        tokenizer.getAndCheckNext("#");
        return new MapColour(tokenizer.getNext());
    }

    // CHARCOLOUR :== “CHARCOLOUR #” HEXVALUE{6}
    public CharacterColour parseCharacterColour(){
        tokenizer.getAndCheckNext("CHARCOLOUR");
        tokenizer.getAndCheckNext("#");
        return new CharacterColour(tokenizer.getNext());
    }
}
