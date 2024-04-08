package com.stadtmeldeapp.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(

@Valid

                @NotBlank(message = "Nutzername darf nicht leer sein")
                @Size(min = 3, max = 20, message = "Ungültiger Nutzername")
                String username,

                @NotBlank(message = "Passwort darf nicht leer sein")
                @Size(min = 6, max = 20, message = "Ungültiges Passwort")
                String password) {
}