package com.tjpeisde.onlinebooking.service;


import com.tjpeisde.onlinebooking.dao.AuthorityDao;
import com.tjpeisde.onlinebooking.entity.Authority;
import com.tjpeisde.onlinebooking.entity.Token;
import com.tjpeisde.onlinebooking.entity.User;
import com.tjpeisde.onlinebooking.entity.UserRole;
import com.tjpeisde.onlinebooking.exception.UserNotExistException;
import com.tjpeisde.onlinebooking.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private AuthorityDao authorityDao;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 AuthorityDao authorityDao,
                                 JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authorityDao = authorityDao;
        this.jwtUtil = jwtUtil;
    }

    public Token authenticate(User user, UserRole role) throws
            UserNotExistException {
        try {
            authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword()));
        } catch (AuthenticationException exception) {
            throw new UserNotExistException("User Doesn't Exist");
        }
        Authority authority =
                authorityDao.findById(user.getUsername()).orElse(null);
        if (!authority.getAuthority().equals(role.name())) {
            throw new UserNotExistException("User Doesn't Exist");
        }
        return new Token(jwtUtil.generateToken(user.getUsername()));
    }


}
