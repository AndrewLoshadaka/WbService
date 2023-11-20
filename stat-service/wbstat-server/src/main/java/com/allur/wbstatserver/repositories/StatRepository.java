package com.allur.wbstatserver.repositories;

import com.allur.wbstatserver.model.entities.StatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface StatRepository extends JpaRepository<StatEntity, Integer> {
    List<StatEntity> findByNmIdIn(List<Integer> nmIds);

}
