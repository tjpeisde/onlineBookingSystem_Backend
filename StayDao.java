package com.tjpeisde.onlinebooking.dao;

import java.util.List;
import com.tjpeisde.onlinebooking.entity.Stay;
import com.tjpeisde.onlinebooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StayDao extends JpaRepository <Stay, Long>{
    List<Stay> findByHost(User user);

    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Long> ids, int guestNumber);

}
