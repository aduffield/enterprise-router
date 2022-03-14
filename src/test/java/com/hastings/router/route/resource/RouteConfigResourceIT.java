package com.hastings.router.route.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastings.router.EnterpriseRouterApplication;
import com.hastings.router.route.model.RouteConfig;
import com.hastings.router.route.repository.RouteConfigRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnterpriseRouterApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "dev")
public class RouteConfigResourceIT extends CommonResourceIT {

    private static final Logger LOG = LoggerFactory.getLogger(RouteConfigResourceIT.class);
    @Autowired
    RouteConfigRepository routeConfigRepository;
    @Autowired
    WebApplicationContext context;
    private String token;

    /**
     * @param obj
     * @return
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void initTests() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        this.token = getToken();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void getRouteConfigsNoPatternMatch() throws Exception {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setStatus(1);
        routeConfig.setId("mtas");
        routeConfig.setPattern("000000##0");
        routeConfig.setAppPath("mta_path");
        routeConfig.setSum(Arrays.asList(7, 9));
        routeConfigRepository.save(routeConfig);

        mvc.perform(get("/app-route/mtas/12345678901")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"mtas\",\"policyNumber\":\"12345678901\",\"statusCode\":\"1\",\"statusText\":null,\"appPath\":\"unresolved\"}"))
                .andDo(print());
    }

    @Test
    public void createSuccessfulRouteConfigWithToken() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .post("/route-config")
                        .content("{\"id\":\"mtas\"," +
                                "\"pattern\":\"0000000000##\"," +
                                "\"brand\" : \"\"," +
                                "\"arflag\" : \"\"," +
                                "\"lob\" : \"scar\"," +
                                "\"apppath\": \"renewal\"," +
                                "\"sum\": [7,9]," +
                                "    \"status\":1" +
                                "}")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    public void createSuccessfulRouteConfigWithNoToken() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/route-config")
                        .content("{\n" +
                                "    \"id\":\"mtas\",\n" +
                                "    \"pattern\":\"0000000000##\",\n" +
                                "    \"brand\" : \"\",\n" +
                                "    \"arflag\" : \"\",\n" +
                                "    \"lob\" : \"scar\",\n" +
                                "    \"apppath\": \"renewal\",\n" +
                                "    \"sum\": [7,9],\n" +
                                "    \"status\":1\n" +
                                "\n" +
                                "}").header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();

        String content = result.getResponse().getContentAsString();
        LOG.debug("CONTENT {}", content);
    }

}