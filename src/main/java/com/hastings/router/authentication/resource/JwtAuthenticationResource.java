package com.hastings.router.authentication.resource;

import com.hastings.router.authentication.JwtUserDetailsService;
import com.hastings.router.authentication.model.AuthenticationRequest;
import com.hastings.router.authentication.model.AuthenticationResponse;
import com.hastings.router.user.model.RouterUser;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Rest resource for handling the REST authentication - namely creating jwt tokens,
 */
@RestController
public class JwtAuthenticationResource {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationResource.class);

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Authenticates a user based on the passed in JWTRequest object.
     *
     * @param authenticationRequest details of the user requireing authentication
     * @return The Response Entity containing the JWT token and the RouterUser details.
     * @throws Exception
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        LOG.debug("In createAuthenticationToken()...xx", authenticationRequest);
        RouterUser routerUser = jwtUserDetailsService.findRouterUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        String token = jwtUserDetailsService.getAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword(), routerUser);

        LOG.debug("RouterUser authenticated: {}", routerUser);
        AuthenticationResponse authenticationResponse = convertToDto(routerUser, token);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
    }


    /**
     * @param routerUser
     * @param token
     * @return
     */
    private AuthenticationResponse convertToDto(RouterUser routerUser, String token) {
        AuthenticationResponse authenticationResponse = modelMapper.map(routerUser, AuthenticationResponse.class);
        authenticationResponse.setToken(token);
        return authenticationResponse;
    }

}

/**
 * @param username
 * @param password
 * @throws Exception
 */
//    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
//}
