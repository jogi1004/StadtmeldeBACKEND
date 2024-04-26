package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.IconEntity;


@Repository
public interface IconRepository extends JpaRepository<IconEntity, Integer> {
}