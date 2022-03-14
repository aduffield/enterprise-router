package com.hastings.router.route.resource;

import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 *
 */
public class CommonResourceIT {

    private static final Logger LOG = LoggerFactory.getLogger(CommonResourceIT.class);

    @Autowired
    protected MockMvc mvc;

    protected String getToken() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/authenticate")
                .content("{\"username\":\"hd-router-su@hastingsdirect.com\",\"password\":\"123\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        LOG.debug("Got result {}", result.getResponse().getContentAsString());

        String token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");

        LOG.debug("TOKEN IS {}", token);

        return token;

    }
}
