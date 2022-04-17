#!/bin/bash

echo 'USER : lambda_b:'
echo -e '\tlecture fichier et sous-répertoires dans dir_b:'
cat /home/lambda_b/dir_b/fichier_1b.txt
cat /home/lambda_b/dir_b/repertoire_1b/file.txt
echo -e '\tOK'

echo -e '\n\tlecture de fichiers et sous-répertoires qui se trouve dans dir_c:'
cat /home/administrateur/dir_c/fichier_1c.txt
cat /home/administrateur/dir_c/repertoire_1c/file.txt
echo -e '\tOK'

echo -e '\n\n\tPossibilité de modifier un fichier et créer des fichiers/repertoires dans toute larborescence dans dir_b:'

echo -e '\n\tmodifier un fichier'
cat /home/lambda_b/dir_b/file.txt
echo 'file.txt in dir_a readed! MODIFIED' > /home/lambda_b/dir_b/file.txt
echo -e '\tafter modify:'
cat /home/lambda_b/dir_b/file.txt

echo -e '\n\tcreer un fichier'
ls -la /home/lambda_b/dir_b/
touch /home/lambda_b/dir_b/file2.txt
echo -e '\tafter create:'
ls -la /home/lambda_b/dir_b/

echo -e '\n\tcreer un repertoire'
ls -la /home/lambda_b/dir_b/
mkdir /home/lambda_b/dir_b/new_dir
echo -e '\tafter create:'
ls -la /home/lambda_b/dir_b/

echo -e '\n\n\tImpossibile de supprimer/renommer/modifier/créer fichiers dans dir_c:'

echo -e '\n\trenommer un fichier dans le répértoire dir_c'
mv /home/administrateur/dir_c/fichier_1c.txt /home/administrateur/dir_c/fichier_1c_modifier
echo -e '\n\tafter rename, no changes : permission denied'
ls -l /home/administrateur/dir_c/

echo -e '\n\tmodifier un fichier dans le répértoire dir_c'

echo -e 'content of file :'
cat /home/administrateur/dir_c/fichier_1c.txt
echo 'je suis entrain de modifier ce fichier' > /home/administrateur/dir_b/fichier_1b.txt
echo -e '\tafter modify : (should be the same)'
cat /home/administrateur/dir_b/fichier_1b.txt

echo -e '\n\tDelete'
ls -la /home/administrateur/dir_c/
rm -f /home/administrateur/dir_c/fichier_1c.txt
echo -e '\tafter delete : (should be not delete the file : permission denied)'
ls -la /home/administrateur/dir_c/

echo -e '\n\tImpossible de lire/creer/modifier/supprimer/renommer dans dir_a'

echo -e '\n\trenommer un fichier dans le répértoire dir_a'
mv /home/administrateur/dir_a/fichier_1a.txt /home/administrateur/dir_a/fichier_1a_modifier
echo -e '\n\tafter rename, no changes : permission denied'
ls -l /home/administrateur/dir_a/

echo -e '\n\tmodifier un fichier dans le répértoire dir_a'
echo -e 'content of file :'
cat /home/administrateur/dir_a/fichier_1a.txt
echo 'je suis entrain de modifier ce fichier' > /home/administrateur/dir_a/fichier_1a.txt
echo -e '\tafter modify : (should be the same)'
cat /home/administrateur/dir_a/fichier_1a.txt

echo -e '\n\tDelete'
ls -la /home/administrateur/dir_a/
rm -f /home/administrateur/dir_a/fichier_1a.txt
echo -e '\tafter delete : (should be not delete the file : permission denied)'
ls -la /home/administrateur/dir_a/
