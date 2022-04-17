#include "check_pass.h"

/**
 * cette fonction permet de demander à l'utilisateur son mot de passe et de verifier si celui ci est correct.
 *
 * @param username le nom d'utilisateur
 * @return 0 si le mot de passe est correct, 1 si le mot de passe est faux, -1 si le fichier n'existe pas.
 */
int check_password(char *username) {
    char password [PASSWORD_SIZE];
    ssize_t read;
    char * line = NULL;
    size_t size = 0;
    int found = 1;

    FILE *file;
    file = fopen("/home/administrateur/passwd", "r");

    if(file != NULL) {
        printf("Veuillez saisir un mot de passe : ");
        scanf("%s", password);

        while ((read = getline(&line, &size, file)) != EOF && found == 1) {
            if(strstr(line, username) != NULL && strstr(line, password) != NULL) {
		        found = 0;
            }
        }

        if(found != 0) {
            return 1;
        } else {
            return 0;
        }
    }
    return -1;
}

/**
 * cette fonction vérifie si l'ID du groupe passe en parametres appartient à la liste des groupes deja existants.
 *
 * @param gid l'identifiant du groupe que nous voulons comparer.
 * @param groups l'adresse du premier gid dans le tableau des groupes ID.
 * @param groups_size la taille du tableau de groupes.
 *
 * @return 0 si le tableau des groupes contient un élément gid égal à gid donné en parametres.
 */
int sameGid(int gid, int *groups, int groups_size) {
    for(int n = 0; n < groups_size; n++) {
        if(gid == groups[n]) {
            return 0;
        }
    }

    return 1;
}

/**
 *  Cette fonction vérifie si l'utilisateur dispose du groupe requis pour supprimer le fichier.
 *
 * @param path Le chemin pour vérifier les permissions des dossiers.
 * @param username Le nom de l'utilisateur
 * @return 0 si l'utilisateur dispose des permissions necessaire, 1 sinon.
 */
int check_GID(char *path, char* username) {
    struct stat buf;
    int usergid;
    int groups_size = 0;

    lstat(path, &buf);

    usergid = getegid();

    getgrouplist(username, usergid, NULL, &groups_size);
    __gid_t groups[groups_size];
    getgrouplist(username, usergid, groups, &groups_size);

    return sameGid(buf.st_gid, groups, groups_size);

}

/**
 * Cette fonction permet de verifier si l'utilisateur dispose d'un groupe
 * qui a l'autorisation de supprimer le fichier et si l'utilisateur
 * a la bonne association de nom et de mot de passe.
 * @param path chemin du fichier
 * @param name Le nom de l'utilisateur
 * @return 1 if the user have bad group id, 2 if the user have bad association of name/password and 0 if the user check his identity successfully.
 */
int check_user(char *path, char *username) {
    if(check_GID(path, username) == 1) {
        printf("Vous n'appartennez pas au bon groupe.\n");
        return 1;
    }

    if(check_password(username) == 1) {
        printf("Mot de passe incorrect\n");
        return 2;
    }

    return 0;

}
