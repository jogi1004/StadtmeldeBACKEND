package com.stadtmeldeapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.MaincategoryDTO;
import com.stadtmeldeapp.DTO.SubcategoryDTO;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.service.CategoryService;
import com.stadtmeldeapp.service.ReportingLocationService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ReportingLocationService reportingLocationService;

    @Deprecated
    @GetMapping("/main") // kommt weg
    public ResponseEntity<List<MaincategoryEntity>> getAllMainCategories() {
        List<MaincategoryEntity> mainCategories = categoryService.getAllMainCategories();
        return new ResponseEntity<>(mainCategories, HttpStatus.OK);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<MaincategoryEntity> getMainCategoryById(@PathVariable("id") int id) throws NotFoundException {
        MaincategoryEntity mainCategory = categoryService.getMainCategoryById(id);
        return new ResponseEntity<>(mainCategory, HttpStatus.OK);
    }

    @PostMapping("/main")
    public ResponseEntity<MaincategoryEntity> createOrUpdateMainCategory(
            @RequestBody @NonNull MaincategoryDTO mainCategoryDTO, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity reportingLocationEntity = reportingLocationService
                .getReportingLocationById(mainCategoryDTO.reportingLocationId());
        MaincategoryEntity maincategoryEntity = new MaincategoryEntity();
        maincategoryEntity.setTitle(mainCategoryDTO.title());
        maincategoryEntity.setReportingLocationEntity(reportingLocationEntity);
        MaincategoryEntity savedMainCategory = categoryService.saveMainCategory(maincategoryEntity, request);
        return new ResponseEntity<>(savedMainCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/main/{id}")
    public ResponseEntity<Void> deleteMainCategory(@PathVariable("id") int id, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        MaincategoryEntity maincategoryEntity = categoryService.getMainCategoryById(id);

        if (maincategoryEntity != null) {
            categoryService.deleteMainCategory(id, request);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<SubcategoryEntity> getSubCategoryById(@PathVariable("id") int id) throws NotFoundException {
        SubcategoryEntity subCategory = categoryService.getSubCategoryById(id);
        if (subCategory != null) {
            return new ResponseEntity<>(subCategory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sub")
    public ResponseEntity<SubcategoryEntity> createOrUpdateSubCategory(
            @RequestBody @NonNull SubcategoryDTO subcategoryDTO, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        MaincategoryEntity maincategoryEntity = categoryService.getMainCategoryById(subcategoryDTO.maincategoryId());
        SubcategoryEntity subcategoryEntity = new SubcategoryEntity();
        subcategoryEntity.setTitle(subcategoryDTO.title());
        subcategoryEntity.setMaincategoryEntity(maincategoryEntity);
        SubcategoryEntity savedSubCategory = categoryService.saveSubCategory(subcategoryEntity, request);
        return new ResponseEntity<>(savedSubCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/sub/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable("id") int id, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        SubcategoryEntity subcategoryEntity = categoryService.getSubCategoryById(id);

        if (subcategoryEntity != null) {
            categoryService.deleteSubCategory(id, request);
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

    @GetMapping("/main/location/{locationName}")
    public ResponseEntity<List<MaincategoryEntity>> getCategoriesByLocationName(
            @PathVariable("locationName") String locationName) {
        List<MaincategoryEntity> categories = categoryService.getMaincategoriesByLocationName(locationName);
        if (categories.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PutMapping("/main/{id}")
    public ResponseEntity<MaincategoryEntity> updateMainCategory(@PathVariable("id") int id,
            @RequestBody @NonNull String newTitle, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        MaincategoryEntity existingMainCategory = categoryService.getMainCategoryById(id);
        existingMainCategory.setTitle(newTitle);
        MaincategoryEntity updatedMainCategory = categoryService.saveMainCategory(existingMainCategory, request);
        return new ResponseEntity<>(updatedMainCategory, HttpStatus.OK);
    }

    @PutMapping("/sub/{id}")
    public ResponseEntity<SubcategoryEntity> updateSubCategory(@PathVariable("id") int id,
            @RequestBody @NonNull SubcategoryDTO newSubcategory, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        SubcategoryEntity existingSubCategory = categoryService.getSubCategoryById(id);

        MaincategoryEntity maincategoryEntity = categoryService.getMainCategoryById(newSubcategory.maincategoryId());
        existingSubCategory.setTitle(newSubcategory.title());
        existingSubCategory.setMaincategoryEntity(maincategoryEntity);

        SubcategoryEntity updatedSubCategory = categoryService.saveSubCategory(existingSubCategory, request);
        return new ResponseEntity<>(updatedSubCategory, HttpStatus.OK);
    }

}
