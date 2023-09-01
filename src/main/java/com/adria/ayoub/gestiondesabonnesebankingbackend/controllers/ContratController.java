package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.ContratService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequestMapping("/api/contrats")
public class ContratController {

    private final int PAGE_SIZE = 10;

    private ContratService contratService;

    public ContratController(ContratService contratService){
        this.contratService = contratService;
    }

    /**
     * Get request pour chercher/filtrer des contrats
     * @param search pour identifier quelle partie de recherche (intitule, statut ou abonne)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort filtre (field et direction)
     * @return List des contrats  si existe et le statut ok, no content ou server error, et d'autre info comme page, totalpages...
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getLesContratsPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String val,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        if(page < 0){
            page = 0;
        }

        try {
            List<Sort.Order> orders = getOrdersFromSortParam(sort);

            Pageable pagingSort = PageRequest.of(page, PAGE_SIZE, Sort.by(orders));

            Page<Contrat> pageContrats;
            if (search == null || val == null){
                pageContrats = contratService.trouverTousLesContrats(pagingSort);
            }else {
                pageContrats = contratService.trouverUneListeDesContrats(search,val, pagingSort);
            }

            List<Contrat> contrats = pageContrats.getContent();

            if (contrats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("contrats", contrats);
            response.put("page", pageContrats.getNumber());
            response.put("totalDesContrats", pageContrats.getTotalElements());
            response.put("totalDesPages", pageContrats.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get request pour trouver un seul contrat
     * @param id du contrat
     * @return ResponseEntity<Contrat>
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contrat> getUnSeulContrat(@PathVariable Long id){
        try{
            Contrat contrat = contratService.trouverUnContratById(id).get();
            return new ResponseEntity<>(contrat,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Post request pour ajouter un contrat
     * @param contrat
     * @return ResponseEntity<Contrat>
     */
    @PostMapping
    public ResponseEntity<Contrat> ajouterUnContrat(@RequestBody Contrat contrat) {
        try {
            Contrat _contrat = contratService.ajouterContrat(new Contrat(null,contrat.getIntitule(), contrat.getStatut(),contrat.getAbonne(),contrat.getOffres()));
            return new ResponseEntity<>(_contrat, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put request pour modifier un offre
     * @param id de l'offre à modifier
     * @param contrat fournit par l'utilisateur
     * @return ResponseEntity<Contrat>
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contrat> modifierUnContrat(@PathVariable("id") Long id, @RequestBody Contrat contrat) {
        Optional<Contrat> contratOptional = contratService.trouverUnContratById(id);

        if (contratOptional.isPresent()) {
            Contrat _contrat = contratOptional.get();
            _contrat.setIntitule(contrat.getIntitule());
            _contrat.setStatut(contrat.getStatut());
            if(contrat.getAbonne() != null){
                _contrat.setAbonne(contrat.getAbonne());
            }
            if(contrat.getOffres() != null){
                _contrat.setOffres(contrat.getOffres());
            }
            return new ResponseEntity<>(contratService.ajouterContrat(_contrat), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete request pour supprimer un contrat
     * @param id du contrat à supprimer
     * @return ResponseEntity<HttpStatus>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnContrat(@PathVariable("id") Long id) {
        try {
            contratService.supprimerContratById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete request pour supprimer tous les contrats
     * @return ResponseEntity<HttpStatus>
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesContrats() {
        try {
            contratService.supprimerTousLesContrats();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put request pur changer le statut d'un contrat
     * @param id de contrat
     * @param requestBody contient le nouveau statut envoyé par l'utilisateur
     * @return un objet de type Contrat
     */
    @PutMapping("{id}/statut")
    public ResponseEntity<Contrat> changerLeStatutDuContrat(@PathVariable Long id, @RequestBody String requestBody){
        Contrat contrat = contratService.trouverUnContratById(id).get();

        String statutString = requestBody.replaceAll("\"", "").trim();

        Statut statut = Statut.valueOf(statutString.toUpperCase());

        contrat.setStatut(statut);

        return new ResponseEntity<>(contratService.ajouterContrat(contrat), HttpStatus.OK);
    }

    /**
     * Put request pour ajouter un offre à un contrat
     * @param id du contrat
     * @param offre_id
     * @return ResponseEntity<Contrat>, Ok si ajouté, Not ok si deja existe
     */
    @PutMapping("{id}/offres/{offre_id}")
    public ResponseEntity<Contrat> ajouterUnOffreAUnContrat(@PathVariable Long id, @PathVariable Long offre_id){
        Contrat contrat = contratService.trouverUnContratById(id).get();
        Offre offre = contratService.trouverUnOffreById(offre_id).get();

        if(contrat != null && offre != null){
            if(contrat.ajouterOffre(offre)){
                return new ResponseEntity<>(contratService.ajouterContrat(contrat), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Put request pour modifier un contrat par elemination d'un offre
     * @param id du contrat
     * @param offre_id
     * @return ResponseEntity<Contrat>
     */
    @PutMapping("{id}/offres/{offre_id}/retirer")
    public ResponseEntity<Contrat> retirerUnOffreDansUnContrat(@PathVariable Long id, @PathVariable Long offre_id){
        Contrat contrat = contratService.trouverUnContratById(id).get();
        Offre offre = contratService.trouverUnOffreById(offre_id).get();

        if(contrat != null && offre != null){
            contrat.retirerOffre(offre);
            return new ResponseEntity<>(contratService.ajouterContrat(contrat), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Put request pour retirer tous les offres d'un contrat
     * @param id du contrat
     * @return ResponseEntity<Contrat>
     */
    @PutMapping("{id}/retirer_tous_les_offres")
    public ResponseEntity<Contrat> retirerTousLesOffreDansUnContrat(@PathVariable Long id){
        Contrat contrat = contratService.trouverUnContratById(id).get();

        if(contrat != null){
            contrat.retirerTousLesOffres();
            return new ResponseEntity<>(contratService.ajouterContrat(contrat), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Methode pour obtiens une liste des ordres pour les filtres
     * @param sort de type String[]
     * @return une liste des Order
     */
    private List<Sort.Order> getOrdersFromSortParam(String[] sort){
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        return orders;
    }

    /**
     * Methode pour obtenir la direction de Sort (pour convertir la deriction de String vers Direction)
     * @param direction de type String
     * @return soit Direction.ASC ou Direction.DESC
     */
    private Sort.Direction getSortDirection(String direction){
        if(direction.equals("desc")){
            return Sort.Direction.DESC;
        }else{
            return Sort.Direction.ASC;
        }
    }
}
