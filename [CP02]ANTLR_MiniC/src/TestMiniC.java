import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestMiniC {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		MiniCLexer lexer = new MiniCLexer(new ANTLRFileStream("test.c"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniCParser parser = new MiniCParser(tokens);
		ParseTree tree = parser.program();

	}

}