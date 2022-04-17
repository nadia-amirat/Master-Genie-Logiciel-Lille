
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <stdint.h>
#include "hardware.h"
#include "drive.h"


void empty_it() {
    return;
}
void init_hdware() {
  unsigned int i;

  /* init hardware */
  if (init_hardware("hardware.ini") == 0) {
      fprintf(stderr, "Error in hardware initialization\n");
      exit(EXIT_FAILURE);
  }

  /* Interreupt handlers */
  for (i = 0; i < 16; i++)
      IRQVECTOR[i] = empty_it;

}

void seek_sector(unsigned int cylindre, unsigned int secteur)
{
  //SEEK  ne pas oublier que numSec et numCy sont sur 16bits
  // je met le numero de cylindre dans les registres data reg
  _out(HDA_DATAREGS,(cylindre >> 8)); // le bit de poids fort
  _out(HDA_DATAREGS+1,(cylindre & 0x00ff)); //poids faible

  // je met le numero de secteur dans les registres data reg
  _out(HDA_DATAREGS+2,(secteur >> 8)); // le bit de poids fort
  _out(HDA_DATAREGS+3,(secteur & 0x00ff)); //poids faible

  // maintenant je peux lancer la commande SEEK dans le registre de commandes
  _out(HDA_CMDREG,CMD_SEEK);

  //j'attends la fin du SEEK
  _sleep(HDA_IRQ);
}

void read_sector(unsigned int cylinder, unsigned int sector,unsigned char *buffer)
{
   seek_sector(cylinder,sector);
   _out(HDA_DATAREGS, 0);
   _out(HDA_DATAREGS+1, 1);
   _out(HDA_CMDREG,CMD_READ);
  _sleep(HDA_IRQ);
  memcpy(buffer, MASTERBUFFER, 256);
  // l'affichage

   // for(int i=0; i<HDA_SECTORSIZE;i++) {
   //   printf("%X ", MASTERBUFFER[i]);
   // }
}

void write_sector(unsigned int cylinder, unsigned int sector,const unsigned char *buffer)
{
  for(int i =0; i<HDA_SECTORSIZE; i++){
          MASTERBUFFER[i] = buffer[i];
      }
  seek_sector(cylinder,sector);
  //Write
      memcpy(MASTERBUFFER,buffer, HDA_SECTORSIZE);
      _out(HDA_DATAREGS, 0);
      _out(HDA_DATAREGS+1, 1);
      _out(HDA_CMDREG,CMD_WRITE);

      _sleep(HDA_IRQ);


}
void format_sector(unsigned int cylinder, unsigned int sector,unsigned int nsector,unsigned int value)
{
  seek_sector(cylinder,sector);
  //nsector est sur 16bits
  _out(HDA_DATAREGS,(nsector >> 8));
  _out(HDA_DATAREGS+1,(nsector & 0x00ff));
  //val est sur 32 bits
  _out(HDA_DATAREGS+2, (value >> 24)); //recup premier octet
  _out(HDA_DATAREGS+3, (value >> 16));            //ici j'ai utilisé pyhton afin de m'assurer que les decalages
  _out(HDA_DATAREGS+4, (value >> 8));             // renvoient bien les bonnes valeurs
  _out(HDA_DATAREGS+5, (value & 0x000000FF));
  //formatter
      _out(HDA_CMDREG,CMD_FORMAT);
      for(int j=0;j<nsector;j++){
          _sleep(HDA_IRQ);
      }

}
