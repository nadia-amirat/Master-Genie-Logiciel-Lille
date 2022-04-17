#! /bin/bash

# decrypter la clé du responsable1 ou son représentant
read -p "Veuillez entrer 1 si vous êtes le résponsabe technique, 2 si vous êtes son representant: " responsable1


if [ "$responsable1" == "1" ]; then
	openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb1/key1.enc -out ./ramdisk/key1.clear
elif [ "$responsable1" == "2" ]; then
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb3/key3.enc -out ./ramdisk/key3.clear
else
    echo "Erreur "
    exit
fi

read -p "Veuillez entrer 1 si vous êtes le résponsabe juridique, 2 si vous êtes son representant: " responsable2

if [ "$responsable2" == "1" ]; then
	openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb2/key2.enc -out ./ramdisk/key2.clear
elif [ "$responsable2" == "2" ]; then
    	openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb4/key4.enc -out ./ramdisk/key4.clear
else
    echo "Ereur"
    exit
fi

# Dechiffrer le bon fichier
if [ "$responsable1" == "1" ] && [ "$responsable2" == "1" ]; then

	openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey12.enc -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key2.clear)
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key1.clear)
    rm ./ramdisk/masterkey1.enc
    rm ./ramdisk/key1.clear
    rm ./ramdisk/key2.clear

elif [ "$responsable1" == "1" ] && [ "$responsable2" == "2" ]; then

    openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey14.enc -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key4.clear)
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key1.clear)
    rm ./ramdisk/masterkey1.enc
    rm ./ramdisk/key1.clear
    rm ./ramdisk/key4.clear

elif [ "$responsable1" == "2" ] && [ "$responsable2" == "1" ]; then

    openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey32.enc -out ./ramdisk/masterkey3.enc -pass file:<(cat ./ramdisk/key2.clear)
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key3.clear)
    rm ./ramdisk/masterkey3.enc
    rm ./ramdisk/key2.clear
    rm ./ramdisk/key3.clear

elif [ "$responsable1" == "2" ] && [ "$responsable2" == "2" ]; then

    openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey34.enc -out ./ramdisk/masterkey3.enc -pass file:<(cat ./ramdisk/key4.clear)
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key3.clear)
    rm ./ramdisk/masterkey3.enc
    rm ./ramdisk/key3.clear
    rm ./ramdisk/key4.clear

else
    echo "Erreur"
    exit
fi
