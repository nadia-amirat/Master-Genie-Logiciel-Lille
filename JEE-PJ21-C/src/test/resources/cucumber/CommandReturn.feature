# language: fr
Fonctionnalité: Un Employé peut ajuster le stock d'un produit à partir de la liste d'une commande

  Scénario: L'Employé remet en stock un produit non périssable
    Soit un article "Stylo" non périssable
    Et une commande qui date d'il y a 5 jours
    Étant donné que il y a 10 "Stylo" en stock
    Et 5 "Stylo" dans la commande
    Lorsque l'Employé retire les articles de la commande
    Alors les articles sont retirés de la commande
    Et il doit y avoir 15 "Stylo" en stock

  Scénario: L'Employé remet en stock un produit non périssable dans un nouveau stock
    Soit un article "Stylo" non périssable
    Et une commande qui date d'il y a 5 jours
    Étant donné que il n'y a pas de stock de "Stylo"
    Et 5 "Stylo" dans la commande
    Lorsque l'Employé retire les articles de la commande
    Alors les articles sont retirés de la commande
    Et un nouveau stock de 5 "Stylo" est crée

  Scénario: L'Employé essaie de traiter une commande de plus de 7 jours, mais ne peut pas
    Soit un article "Stylo" non périssable
    Et une commande qui date d'il y a 10 jours
    Et 5 "Stylo" dans la commande
    Lorsque l'Employé retire les articles de la commande
    Alors il y a une erreur

  Scénario: L'Employé retire et détruit un produit périssable de la commande
    Soit un article "Lait" périssable
    Et une commande qui date d'il y a 5 jours
    Et 1 "Lait" dans la commande
    Lorsque l'Employé retire les articles de la commande
    Alors les articles sont retirés de la commande
    Et ils sont détruits
