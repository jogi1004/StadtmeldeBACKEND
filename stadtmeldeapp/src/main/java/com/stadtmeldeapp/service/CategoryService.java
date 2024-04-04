package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.Repository.MaincategoryRepository;
import com.stadtmeldeapp.Repository.SubcategoryRepository;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private MaincategoryRepository mainCategoryRepository;

    @Autowired
    private SubcategoryRepository subCategoryRepository;

    public List<MaincategoryEntity> getAllMainCategories() {
        return mainCategoryRepository.findAll();
    }

    public MaincategoryEntity getMainCategoryById(int id) {
        return mainCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MainCategory not found with id " + id));
    }

    public MaincategoryEntity saveMainCategory(@NonNull MaincategoryEntity mainCategory) {
        return mainCategoryRepository.save(mainCategory);
    }

    public void deleteMainCategory(int id) {
        mainCategoryRepository.deleteById(id);
    }

    public SubcategoryEntity getSubCategoryById(int id) {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id " + id));
    }

    public SubcategoryEntity saveSubCategory(@NonNull SubcategoryEntity subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public void deleteSubCategory(int id) {
        subCategoryRepository.deleteById(id);
    }

    public List<SubcategoryEntity> getSubCategoriesByMainCategoryId(int mainCategoryId) {
        return subCategoryRepository.findByMaincategoryId(mainCategoryId);
    }
}
