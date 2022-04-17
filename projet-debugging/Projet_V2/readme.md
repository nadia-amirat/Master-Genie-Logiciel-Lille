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

