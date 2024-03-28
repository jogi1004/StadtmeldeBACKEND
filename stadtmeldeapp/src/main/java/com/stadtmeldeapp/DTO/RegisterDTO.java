package com.stadtmeldeapp.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "Nutzername darf nicht leer sein") String username,

        @NotBlank(message = "Email darf nicht leer sein") @Email(message = "Ungültiges E-Mail-Format") String email,

        @NotBlank(message = "Passwort darf nicht leer sein") @Size(min = 6, max = 20, message = "Passwort muss zwischen 6 und 20 Zeichen lang sein") String password) {
}
