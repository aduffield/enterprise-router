package com.hastings.router.authentication.resource;

import com.hastings.router.authentication.JwtUserDetailsService;
import com.hastings.router.authentication.model.AuthenticationRequest;
import com.hastings.router.authentication.model.AuthenticationResponse;
import com.hastings.router.route.repository.AzureStorageRouteConfigRepository;
import com.hastings.router.user.model.RouterUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationResourceTest {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationResourceTest.class);

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    //Class under test
    private JwtAuthenticationResource jwtAuthenticationResource = new JwtAuthenticationResource();

    @Test
    public void createAuthenticationToken() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("hi@hello.com");
        authenticationRequest.setPassword("1234");

        RouterUser routerUser = new RouterUser();
        routerUser.setUserName("hi2@hello.com");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setFirstName("andy");

        when(jwtUserDetailsService.findRouterUser(authenticationRequest.getUsername(),
                authenticationRequest.getPassword())).thenReturn(routerUser);

        when(jwtUserDetailsService.getAuthenticationToken(authenticationRequest.getUsername(),
                authenticationRequest.getPassword(), routerUser)).thenReturn("hellotoken");

        when(modelMapper.map(routerUser, AuthenticationResponse.class)).thenReturn(authenticationResponse);

        ResponseEntity<?> actual = jwtAuthenticationResource.createAuthenticationToken(authenticationRequest);

        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        LOG.debug("actual: {}", ((AuthenticationResponse) actual.getBody()).getToken());


    }
}