package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.ProfilePictureEntity;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePictureEntity, Integer> {
}