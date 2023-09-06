# Gestion des abonnes e-banking, leurs contrats d'abonnement et les offres commerciales - Partie Backend

Cette partie est développé avec Spring boot, contient trois services : AbonneService, ContratService et OffreService.
</br>
Ce projet de gestion des abonnés et leurs contrats est développé pour le but d'aider l'agent backoffice de bien gérer les abonnés, faire des operations create, update, delete, faire des recherches par différents attributs, ainsi que le tri avec n'importe quel attribut.
</br>
## Les APIs
VoICi quelsue REST endpoints défine dans :
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
