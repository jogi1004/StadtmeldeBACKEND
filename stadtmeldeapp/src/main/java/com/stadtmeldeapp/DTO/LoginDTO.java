package com.stadtmeldeapp.DTO;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "Nutzername darf nicht leer sein") String username,
        @NotBlank(message = "Passwort darf nicht leer sein") String password) {
}