package com.ucu.topicos.controllers;


import com.ucu.topicos.services.ProviderService;
import dtos.ProvidersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping
    public ResponseEntity<Object> getProviders(
            @RequestParam (required = false) String nombre,
            @RequestParam (required = false) String rut,
            @RequestParam (required = false) Integer puntajeDesde,
            @RequestParam (required = false) Integer puntajeHasta,
            @RequestParam (required = false) Integer offset
    ){
        try{
            ProvidersResponse response = this.providerService.getProviders(nombre, rut, puntajeDesde, puntajeHasta, offset);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

}
