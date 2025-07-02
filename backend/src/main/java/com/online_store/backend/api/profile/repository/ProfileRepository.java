package com.online_store.backend.api.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
