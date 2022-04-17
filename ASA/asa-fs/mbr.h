#pragma once
#define MBR_MAGIC 0xABCDEF
#define NB_VOLUME_MAX 8
#define HDA_MAXSECTOR 16


//une information associée à chaque volume permettra de déterminer si le volume est
#define BASE 0
#define ANNEXE 1
#define AUTRE 2

extern struct mbr_s mbr;
struct info_volume {
  unsigned int secteur;
  unsigned int cylindre;
  unsigned int nb_sector;
  int type;
};
struct mbr_s {
  int mbr_magic;
  int nb_volume;
  struct info_volume volume[NB_VOLUME_MAX];
};

const char* buffer;
int initialize_mbr();
void sauvegarde();
int cylindre_vol(unsigned int vol,unsigned int nb_bloc);
int sector_vol(unsigned int vol,unsigned int nb_bloc);
void read_bloc(unsigned int vol, unsigned int nb_bloc, unsigned char *buffer);
void write_bloc(unsigned int vol, unsigned int nb_bloc, const unsigned char *buffer);
void format_vol(unsigned int vol);

void init_hdware2();
