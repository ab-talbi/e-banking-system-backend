package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AbonneRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import org.springframework.stereotype.Service;

@Service
public class AbonneServiceImpl implements AbonneService {

    private AbonneRepository abonneRepository;

    public AbonneServiceImpl(AbonneRepository abonneRepository){
        this.abonneRepository = abonneRepository;
    }


}
