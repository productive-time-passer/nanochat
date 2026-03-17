# AI Tax Advisory - Tax Engine (Spring Boot)

Production-oriented deterministic tax engine implementing plugin-based primitives for Indian personal taxation.

## Implemented primitives and sample plugins

- Income: `SalaryIncomeClassificationPlugin`
- Exemption: `AllowanceExemptionPlugin` (HRA)
- Deduction: `InvestmentDeductionPlugin` (80C)
- Tax rate: `SlabTaxPlugin`
- Tax credit: `TdsCreditPlugin`
- Compliance: `ItrFilingCompliancePlugin`

## Run

```bash
mvn spring-boot:run
```

## Test

```bash
mvn test
```

## API

`POST /api/v1/tax/compute`

Example payload:

```json
{
  "taxpayerId": "PAN123",
  "age": 35,
  "residencyStatus": "RESIDENT",
  "regime": "OLD",
  "financialYear": "2024-25",
  "assessmentYear": "2025-26",
  "facts": {
    "salaryIncome": 1200000,
    "hraReceived": 240000,
    "rentPaid": 300000,
    "section80cInvestment": 180000,
    "tds": 90000
  }
}
```
