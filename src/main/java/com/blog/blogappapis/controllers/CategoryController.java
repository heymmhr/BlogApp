package com.blog.blogappapis.controllers;

import com.blog.blogappapis.payloads.ApiResponse;
import com.blog.blogappapis.payloads.CategoryDto;
import com.blog.blogappapis.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // create

    @PostMapping("/")
    public ResponseEntity <ApiResponse> createCategory(@Valid @RequestBody CategoryDto categoryDto){

        CategoryDto createCategory = this.categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(new ApiResponse("Category created successfully",true,createCategory));
    }

    // update

    @PutMapping("/{catId}")
    public ResponseEntity <ApiResponse> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer catId){

        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, catId);
        return ResponseEntity.ok(new ApiResponse("Category updated successfully", true, updateCategory));
    }

    // delete

    @DeleteMapping("/{catId}")
    public ResponseEntity <ApiResponse> deleteCategory( @PathVariable Integer catId){

        this.categoryService.deleteCategory(catId);
        return ResponseEntity.ok(new ApiResponse("category is deleted successfully!!", true,null));
    }

    // get

    @GetMapping("/{catId}")
    public ResponseEntity <ApiResponse> getCategory( @PathVariable Integer catId){

        CategoryDto categoryDto = this.categoryService.getCategory(catId);
        return ResponseEntity.ok(new ApiResponse("Category datas",true,categoryDto));
    }
    
    // get all user

    @GetMapping("/")
    public ResponseEntity <ApiResponse> getCategories(){

        List<CategoryDto> categories = this.categoryService.getCategories();
        return ResponseEntity.ok(new ApiResponse("All categories", true,categories));
    }
}
