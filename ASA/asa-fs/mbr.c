#include "mbr.h"
#include "drive.h"
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "hardware.h"
struct mbr_s mbr ;
#define NB_VOLUME_MAX 8

//exo5
int initialize_mbr() {
    buffer= malloc(HDA_SECTORSIZE); //taille d'un secteur

    read_sector(0, 0, buffer);
    memcpy(&mbr,buffer,sizeof(struct mbr_s));
    if(mbr.mbr_magic != MBR_MAGIC){
        mbr.mbr_magic = MBR_MAGIC;
        mbr.nb_volume = 0;
        memset(&mbr.volume,0,NB_VOLUME_MAX*sizeof(struct info_volume));
        sauvegarde();
    }
    return 0;

}

void sauvegarde() {
  memcpy(buffer,&mbr,sizeof(struct mbr_s));
  write_sector(0,0,buffer);

}

//exo6
int cylindre_vol(unsigned int vol,unsigned int nb_bloc) {
  struct info_volume v = mbr.volume[vol];
  unsigned int secteur = v.secteur;
  unsigned int cylindre = v.cylindre;
  return ((nb_bloc + secteur)/HDA_MAXSECTOR) + cylindre;//HDA_MAXSECTOR = 16

}

int sector_vol(unsigned int vol,unsigned int nb_bloc) {
  struct info_volume v = mbr.volume[vol];
  unsigned int secteur = v.secteur;
  unsigned int cylindre = v.cylindre;
  return (secteur + nb_bloc)%HDA_MAXSECTOR;
}

//Exo7
void read_bloc(unsigned int vol, unsigned int nb_bloc, unsigned char *buffer) {
  unsigned int cylindre = cylindre_vol(vol,nb_bloc);
  unsigned int sector = sector_vol(vol,nb_bloc);
  read_sector(cylindre,sector,buffer);

}
//nbloc numero bloc
void write_bloc(unsigned int vol, unsigned int nb_bloc, const unsigned char *buffer) {
  unsigned int cylindre = cylindre_vol(vol,nb_bloc);
  unsigned int sector = sector_vol(vol,nb_bloc);
  write_sector(cylindre,sector,buffer);
}

void format_vol(unsigned int vol) {

  unsigned int cylindre;
  unsigned int secteur;
  unsigned int nb_sector = mbr.volume[vol].nb_sector;

    for(unsigned int nsector=0; nsector<nb_sector; nsector++){
        cylindre = cylindre_vol(vol, nsector);
        secteur = sector_vol(vol, nsector);
        format_sector(cylindre, secteur,1,0);
    }


}


void init_hdware2() {
  unsigned int i;

  /* init hardware */
  if (init_hardware("hardware.ini") == 0) {
      fprintf(stderr, "Error in hardware initialization\n");
      exit(EXIT_FAILURE);
  }

  /* Interreupt handlers */
  for (i = 0; i < 16; i++)
      IRQVECTOR[i] = empty_it;
      _mask(1);
      initialize_mbr();

}
