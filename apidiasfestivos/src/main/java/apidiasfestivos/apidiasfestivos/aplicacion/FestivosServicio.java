package apidiasfestivos.apidiasfestivos.aplicacion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import apidiasfestivos.apidiasfestivos.core.interfaces.repositorio.IFestivoRepositorio;
import apidiasfestivos.apidiasfestivos.core.interfaces.servicio.IFestivoServicio;
import apidiasfestivos.apidiasfestivos.dominio.DTOs.tablaFestivosDto;
import apidiasfestivos.apidiasfestivos.dominio.entidades.Festivo;

@Service
public class FestivosServicio implements IFestivoServicio {
    private IFestivoRepositorio repositorio;

    public FestivosServicio(IFestivoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public String EsFestivo(int año, int mes, int dia) {
        try {
            if (año<0){
                return "la fecha no es valida";
            }
            LocalDate fecha = LocalDate.of(año, mes, dia);
            boolean esTipo1 = repositorio.esFestivoTipo1(dia, mes);
            LocalDate domingoDePascua = this.obtenerDomingoDePascua(año);

            if (esTipo1) {
                return "El dia es festivo";
            }

            List<Festivo> festivosLeyPuente = repositorio.obtenerFestivosPorTipo("Ley Puente Festivo");
    
            for (Festivo festivo : festivosLeyPuente) {
                LocalDate fechaFestivo = LocalDate.of(año, festivo.getMes(), festivo.getDia());    
                if (fechaFestivo.getDayOfWeek() == DayOfWeek.MONDAY) {
                    if (fechaFestivo.equals(fecha)) {
                        return "El dia es festivo";
                    }
                } else {
                    LocalDate siguienteLunes = fechaFestivo.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                    if (siguienteLunes.equals(fecha)) {
                        return "El dia es festivo";
                    }
                }
            }
            List<Festivo> festivosDomingoPascua = repositorio.obtenerFestivosPorTipo("Basado en Pascua");
            for (Festivo festivo : festivosDomingoPascua) {
                LocalDate fechaFestivoDomingoPascua=domingoDePascua.plusDays(festivo.getDiasPascua());
                if (fechaFestivoDomingoPascua.equals(fecha)){

                    return "El dia es festivo";
                }
            }
            List<Festivo> festivosDomingoPascuaYPuenteFestivo = repositorio.obtenerFestivosPorTipo("Basado en Pascua y Ley Puente Festivo");
            for (Festivo festivo : festivosDomingoPascuaYPuenteFestivo) {
                LocalDate fechaFestivoPascuaYLeyPuente = domingoDePascua.plusDays(festivo.getDiasPascua());
                if (fechaFestivoPascuaYLeyPuente.getDayOfWeek()==DayOfWeek.MONDAY){
                    if (fechaFestivoPascuaYLeyPuente.equals(fecha)){
                        return "El dia es festivo";
                    }
                }
                else{
                    LocalDate siguienteLunes = fechaFestivoPascuaYLeyPuente.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                    if (siguienteLunes.equals(fecha)) {
                        return "El dia es festivo";
                    }
                }
            }
            
            return "No es un dia festivo";
        } catch (DateTimeParseException e) {
            return "La fecha ingresada no es válida. Por favor verifica los valores proporcionados.";
        }
    }
    
    @Override
    public LocalDate obtenerDomingoDePascua(int año) {
        int a = año % 19;
        int b = año / 100;
        int c = año % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mes = (h + l - 7 * m + 114) / 31; 
        int dia = ((h + l - 7 * m + 114) % 31) + 1;
    
        return LocalDate.of(año, mes, dia);
    }

    @Override
    public List<tablaFestivosDto> mostrarFestivos(int año) {
        List<tablaFestivosDto> listaFestivosDto = new ArrayList<>();

        try {
            if (año < 0) {
                throw new IllegalArgumentException("El año no puede ser negativo");
            }
            LocalDate domingoDePascua = this.obtenerDomingoDePascua(año);
            List<Festivo> todosLosFestivos = this.repositorio.findAll();
            
            for (Festivo festivo : todosLosFestivos) {
                LocalDate fechaFestivo = null;
                String tipoFestivo = this.repositorio.obtenerTipoPorNombreFestivo(festivo.getNombre());
                if (tipoFestivo.equals("Fijo")){
                    fechaFestivo = LocalDate.of(año, festivo.getMes(), festivo.getDia());
                }
                else if(tipoFestivo.equals("Ley Puente Festivo")){
                    LocalDate fechaPuentes = LocalDate.of(año, festivo.getMes(), festivo.getDia());
                    if (fechaPuentes.getDayOfWeek() == DayOfWeek.MONDAY) {
                        fechaFestivo=fechaPuentes;
                    } 
                    else {
                        fechaFestivo=fechaPuentes.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                    }    
                }
                else if(tipoFestivo.equals("Basado en Pascua")){
                    LocalDate fechaFestivoDomingoPascua=domingoDePascua.plusDays(festivo.getDiasPascua());
                    fechaFestivo=fechaFestivoDomingoPascua;
                }
                else if(tipoFestivo.equals("Basado en Pascua y Ley Puente Festivo")){
                    LocalDate fechaFestivoDomingoPascua=domingoDePascua.plusDays(festivo.getDiasPascua());
                    if(fechaFestivoDomingoPascua.getDayOfWeek()==DayOfWeek.MONDAY){
                        fechaFestivo = fechaFestivoDomingoPascua;
                    }
                    else{
                        fechaFestivo = fechaFestivoDomingoPascua.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                    }
                }
                if (fechaFestivo != null) {
                    tablaFestivosDto festivoDto = new tablaFestivosDto(
                            festivo.getId(),
                            festivo.getNombre(),
                            fechaFestivo
                    );
                    listaFestivosDto.add(festivoDto);
                }
                }
            }
            catch (Exception e) {
                System.err.println("Error al calcular los festivos: " + e.getMessage());
                // Puedes lanzar la excepción o devolver una lista vacía
            }
            return listaFestivosDto;

    
            }
        
}
    


