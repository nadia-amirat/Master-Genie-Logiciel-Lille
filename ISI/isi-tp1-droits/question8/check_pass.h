#ifndef CHECK_PASS_H_
#define CHECK_PASS_H_
#define PASSWORD_SIZE 20

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <grp.h>

/**
 * Cette fonction permet de verifier si l'utilisateur dispose d'un groupe
 * qui a l'autorisation de supprimer le fichier et si l'utilisateur
 * a la bonne association de nom et de mot de passe.
 * @param path chemin du fichier
 * @param name Le nom de l'utilisateur
 * @return 1 if the user have bad group id, 2 if the user have bad association of name/password and 0 if the user check his identity successfully.
 */
int check_user(char *pathname, char *username);

#endif
