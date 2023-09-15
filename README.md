# Gestion des abonnes e-banking, leurs contrats d'abonnement et les offres commerciales - Partie Backend

Cette partie est développé avec Spring boot, contient trois services : AbonneService, ContratService et OffreService.
</br>
Ce projet de gestion des abonnés et leurs contrats est développé pour le but d'aider l'agent backoffice de bien gérer les abonnés, faire des operations create, update, delete, faire des recherches par différents attributs, ainsi que le tri avec n'importe quel attribut.
</br>
## Les APIs
VoICi quelques REST endpoints définies :
### ```AbonneController``` en **port 8089**

* GET des abonnés
```
http://localhost:8089/api/abonnes

```
```
http://localhost:8089/api/abonnes/1

```
```
http://localhost:8089/api/abonnes?search=nom&val=Zad&page=1&sort=nom,desc

```

* POST un abonné

```
http://localhost:8089/api/abonnes

{
    "nom":"Saadani",
    "prenom":"Brahim",
    "email":"brahim@gmail.com",
    "adresse":"852, Av Gueliz, Marrkech",
    "telephone":"+212611111111",
    "sexe":"homme",
    "statut":"actif"
}

```
```
http://localhost:8089/api/abonnes

{
    "nom":"LAMIN",
    "prenom":"Amina",
    "email":"amina@hotmail.fr",
    "adresse":"986, bd Rachidi , Lusitania CASABLANCA",
    "telephone":"00212500101212",
    "sexe":"FEMME",
    "statut":"ACTIF",
    "contratId":1
}

```
```
http://localhost:8089/api/abonnes

{
    "nom":"KAMAL",
    "prenom":"Ahmed",
    "email":"ahmed@gmail.com",
    "adresse":"12, bd Dar Tounsi, Marrakech",
    "telephone":"00212798989898",
    "sexe":"Homme",
    "statut":"ACTIF",
    "contratId":3,
    "agenceId":3,
    "backOfficeId":3
}

```

* PUT (modifier) un abonné
```
http://localhost:8089/api/abonnes/1

{
    "nom":"SAADANI",
    "prenom":"Brahim",
    "email":"brahim@yahoo.fr",
    "adresse":"852, Av Gueliz, Marrkech",
    "telephone":"00212622222222",
    "sexe":"HOMME",
    "statut":"Suspendu",
    "contratId":1,
    "agenceId":1,
    "backOfficeId":1
}
```
* PUT changer statut d'un abonné
```
http://localhost:8089/api/abonnes/1/statut

"ACTIF"
```

* PUT associer une agence (3) à un abonné (2)
```
http://localhost:8089/api/abonnes/2/agence/3

```

* PUT associer un backOffice (3) à un abonné (2)
```
http://localhost:8089/api/abonnes/2/backoffice/3

```

* PUT associer un contrat (2) à un abonné (2)
```
http://localhost:8089/api/abonnes/2/contrat/2

```

* PUT disassocier l'agence de l'abonné (1)
```
http://localhost:8089/api/abonnes/1/retirer_agence

```

* PUT disassocier le backoffice de l'abonné (1)
```
http://localhost:8089/api/abonnes/1/retirer_backoffice

```
* PUT disassocier le contrat de l'abonné (1)
```
http://localhost:8089/api/abonnes/1/retirer_contrat

```

* DELETE supprimer l'abonné (1)
```
http://localhost:8089/api/abonnes/1

```

* DELETE supprimer tous les abonnés
```
http://localhost:8089/api/abonnes

```

### ```ContratController``` en **port 8089**

* GET des contrats
```
http://localhost:8089/api/contrats

```
```
http://localhost:8089/api/contrats/1

```
```
http://localhost:8089/api/contrats?search=abonne&val=ahm&page=0&sort=intitule,desc

```

* POST un contrat

```
http://localhost:8089/api/contrats

{
    "intitule":"CONTRAT 1",
    "statut":"ACTif"
}

```
```
http://localhost:8089/api/contrats

{
    "intitule":"CONTRAT 2",
    "statut":"actif",
    "abonneId":2
}

```
```
http://localhost:8089/api/contrats

{
    "intitule":"CONTRAT 3",
    "statut":"ACTIF",
    "abonneId":3,
    "offresIds":[1,2,3]
}

```

* PUT (modifier) un contrat
```
http://localhost:8089/api/contrats/1

{
    "intitule":"contrat 1 modifié",
    "statut":"SUSPENDU"
}
```

```
http://localhost:8089/api/contrats/2

{
    "intitule":"contrat 2 modifié",
    "statut":"ACTIF",
    "abonneId":1,
    "offresIds":[1]
}
```
* PUT changer le statut d'un contrat
```
http://localhost:8089/api/contrats/1/statut

"ACTIF"
```

* PUT ajouter un offre (1) au contrat (2)
```
http://localhost:8089/api/contrats/2/offres/1

```

* PUT retirer un offre (1) du contrat (2)
```
http://localhost:8089/api/contrats/2/offres/1/retirer

```

* PUT retirer tous les offres d'un contrat (3)
```
http://localhost:8089/api/contrats/3/retirer_tous_les_offres

```

* DELETE supprimer un contrat (1)
```
http://localhost:8089/api/contrats/1

```

* DELETE supprimer tous les contrats
```
http://localhost:8089/api/contrats

```

### ```OffreController``` en **port 8089**

* GET des offres
```
http://localhost:8089/api/offres

```
```
http://localhost:8089/api/offres/1

```
```
http://localhost:8089/api/offres?search=description&val=ffr&page=0&sort=description,asc

```

* POST un offre

```
http://localhost:8089/api/offres

{
    "libelle":"offre",
    "description":"description de l'offre"
}

```

* PUT (modifier) un offre
```
http://localhost:8089/api/offres/1

{
    "libelle":"offre modifié",
    "description":"description de l'offre modifié"
}
```
* DELETE supprimer un offre (1)
```
http://localhost:8089/api/offres/1

```

* DELETE supprimer tous les offres
```
http://localhost:8089/api/offres

```
