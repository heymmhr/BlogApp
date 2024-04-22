package com.blog.blogappapis.controllers;

import com.blog.blogappapis.entities.Comment;
import com.blog.blogappapis.payloads.ApiResponse;
import com.blog.blogappapis.payloads.CommentDto;
import com.blog.blogappapis.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<ApiResponse> createComment
            (@RequestBody CommentDto comment, @PathVariable Integer postId){

        CommentDto createComment = this.commentService.createComment(comment, postId);
        return ResponseEntity.ok(new ApiResponse
                ("Comment created successfully", true, createComment));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){

        this.commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse
                ("Comment deleted successfully1!!", true,null));
    }
}
