# Rendu "Les droits d’accès dans les systèmes UNIX"

## Binome

- Nom, Prénom, email: AMIRAT, Nadia, nadia.amirat.etu@univ-lille.fr

- Nom, Prénom, email: AMARA, Cyria, cyria.amara.etu@univ-lille.fr

## Question 1

ajouter le user toto au groupe ubuntu : <code> gpasswd -a toto ubuntu </code> 

<code>-r--rw-r-- 1 toto ubuntu    5 Jan  5 09:35 titi.txt</code>

Le processus peut il écrir sur le fichier titi.txt : non 
justification : le user toto à juste le droit de lecture <code>r--</code>


## Question 2

Un répertoire doit être exécutable pour que vous puissiez accéder à l'ensemble de ses sous-répertoires.
<code>su ubuntu</code> : pour passer de root à ubuntu
<code>sudo -s</code> : pour passer de ubuntu vers root
changer les droits au groupe ubuntu : <code>chmod g-x mydir/</code>
<code>
toto@vm1:/home/ubuntu$ cd mydir/
bash: cd: mydir/: Permission denied</code>

Je ne peux pas acceder au repertoire mydir via le user toto, car j'ai deja enlever les droits d'execution pour le groupe ubuntu qui contient le user toto.
 apres avoir créer le fichier data.txt dans le repertoire mydir : 
<code>
toto@vm1:/home/ubuntu$ ls -al mydir/
ls: cannot access 'mydir/.': Permission denied
ls: cannot access 'mydir/..': Permission denied
ls: cannot access 'mydir/data.txt': Permission denied
total 0
d????????? ? ? ? ?            ? .
d????????? ? ? ? ?            ? ..
-????????? ? ? ? ?            ? data.txt
</code>
## Question 3
<code>
ubuntu@vm1:~$ su toto
Password: 
toto@vm1:/home/ubuntu$ ./a.out mydir/data.txt 
The Effective UID =: 1001
The Real      UID =: 1001
The Effective GID =: 1001
The Real      GID =: 1001</code>

Le processus n'arrive pas à ouvrir le fichier <code>mydir/mydata.txt </code> en lecture.

apres avoir fait un chmod u+s (set-user-ID) :

<code>The Effective UID =: 1000
The Real      UID =: 1001
The Effective GID =: 1001
The Real      GID =: 1001</code>

Le processus arrive  à ouvrir le fichier <code>mydir/mydata.txt </code> en lecture car EUID vaut 1000 qui est celui de l'utilisateur ubuntu.

## Question 4

<code>Effective user ID is : 1001
Effective Group ID is : 1001</code>
Comment un utilisateur peut changer un de ses attributs sans demander à l’administrateur :
set-user-id pour que egid sera égale à euid.
## Question 5
<code>chfn</code> : Modifier les informations personnel d'un utilisateur.
Par exemple :

Full Name
Room Number
Work Phone
Home Phone

le résultat de : <code>ls -al /usr/bin/chfn</code>
<code>-rwsr-xr-x 1 root root 85064 May 28  2020 /usr/bin/chfn</code>

Explication des permissions : 

- root : comme utilisateur et groupe
- droits de lecture pour tous le monde
- droits d'éxecution pour tous le monde
- droits d'écriture à root seulement
- Le flag set-user-id est activé.

Les informations ont bien été mises à jour correctement.


## Question 6

Les mots de passes des utilisateurs sont stockés à : <code>'/etc/shadow'</code>
il est fermé à la lecture par tous le monde.

Pourquoi : car s'ils étaient stockés dans passwd ils seraient visible par tous le monde, alors que dans <code>'/etc/shadow'</code> ils seraient accessible seulement par root.

## Question 7

Mettre les scripts bash dans le repertoire *question7*.

## Question 8

Le programme et les scripts dans le repertoire *question8*.

## Question 9

Le programme et les scripts dans le repertoire *question9*.

## Question 10

Les programmes *groupe_server* et *groupe_client* dans le repertoire
*question10* ainsi que les tests. 








