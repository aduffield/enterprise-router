package com.hastings.router.user.resource;

import com.hastings.router.user.model.CreateUserRequest;
import com.hastings.router.user.model.CreateUserResponse;
import com.hastings.router.user.model.RouterUser;
import com.hastings.router.user.model.UpdateUserPasswordRequest;
import com.hastings.router.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
public class UserResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("/user")
    public ResponseEntity<List<CreateUserResponse>> getUsers() {
        List<CreateUserResponse> userResponses = convertToDtos(userService.findUsers());
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @PutMapping("/user/{userName}")
    public ResponseEntity<Boolean> savePassword(@PathVariable("userName") String userName,
                                @RequestBody UpdateUserPasswordRequest updateUserPasswordRequest) {
        Boolean updated = userService.updatePassword(userName, updateUserPasswordRequest.getPassword());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<RouterUser> getUser(@PathVariable("userName") String userName) {
        RouterUser user = userService.findUser(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping ("/user/{userName}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("userName") String userName) {
        boolean deleted = userService.deleteUser(userName);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @PostMapping("/user/{userName}/approve")
    public ResponseEntity<Boolean> approveUser(@PathVariable("userName") String userName) {
        Boolean approved = userService.approveUser(userName);
        return new ResponseEntity<>(approved, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        LOG.debug("create user {}", createUserRequest);
        RouterUser routerUser = convertToEntity(createUserRequest);
        RouterUser createdRouterUser = userService.createUser(routerUser);
        CreateUserResponse createUserResponse = convertToDto(createdRouterUser);
        return ResponseEntity.ok(createUserResponse);
    }

    /**
     * Converts the Dto request to a service entity
     *
     * @param createUserRequest the requeest object to convert
     * @return the entity after conversion
     */
    private RouterUser convertToEntity(CreateUserRequest createUserRequest) {
        return modelMapper.map(createUserRequest, RouterUser.class);
    }

    private CreateUserResponse convertToDto(RouterUser routerUser) {
        return modelMapper.map(routerUser, CreateUserResponse.class);
    }

    private List<CreateUserResponse> convertToDtos(List<RouterUser> routerUsers) {
        return modelMapper.map(routerUsers, List.class);
    }

}
