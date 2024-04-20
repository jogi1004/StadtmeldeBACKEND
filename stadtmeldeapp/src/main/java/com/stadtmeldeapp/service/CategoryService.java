package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.MainCategoryWithSubCategoriesDTO;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.Repository.MaincategoryRepository;
import com.stadtmeldeapp.Repository.SubcategoryRepository;

import java.util.ArrayList;
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

    public MaincategoryEntity getMainCategoryById(int id) throws NotFoundException {
        return mainCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hauptkategorie nicht gefunden"));
    }

    public MaincategoryEntity saveMainCategory(@NonNull MaincategoryEntity mainCategory) {
        return mainCategoryRepository.save(mainCategory);
    }

    public void deleteMainCategory(int id) {
        mainCategoryRepository.deleteById(id);
    }

    public SubcategoryEntity getSubCategoryById(int id) throws NotFoundException {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unterkategorie nicht gefunden"));
    }

    public SubcategoryEntity saveSubCategory(@NonNull SubcategoryEntity subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public void deleteSubCategory(int id) {
        subCategoryRepository.deleteById(id);
    }

    public List<SubcategoryEntity> getSubCategoriesByMainCategoryId(int mainCategoryId) {
        return subCategoryRepository.findByMaincategoryEntity_Id(mainCategoryId);
    }

    public List<MaincategoryEntity> getMaincategoriesByLocationName(String reportingLocationName) {
        return mainCategoryRepository.findByReportingLocationEntity_Name(reportingLocationName);
    }

    public List<MainCategoryWithSubCategoriesDTO> getMainCategoriesWithSubcategoriesByLocationName(String reportingLocationName) {
        List<MainCategoryWithSubCategoriesDTO> mainCategoryWithSubCategoriesDTOs = new ArrayList<MainCategoryWithSubCategoriesDTO>();
        
        List<MaincategoryEntity> mainCategory = getMaincategoriesByLocationName(reportingLocationName);
        
        for (MaincategoryEntity maincategoryEntity : mainCategory) {
            int mainCategoryId = maincategoryEntity.getId();
            List<SubcategoryEntity> subcategoryEntities = getSubCategoriesByMainCategoryId(mainCategoryId);
            mainCategoryWithSubCategoriesDTOs.add(new MainCategoryWithSubCategoriesDTO(maincategoryEntity, subcategoryEntities));
        }
        return mainCategoryWithSubCategoriesDTOs;
    }
}
