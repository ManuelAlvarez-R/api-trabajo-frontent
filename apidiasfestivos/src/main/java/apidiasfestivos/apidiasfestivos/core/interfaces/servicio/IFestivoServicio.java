package apidiasfestivos.apidiasfestivos.core.interfaces.servicio;

import java.time.LocalDate;
import java.util.List;

import apidiasfestivos.apidiasfestivos.dominio.DTOs.tablaFestivosDto;


public interface IFestivoServicio {

//metodo de comprobar si es festivo//
public String EsFestivo(int año, int mes, int dia);
//metodo de obtener el domingo de pascua//
public LocalDate obtenerDomingoDePascua(int año);
public List<tablaFestivosDto> mostrarFestivos(int año);

}
