package com.tjpeisde.onlinebooking.dao;

import com.tjpeisde.onlinebooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {

}
