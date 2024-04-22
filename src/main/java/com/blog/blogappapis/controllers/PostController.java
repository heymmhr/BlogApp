package com.blog.blogappapis.controllers;


import com.blog.blogappapis.config.AppConstants;
import com.blog.blogappapis.entities.Post;
import com.blog.blogappapis.payloads.ApiResponse;
import com.blog.blogappapis.payloads.PostDto;
import com.blog.blogappapis.payloads.PostResponse;
import com.blog.blogappapis.repositories.PostRepo;
import com.blog.blogappapis.services.FileService;
import com.blog.blogappapis.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Autowired
    private PostRepo postRepo;

    @Value("${project.image}")
    private String path;

    //create

    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<ApiResponse> createPost
            (@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId) {

        PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
        return ResponseEntity.ok(new ApiResponse("Post created", true, createPost));

    }

    // get by user

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<ApiResponse> getPostByUser(@PathVariable Integer userId) {

        List<PostDto> posts = this.postService.getPostByUser(userId);
        return ResponseEntity.ok(new ApiResponse("Getting user by id",true, posts));

    }

    // get by category

    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<ApiResponse> getPostByCategory(@PathVariable Integer categoryId) {

        List<PostDto> posts = this.postService.getPostByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse("Get post by category", true, posts));
    }

    // get all posts

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse> getAllPost(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {

        PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(new ApiResponse("Requested page is", true,postResponse));
    }

    //get post details by id

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> getPostById(@PathVariable Integer postId) {

        PostDto postDto = this.postService.getPostById(postId);
        return ResponseEntity.ok(new ApiResponse("Saved datas", true,postDto));
    }

    // delete post by id
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId) {
        this.postService.deletePost(postId);
        return new ApiResponse("Post is successfully deleted !!", true,null);
    }

    // update post by id
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {

        PostDto updatePost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    //search
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {
        List<PostDto> result = this.postService.searchPosts(keywords);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // post image upload

    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId
    ) throws IOException {

        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);

        postDto.setImageName(fileName);
        PostDto updatePost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    //method to serve files

    @GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName, HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

    //using native query to get posts

    @GetMapping("/post-sql/{pId}")
    public ResponseEntity<List<Post>> getPostId(@PathVariable Integer pId){
      return ResponseEntity.ok(postRepo.postById(pId));
    }

    @GetMapping("/post-sql/allPost")
    public ResponseEntity<ApiResponse> getAllPost(){

        List<Post> allPost = postRepo.getAllPost();
        return ResponseEntity.ok(new ApiResponse("All posts", true,allPost));
    }
}
