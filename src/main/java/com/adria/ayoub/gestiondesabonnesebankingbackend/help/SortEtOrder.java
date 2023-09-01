package com.adria.ayoub.gestiondesabonnesebankingbackend.help;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortEtOrder {

    /**
     * Methode pour obtiens une liste des ordres pour les filtres
     * @param sort de type String[]
     * @return une liste des Order
     */
    public static List<Sort.Order> getOrdersFromSortParam(String[] sort){
        List<Sort.Order> orders = new ArrayList<>();

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
    private static Sort.Direction getSortDirection(String direction){
        if(direction.equals("desc")){
            return Sort.Direction.DESC;
        }else{
            return Sort.Direction.ASC;
        }
    }
}
