package com.tjpeisde.onlinebooking.service;

import com.tjpeisde.onlinebooking.dao.AuthorityDao;
import com.tjpeisde.onlinebooking.dao.UserDao;
import com.tjpeisde.onlinebooking.entity.Authority;
import com.tjpeisde.onlinebooking.entity.User;
import com.tjpeisde.onlinebooking.entity.UserRole;
import com.tjpeisde.onlinebooking.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {
    private UserDao userDao;
    private AuthorityDao authorityDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserDao userDao, AuthorityDao authorityDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.authorityDao = authorityDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) throws UserAlreadyExistException {
        if(userDao.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userDao.save(user);
        authorityDao.save(new Authority(user.getUsername(),
                role.name()));
    }
}
