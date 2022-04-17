#! /bin/bash

read -p "Veuillez entrer le nom du proprietaire de la carte bancaire " nom



openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/db.enc -out ./ramdisk/db.clear -pass file:<(cat ./ramdisk/key.clear)

cartes='./ramdisk/db.clear'

grep "^$nom" ./ramdisk/db.clear


rm ./ramdisk/db.clear
