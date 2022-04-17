#ifndef _MINIIO_H_
#define _MINIIO_H_
#define STACK_SIZE 8192 //donnee recup depuis stackoverflow


void clear_screen();				/* clear screen */
void putc(char aChar);				/* print a single char on screen */
void puts(char *aString);			/* print a string on the screen */
void puthex(unsigned aNumber);			/* print an Hex number on screen */
void putdec(unsigned aNumber);			/* print a decimal number on screen */
void controleur_clavier();
void print_Hello_World();
void keyboard_map(unsigned char c);
void print_compteur(void *args);
void print_controleur_clavier();
void display_cpt();
#endif
