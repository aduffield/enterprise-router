package com.hastings.router.authentication.filter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtRequestFilterTest {

    //Class under test
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        jwtRequestFilter = new JwtRequestFilter();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void doFilterInternal() {
    }
}