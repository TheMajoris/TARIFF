package com.cs203.core.repository;

import com.cs203.core.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    // Find user by username
    Optional<UserEntity> findByUsername(String username);
    
    // Find user by email
    Optional<UserEntity> findByEmail(String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find all admin users
    List<UserEntity> findByIsAdminTrue();
    
    // Find all non-admin users
    List<UserEntity> findByIsAdminFalse();
    
    // Find users by first name (case insensitive)
    List<UserEntity> findByFirstNameIgnoreCase(String firstName);
    
    // Find users by last name (case insensitive)
    List<UserEntity> findByLastNameIgnoreCase(String lastName);
    
    // Find users by partial username (contains)
    List<UserEntity> findByUsernameContainingIgnoreCase(String username);
    
    // Find users by email domain
    @Query("SELECT u FROM UserEntity u WHERE u.email LIKE %:domain%")
    List<UserEntity> findByEmailDomain(@Param("domain") String domain);
    
    // Find users by full name (first + last)
    @Query("SELECT u FROM UserEntity u WHERE " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<UserEntity> findByFullNameContaining(@Param("fullName") String fullName);
    
    // Count admin users
    long countByIsAdminTrue();
    
    // Count regular users
    long countByIsAdminFalse();
}
