package com.coms3090.calculator.entity;

public class CalculationResult {

    private String operation;
    private double result;

    public CalculationResult(String operation, double result) {
        this.operation = operation;
        this.result = result;
    }

    // Getters and Setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
