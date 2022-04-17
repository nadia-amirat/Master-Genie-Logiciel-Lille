#include <stdlib.h>
#include <stdio.h>
#include "hardware.h"

#define NBSEC 5
static void empty_it() {
    return;
}


void read_sector(unsigned int cylindre, unsigned int secteur) {
    _out(HDA_DATAREGS,(cylindre >> 8)); // le bit de poids fort
    _out(HDA_DATAREGS+1,(cylindre & 0x00ff)); //poids faible

    // je met le numero de secteur dans les registres data reg
    _out(HDA_DATAREGS+2,(secteur >> 8)); // le bit de poids fort
    _out(HDA_DATAREGS+3,(secteur & 0x00ff)); //poids faible

    // maintenant je peux lancer la commande SEEK dans le registre de commandes
    _out(HDA_CMDREG,CMD_SEEK);

    //j'attends la fin du SEEK
    _sleep(HDA_IRQ);

      // mnt il faut passer à la lecture
         _out(HDA_DATAREGS,1);
         _out(HDA_CMDREG, CMD_READ);
         _sleep(HDA_IRQ);


     // l'affichage

      for(int i=0; i<HDA_SECTORSIZE;i++) {
        printf("%u", MASTERBUFFER[i]);
      }

}

void frmt(unsigned int cylindre, unsigned int secteur) {

      // je met le numero de piste dans les registres data reg
      _out(HDA_DATAREGS,(cylindre >> 8)); // le bit de poids fort
      _out(HDA_DATAREGS+1,(cylindre & 0x00ff)); //poids faible

      // je met le numero de secteur dans les registres data reg
      _out(HDA_DATAREGS+2,(secteur >> 8)); // le bit de poids fort
      _out(HDA_DATAREGS+3,(secteur & 0x00ff)); //poids faible

      // maintenant je peux lancer la commande SEEK dans le registre de commandes
      _out(HDA_CMDREG,CMD_SEEK);

      //j'attends la fin du SEEK
      _sleep(HDA_IRQ);

      // on passe à format ici
      _out(HDA_DATAREGS,(NBSEC >> 8)); // le bit de poids fort
      _out(HDA_DATAREGS+1,(NBSEC & 0x00ff)); //poids faible //initialise nbSec secteurs avec val
      // on met tout à 0
      _out(HDA_DATAREGS+2,0x00);
      _out(HDA_DATAREGS+3,0x00);
      _out(HDA_DATAREGS+4,0x00);
      _out(HDA_DATAREGS+5,0x00);
      _out(HDA_CMDREG,CMD_FORMAT);
      _sleep(HDA_IRQ);

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
    int cylindre = atoi(argv[1]);
    int secteur = atoi(argv[2]);
    printf("lecture 1\n");
    read_sector(cylindre,secteur);
    printf("\n");
    frmt(cylindre,secteur);
    printf("\nlecture 2\n");
    read_sector(cylindre,secteur);
    printf("\n");
    exit(EXIT_SUCCESS);
}
