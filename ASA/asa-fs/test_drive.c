#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <stdint.h>
#include "hardware.h"
#include "drive.h"
#define SECTOR_SIZE 256
//void read_sector(unsigned int cylinder, unsigned int sector,unsigned char *buffer);

int main(void) {
    unsigned int i;
    unsigned char buffer1[SECTOR_SIZE]; //pour la lecture
    unsigned char buffer2[SECTOR_SIZE]; // pour l'ecriture

    /* init hardware */
    init_hdware();
    _mask(1);

   printf("\n");
   printf("+------------------------------+\n");
   printf("| Lecture avant formatage      |\n");
   printf("+------------------------------+\n");
   printf("\n");

   read_sector(2, 2, buffer1);
   for(int i=0; i<HDA_SECTORSIZE;i++) {
     printf("%X ", MASTERBUFFER[i]);
   }

   printf("\n");
   printf("+------------------------------+\n");
   printf("|   Lecture apres formatage    |\n");
   printf("+------------------------------+\n");
   printf("\n");
   //void format_sector(unsigned int cylinder, unsigned int sector,unsigned int nsector,unsigned int value);

   format_sector(2, 2,6,0);

   printf("\n");

   read_sector(2, 2, buffer1);
   for(int i=0; i<HDA_SECTORSIZE;i++) {
     printf("%X ", MASTERBUFFER[i]);
   }

   printf("\n");
   printf("+------------------------------+\n");
   printf("|  Ecriture                    |\n");
   printf("+------------------------------+\n");
   printf("\n");

   write_sector(2, 2, buffer2);

   printf("+------------------------------+\n");
   printf("|  Lecture                     |\n");
   printf("+------------------------------+\n");
   printf("\n");

   read_sector(2, 2, buffer2);
   for(int i=0; i<HDA_SECTORSIZE;i++) {
     printf("%X ", MASTERBUFFER[i]);
   }
   printf("\n");

    exit(EXIT_SUCCESS);
}
