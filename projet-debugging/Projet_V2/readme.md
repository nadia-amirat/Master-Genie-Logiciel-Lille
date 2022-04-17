## Projet Debugging

### Groupe 01 Génie Logiciel

**Binôme :**
- Manon MOUQUET
- Nadia AMIRAT

**GitLab :** 
https://gitlab.univ-lille.fr/nadia.amirat.etu/projet-debugging/-/tree/master/Projet_V2

### Compilation et exécution du projet :

Une fois le projet décompresser ouvrir un terminal à la racine du dossier **debugging**

1 - taper la commande : 

``` mvn package ``` afin de générer le dossier target

2 - ouvrir  le projet avec InteliJ :


SDK 11

* Éditer la configuration du projet 
* Dans l'onglet Build and run sélectionner la classe :

```debugger.Debugger```

Dans working directory sélectionner : 

le chemin ```debugging/target/classes``` du dossier classes générer précédemment avec   ``` mvn package ``` 

- Exécuter le projet sous InteliJ
Voir la vidéo de démonstration à ouvrir avec **VLC** .

**Comandes non implémentées :**

Nous n'avons pas réussi à implémenter la méthode stack-top et break-before-method-call.

**Remarque :**

Lors de la création du projet sous InteliJ celui-ci s'éxecutait uniquement sur la machine de la personne qui l'a crée.
Donc un nouveau projet a dû être créer via sa machine; mais le problème persiste (pas moyen d'executer le projet par la personne qui n'a PAS crée le projet).

On a dû donc travailler chacune sur la version du projet créer sur sa machine pour éviter de perdre beaucoup de temps à resoudre le problème, et donc à chaque fois faire un copier coller des méthodes implémenté par l'autre binôme pour s'assurer que tout marche ensuite faire un push sur GitLab.

Nous avons également fourni une vidéo de démonstration pour montrer que notre programme marche.
