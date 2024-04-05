package com.stadtmeldeapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/main")
    public ResponseEntity<List<MaincategoryEntity>> getAllMainCategories() {
        List<MaincategoryEntity> mainCategories = categoryService.getAllMainCategories();
        return new ResponseEntity<>(mainCategories, HttpStatus.OK);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<MaincategoryEntity> getMainCategoryById(@PathVariable("id") int id) {
        MaincategoryEntity mainCategory = categoryService.getMainCategoryById(id);
        return new ResponseEntity<>(mainCategory, HttpStatus.OK);
    }

    @PostMapping("/main")
    public ResponseEntity<MaincategoryEntity> createOrUpdateMainCategory(@RequestBody @NonNull MaincategoryEntity mainCategory) {
        MaincategoryEntity savedMainCategory = categoryService.saveMainCategory(mainCategory);
        System.out.println(savedMainCategory);
        return new ResponseEntity<>(savedMainCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/main/{id}")
    public ResponseEntity<Void> deleteMainCategory(@PathVariable("id") int id) {
        categoryService.deleteMainCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<SubcategoryEntity> getSubCategoryById(@PathVariable("id") int id) {
        SubcategoryEntity subCategory = categoryService.getSubCategoryById(id);
        return new ResponseEntity<>(subCategory, HttpStatus.OK);
    }

    @PostMapping("/sub")
    public ResponseEntity<SubcategoryEntity> createOrUpdateSubCategory(@RequestBody @NonNull SubcategoryEntity subCategory) {
        SubcategoryEntity savedSubCategory = categoryService.saveSubCategory(subCategory);
        return new ResponseEntity<>(savedSubCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/sub/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable("id") int id) {
        categoryService.deleteSubCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*@GetMapping("/sub/main/{mainCategoryId}")
    public ResponseEntity<List<SubcategoryEntity>> getSubCategoriesByMainCategoryId(@PathVariable("mainCategoryId") int mainCategoryId) {
        List<SubcategoryEntity> subCategories = categoryService.getSubCategoriesByMainCategoryId(mainCategoryId);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }*/
}
