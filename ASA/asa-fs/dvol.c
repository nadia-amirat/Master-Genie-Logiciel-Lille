#include "mbr.h"
#include <stdio.h>
#include <stdlib.h>


void display_volumes(){
    char *type[3] = {"base pour systeme de fichier\0",
                      "annexe du volume de fichier\0",
                      "autre\0"};
    printf("\nIl existe actuellement %d volumes sur le disque. \n\n",mbr.nb_volume);

    for(int i=0; i<mbr.nb_volume; i++){
        printf("\nVolume numeros : %d\n", i);
        printf("- cylindre de départ : %d\n", mbr.volume[i].cylindre);
        printf("- secteur de départ : %d\n", mbr.volume[i].secteur);
        printf("- nombre de bloc que contient le volume : %d\n", mbr.volume[i].nb_sector);
        printf("- type de volume : %s\n", type[mbr.volume[i].type]);
}
    }


int main()
{
    init_hdware2();
    display_volumes();
    return 0;
}
