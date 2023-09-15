package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.OffreDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.OffreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/offres")
public class OffreController {

    private OffreService offreService;

    public OffreController(OffreService offreService){
        this.offreService = offreService;
    }

    /**
     * Get request pour chercher/filtrer des offres
     * @param search pour identifier quelle partie de recherche (libelle ou description)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort filtre (fiels et direction)
     * @return List des offres  si existe et le statut ok, no content ou server error, et d'autre info comme page totalpages...
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getLesOffresPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String val,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        try {
            Page<Offre> pageOffres = offreService.trouverLesOffres(search,val,page,sort);

            List<Offre> offres = pageOffres.getContent();

            if (offres.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("offres", offres);
            response.put("page", pageOffres.getNumber());
            response.put("totalDesOffres", pageOffres.getTotalElements());
            response.put("totalDesPages", pageOffres.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get request pour trouver un seul offre
     * @param id de l'offre
     * @return ResponseEntity<Offre>
     */
    @GetMapping("/{id}")
    public ResponseEntity<Offre> getUnSeulOffre(@PathVariable Long id) throws NotFoundException {
        Offre offre = offreService.trouverUnOffreById(id);
        return new ResponseEntity<>(offre,HttpStatus.OK);
    }

    /**
     * methode Post pour la creation d'un offre
     * @param offreDto dto
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Offre> ajouterUnOffre(@RequestBody @Valid OffreDto offreDto) {
        Offre offre = offreService.ajouterOffre(offreDto);
        return new ResponseEntity<>(offre, HttpStatus.CREATED);
    }

    /**
     * Put methode pour modifier un offre
     * @param id de l'offre
     * @param offreDto l'objet envoyé par le client
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Offre> modifierUnOffre(@PathVariable("id") Long id, @RequestBody @Valid OffreDto offreDto) throws NotFoundException {
        Offre offre = offreService.modifierOffre(id,offreDto);
        return new ResponseEntity<>(offre, HttpStatus.OK);
    }

    /**
     * Delete request pour supprimer un seul offre par l'id
     * @param id de l'offre à supprimer
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnOffre(@PathVariable("id") Long id) {
        offreService.supprimerOffreById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete request pour supprimer tous les offres
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesOffres() {
        offreService.supprimerTousLesOffres();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
