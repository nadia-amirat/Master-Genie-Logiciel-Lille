#include <stdlib.h>
#include <stdio.h>
#include "hardware.h"

static void empty_it() {
    return;
}

void dumps(unsigned int piste, unsigned int secteur)
{
  //SEEK  ne pas oublier que numSec et numCy sont sur 16bits
  // je met le numero de piste dans les registres data reg
  _out(HDA_DATAREGS,(piste >> 8)); // le bit de poids fort
  _out(HDA_DATAREGS+1,(piste& 0x00ff)); //poids faible

  // je met le numero de secteur dans les registres data reg
  _out(HDA_DATAREGS+2,(secteur >> 8)); // le bit de poids fort
  _out(HDA_DATAREGS+3,(secteur& 0x00ff)); //poids faible

  // maintenant je peux lancer la commande SEEK dans le registre de commandes
  _out(HDA_CMDREG,CMD_SEEK);

  //j'attends la fin du SEEK
  _sleep(HDA_IRQ);


  // mnt il faut passer à la lecture
     _out(HDA_DATAREGS,0);
     _out(HDA_DATAREGS+1,1);
     _out(HDA_CMDREG, CMD_READ);
     _sleep(HDA_IRQ);


 // l'affichage

  for(int i=0; i<HDA_SECTORSIZE;i++) {
    printf("%X ", MASTERBUFFER[i]);
  }
}


int main(int argc, char** argv) {
    unsigned int i;

    /* init hardware */
    if (init_hardware("hardware.ini") == 0) {
        fprintf(stderr, "Error in hardware initialization\n");
        exit(EXIT_FAILURE);
    }

    /* Interreupt handlers */
    for (i = 0; i < 16; i++)
        IRQVECTOR[i] = empty_it;

    /* Allows all IT */
    _mask(1);
    int piste = atoi(argv[1]);
    int secteur = atoi(argv[2]);
    dumps(piste,secteur);
    exit(EXIT_SUCCESS);
}
