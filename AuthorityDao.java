package com.tjpeisde.onlinebooking.dao;

import com.tjpeisde.onlinebooking.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityDao extends JpaRepository<Authority, String> {
}
