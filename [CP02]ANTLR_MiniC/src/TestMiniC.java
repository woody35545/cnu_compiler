import java.io.File;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestMiniC {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		for (String s : args) {
			builder.append(s);
		}
		String str = builder.toString();
		CharStream codeCharStream = CharStreams.fromFileName(str);
		MiniCLexer lexer = new MiniCLexer(codeCharStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniCParser parser = new MiniCParser(tokens);
		ParseTree tree = parser.program();
	}
}
