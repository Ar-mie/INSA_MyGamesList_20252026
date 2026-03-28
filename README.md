# Projet Programmation Mobile - INSA 4INFO (2025/2026)


## Projet d'Emie Arlin et Arthur Coquet

Ce projet implémente tous les TPs obligatoires.

En terme de bonus, nous avons réalisé la consolidation de notre modèle de données et un bouton filtrant les favoris pour n'afficher que les jeux mis en favoris.

*Collaborateurs :* Emie Arlin et Arthur Coquet

## Détails des choix de design

### Consolidation des données
En ce qui concerne la consolidation des données, notre projet utilise la classe GameComplet pour un accès plus pertinent aux données d'un jeu.
En effet, contrairement à la classe Game qui ne peut contenir que des String, des Long et des listes de Long, un objet GameComplet peut contenir directement la liste de Genres ainsi que la liste de Plateforme du jeu, ce qui évite de devoir aller chercher les informations dans plusieurs Maps différentes à partir d'un ID.
Tout accès à nos données de jeu se font donc à partir d'une Map regroupant des GamesComplets.

### Bouton de filtre sur les favoris
Lorsque nous avons effectué le TP de mise en favori (TP 6), nous avons pensé qu'il serait pertinent d'avoir un bouton global pour n'afficher que les jeux en favori.
Ainsi, le bouton à gauche du bouton de recherche représentant une étoile pleine dans la barre du haut sert à cela. 
L'utilisateur peut également appuyer de nouveau sur l'icône pour effacer la séléction et revenir à la liste complète des jeux.
Les autres boutons favoris fonctionnent normalement comme attendu dans les spécifications du TP 6.

### Barre de recherche
La barre de recherche apparait lorsque l'utilisateur clique sur le bouton recherche (petite loupe en haut à droite). 
Nous avons fait le choix de ne pas ajouter de bouton retour lorsque la barre de recherche est affichée, mais de pouvoir cliquer de nouveau sur le bouton loupe pour la faire disparaître.
Lorsque la barre de recherche disparaît, la selection en cours ne disparaît pas et le filtre est toujours actif, comme attendu dans les spécifications du TP.
