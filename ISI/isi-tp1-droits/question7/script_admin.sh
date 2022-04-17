#!/bin/bash
echo 'USER administrateur:'

echo -e '\n\tlecture de fichiers et sous-répertoires dans dir_c:'
cat /home/administrateur/dir_c/file.txt
cat /home/administrateur/dir_c/repertoire/file.txt
echo -e '\tOK'


echo -e '\n\n\tPossibilité de modifier/créer des fichiers dans toute larborescence dans dir_c:'

echo -e '\n\tmodifier un fichier'
cat /home/administrateur/dir_c/file.txt
echo 'aaa' > /home/administrateur/dir_c/file.txt
echo -e '\tafter modify :'
cat /home/administrateur/dir_c/file.txt

echo -e '\n\tcreer un fichier'
ls -la /home/administrateur/dir_c/
touch /home/administrateur/dir_c/newfile.txt
echo -e '\tafter create:'
ls -la /home/administrateur/dir_c/




echo -e '\n\n\tPossibilité de modifier/créer des fichiers dans toute larborescence dans dir_a:'

echo -e '\n\trenommer un fichier'
ls -la /home/lambda_a/dir_a/
mv /home/lambda_a/dir_a/newfile.txt /home/lambda_a/dir_a/newrenameFile.txt
echo -e '\tafter create:'
ls -la /home/lambda_a/dir_a/

echo -e '\n\tsupprimer un fichier'
ls -la /home/lambda_a/dir_a/
rm /home/lambda_a/dir_a/newFile.txt
echo -e '\tafter delete:'
ls -la /home/lambda_a/dir_a/


echo -e '\n\n\tPossibilité de modifier/créer des fichiers dans toute larborescence dans dir_b:'

echo -e '\n\trenommer un fichier'
ls -la /home/lambda_b/dir_b/
mv /home/lambda_b/dir_b/newFile.txt /home/lambda_b/dir_b/newrenameFile.txt
echo -e '\tafter create:'
ls -la /home/lambda_b/dir_b/

echo -e '\n\tsupprimer un fichier'
ls -la /home/lambda_b/dir_b/
rm /home/lambda_b/dir_b/newFile.txt
echo -e '\tafter delete:'
ls -la /home/lambda_b/dir_b/
