#! /bin/bash

# decrypter la clé du responsable 1
echo "Responsable 1 :  veuillez entrer votre mot de passe : "
openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb1/key1.enc -out ./ramdisk/key1.clear

# decrypter la clé du responsable 2
echo "Responsable 2 : veuillez entrer votre mot de passe : "
openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb2/key2.enc -out ./ramdisk/key2.clear

# decrypter la clé principale et la mettre dans la ram
openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/key.enc -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key2.clear)

rm ./ramdisk/key2.clear

openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key1.clear)

rm ./ramdisk/masterkey1.enc

rm ./ramdisk/key1.clear
