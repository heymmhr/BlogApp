package com.blog.blogappapis.repositories;

import com.blog.blogappapis.entities.Category;
import com.blog.blogappapis.entities.Post;
import com.blog.blogappapis.entities.User;
import com.blog.blogappapis.payloads.PostDto;
import com.blog.blogappapis.payloads.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

    List<Post> findByTitleContaining(String title);

    @Query(value = "select * from posts where post_id = :pId",nativeQuery = true)
    List<Post> postById( @Param("pId") int pId);


    @Query(value = "select * from posts", nativeQuery = true)
   List<Post> getAllPost();





}
