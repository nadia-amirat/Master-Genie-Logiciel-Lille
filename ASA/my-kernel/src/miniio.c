#include "ioport.h"
#include "miniio.h"

/* base address for the video output assume to be set as character oriented by the multiboot */
unsigned char *video_memory = (unsigned char *) 0xB8000;

/* clear screen */
void clear_screen() {
  int i;
  for(i=0;i<80*25;i++) { 			/* for each one of the 80 char by 25 lines */
    video_memory[i*2+1]=0x0F;			/* color is set to black background and white char */
    video_memory[i*2]=(unsigned char)' '; 	/* character shown is the space char */
  }
}

/* print a string on the screen */
void puts(char *aString) {
  char *current_char=aString;
  while(*current_char!=0) {
    putc(*current_char++);
  }
}

/* print an number in hexa */
char *hex_digit="0123456789ABCDEF";
void puthex(unsigned aNumber) {
  int i;
  int started=0;
  for(i=28;i>=0;i-=4) {
    int k=(aNumber>>i)&0xF;
    if(k!=0 || started) {
      putc(hex_digit[k]);
      started=1;
    }
  }
  if(!started) putc('0');
}

/* print a positive number in decimal */
void putdec(unsigned aNumber) {
  unsigned b;
  int started=0;
  /* 1,000,000,000 <= 2^32 < 10,000,000,000 */
  for(b=1000000000;b>0;b/=10) {
    int d=aNumber/b;
    if(d!=0 || started) {
      putc(hex_digit[d]);
      started=1;
    }
    aNumber=aNumber%b;
  }
  if(!started) putc('0');
}

/* print a char on the screen */
int cursor_x=0;					/* here is the cursor position on X [0..79] */
int cursor_y=0;					/* here is the cursor position on Y [0..24] */

void setCursor() {
  int cursor_offset = cursor_x+cursor_y*80;
  _outb(0x3d4,14);
  _outb(0x3d5,((cursor_offset>>8)&0xFF));
  _outb(0x3d4,15);
  _outb(0x3d5,(cursor_offset&0xFF));
}

void putc(char c) {
  if(cursor_x>79) {
    cursor_x=0;
    cursor_y++;
  }
  if(cursor_y>24) {
    cursor_y=0;
    clear_screen();
  }
  switch(c) {					/* deal with a special char */
    case '\r': cursor_x=0; break;		/* carriage return */
    case '\n': cursor_x=0; cursor_y++; break; 	/* new ligne */
    case 0x8 : if(cursor_x>0) cursor_x--; break;/* backspace */
    case 0x9 : cursor_x=(cursor_x+8)&~7; break;	/* tabulation */
						/* or print a simple character */
    default  :
      video_memory[(cursor_x+80*cursor_y)*2]=c;
      cursor_x++;
      break;
  }
  setCursor();
}


int shift=0;

void keyboard_map(unsigned char c) {

    switch(c){

       case 0x2A: shift=1; break;
       case 0xAA: shift=0; break;
       case 0x1E: if(shift) putc('A'); else putc('a'); break;
       case 0x30: if(shift) putc('B'); else putc('b'); break;
       case 0x2E: if(shift) putc('C'); else putc('c'); break;
       case 0x20: if(shift) putc('D'); else putc('d'); break;
       case 0x12: if(shift) putc('E'); else putc('e'); break;
       case 0x21: if(shift) putc('F'); else putc('f'); break;
       case 0x22: if(shift) putc('G'); else putc('g'); break;
       case 0x23: if(shift) putc('H'); else putc('h'); break;
       case 0x17: if(shift) putc('I'); else putc('i'); break;
       case 0x24: if(shift) putc('J'); else putc('j'); break;
       case 0x25: if(shift) putc('K'); else putc('k'); break;
       case 0x26: if(shift) putc('L'); else putc('l'); break;
       case 0x32: if(shift) putc('M'); else putc('m'); break;
       case 0x31: if(shift) putc('N'); else putc('n'); break;
       case 0x18: if(shift) putc('O'); else putc('o'); break;
       case 0x19: if(shift) putc('P'); else putc('p'); break;
       case 0x10: if(shift) putc('Q'); else putc('q'); break;
       case 0x13: if(shift) putc('R'); else putc('r'); break;
       case 0x1F: if(shift) putc('S'); else putc('s'); break;
       case 0x14: if(shift) putc('T'); else putc('t'); break;
       case 0x16: if(shift) putc('U'); else putc('u'); break;
       case 0x2F: if(shift) putc('V'); else putc('v'); break;
       case 0x11: if(shift) putc('W'); else putc('w'); break;
       case 0x2D: if(shift) putc('X'); else putc('x'); break;
       case 0x15: if(shift) putc('Y'); else putc('y'); break;
       case 0x2C: if(shift) putc('Z'); else putc('z'); break;
      // case 0x2A: return 'debut shift';
      // case 0xAA: return 'fin shift';
       case 0xB: putc('0');break;
       case 0x2: putc('1');break;
       case 0x3: putc('2');break;
       case 0x4: putc('3');break;
       case 0x5: putc('4');break;
       case 0x6: putc('5');break;
       case 0x7: putc('6');break;
       case 0x8: putc('7');break;
       case 0x9: putc('8');break;
       case 0xA: putc('9');break;
       case 0xD: putc('=');break;
       case 0xC: putc('-');break;
       case 0x27:putc(';');break;
       case 0x33:putc('<');break;
       case 0x34:putc('>');break;
       case 0x39:putc(' '); break;
       case 0x28:putc('"');

  }

}
int cpt = 0;
void controleur_clavier() {
  unsigned char key = _inb(0x60);//recuperer la touche clavier au click
  puts("key ");
  puthex(key);
  puts(" : ");
  keyboard_map(key);
  puts("\n");
}

void print_Hello_World() {
  puts("Hello World!\n\n");

}

void display_cpt() {
  while(1) {
      __asm volatile("cli");
      int cpt_cursor_x = cursor_x;
      int cpt_cursor_y = cursor_y;
      cursor_x = 70;
      cursor_y = 5;
      putdec(cpt++);
      puts("\n");
      for(int i=0;i<100000000;i++);
      cursor_x = cpt_cursor_x;
      cursor_y = cpt_cursor_y;
      _outb(0xA0, 0x20);
      _outb(0x20, 0x20);
      __asm volatile("sti");

  }
}
