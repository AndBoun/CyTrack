package com.coms3090.calculator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Calculation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String operation;
    private double operand1;
    private double operand2;
    private double result;
    // Default constructor

    public Calculation() {
    }

    public Calculation(Long id, String operation, double operand1, double operand2, double result) {
        this.id = id;
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }
    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getOperand1() {
        return operand1;
    }

    public void setOperand1(double operand1) {
        this.operand1 = operand1;
    }

    public double getOperand2() {
        return operand2;
    }

    public void setOperand2(double operand2) {
        this.operand2 = operand2;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Calculation{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", operand1=" + operand1 +
                ", operand2=" + operand2 +
                ", result=" + result +
                '}';
    }
}
