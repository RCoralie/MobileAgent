# MobileAgent
TP Applications Réparties - Application d’agents mobiles élémentaire


## Objectif 1 : Réalisation de LookForHotel avec le modèle RMI

L'application client est crée à partir des package Client + Common et l'application server est crée à partir des packages Server + Common.

#### Côté serveur :

##### Définition des interfaces contenant les méthodes distantes : _Annuaire et _Chaîne

Les interfaces doivent hériter de l'interface java.rmi.Remote. Ces interfaces doivent contenir toutes les spécifications des méthodes qui seront susceptibles d'être appelées à distance.

La communication entre le client et le serveur lors de l'invocation d'une méthode distante peut échouer pour diverses raisons (crash du serveur, rupture de la liaison, etc.) Ainsi chaque méthode appelée à distance doit déclarer qu'elle est en mesure de lever l'exception java.rmi.RemoteException.

##### Implémentation de ces interfaces : Annuaire & Chaîne

Ces classes correspondent à l'objet distant. Elles doivent donc implémenter l'interface définie et contenir le code nécessaire.

Ces classes doivent obligatoirement hériter de la classe UnicastRemoteObject qui contient les différents traitements élémentaires pour un objet distant dont l'appel par le stub du client est unique. 

##### Classe permettant d'instancier les objets et de les enregistrer dans un registre RMI : Server

Ces opérations peuvent être effectuées dans la méthode main d'une classe dédiée ou dans la méthode main de la classe de l'objet distant. Nous avons choisit de créer une classe Server dédié, afin de regrouper toutes ces opérations pour un ensemble d'objets distants.

La marche à suivre contient trois étapes :
 - la mise en place d'un security manager dédié : facultatif mais sans security manager, il faut obligatoirement mettre à la disposition du serveur toutes les classes dont il aura besoin (elles doivent être dans le CLASSPATH du serveur). Avec un security manager, le serveur peut charger dynamiquement certaines classes.
 - l'instanciation des objets de la classe distante
 - le lancement des registre de noms RMI : ce lancement ne doit avoir lieu qu'une seule et unique fois (pour chaque registre) et il doit avoir lieu avant de pouvoir enregistrer un objet ou obtenir une référence
 - l'enregistrement de la classe dans le registre de noms RMI correspondant


#### Côté client :

Les opérations côté client sont effectuées dans la méthode main d'une classe dédiée LookForHotels:

##### - la mise en place d'un security manager (facultative)
 
##### - l'obtention d'une référence sur l'objet distant : 
 
Pour obtenir une référence sur l'objet distant à partir de son nom, il faut utiliser la méthode statique lookup() de la classe Naming. Cette méthode attend en paramètre une URL indiquant le nom qui référence l'objet distant (cette URL est composée de plusieurs éléments : le préfix rmi://, le nom du serveur (hostname) et le nom de l'objet tel qu'il a été enregistré dans le registre précédé d'un slash). Si le nom fourni dans l'URL n'est pas référencé dans le registre, la méthode lève l'exception NotBoundException.

Si l'on récupère au préalable le registre avec la méthode getRegistry() et que l'on appel la méthode lookup() sur ce registre, nous n'avons plus besoin de renseigner l'URL complète, il suffit de renseigner le nom de l'objet tel qu'il a été enregistré dans le registre en question

Cette méthode lookup() va rechercher dans le registre du serveur l'objet et retourner un objet stub. L'objet retourné est de la classe Remote (cette classe est la classe mère de tous les objets distants). 

##### - l'appel à la méthode à partir de la référence sur l'objet distant

L'objet retourné étant de type Remote, il faut réaliser un cast vers l'interface qui définit les méthodes de l'objet distant. Une fois le cast réalisé, il suffit simplement d'appeler la méthode.


#### Package Common :

Le package common contient les classes partagées entre le serveur et le client, c'est à dire les remote interfaces (_Chaine & _Annuaire) et les objects serializable non-primitive (Hotels & Numero).

Les objets serializable sont les objets instances de classes implémentant l'interface Serializable. Ce sont des classes dont les objets peuvent être transcrits en "stream", c'est à dire en flots d'octets. La plupart des classes (et de leurs sous-classes) de base String, HashTable, Vector, HashSet, ArrayList etc. sont Serializable.



## Objectif 2 : 

