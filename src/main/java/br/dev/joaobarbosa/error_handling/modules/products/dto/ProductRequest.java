package br.dev.joaobarbosa.error_handling.modules.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record ProductRequest (
    @NotBlank
    @NotNull
    String name,

    @NotNull
    BigDecimal price
) {}
