#!/bin/bash

sudo mkdir ./ramdisk
mkdir usb1 usb2 usb3 usb4
mkdir disk

#monter un dossier ramDisk dans la ram
sudo mount -t tmpfs -o rw,size=10M tmpfs ./ramdisk

#Crer un fichier key.clear
dd if=/dev/random bs=32 count=1 > ./ramdisk/key.clear

# crer db.clear et le crypter avec key.clear
touch ./ramdisk/db.clear

openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/db.clear -out ./disk/db.enc -pass file:<(cat ./ramdisk/key.clear)

rm ./ramdisk/db.clear

#On génère une clé aléatoirement pour chacun des ayant droits
dd if=/dev/random bs=32 count=1 > ./ramdisk/key1.clear
dd if=/dev/random bs=32 count=1 > ./ramdisk/key2.clear
dd if=/dev/random bs=32 count=1 > ./ramdisk/key3.clear
dd if=/dev/random bs=32 count=1 > ./ramdisk/key4.clear

#Les responsables choisissent un mot de passe pour protéger leur clé. La clé protégée est copiée sur leur clé usb
echo "Responsable Technique"
openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/key1.clear -out usb1/key1.enc
echo "Responsable Juridique"
openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/key2.clear -out usb2/key2.enc
echo "Representant du Responsable Technique"
openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/key3.clear -out usb3/key3.enc
echo "Representant du Responsable Juridique"
openssl enc -pbkdf2 -aes-256-cbc -in ./ramdisk/key4.clear -out usb4/key4.enc



#Crypter la cle principale par la cle du respo1 ( responsable thechnique)
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/key.clear -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key1.clear)

# On supprime la clé du respo1 en clair
rm ./ramdisk/key1.clear

# Crypter une deuxième fois par la cle du respo2 ( responsable juridique)
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./disk/masterkey12.enc -pass file:<(cat ./ramdisk/key2.clear)

# Crypter une deuxième fois par la cle du respo4 ( représentant du responsable juridique)
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./disk/masterkey14.enc -pass file:<(cat ./ramdisk/key4.clear)

rm ./ramdisk/masterkey1.enc

#Crypter la cle master par la cle du respo3 ( representant du responsable thechnique)
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/key.clear -out ./ramdisk/masterkey3.enc -pass file:<(cat ./ramdisk/key3.clear)

# On supprime la clé du respo3 en clair
rm ./ramdisk/key3.clear

# Crypter une deuxième fois par la cle du respo2 ( responsable juridique)
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./disk/masterkey32.enc -pass file:<(cat ./ramdisk/key2.clear)

# On supprime la clé du respo2 en clair
rm ./ramdisk/key2.clear

# Crypter une deuxième fois par la cle du respo4 ( representant du responsable juridique)
openssl enc  -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./disk/masterkey34.enc -pass file:<(cat  ./ramdisk/key4.clear)

# On supprime la clé du respo4 en clair
rm ./ramdisk/key4.clear
rm ./ramdisk/masterkey3.enc
rm ./ramdisk/key.clear
