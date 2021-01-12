## Hi, this is PacMan Maze Creator

This project is a domain specific language project.

It consists of all the language design steps: Language Grammar Design, Tokenization, Parsing, Validation and Evaluation.
The grammar follows best practices of:<br/> 
<br/> 
1. Not being ambiguous<br/> 
2. No left-recursion<br/> 
3. Locally deterministic<br/> 

This allows a simple tokenizing method or splitting input using constant literals.<br/> 

This further allows us to use "Single Symbol Lookahead" parsing (implementing checkNextToken, getNextToken, getAndCheckNextToken) <br/> 
<br/> 

Files map as follows: <br/> 
-Files ending with "tokenizer" are tokenizing files <br/> 
-Files ending with "parser" are parsing files <br/> 
-Files ending with "checker" are validating files <br/> 
-Files ending with "evaluator" are evaluating files <br/>

The folder under PMLGraphics implements all the game graphics. Graphic elements are mapped to elements

Files can be found in src: "src/main/java/PacManDSL/"

Here are a few important details for you to be able to run our game.

1. Version of Java.<br/> 
  -This project is implemented using version 14 and 15, so any of these should work.
  
2. Please set the 'src' directory as the source root file.<br/> 
  -Right click on 'src' -> Mark Directory As -> Sources Root

3. Edit Configurations (under Run -> Edit Configurations -> Application)<br/>
  -Set 'Main Class' to Main (the main class under cpsc410_project1_team3 -> PacMan -> src -> main -> java -> Main)<br/>
  -'VM Options' set to -XstartOnFirstThread<br/>
  -Working Directory set to 'PacMan' directory (i.e. for me this is /Users/eliasaguirre/Desktop/CPSC/CPSC410/ProjectCode/cpsc410_project1_team3/PacMan) <br/>
  -'Use classpath of module' to PacMan.main<br/>
  -JRE a valid sdk (14 or 15)
  
4. sample.txt file will contain the input for the DSL. <br/>
  -Modify this file to try different DSL inputs
  
  
  
