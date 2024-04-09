package com.stadtmeldeapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.stadtmeldeapp.DTO.MaincategoryDTO;
import com.stadtmeldeapp.DTO.SubcategoryDTO;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.service.CategoryService;
import com.stadtmeldeapp.service.ReportingLocationService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ReportingLocationService reportingLocationService;

    @GetMapping("/main") // kommt weg
    public ResponseEntity<List<MaincategoryEntity>> getAllMainCategories() {
        List<MaincategoryEntity> mainCategories = categoryService.getAllMainCategories();
        return new ResponseEntity<>(mainCategories, HttpStatus.OK);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<MaincategoryEntity> getMainCategoryById(@PathVariable("id") int id) {
        MaincategoryEntity mainCategory = categoryService.getMainCategoryById(id);
        if (mainCategory != null) {
            return new ResponseEntity<>(mainCategory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/main")
    public ResponseEntity<MaincategoryEntity> createOrUpdateMainCategory(
            @RequestBody @NonNull MaincategoryDTO mainCategoryDTO) {
        ReportingLocationEntity reportingLocationEntity = reportingLocationService.getReportingLocationById(mainCategoryDTO.getreportingLocationID());
        MaincategoryEntity maincategoryEntity = new MaincategoryEntity();
        maincategoryEntity.setTitle(mainCategoryDTO.getTitle());
        maincategoryEntity.setReportingLocationEntity(reportingLocationEntity);
        MaincategoryEntity savedMainCategory = categoryService.saveMainCategory(maincategoryEntity);
        return new ResponseEntity<>(savedMainCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/main/{id}")
    public ResponseEntity<Void> deleteMainCategory(@PathVariable("id") int id) {
        MaincategoryEntity maincategoryEntity = categoryService.getMainCategoryById(id);

        if (maincategoryEntity != null) {
            categoryService.deleteMainCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<SubcategoryEntity> getSubCategoryById(@PathVariable("id") int id) {
        SubcategoryEntity subCategory = categoryService.getSubCategoryById(id);
        if (subCategory != null) {
            return new ResponseEntity<>(subCategory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sub")
    public ResponseEntity<SubcategoryEntity> createOrUpdateSubCategory(
            @RequestBody @NonNull SubcategoryDTO subcategoryDTO) {
        MaincategoryEntity maincategoryEntity = categoryService.getMainCategoryById(subcategoryDTO.getMainCategoryID());
        SubcategoryEntity subcategoryEntity = new SubcategoryEntity();
        subcategoryEntity.setTitle(subcategoryDTO.getTitle());
        subcategoryEntity.setMaincategoryEntity(maincategoryEntity);
        SubcategoryEntity savedSubCategory = categoryService.saveSubCategory(subcategoryEntity);
        return new ResponseEntity<>(savedSubCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/sub/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable("id") int id) {
        SubcategoryEntity subcategoryEntity = categoryService.getSubCategoryById(id);

        if (subcategoryEntity != null) {
            categoryService.deleteSubCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/sub/main/{mainCategoryId}")
    public ResponseEntity<List<SubcategoryEntity>> getSubCategoriesByMainCategoryId(
            @PathVariable("mainCategoryId") int mainCategoryId) {
        List<SubcategoryEntity> subCategories = categoryService.getSubCategoriesByMainCategoryId(mainCategoryId);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @SuppressWarnings("null")
    @GetMapping("/main/location/{locationName}")
    public ResponseEntity<List<MaincategoryEntity>> getCategoriesByLocationName(
            @PathVariable("locationName") String locationName) {
        List<MaincategoryEntity> categories = categoryService.getMaincategoriesByLocationName(locationName);
        if (categories.size()==0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

}
