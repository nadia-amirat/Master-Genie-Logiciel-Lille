# lmd-graph


#### Limitation d'une approche par generation de code :

La génération de code est très utile lorsque le code généré peut être directement utilisé sans devoir à le personalisé.
Dès que le développeur commence à modifier le code généré celui-ci devient difficile de l'entretenir, car à chaque nouvelle génération de code celui-ci est ecrasé, le code qui a était rajouter  mannuelement est impacté par la regénération et doncc le model défini au tout début n'est plus respecté.


#### Approche alternative  : 

Le code généré doit être soigneusement isolé dans la base de code et la regeneration de code doit être possible uniquement s'il elle n'impacte pas le code existant.

Une approche serait d'utiliser l'heritage afin de pouvoir personalisé le code source librement.

Envisager la protection de la partie du code personalisé avec des commentaires spéciaux de tel sorte à ce que lors de la prochaine génération cette partie ne soit pas impacté.