package com.coms3090.calculator.service;

import org.springframework.stereotype.Service;

@Service
public class Calculator {
    public double add(double a, double b) {
        return a + b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }
}
