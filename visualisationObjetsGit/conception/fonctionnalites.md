# Projet JAVA
### Cahier des charges

## Fonctionnalités recherchées :

- visualisation brute des fichiers git
  - ( en particulier .git/objects )

- affichage de l'ensemble des clés de hachage

- navigation entre les fichiers et affichage du contenu
( voir [git cat-file](https://git-scm.com/docs/git-cat-file) )

- pas un outil d'historique ou de diff




## Dossiers  .git de base :

1. ) dossier branches :
  - ce dossier n'est pas utilisé par les versions récentes de git
  - (nous allons donc l'ignorer)

2. ) fichier config :
  - contient les options de configuration spécifiques au projet git.

3. ) fichier description :
  - utilisé par GitWeb
  - (nous ignorerons ce fichier)

4. ) fichier HEAD :
  * ce fichier pointe sur la branche qui est couramment checked out (sur laquelle on travaille)

5. ) dossier hooks :
  - contains your client- or server-side hook scripts
  - ces scripts sont exécutés lorsques certaines actions importantes se produisent
  - (commit, merge, push, …)
  - (ce dossier sera ignoré)

6. ) dossier info :
  - contient un fichier « exclude » global pour les fichiers qu'on ne veut pas suivre dans un .gitignore

7. ) dossier objects :
  - C'est dans le répertoire objects/ que sont stockés tous les objets Git (commits, tags, trees, blobs)
  - **Les objects sont compressés avec zlib**.
  - (**Notre dossier principal**)
  - commandes pour test :
    - echo 'test content' | git [hash-object](https://git-scm.com/docs/git-hash-object) -w --stdin
      - (stocke un fichier contenant le texte 'test content' et renvoie la clé de hachege)
    - git [hash-object](https://git-scm.com/docs/git-hash-object) -w file.txt
      - (stocke le fichier(option w) file.txt et renvoie la clé de hachege)
    - git [cat-file](https://git-scm.com/docs/git-cat-file) -p [clé de hachage]
      - affiche le contenu de l'objet correspondant à la clé de hachage
      - exemple : git [cat-file](https://git-scm.com/docs/git-cat-file) -p d670460b4b4aece5915caf5c68d12f560a9fe3e4 => test content
  - dossier info :
    -
  - dossier pack :
    -
  - **plusieurs dossiers nommés d'après les 2 premiers caractères de l'empreinte :**
    - les fichier à l'interieur sont nommés d'après les 38 autres caractères
    - (ex : d6/70460b4b4aece5915caf5c68d12f560a9fe3e4 => d670460b4b4aece5915caf5c68d12f560a9fe3e4)
    - ce dossier et fichier résultent de la commande echo 'test content' | git [hash-object](https://git-scm.com/docs/git-hash-object) -w --stdin
  - **Il y a 4 types d'objets qui nous interessent :**
    - **Le blob**
    - **l'arbre**
    - **le commit**
    - **le tag**

  ### I. BLOB

      Git est adressable par le contenu :
      les fichiers ne sont pas stockés en fonction de leur nom, mais plutôt par le hachage des données qu'ils contiennent, dans un fichier que nous appelons un objet blob.
      Les fichiers dans .git/objects sont compressées avec zlib de sorte que vous ne devez pas regarder directement.

  ### II. ARBRE

      L'objet tree
      (mot anglais signifiant « arbre »),
      est une liste d'objets de type blobs et des informations associées à chaque blob, tel que le nom du fichier et les permissions.
      Cet objet décrit l'arborescence des sources à un instant donné.

  ### III. COMMIT

      L'objet commit, résultant de l'opération du même nom
      (de l'anglais, « commettre », « perpétrer », « s'engager », « décider », ici dans le sens de « confirmer », « valider »)
      donne accès à l'historique d'une arborescence de source.
      Il contient un message de log, un objet arbre et pointe vers un ou plusieurs objets commit parents.

  ### IV. TAG

      L'objet tag
      (étiquette)
      est une manière de représenter un commit spécifique.
      Il est en général utilisé pour marquer certains commits, par exemple par un numéro ou un nom de version (2.1 ou bien Hardy Heron).

  ### V. STOCKAGE DES DOSSIERS

      Git ne comporte qu'un seul dossier appelé ".git" à la racine de votre projet :
      ce dernier contient tout l'historique du projet.


8. ) dossier refs :
  - pointeurs vers des objets commit

9. ) fichier index :
  - informations de staging (git add …. )




## Maquettes :

![maquette1](Maquette.PNG "maquette 1")
![maquette2](Maquette2.PNG "maquette 2")
