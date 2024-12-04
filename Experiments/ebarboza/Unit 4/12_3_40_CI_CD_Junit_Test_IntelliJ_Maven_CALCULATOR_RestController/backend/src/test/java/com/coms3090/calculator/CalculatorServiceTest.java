package com.coms3090.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.coms3090.calculator.service.Calculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CalculatorServiceTest {

    @Autowired
    private Calculator calculator;

    @Test
    public void testAdd() {
        double result = calculator.add(2, 3);
        assertEquals(5, result);
    }

    @Test
    public void testMultiply() {
        double result = calculator.multiply(2, 3);
        assertEquals(6, result);
    }
}
