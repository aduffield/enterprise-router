package com.hastings.router.authentication.filter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CorrelationIdFilterTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;

    private CorrelationIdFilter correlationIdFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        correlationIdFilter = new CorrelationIdFilter();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void verifyOrCreateCorrelationId() {
        String actual = correlationIdFilter.verifyOrCreateCorrelationId("12345");
        assertEquals("12345", actual);
    }

    @Test
    void verifyOrCreateCorrelationIdMaxSize() {
        String actual = correlationIdFilter.verifyOrCreateCorrelationId("123456789012345678901234567890");
        assertEquals("1234567890123456789012", actual);
    }

    @Test
    void createCorrelationId() {
        String actual = correlationIdFilter.verifyOrCreateCorrelationId(null);
        assertNotNull(actual);
    }


    @Test
    void doFilterInternal() throws Exception {

        when(request.getHeader("X-Correlation-Id")).thenReturn("12345678");

        correlationIdFilter.doFilterInternal(request, response, filterChain);
        verify(request, times(1)).getHeader("X-Correlation-Id");//request.getHeader("X-Correlation-Id")
        verify(response, times(1)).addHeader("X-Correlation-Id", "12345678");//response.addHeader("X-Correlation-Id", correlationId);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}