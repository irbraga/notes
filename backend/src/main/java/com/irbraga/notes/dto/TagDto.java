package com.irbraga.notes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TagDto(@NotNull @Size(min = 1, max = 50) String name) {
    
}
