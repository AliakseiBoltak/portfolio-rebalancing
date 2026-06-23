# Portfolio Rebalancing Test Automation

## Overview

This project contains an automated test solution for validating a portfolio rebalancing algorithm.

The solution includes:

- a simple implementation of the portfolio rebalancing calculator used as the System Under Test (SUT);
- automated tests validating trade actions and the calculated number of shares;
- documentation describing assumptions, test cases, and automated test coverage.
---

# Technology Stack

- Java 17
- Maven
- Lombok
- TestNG
- AssertJ

---

# Project Structure

```
src
├── main
│   ├── core
│   │      PortfolioRebalancingCalculator
│   │
│   └── model
│          Portfolio
│          Security
│          Trade
│          TradeAction
│
└── test
       PortfolioRebalancingTest
       PortfolioTestData
```

---


# Build & Run

Execute all automated tests:

```bash
mvn clean test
```

---

# Assumptions

Since several implementation details were not explicitly defined, the following assumptions were made.

## A1. Fractional shares are supported

The calculator allows fractional shares.

Example:

```
BUY 66.6667 IBM
```

instead of

```
BUY 67 IBM
```

---

## A2. Share quantity precision

Calculated quantities are rounded to **4 decimal places**.

Example:

```
66.666666...

↓

66.6667
```

---

## A3. Rounding strategy

Share quantities are rounded using

```
RoundingMode.HALF_UP
```

This is a common and intuitive rounding strategy unless business rules specify otherwise.

---

## A4. Input validation

The calculator assumes valid input data.

Input validation was intentionally omitted to keep the implementation focused on the portfolio rebalancing algorithm.

In a production application, validation would typically be implemented at the service/API.

---

## A5. Trade model

The `Trade` class represents a **trading instruction** produced by the calculator.

It does **not** represent an executed market trade.

---

## A6. Trade quantity

The `quantity` field represents the **number of shares** to buy or sell.

---

## A7. Variance calculation

Target variance is calculated as

```
Current Allocation - Target Allocation
```

Examples

```
Current = 10%
Target = 20%

Variance = -10%

↓

BUY
```

```
Current = 30%
Target = 20%

Variance = +10%

↓

SELL
```

---

## Manual Test Cases

| ID | Test Case | Expected Result                                                                     |
|----|-----------|-------------------------------------------------------------------------------------|
| TC-001 | Verify trade action when current allocation is below target allocation | BUY action is returned|
| TC-002 | Verify trade action when current allocation is above target allocation | SELL action is returned|
| TC-003 | Verify trade action when current allocation equals target allocation | HOLD action is returned and trade quantity equals zero|
| TC-004 | Verify number of shares calculation for BUY action | Number of shares is calculated according to the rebalancing formula|
| TC-005 | Verify number of shares calculation for SELL action | Number of shares is calculated according to the rebalancing formula|
| TC-006 | Verify that every security in the portfolio is processed | One trade instruction is returned for every security in the portfolio|
| TC-007 | Verify number of shares rounding | Number of shares is rounded to four decimal places using HALF_UP rounding|
| TC-008 | Verify calculation for a portfolio containing multiple securities | Trade instructions are generated for all securities while preserving the input order|
| TC-009 | Verify calculation for an empty portfolio | Empty trade list is returned|
| TC-010 | Verify trade action when current allocation is slightly below target allocation | BUY action is returned|
| TC-011 | Verify trade action when current allocation exactly matches target allocation | HOLD action is returned|
| TC-012 | Verify trade action when current allocation is slightly above target allocation | SELL action is returned|

---

## Automated Test Coverage

| ID | Automated Test | Covered Manual Test Cases |
|----|----------------|---------------------------|
| AT-001 | shouldCalculateExpectedTradeForEachSecurity | TC-001, TC-002, TC-003, TC-004, TC-005 |
| AT-002 | shouldCalculateTradesForAllSecurities | TC-006, TC-008 |
| AT-003 | shouldRoundNumberOfSharesToFourDecimalPlaces | TC-007 |
| AT-004 | shouldReturnEmptyTradeListWhenPortfolioHasNoSecurities | TC-009 |
| AT-005 | shouldDetermineTradeActionAroundTargetAllocationBoundary | TC-010, TC-011, TC-012 |