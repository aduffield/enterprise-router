package com.hastings.router.authentication.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * Filter responsible for the checking the correlation id as sent by the request.
 */
@Component
@Order(1)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private final static Logger LOG = LoggerFactory.getLogger(CorrelationIdFilter.class);

    private final Random random = new Random();
    private static final int MAX_ID_SIZE = 22;

    /**
     * @param correlationId
     * @return
     */
    public String verifyOrCreateCorrelationId(String correlationId) {
        String corrId;
        if (correlationId == null) {
            LOG.warn("A correlation-id was not found, so creating one");
            corrId = generateCorrelationId();
            LOG.info("Created new correlation id {}", corrId);
        }

        else if (correlationId.length() > MAX_ID_SIZE) {
            corrId = correlationId.substring(0, MAX_ID_SIZE);
            LOG.warn("Correlation id found, but too long, so truncating to {}", corrId);
        } else {
            corrId = correlationId;
            LOG.info("Correlation-id set to {}", corrId);
        }

        return corrId;
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        LOG.debug("In doFilterInternal()");

        String correlationId = request.getHeader("X-Correlation-Id");
        correlationId = verifyOrCreateCorrelationId(correlationId);
        MDC.put("correlation-id", correlationId);
        response.addHeader("X-Correlation-Id", correlationId);

        filterChain.doFilter(request, response);
    }

    //Generates the correlation id.
    private String generateCorrelationId() {
        long randomNum = random.nextLong();
        return encodeBase62(randomNum);
    }


    /**
     * Encode the given Long in base 62
     *
     * @param n Number to encode
     * @return Long encoded as base 62
     */
    private String encodeBase62(long n) {

        final String base62Chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder builder = new StringBuilder();

        //NOTE: Appending builds a reverse encoded string. The most significant value
        //is at the end of the string. You could prepend(insert) but appending
        // is slightly better performance and order doesn't matter here.
        //perform the first selection using unsigned ops to get negative
        //numbers down into positive signed range.
        long index = Long.remainderUnsigned(n, 62);
        builder.append(base62Chars.charAt((int) index));
        n = Long.divideUnsigned(n, 62);
        //now the long is unsigned, can just do regular math ops
        while (n > 0) {
            builder.append(base62Chars.charAt((int) (n % 62)));
            n /= 62;
        }
        return builder.toString();
    }
}
