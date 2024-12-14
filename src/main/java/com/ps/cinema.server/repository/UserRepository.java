package com.ps.cinema.server.repository;

import com.ps.cinema.server.model.Role;
import com.ps.cinema.server.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findCinemaAppUserByEmail(String email);

    public Optional<User> findCinemaAppUserByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_ADMIN' AND id = :adminId", nativeQuery = true)
    Optional<User> getAdminById(@Param("adminId")int adminId);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_ADMIN' AND username = :username", nativeQuery = true)
    Optional<User> getAdminByUsername(@Param("username")String username);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_ADMIN' AND email = :email", nativeQuery = true)
    Optional<User> getAdminByEmail(@Param("email")String email);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_PRODUCER' AND id = :producerId", nativeQuery = true)
    Optional<User> getProducerById(@Param("producerId") int producerId);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_PRODUCER' AND username = :username", nativeQuery = true)
    Optional<User> getProducerByUsername(@Param("username")String username);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_PRODUCER' AND email = :email", nativeQuery = true)
    Optional<User> getProducerByEmail(@Param("email")String email);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_VIEWER' AND id = :viewerId", nativeQuery = true)
    Optional<User> getViewerById(@Param("viewerId")int viewerId);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_VIEWER' AND username = :username", nativeQuery = true)
    Optional<User> getViewerByUsername(@Param("username")String username);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_VIEWER' AND email = :email", nativeQuery = true)
    Optional<User> getViewerByEmail(@Param("email")String email);

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_VIEWER'", nativeQuery = true)

    List<User> getAllViewers();

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_ADMIN'", nativeQuery = true)
    List<User> getAllAdmins();

    @Query(value = "SELECT * FROM user WHERE role = 'ROLE_PRODUCER'", nativeQuery = true)
    List<User> getAllProducers();

    @Query(value = "SELECT role FROM user WHERE id = :userId", nativeQuery = true)
    String getRoleById(@Param("userId")int userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User SET role = 'ROLE_PRODUCER' WHERE id = :userId", nativeQuery = true)
    void setProducerType(@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User SET role = 'ROLE_ADMIN' WHERE id = :userId", nativeQuery = true)
    void setAdminType(@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User SET role = 'ROLE_VIEWER' WHERE id = :userId", nativeQuery = true)
    void setViewerType(@Param("userId") Integer userId);



}
