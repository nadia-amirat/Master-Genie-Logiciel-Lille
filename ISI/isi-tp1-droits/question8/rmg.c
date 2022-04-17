#include "check_pass.h"

int main(int argc, char** argv) {
    if(argc >= 1 && argc < 3) {
        printf("Veuillez indiquez le fichier à supprimer et votre nom d'utilisateur.\n");
	return -1;
    }

    char* path = argv[1];
    char* username = argv[2];

    int result = check_user(path, username);

    if (result == 0) {
        remove(path);
        printf("%s fichier supprimé avec succès.\n", path);
        return 0;

    } else {
        return result;
    }

}
