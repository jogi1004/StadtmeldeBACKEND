package com.stadtmeldeapp.usermanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

public record RegisterDTO(
    @NotBlank(message = "Nutzername darf nicht leer sein")
    String username, 

    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 6, max = 20, message = "Passwort muss zwischen 6 und 20 Zeichen lang sein")
    String password, 

    @NotBlank(message = "Vorname darf nicht leer sein")
    String firstname, 

    @NotBlank(message = "Nachname darf nicht leer sein")
    String lastname, 

    @NotBlank(message = "Straße darf nicht leer sein")
    String street, 

    @Min(value = 1, message = "Hausnummer darf nicht kleiner als 1 sein")
    @Max(value = 9999, message = "Hausnummer darf nicht größer als 9999 sein")
    int housenumber, 

    @NotBlank(message = "Postleitzahl darf nicht leer sein")
    @Pattern(regexp = "\\d{5}", message = "Die Postleitzahl muss 5 Stellen lang sein")
    String zipcode, 

    @NotBlank(message = "Stadt darf nicht leer sein")
    String city) {   
}
