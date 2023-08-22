package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/abonne")
public class AbonneController {

    private AbonneService abonneService;

    public AbonneController(AbonneService abonneService){
        this.abonneService = abonneService;
    }
}
