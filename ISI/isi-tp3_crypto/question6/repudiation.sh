#!/bin/bash




read -p "L'identité de la personne répudiée : " repudiee

# ici nous listons les responsables et sous responsables presents, ensuite faire une verification

if [ "$repudiee" == "1" ]; then
    echo 'Responsable 2:
    '
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb2/key2.enc -out ./ramdisk/key2.clear
    echo 'Responsable 3:
    '
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb3/key3.enc -out ./ramdisk/key3.clear
    echo 'Responsable 4:
    '
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb4/key4.enc -out ./ramdisk/key4.clear

	openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey32.enc -out ./ramdisk/masterkey3.enc -pass file:<(cat ./ramdisk/key2.clear)
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key3.clear)

    openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey34.enc -out ./ramdisk/masterkey3.enc -pass file:<(cat ./ramdisk/key4.clear)
    openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./ramdisk/masterkey2.clear -pass file:<(cat ./ramdisk/key3.clear)

    rm ./ramdisk/key3.clear ./ramdisk/key2.clear ./ramdisk/masterkey3.enc ./ramdisk/key4.clear

elif [ "$repudiee" == "2" ]; then
      echo 'Responsable 1:
      '
      openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb1/key1.enc -out ./ramdisk/key1.clear
      echo 'Troisieme Responsable:
      '
      openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb3/key3.enc -out ./ramdisk/key3.clear
      echo 'Responsable 4:
      '
      openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb4/key4.enc -out ./ramdisk/key4.clear

  	   openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey34.enc -out ./ramdisk/masterkey3.enc -pass file:<(cat ./ramdisk/key4.clear)
      openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key3.clear)
      openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey14.enc -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key4.clear)
      openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./ramdisk/masterkey2.clear -pass file:<(cat ./ramdisk/key1.clear)

      rm ./ramdisk/key3.clear ./ramdisk/key1.clear ./ramdisk/masterkey1.enc ./ramdisk/masterkey3.enc ./ramdisk/key4.clear



elif [ "$repudiee" == "3" ]; then
        echo ' Responsable 1:
        '
        openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb2/key1.enc -out ./ramdisk/key1.clear
        echo ' Responsable 2:
        '
        openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb3/key2.enc -out ./ramdisk/key2.clear
        echo 'Responsable 4:
        '
        openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb4/key4.enc -out ./ramdisk/key4.clear

    	  openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey12.enc -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key2.clear)
        openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key1.clear)

        openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey14.enc -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key4.clear)
        openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./ramdisk/masterkey2.clear -pass file:<(cat ./ramdisk/key1.clear)

        rm ./ramdisk/key2.clear ./ramdisk/key1.clear ./ramdisk/masterkey1.enc ./ramdisk/key4.clear

elif [ "$repudiee" == "4" ]; then
          echo 'Responsable 1:
          '
          openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb4/key1.enc -out ./ramdisk/key1.clear
          echo ' Responsable 2:
          '
          openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb2/key2.enc -out ./ramdisk/key2.clear
          echo ' Responsable 3:
          '
          openssl enc -d -pbkdf2 -aes-256-cbc -in ./usb3/key3.enc -out ./ramdisk/key3.clear

      	  openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey12.enc -out ./ramdisk/masterkey1.enc -pass file:<(cat ./ramdisk/key2.clear)
          openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey1.enc -out ./ramdisk/key.clear -pass file:<(cat ./ramdisk/key1.clear)

          openssl enc -d -pbkdf2 -aes-256-cbc -in ./disk/masterkey32.enc -out ./ramdisk/masterkey3.enc -pass file:<(cat ./ramdisk/key2.clear)
          openssl enc -d -pbkdf2 -aes-256-cbc -in ./ramdisk/masterkey3.enc -out ./ramdisk/masterkey2.clear -pass file:<(cat ./ramdisk/key3.clear)

          rm ./ramdisk/key2.clear ./ramdisk/key1.clear ./ramdisk/masterkey1.enc ./ramdisk/key3.clear ./ramdisk/masterkey3.clear

else
  echo ' Erreur'
  exit
fi



#Passons maintenant à la verification

fichier_cle1="./ramdisk/masterkey2.clear"
fichier_cle2="./ramdisk/key.clear"

if cmp -s "$fichier_cle1" "$fichier_cle2"; then
    bash init.sh   #reanitialisation
else
    exit
fi
