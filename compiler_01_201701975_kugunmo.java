import java.io.*; // file I/O 를 위한 library import

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

public class main {
	public static void main(String[] args) throws IOException { 

		File input_file = new File("C:\\Users\\user\\eclipse-workspace\\[cnu]compiler_01\\src\\test.hf"); /*input으로 받을 파일*/
		File output_file = new File("C:\\Users\\user\\eclipse-workspace\\[cnu]compiler_01\\src\\output.c"); /* hf파일을 받아 c파일로 변환된 output 파일*/
		FileReader file_reader = new FileReader(input_file); /* Input 파일을 읽기위한 FileReader 객체 file_reader 생성*/
		FileWriter file_writer = new FileWriter(output_file, true); /* Output 파일을 쓰기위한 FileWriter 객체 file_writer 생성*/
		file_writer.write("#include <stdio.h>\n" + "#include <stdlib.h>\n" + "int main(){\n"); /* c파일 실행을 위해 기본적으로 필요한 include와 main함수를 output 파일에 미리 write*/
		file_writer.flush(); /* 내용이 버퍼에만 남고 파일에 작성되지 않는 문제를 막기위해서 flush method 호출 */
		
		char STATEMENT_START = '('; /* hf언어에서는 '('가 문장의 시작이므로 문장의 시작을 나타내는 STATEMENT_START */ 
		char STATEMENT_END = ')';/* hf 언어에서는 ')'가 문장의 끝이므로 문장의 끝을 나타내는 STATEMENT_END */ 
		char QUOTATION_MARKS = '\"'; /* 큰 따옴표 \" 를 의미하는 QUOTATION_MARKS */
		String COMMAND_ECHO = "echo";  /* hf 언어에서 문자열 출력 명령어 echo를 의미하는 COMMAND_ECHO */ 
		String COMMAND_LISTDIR = "list_dir";/* hf 언어에서 디렉토리내 모든 파일 출력 명령어인 list_dir를 의미하는 COMMAND_LISTDIR */
		String COMMAND_DEL = "del";  /* hf 언어에서 파일 삭제 명령어인 del을 의미하는 COMMAND_DEL */
		String COMMAND_SHOW = "show"; /* hf 언어에서 파일 내용을 출력해주는 명령어인 show를 의미하는 COMMAND_SHOW */ 
		String COMMAND_MOV = "mov"; /* hf 언어에서 명령 수행결과를 특정파일에 저장해주는 명령어 mov를 의미하는 COMMAND_MOV */

		int EOF = -1; /* file_reader의 read 함수는 더 이상 읽을 값이 없으면 -1을 반환하므로 -1이 return되면 EOF(end offile)임을 의미. 조건문에서 EOF 판단을 위해 사용 */
		int c; /*file_reader로 read()를 통해 받은 문자를 저장할 변수, EOF를 판단할 때도 사용하고 문자로 사용할 때는 (char)로 형변환해서 사용 */
		int number_of_line = 0; /* 줄 수 나타낼 때 사용, 기능상에 영향을 주지 않음 단지 출력용으로만 사용 */
		String token = ""; /* 하나의 토큰을 저장할 변수 */
		String[] tokens = new String[10]; /* 토큰들을 저장할 String 배열, 본 과제의 경우 명령어가 긴 것이 없기 때문에 임의로 10만큼의 크기로 선언 */
		int size_of_tokens = 0; /* 토큰들을 저장할 배열 tokens가 가지고 있는 원소 수 */

		while (true) {
			c = file_reader.read(); /* 파일에서 한단어를 읽어 c에 저장, 현재는 int형. 문자로 사용시 (char)로 형변환해서 사용) */
			if (c == EOF) { /* file_reader의 read는 end_of_file 일 때 -1값을 리턴하므로 미리 선언한 EOF 변수(값: -1) 과 비교해 같으면 더이상 읽을 내용이 없음을 의미하므로 break; */ 
			
				break; /* 더 이상 읽을 내용이 없다면 break; */
			} else {
				if ((char) c == STATEMENT_START) { /* STATEMENT_START가 나오면 문장의 시작을 의미하므로 문장의 끝을 의미하는 STATEMENT_END 가 나올 때까지 사이의 문장에 대해 토큰화 수행 */
					while (true) {
						c = file_reader.read(); /* 토큰화를 위해 하나씩 입력받음 */
						if ((char) c == STATEMENT_END) { /* STATEMENT_END가 되면 마지막 모아두었던 토큰(마지막 단어)이 토큰 배열에 안들어간 상태이므로 넣어주고 다음 줄 토큰화 할 준비를 함 */
							if (!(token.equals(""))) { /* token 값이 빈 문자면 tokens에 넣지 않음 */ 
								tokens[size_of_tokens] = token.replaceAll("\\s", ""); /* 혹여나 space가 들어가는 경우 replaceAll을 이용해 공백 제거후 token 값을 tokens 배열에 넣어줌 */
								size_of_tokens++; /* tokens 배열이 가진 elements 수가 증가했으므로 size_of_tokens ++; */

							}
							token = ""; /* 다음 문장을 토큰화하기 위해 token에 들어있던 값을 비워줌 */
							number_of_line++; /*다음 라인으로 넘어갔으므로 number_of_line ++; */
							
							/*--- 한줄에 대한 토큰화가 다 끝나고 만들어진 tokens 배열을 이횽해 (hf->c)로 변환하는 시점 ---*/
							
							String command = tokens[0]; /* 문장의 첫 단어인 token[0]이 command를 의미하므로 command 라는 변수를 선언하여 저장 */
							if (command.equals(COMMAND_ECHO)) { /* echo 명령어는 인자가 무조건 1개 이므로 echo 뒤에 오는 값이 출력할 데이터 값임을 의미 */
																
								/*--- echo -> printf 문으로 변환 --*/
								String _echo_param = tokens[1]; /* 출력할 문자열에 대한 정보는 tokens[1]에 들어있으므로 _echo_param 변수에 넣어줌 */
								System.out.println(
										"line" + number_of_line + ": " + COMMAND_ECHO + " \"" + _echo_param + "\""); /* 화면 출력용 기능과 무관 */
								String content = "printf(\"" + _echo_param + "\\n\");"; /* c에서의 출력문은 printf이므로 출력할 문자열인 _echo_param을 넣어서 printf 작성 Quotation mark는 Escape Character를 이용하여 입력하도록 함.*/
								file_writer.write(content + "\n"); /* 파일에 입력할 내용인 content를 output 파일에 작성하고 newline 파일에 작성 */
								file_writer.flush(); /* 내용이 버퍼에만 남고 파일에 작성되지 않는 문제를 막기위해서 flush method 호출 */

							}

							else if (command.equals(COMMAND_LISTDIR)) { // command가  list_dir 일 때, 리눅스의 경우 ls, 윈도우의 경우 dir
								/*-- list_dir -> system("ls -al") 로 변환 --*/
								System.out.println("line" + number_of_line + ": " + COMMAND_LISTDIR); /* 화면 출력용 기능과 무관 */
								String content = "system(\"ls -al\");"; // 파일 입력으로 들어갈 내용
								file_writer.write(content + "\n"); /* 파일에 입력할 내용인 content를 output 파일에 작성하고 newline 파일에 작성 */
								file_writer.flush();/* 내용이 버퍼에만 남고 파일에 작성되지 않는 문제를 막기위해서 flush method 호출 */

							}

							else if (command.equals(COMMAND_DEL)) { /* command가 del 일 때 , 리눅스의 경우 rm, 윈도우의 경우 del
								/*-- del "*filename" -> system("rm \"filename\"") 으로 변환 --*/
								String _del_param = tokens[1]; /* 삭제할 파일이름이 token[1]에 저장되어 있으므로 _del_param에 넣어줌 */ 
								System.out.println("line" + number_of_line + ": " + COMMAND_DEL + " \"" + _del_param + "\"");/* 화면 출력용 기능과 무관 */
								String content = "system(\"rm \\\"" + _del_param + "\\\"\");"; /*system("rm \"filename\"") 으로 변환해야 하므로 escape_characeter를 이용하여 '\'와 Quotation_mark가 파일입력시 반영되도록 함.*/
								file_writer.write(content + "\n");/* 파일에 입력할 내용인 content를 output 파일에 작성하고 newline 파일에 작성 */
								file_writer.flush();/* 내용이 버퍼에만 남고 파일에 작성되지 않는 문제를 막기위해서 flush method 호출 */

							} else if (command.equals(COMMAND_SHOW)) { // command가 show일 때. 리눅스의 경우 cat, 윈도우의 경우 type
								/*-- show "*filename" -> system("cat \"filename\"") 으로 변환 --*/
								String _show_param = tokens[1]; /* 출력할 파일명을 tokens[1]이 담고 있으므로 _show_param에 넣어줌 */
								System.out.println("line" + number_of_line + ": " + COMMAND_SHOW + " \"" + _show_param + "\"");/* 화면 출력용 기능과 무관 */
								String content = "system(\"cat \\\"" + _show_param + "\\\"\");";  /* system("cat \"filename\"") 으로 변환해야 하므로 escape_characeter를 이용하여 '\'와 Quotation_mark가 파일입력시 반영되도록 함.*/
								file_writer.write(content + "\n");/* 파일에 입력할 내용인 content를 output 파일에 작성하고 newline 파일에 작성 */
								file_writer.flush();/* 내용이 버퍼에만 남고 파일에 작성되지 않는 문제를 막기위해서 flush method 호출 */

							}

							else if (command.equals(COMMAND_MOV)) { /* command가 mov 일 때, 리눅스의 경우 '>'를 통해 redirection 수행 */
								/*-- mov list_dir "*filename" -> system("ls-al > \"filename\"") 으로 변환 --*/
								String _mov_param = tokens[2]; /* 출력 결과를 저장할 파일명을 token[2]가 가지고 있으므로 _mov_param에 넣어줌 */
								System.out.println("line" + number_of_line + ": " + COMMAND_MOV + " " + tokens[1] + " \"" + _mov_param + "\"");/* 화면 출력용 기능과 무관 */
								String content = "system(\"ls -al > \\\"" + _mov_param + "\\\"\");"; /* system("ls-al > \"filename\"") 으로 변환해야 하므로 escape_characeter를 이용하여 '\'와 Quotation_mark가 파일입력시 반영되도록 함.*/
								file_writer.write(content + "\n");/* 파일에 입력할 내용인 content를 output 파일에 작성하고 newline 파일에 작성 */
								file_writer.flush();/* 내용이 버퍼에만 남고 파일에 작성되지 않는 문제를 막기위해서 flush method 호출 */
							}
							/*--- 한줄에 대한 (hf->c) 변환 완료---*/


							/*다음 줄을 해석하기 위해 tokens 배열 및 size_of_tokens 초기화*/
							for (int i = 0; i < 10; i++) {
								tokens[i] = null;
							}
							size_of_tokens = 0; /* 한줄 해석이 끝났으므로 다음줄 해석을 위해 초기화 */ 
							break; /* STATEMENT_END가 나온 시점이므로 break 하고 다음줄로 넘어감 */ 
						}
						
						else { /* STATEMENT_END가 나오기 전까지 ( *SOMETHING* ) 괄호 사이의 *SOMETHING*에 해당하는 값들을 토크나이즈함 */ 
							if (Character.isWhitespace((char) c) && !(token.equals(""))) { /* white_space가 나오면 한 단어가 끝났음을 의미하므로 그동안 모인 한단어씩 모여 생성된 token을 tokens 배열에 넣어줌, 단 token이 빈문자열일 경우 수행하지 않음 */
								tokens[size_of_tokens] = token.replaceAll("\\s", "");  /* 혹여나 space가 들어가는 경우 replaceAll을 이용해 공백 제거후 token 값을 tokens 배열에 넣어줌 */
								size_of_tokens++; /* tokens가 가진 elements 수가 증가했으므로 size_of_tokens++; */
								token = ""; /* 다음 단어를 수집하기 위해 token 값을 비워줌 */
							} else if ((char) c == QUOTATION_MARKS) { /* QUOTATION_MARK, 즉 큰따옴표 안에 값들은 띄어쓰기가 있어도 하나의 토큰으로 인식하도록 하였음 */
								while (true) { /* 다음 QUOTATION_MARK가 token에 char값을 넣어줌 */
									c = file_reader.read(); /* 한단어 읽어서 c에 넣어줌 */
									if ((char) c == QUOTATION_MARKS) {
										/* QUOTATION_MARK를 만나면 끝을 의미하므로 그동안 collecting 해서 생긴 token 값을 tokens에 넣어줌*/
										if (!(token.equals(""))) { /* 토큰이 빈 문자열이 아닌경우에만 수행 */
											tokens[size_of_tokens] = token; /* 생성된 token을 tokens에 넣어줌 */
											size_of_tokens++; /* tokens가 가진 elements 수가 증가했으므로 size_of_tokens++; */
										}
										token = ""; /* 다음 단어를 수집하기 위해 token 값을 비워줌 */
										break; /* QUOTATION_MARK를 2번째 만난 상황으로 마지막 따옴표임을 의미, 따라서 더이상 따옴표 안에 토큰화될 단어가 없으므로 break; */
									} else { /* 마지막 QUOTATION_MARK가 나오지 않은경우 계속 한문자씩 token에 넣어줌*/ 
										token += (char) c; /* c를 int로 선언했으므로 (char)형 변환 */
									}
								}
							} else { /* white_space도 Quotation_mark도 아닌경우 계속 token에 입력받은 한단어씩 넣어줌 */
								token += (char) c; /* c를 int로 선언했으므로 (char)형 변환 */ 
							}

						}
					}
				}

			}

		}
		file_writer.write("}"); /* 모든 코드가 변환이 끝나서 작성된 상태이므로 c의 메인문을 '}' 입력하여 닫아줌 */
		file_writer.flush();/* 내용이 버퍼에만 남고 파일에 작성되지 않는 문제를 막기위해서 flush method 호출 */
		System.out.println("(.hf) -> (.c) 변환이 완료되었습니다. 생성된 c 파일을 확인해 보세요!"); /* 변환 완료 메시지 출력 */
	}
}