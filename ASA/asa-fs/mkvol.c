#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "hardware.h"
#include "mbr.h"
#include "hardware.h"
#include "drive.h"

#define MAX_VOLUME 8

void mkdir_volume(unsigned int sector, unsigned int cylinder, unsigned int nbBloc){
	if(mbr.nb_volume == MAX_VOLUME)
		printf(stderr, "Not enought space to create another volume\n");
	else {


		struct info_volume volume;
		volume.secteur = sector;
		volume.cylindre = cylinder;
		volume.nb_sector = nbBloc;
		volume.type = BASE;
		mbr.volume[mbr.nb_volume] = volume;
    mbr.nb_volume = mbr.nb_volume + 1 ;

	}
	sauvegarde();
}


int main(int argc, char **argv){

    init_hdware2();
    mkdir_volume((unsigned int)atoi(argv[1]),(unsigned int)atoi(argv[2]),(unsigned int)atoi(argv[3]));

    return 0;

}
