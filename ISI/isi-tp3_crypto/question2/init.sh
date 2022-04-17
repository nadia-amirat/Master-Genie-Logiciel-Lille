#!/bin/bash


#creation des dossiers et fichiers necessaires
#chaque responsable a une clé (usb1 usb2)
sudo mkdir ./ramdisk
mkdir usb1 usb2 disk


#monter le ramdisk dans la ram :
sudo mount -t tmpfs -o rw,size=1M tmpfs ./ramdisk


# generer aleatoirement la  clé principale en clair
dd if=/dev/random bs=32 count=1 > ./ramdisk/key.clear

# creer le fichier db.clear où seront stocker les noms et numeros de carte bancaire
sudo touch ./ramdisk/db.clear

#crypter le fichier db.clear avec la key
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/db.clear -out ./disk/db.enc -pass file:<(cat ./ramdisk/key.clear)

#supprimer le fichier en clair
rm ./ramdisk/db.clear

#On génère une clé aléatoirement
dd if=/dev/random bs=32 count=1 > ./ramdisk/key1.clear


# Le responsable choisit un mot de passe pour protéger sa clé. La clé protégée est copiée sur sa clé usb
openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/key1.clear -out usb1/key1.enc

# crypter la cle principale key par la cle du resposable 1 (key1)

openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/key.clear -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key1.clear)


# On supprime la clé du responsable 1 en clair
rm ./ramdisk/key1.clear

# On supprime la clé principale en clair
rm ./ramdisk/key.clear



# En ce qui concerne le resposable 2 :



#  On génère une clé aléatoirement
dd if=/dev/random bs=32 count=1 > ./ramdisk/key2.clear


#  Le responsable choisit un mot de passe pour protéger sa clé. La clé protégée est copiée sur sa clé usb2
openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/key2.clear -out usb2/key2.enc

# 4) crypter la cle principale par la cle du respo2
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./disk/key.enc -pass file:<(cat ./ramdisk/key2.clear)




# On supprime la clé en clair
rm ./ramdisk/key2.clear


# On supprime la cle crypté par la clé de respo1
rm ./ramdisk/masterkey1.enc
