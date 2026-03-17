package com.nanochat.taxengine.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public record ComputeTaxRequest(
        @NotBlank String taxpayerId,
        @NotNull Integer age,
        @NotBlank String residencyStatus,
        @NotBlank String regime,
        @NotBlank String financialYear,
        @NotBlank String assessmentYear,
        Map<String, BigDecimal> facts
) {
}
