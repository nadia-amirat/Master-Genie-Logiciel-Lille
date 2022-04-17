#!/bin/bash

# Creation des utilisateurs

adduser administrateur
adduser lambda_a
adduser lambda_b
addgroup groupe_a
addgroup groupe_b
addgroup groupe_c

# Association des groupes aux utilisateurs
adduser lambda_a groupe_a
adduser lambda_a groupe_c
adduser lambda_b groupe_b
adduser lambda_b groupe_c
adduser administrateur groupe_a
adduser administrateur groupe_b
adduser administrateur groupe_c

# Creation des différentes répertoires / fichiers
mkdir dir_a dir_b dir_c
mkdir dir_a/repertoire_1a
touch dir_a/fichier_1a
sudo chown administrateur:groupe_a dir_a
sudo chown administrateur:groupe_b dir_b
sudo chown administrateur:groupe_c dir_c

# Changement des droits
sudo chmod g+t dir_a dir_b dir_c
sudo chmod o-r dir_a dir_b dir_c
sudo chmod g+rwx dir_a dir_b dir_c
sudo chmod g+s dir_a dir_b dir_c
sudo chmod g-w dir_c


