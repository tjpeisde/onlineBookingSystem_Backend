package com.tjpeisde.onlinebooking.controller;

import com.tjpeisde.onlinebooking.entity.Token;
import com.tjpeisde.onlinebooking.entity.User;
import com.tjpeisde.onlinebooking.entity.UserRole;
import com.tjpeisde.onlinebooking.service.AuthenticationService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthenticationController {
    private AuthenticationService authenticationService;
    @Autowired
    public AuthenticationController(AuthenticationService
                                            authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/authenticate/guest")
    public Token authenticateGuest(@RequestBody User user) {
        return authenticationService.authenticate(user,
                UserRole.ROLE_GUEST);
    }
    @PostMapping("/authenticate/host")
    public Token authenticateHost(@RequestBody User user) {
        return authenticationService.authenticate(user,
                UserRole.ROLE_HOST);
    }
}
