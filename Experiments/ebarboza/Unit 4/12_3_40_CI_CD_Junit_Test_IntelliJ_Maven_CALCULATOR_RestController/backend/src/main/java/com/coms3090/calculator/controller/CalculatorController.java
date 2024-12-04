package com.coms3090.calculator.controller;


import com.coms3090.calculator.entity.CalculationResult;
import com.coms3090.calculator.service.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @Autowired
    private Calculator calculator;

    // Endpoint for addition
    @GetMapping("/add")
    public CalculationResult  add(@RequestParam double num1, @RequestParam double num2) {
        double result =  calculator.add(num1,num2);
        return new CalculationResult("addition", result);
    }

    // Endpoint for multiplication
    @GetMapping("/multiply")
    public CalculationResult multiply(@RequestParam double num1, @RequestParam double num2) {
        double result =  calculator.multiply(num1,num2);
        return new CalculationResult("multiplication", result);
    }
}
