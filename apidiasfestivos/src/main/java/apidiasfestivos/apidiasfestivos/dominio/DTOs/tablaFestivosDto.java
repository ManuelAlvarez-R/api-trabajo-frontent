package apidiasfestivos.apidiasfestivos.dominio.DTOs;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class tablaFestivosDto {
@Id
private int id;
private String nombrefestivo;
private LocalDate diacorrespondiente;
public tablaFestivosDto() {
}
public tablaFestivosDto(int id, String nombrefestivo, LocalDate diacorrespondiente) {
    this.id = id;
    this.nombrefestivo = nombrefestivo;
    this.diacorrespondiente = diacorrespondiente;
}
public int getId() {
    return id;
}
public void setId(int id) {
    this.id = id;
}
public String getNombreFestivo() {
    return nombrefestivo;
}
public void setNombreFestivo(String nombrefestivo) {
    this.nombrefestivo = nombrefestivo;
}
public LocalDate getDiaCorrespondiente() {
    return diacorrespondiente;
}
public void setDiaCorrespondiente(LocalDate diacorrespondiente) {
    this.diacorrespondiente = diacorrespondiente;
}


}
