package com.blog.blogappapis.repositories;

import com.blog.blogappapis.entities.Post;
import com.blog.blogappapis.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query(value = "select * from users", nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = "select * from users u where u.id>= :uId", nativeQuery = true)
    List<User> getUserGreaterId(@Param("uId") int uId);


}
