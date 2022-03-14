package com.hastings.router.health.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthResource {

    @GetMapping("/health")
    public ResponseEntity getAllOrganisations() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
