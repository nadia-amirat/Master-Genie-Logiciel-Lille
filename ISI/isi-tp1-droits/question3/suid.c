#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main (int argc, char *argv[]) {
  FILE *f;
  char Buffer[128];

  f = fopen(argv[1], "r");
  if (f == NULL) {
    perror("Cannot open file !");
    exit(EXIT_FAILURE);
  }

  while( fgets(Buffer,128,f) ) {
    
    printf("%s",Buffer);

  }
  int euid = geteuid();
  int egid = getegid();
  int ruid = getuid();
  int rgid = getgid();
  printf("The Effective UID =: %d\n", euid);
  printf("The Real      UID =: %d\n", ruid);
  printf("The Effective GID =: %d\n", egid);
  printf("The Real      GID =: %d\n", rgid);

  exit(EXIT_SUCCESS);
  fclose(f);
}
