package com.tjpeisde.onlinebooking.dao;

import com.tjpeisde.onlinebooking.entity.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDao
        extends ElasticsearchRepository<Location, Long>, CustomLocationDao {
        // interface can extend more than 1 interface
}
