#include <stdio.h>
#include <stdlib.h>
int main(){
printf("hello world\n");
system("ls -al");
system("ls -al > \"test.txt\"");
system("cat \"test.txt\"");
system("rm \"test.txt\"");
}
