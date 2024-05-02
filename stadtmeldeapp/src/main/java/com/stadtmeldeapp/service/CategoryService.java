package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.MainCategoryDTOw;
import com.stadtmeldeapp.DTO.MainCategoryWithSubCategoriesDTO;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.Repository.MaincategoryRepository;
import com.stadtmeldeapp.Repository.SubcategoryRepository;

import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private MaincategoryRepository mainCategoryRepository;

    @Autowired
    private SubcategoryRepository subCategoryRepository;

    @Autowired
    private UserService userService;
    public List<MaincategoryEntity> getAllMainCategories() {
        return mainCategoryRepository.findAll();
    }

    public MaincategoryEntity getMainCategoryById(int id) throws NotFoundException {
        return mainCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hauptkategorie nicht gefunden"));
    }

    public MaincategoryEntity saveMainCategory(@NonNull MaincategoryEntity mainCategory, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity isAdminForLocation = userService.getUserFromRequest(request).getAdminForLocation();
        if (! mainCategory.getReportingLocationEntity().equals(isAdminForLocation)) throw new NotAllowedException("Keine Berechtigung");
        return mainCategoryRepository.save(mainCategory);
    }

    public void deleteMainCategory(int id, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity isAdminForLocation = userService.getUserFromRequest(request).getAdminForLocation();
        if (!getMainCategoryById(id).getReportingLocationEntity().equals(isAdminForLocation)) throw new NotAllowedException("Keine Berechtigung");
        mainCategoryRepository.deleteById(id);
    }

    public SubcategoryEntity getSubCategoryById(int id) throws NotFoundException {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unterkategorie nicht gefunden"));
    }

    public SubcategoryEntity saveSubCategory(@NonNull SubcategoryEntity subCategory, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity isAdminForLocation = userService.getUserFromRequest(request).getAdminForLocation();
        if (!subCategory.getMaincategoryEntity().getReportingLocationEntity().equals(isAdminForLocation)) throw new NotAllowedException("Keine Berechtigung");
        return subCategoryRepository.save(subCategory);
    }

    public void deleteSubCategory(int id, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity isAdminForLocation = userService.getUserFromRequest(request).getAdminForLocation();
        ReportingLocationEntity subCategoryLocation = getSubCategoryById(id).getMaincategoryEntity().getReportingLocationEntity();
        if (!subCategoryLocation.equals(isAdminForLocation)) throw new NotAllowedException("Keine Berechtigung");
        subCategoryRepository.deleteById(id);
    }

    public List<SubcategoryEntity> getSubCategoriesByMainCategoryId(int mainCategoryId) {
        return subCategoryRepository.findByMaincategoryEntity_Id(mainCategoryId);
    }

    public List<MainCategoryDTOw> getMaincategoriesByLocationName(String reportingLocationName) {
        List<MaincategoryEntity> maincategoryEntities = mainCategoryRepository.findByReportingLocationEntity_Name(reportingLocationName);
        List<MainCategoryDTOw> mainCategoryDTOs = new ArrayList<MainCategoryDTOw>();
        for(MaincategoryEntity mainCategory : maincategoryEntities) {
            MainCategoryDTOw mainCategoryDTO = new MainCategoryDTOw(mainCategory.getId(), mainCategory.getTitle(), mainCategory.getReportingLocationEntity().getId(), mainCategory.getIconId());
            mainCategoryDTOs.add(mainCategoryDTO);
        }
        return mainCategoryDTOs;
    }

    public List<MainCategoryWithSubCategoriesDTO> getMainCategoriesWithSubcategoriesByLocationName(String reportingLocationName) {
        List<MainCategoryWithSubCategoriesDTO> mainCategoryWithSubCategoriesDTOs = new ArrayList<MainCategoryWithSubCategoriesDTO>();
        
        List<MaincategoryEntity> mainCategory = mainCategoryRepository.findByReportingLocationEntity_Name(reportingLocationName);
        
        for (MaincategoryEntity maincategoryEntity : mainCategory) {
            int mainCategoryId = maincategoryEntity.getId();
            List<SubcategoryEntity> subcategoryEntities = getSubCategoriesByMainCategoryId(mainCategoryId);
            mainCategoryWithSubCategoriesDTOs.add(new MainCategoryWithSubCategoriesDTO(maincategoryEntity, subcategoryEntities));
        }
        return mainCategoryWithSubCategoriesDTOs;
    }
}
