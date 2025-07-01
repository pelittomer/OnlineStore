package com.online_store.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
