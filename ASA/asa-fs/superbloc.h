#ifndef _SUPERBLOC_H_
#define _SUPERBLOC_H_
#include "mbr.h"
#define MAGIC_SUPER_BLOC 0x123456

// structure pour le super bloc
struct superbloc {
  int magic; //détrompeur
  int n_serie; // numéro de série
  char name[32]; //nom du volume 32 carac au max
  int racine_bloc; //identifiant vers le premier bloc utilisé
  int first_free_bloc; // identifiant vers le premier block libre
  unsigned int nfree_block;
  int super_first_inode; // numéro du premier inode
};

struct free_bloc {
  int n_blocs; // nombre de blocs consécutifs libres
  int first_free_bloc; // numéro de bloc à partir du quel commence le prochain ensemble de blocs libres
};

void init_volume(unsigned int vol);
int load_super(unsigned int vol);
void save_super();
unsigned int new_bloc();
void free_bloc(unsigned int bloc);
#endif
