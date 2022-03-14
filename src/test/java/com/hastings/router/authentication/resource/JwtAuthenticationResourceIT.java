package com.hastings.router.authentication.resource;

import com.hastings.router.EnterpriseRouterApplication;
import com.hastings.router.route.resource.CommonResourceIT;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnterpriseRouterApplication.class)
@AutoConfigureMockMvc
class JwtAuthenticationResourceIT extends CommonResourceIT {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationResourceIT.class);

    private String token;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void initTests() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build();
        this.token = getToken();
    }

    @Test
    public void getRouteConfigsNoPatternMatch() throws Exception {

        //Create a user
        MvcResult result = mvc.perform(post("/user")
                .content("{" +
                        "\"firstName\":\"Joe\"," +
                        "\"lastName\":\"Bloggs\"," +
                        "\"emailAddress\":\"jbloggs@gmail.com\"," +
                        "\"userName\":\"jbloggs@gmail.com\"," +
                        "\"roles\": [" +
                        "\"STANDARD_USER\"" +
                        "]}")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        String content = result.getResponse().getContentAsString();
        LOG.debug("content {}", content);

        //Extract the password
        String password = JsonPath.read(result.getResponse().getContentAsString(), "$.password");

        //Authenticate the user
        MvcResult authResult = mvc.perform(MockMvcRequestBuilders
                .post("/authenticate")
                .content("{\"username\":\"jbloggs@gmail.com\",\"password\":\"" + password + "\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String token = JsonPath.read(authResult.getResponse().getContentAsString(), "$.token");
        LOG.debug("Token is {}", token);
    }

}