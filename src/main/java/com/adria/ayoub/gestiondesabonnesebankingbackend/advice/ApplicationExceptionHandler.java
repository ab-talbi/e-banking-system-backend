package com.adria.ayoub.gestiondesabonnesebankingbackend.advice;

import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyExistsException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * Pour la gestion de l'éxception de validation faild MethodArgumentNotValidException
     * @param methodArgumentNotValidException
     * @return Map<String,String> contiens tous les erreurs <field,message>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String,String> erreur = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(e -> {
            erreur.put(e.getField(),e.getDefaultMessage());
        });
        return erreur;
    }

    /**
     * Pour la gestion de NotFoundException
     * @param notFoundException
     * @return Map<String,String> <erreur,message>
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String,String> handleNotFoundException(NotFoundException notFoundException){
        Map<String,String> erreur = new HashMap<>();
        erreur.put("erreur",notFoundException.getMessage());
        return erreur;
    }

    /**
     * Pour la gestion de AlreadyExistsException
     * @param alreadyExistsException
     * @return Map<String,String>
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    public Map<String,String> handleAlreadyExistsException(AlreadyExistsException alreadyExistsException){
        Map<String,String> erreur = new HashMap<>();
        erreur.put("erreur",alreadyExistsException.getMessage());
        return erreur;
    }
}
