package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.OffreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequestMapping("/api/offres")
public class OffreController {

    private final int PAGE_SIZE = 10;

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
    public ResponseEntity<Map<String, Object>> getTousLesOffresPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String val,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        if(page < 0){
            page = 0;
        }

        try {
            List<Order> orders = getOrdersFromSortParam(sort);

            Pageable pagingSort = PageRequest.of(page, PAGE_SIZE, Sort.by(orders));

            Page<Offre> pageOffres;
            if (search == null || val == null){
                pageOffres = offreService.trouverTousLesOffres(pagingSort);
            }else {
                pageOffres = offreService.trouverUneListeDesOffres(search,val, pagingSort);
            }

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
     * methode Post pour la creation d'un offre
     * @param offre entity
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Offre> ajouterUnOffre(@RequestBody Offre offre) {
        try {
            Offre _offre = offreService.ajouterOffre(new Offre(null,offre.getLibelle(), offre.getDescription(),null));
            return new ResponseEntity<>(_offre, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put methode pour modifier un offre
     * @param id de l'offre
     * @param offre l'objet envoyé par le client
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Offre> modifierUnOffre(@PathVariable("id") Long id, @RequestBody Offre offre) {
        Optional<Offre> offreOptional = offreService.trouverUnOffreById(id);

        if (offreOptional.isPresent()) {
            Offre _offre = offreOptional.get();
            _offre.setLibelle(offre.getLibelle());
            _offre.setDescription(offre.getDescription());
            return new ResponseEntity<>(offreService.ajouterOffre(_offre), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete request pour supprimer un seul offre par l'id
     * @param id de l'offre à supprimer
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnOffre(@PathVariable("id") Long id) {
        try {
            offreService.supprimerOffreById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete request pour supprimer tous les offres
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesOffres() {
        try {
            offreService.supprimerTousLesOffres();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Methode pour obtiens une liste des ordres pour les filtres
     * @param sort de type String[]
     * @return une liste des Order
     */
    private List<Order> getOrdersFromSortParam(String[] sort){
        List<Order> orders = new ArrayList<Order>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        return orders;
    }

    /**
     * Methode pour obtenir la direction de Sort (pour convertir la deriction de String vers Direction)
     * @param direction de type String
     * @return soit Direction.ASC ou Direction.DESC
     */
    private Direction getSortDirection(String direction){
        if(direction.equals("desc")){
            return Direction.DESC;
        }else{
            return Direction.ASC;
        }
    }

}
