#! /bin/bash

read -p "Veuillez entrer le nom du proprietaire de la carte bancaire " nom
read -p "Veuillez entrer le numero de la carte : " numero


openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/db.enc -out ./ramdisk/db.clear -pass file:<(cat ./ramdisk/key.clear)

carte="$nom:$numero"
cartes='./ramdisk/db.clear'

sed -i "/^$carte$/d" $cartes

openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/db.clear -out ./disk/db.enc -pass file:<(cat ./ramdisk/key.clear)

rm ./ramdisk/db.clear
