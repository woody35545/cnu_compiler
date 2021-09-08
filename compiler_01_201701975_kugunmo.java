
/*  
 	# 이름: 구건모
	# 학번: 201701975
	# 개발환경: windows10(os)에서 eclipse를 통해 제작하였고, 만들어진 .c 파일은 Linux terminal(wsl)에서 gcc로 compile 하여 테스팅 하였습니다.
	# Java compiler version: JDK(javac) version 1.8.0_144
	# 테스팅 환경의 C compiler version: gcc (Ubuntu 9.3.0-17ubuntu1~20.04) 9.3.0
	# 리눅스 환경에 맞게 shell 명령어가 변환되도록 했기 때문에 window 환경에서는 변환된 .c 파일을 컴파일하여 실행했을 때 제대로 작동하지 않습니다.
	# 예외처리가 없어도 된다고 명시해주셔서 예외처리는 따로 하지 않았습니다. 따라서 입력파일(.hf)이 문법상의 오류가 없는 상태에 대해서만 .c 파일로 변환가능합니다.
	# 감사합니다.
*/
import java.io.*; // file IO 를 위한 library import

public class main {

	public static void main(String[] args) throws IOException {

		File input_file = new File("C:\\Users\\user\\eclipse-workspace\\[cnu]compiler_01\\src\\test.hf"); // Input으로
		File output_file = new File("C:\\Users\\user\\eclipse-workspace\\[cnu]compiler_01\\src\\output.c"); // 변환된
		FileReader file_reader = new FileReader(input_file); // Input 파일을 읽기위한 FileReader 객체 file_reader 생성
		FileWriter file_writer = new FileWriter(output_file, true); // Output 파일을 쓰기위한 FileWriter 객체 file_writer 생성
		file_writer.write("#include <stdio.h>\n" + "#include <stdlib.h>\n" + "int main(){\n");
		file_writer.flush();

		char STATEMENT_START = '(';
		char STATEMENT_END = ')';
		char QUOTATION_MARKS = '\"';
		String COMMAND_ECHO = "echo";
		String COMMAND_LISTDIR = "list_dir";
		String COMMAND_DEL = "del";
		String COMMAND_SHOW = "show";
		String COMMAND_MOV = "mov";

		int EOF = -1; // file_reader의 read 함수는 더 이상 읽을 값이 없으면 -1을 반환하므로 -1이 return되면 EOF(end of
						// file)임을 의미. 조건문에서 EOF 판단을 위해 사용
		int c;
		
		int number_of_line = 0; // 줄 수 나타낼 때 사용
		String token = "";
		String[] tokens = new String[10];

		int size_of_tokens = 0;

		while (true) {
			c = file_reader.read();
			if (c == EOF) {
				break;
			} else {
				if ((char) c == STATEMENT_START) {
					while (true) {
						c = file_reader.read();
						if ((char) c == STATEMENT_END) { // STATEMENT_END가 되면 마지막 모아두었던 토큰이 안들어간 상태이므로 넣어주고 다음 줄 토큰화할
															// 준비함.
							if (!(token.equals(""))) {
								tokens[size_of_tokens] = token.replaceAll("\\s", "");
								size_of_tokens++;

							}
							token = "";
							number_of_line++;

							/// 번역부 start
							String command = tokens[0];
							if (command.equals(COMMAND_ECHO)) { // echo 명령어는 인자가 무조건 1개 이므로 echo 뒤에 오는 값이 출력할 데이터 값임을
																	// 의미
								// echo -> printf 문으로 변환
								String _echo_param = tokens[1];
								System.out.println(
										"line" + number_of_line + ": " + COMMAND_ECHO + " \"" + _echo_param + "\"");
								String content = "printf(\"" + _echo_param + "\\n\");"; // 파일 입력으로 들어갈 내용
								file_writer.write(content + "\n");
								file_writer.flush();

							}

							else if (command.equals(COMMAND_LISTDIR)) { // list_dir -> 리눅스의 경우 ls, 윈도우의 경우 dir
								// list_dir -> system("dir") 로 변환
								System.out.println("line" + number_of_line + ": " + COMMAND_LISTDIR);
								String content = "system(\"ls -al\");"; // 파일 입력으로 들어갈 내용
								file_writer.write(content + "\n");
								file_writer.flush();

							}

							else if (command.equals(COMMAND_DEL)) { // del: 리눅스의 경우 rm, 윈도우의 경우 del
								String _del_param = tokens[1];
								System.out.println(
										"line" + number_of_line + ": " + COMMAND_DEL + " \"" + _del_param + "\"");
								String content = "system(\"rm \\\"" + _del_param + "\\\"\");"; // 파일 입력으로 들어갈 내용
								file_writer.write(content + "\n");
								file_writer.flush();

							} else if (command.equals(COMMAND_SHOW)) { // show -> 리눅스의 경우 cat, 윈도우의 경우 type
								String _show_param = tokens[1];
								System.out.println(
										"line" + number_of_line + ": " + COMMAND_SHOW + " \"" + _show_param + "\"");
								String content = "system(\"cat \\\"" + _show_param + "\\\"\");"; // 파일 입력으로 들어갈 내용
								file_writer.write(content + "\n");
								file_writer.flush();

							}

							else if (command.equals(COMMAND_MOV)) {
								String _mov_param = tokens[2];
								System.out.println("line" + number_of_line + ": " + COMMAND_MOV + " " + tokens[1]
										+ " \"" + _mov_param + "\"");
								String content = "system(\"ls -al > \\\"" + _mov_param + "\\\"\");"; // 파일 입력으로 들어갈 내용
								file_writer.write(content + "\n");

								file_writer.flush();
							}
							/// 번역부 end

							/// 다음 줄의 단어들을 tokens에 담기 위해 초기화
							for (int i = 0; i < 10; i++) {
								tokens[i] = null;
							}
							size_of_tokens = 0;
							//
							break;
						}

						else { // ( SOMETHING ) 괄호 사이의 값들을 토크나이즈함
							if (Character.isWhitespace((char) c) && !(token.equals(""))) {
								tokens[size_of_tokens] = token.replaceAll("\\s", "");
								size_of_tokens++;
								token = "";
							} else if ((char) c == QUOTATION_MARKS) {
								while (true) {
									c = file_reader.read();
									if ((char) c == QUOTATION_MARKS) {
										// QUATATION_MARK를 만나면 끝을 의미하므로 그동안 collecting 한 word 값을 토큰에 넣어줌
										if (!(token.equals(""))) {
											tokens[size_of_tokens] = token;
											size_of_tokens++;
										}
										token = "";
										break;
									} else {
										token += (char) c;
									}
								}
							} else {
								token += (char) c;
							}

						}
					}
				}

			}

		}
		file_writer.write("}");
		file_writer.flush();
	}
}
