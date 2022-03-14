package com.hastings.router.user.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastings.router.EnterpriseRouterApplication;
import com.hastings.router.route.resource.CommonResourceIT;
import com.hastings.router.user.model.CreateUserResponse;
import com.hastings.router.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User resource Integration tests
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnterpriseRouterApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "dev")
class UserResourceIT extends CommonResourceIT {

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceIT.class);

    @Autowired
    WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommandLineRunner commandLineRunner;

    private String token;

    /**
     * Set up the mocked context; create the super-user; get the super-user token
     *
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        commandLineRunner.run();
        this.token = getToken();
    }

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    void getUsers() throws Exception{

        //Create a user
        mvc.perform(MockMvcRequestBuilders.post("/user").content(getContent("Andy@gmail3.com"))
                .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Create another user
        mvc.perform(MockMvcRequestBuilders.post("/user").content(getContent("Andy@gmail2.com"))
                .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Create a third user
        mvc.perform(MockMvcRequestBuilders.post("/user").content(getContent("Andy@gmail1.com"))
                .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Get all the users, check it's the correct count
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user")
                        .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();

        List<CreateUserResponse> actual = objectMapper.readValue(content, List.class);
        assertEquals(4, actual.size(),"4 users created");


    }

    @Test
    void savePassword() {
    }

    @Test
    void getUserSuccess() throws Exception {

        //Create a user
        mvc.perform(MockMvcRequestBuilders.post("/user").content(getContent("Andy@gmail3.com"))
                .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Check its possible to retrieve the user.
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/Andy@gmail3.com")
                        .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        LOG.debug("CONTENT {}", content);
    }


    @Test
    void getUserFail_noUser() throws Exception {

        //Create a user
        mvc.perform(MockMvcRequestBuilders.post("/user").content(getContent("Andy@gmail3.com"))
                .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Get a user who does not exist
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/1234")
                        .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        String content = result.getResponse().getContentAsString();
        LOG.debug("CONTENT {}", content);
    }


    @Test
    void approveUser() {
    }

    @Test
    void createUser() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/user").content(getContent("Andy@gmail3.com"))
                        .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        LOG.debug("CONTENT {}", content);

    }


    //Quick and nasty - create a json object for a user
    private String getContent(String userName) {
        return "{\"userName\":\"" + userName + "\"," +
                "\"firstName\":\"Andy\",\"lastName\":\"Duffield\"," +
                "\"emailAddress\":\"AAndy@gmail.com\"," +
                "\"roles\": [" +
                "\"STANDARD_USER\"" +
                "]}";
    }

}