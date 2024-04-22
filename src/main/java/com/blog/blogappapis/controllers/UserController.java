package com.blog.blogappapis.controllers;


import com.blog.blogappapis.entities.User;
import com.blog.blogappapis.payloads.ApiResponse;
import com.blog.blogappapis.payloads.UserDto;
import com.blog.blogappapis.repositories.UserRepo;
import com.blog.blogappapis.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;
    //POST - create user

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserDto userDto){

        UserDto createUserDto = this.userService.createUser(userDto);
        return ResponseEntity.ok(new ApiResponse("Data created successfully",true,createUserDto) );
    }


    //PUT -update user
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable ("userId") Integer uid){

        UserDto updateUser = this.userService.updateUser(userDto, uid);
        return ResponseEntity.ok(new ApiResponse("Updated user successfully",true,updateUser));
    }

    //DELETE - delete user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable ("userId") Integer uid){
        this.userService.deleteUser(uid);
        return ResponseEntity.ok(new ApiResponse("User Deleted Successfully", true,null));
    }

    //GET - user get

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllUsers(){

        List<UserDto> users =userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse("Data saved successfully",true,users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getSingleUser(@PathVariable Integer userId){

        Optional<UserDto> user = Optional.ofNullable(userService.getUserById(userId));

        if (user.isPresent()){
            return ResponseEntity.ok(new ApiResponse( "User found: प्रयोगकर्ता फेला पर्यो", true,user));
        }else {
            return ResponseEntity.ok(new ApiResponse("User not found: प्रयोगकर्ता फेला परेन",false,null));
        }

    }

    // getting user using native query

    @GetMapping("/sql-user")
    public ResponseEntity<ApiResponse> getAllUserSql(){
        List<User> allUser = userRepo.getAllUsers();
        return ResponseEntity.ok(new ApiResponse("All users",true,allUser));
    }

    @GetMapping("/sql-user/{uId}")
    public ResponseEntity<ApiResponse> getUserId(@PathVariable Integer uId){
        List<User> allUser = userRepo.getUserGreaterId(uId);
        return ResponseEntity.ok(new ApiResponse("All users",true,allUser));
    }


}
