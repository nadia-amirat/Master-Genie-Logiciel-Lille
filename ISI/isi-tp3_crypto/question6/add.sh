#! /bin/bash


read -p "Veuillez entrer le nom du proprietaire de la carte bancaire " nom
read -p "Veuillez entrer le numero de la carte : " numero


carte="$nom:$numero"

openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/db.enc -out ./ramdisk/db.clear -pass file:<(cat ./ramdisk/key.clear)

cartes='./ramdisk/db.clear'

echo $carte >>$cartes

# crypter le fichier contenant les noms et cartes puis le transferer vers disk

openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/db.clear -out ./disk/db.enc -pass file:<(cat  ./ramdisk/key.clear)

rm ./ramdisk/db.clear
