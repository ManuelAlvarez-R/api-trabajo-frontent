package apidiasfestivos.apidiasfestivos.presentacion;

import org.springframework.web.bind.annotation.RestController;

import apidiasfestivos.apidiasfestivos.core.interfaces.servicio.IFestivoServicio;
import apidiasfestivos.apidiasfestivos.dominio.DTOs.tablaFestivosDto;
import apidiasfestivos.apidiasfestivos.dominio.entidades.Festivo;

import java.time.DateTimeException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/festivos")
@CrossOrigin(origins = "http://localhost:4200/")
public class FestivosControlador {
    //inyeccion//
    private IFestivoServicio servicio;
    public FestivosControlador(IFestivoServicio servicio) {
        this.servicio = servicio;
    }
    //fin de inyeccion//
      
    @RequestMapping(value = "/verificar/{año}/{mes}/{dia}", method = RequestMethod.GET)
    public ResponseEntity<String> EsFestivo(@PathVariable int año, @PathVariable int mes, @PathVariable int dia) {
        try {
            String resultado = servicio.EsFestivo(año, mes, dia);
            return ResponseEntity.ok(resultado); 
        } catch (DateTimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("La fecha ingresada no es válida. Por favor, verifica los valores proporcionados.");
        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado. Por favor intenta nuevamente.");
        }
    }
    
    @RequestMapping(value = "/obtener/{año}", method=RequestMethod.GET)
    public ResponseEntity<List<tablaFestivosDto>> mostrarFestivos(@PathVariable int año){
        try{
            List<tablaFestivosDto> listaFestivos = servicio.mostrarFestivos(año);
            return ResponseEntity.ok(listaFestivos);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    }
    


    

