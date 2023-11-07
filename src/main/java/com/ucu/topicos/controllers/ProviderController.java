package com.ucu.topicos.controllers;


import com.ucu.topicos.services.ProviderService;
import dtos.Provider;
import dtos.ProviderRequest;
import dtos.ProvidersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/search")
    public ResponseEntity<Object> getProviders(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String rut,
            @RequestParam (required = false) Integer scoreFrom,
            @RequestParam (required = false) Integer scoreTo,
            @RequestParam (required = false) Integer offset
    ){
        try{
            ProvidersResponse response = this.providerService.getProviders(name, rut, scoreFrom, scoreTo, offset);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addProvider (@RequestBody ProviderRequest dto){
        try{
            this.providerService.addProvider(dto);
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{rut}")
    public ResponseEntity<Object> getProviders(
            @PathVariable String rut){
        try{
            Provider response = this.providerService.getProvider(rut);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

}
