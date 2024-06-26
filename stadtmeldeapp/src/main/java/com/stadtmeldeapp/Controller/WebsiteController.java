package com.stadtmeldeapp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.MainCategoryDTO;
import com.stadtmeldeapp.DTO.MainCategoryWithSubCategoriesDTO;
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.DTO.ReportPictureDTO;
import com.stadtmeldeapp.service.CategoryService;
import com.stadtmeldeapp.service.ReportService;
import com.stadtmeldeapp.service.StatusService;
import com.stadtmeldeapp.service.UserService;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ReportEntity;
import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.Entity.UserEntity;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebsiteController {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private ReportService reportService;

  @Autowired
  private UserService userService;

  @Autowired
  private StatusService statusService;

  @GetMapping("/")
  public String index(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("home", true);
    return "landingPage";
  }

  @GetMapping("/category")
  public String cityInfo(HttpSession session, Model model)
      throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("category", true);
    logger.info("GET CATEGORYS");
    List<MainCategoryDTO> mainCategory = categoryService.getMaincategoriesByLocationName("Bexbach");
    if (mainCategory.size() > 0) {
      model.addAttribute("MainCategories", mainCategory);
      return "category";
    }
    logger.info("Main Categories not found.");
    model.addAttribute("MainCategories", null);
    return "category";
  }

  @GetMapping("/subcategory")
  public String getSubcategory(@RequestParam("maincategoryId") int maincategoryId, HttpSession session, Model model)
      throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("category", true);
    logger.info("GET CATEGORYS");
    MaincategoryEntity mainCategory = categoryService.getMainCategoryById(maincategoryId);
    List<SubcategoryEntity> subcategories = categoryService.getSubCategoryEntitiesByMainCategoryId(maincategoryId);
    if (mainCategory != null) {
      model.addAttribute("MainCategory", mainCategory);
      if (subcategories.size() > 0) {
        model.addAttribute("SubCategories", subcategories);
        return "subcategory";
      }
      model.addAttribute("SubCategories", null);
      return "subcategory";
    }
    logger.info("Main Categories not found.");
    model.addAttribute("MainCategory", null);
    model.addAttribute("SubCategories", null);
    return "subcategory";
  }

  @GetMapping("/services")
  public String services(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("services", true);
    return "services";
  }

  @GetMapping("/overview")
  public String overview(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("overview", true);

    List<MainCategoryWithSubCategoriesDTO> mainCategoryWithSubCategories = categoryService
        .getMainCategoriesWithSubcategoriesByLocationName(
            /* userEntity.getAdminForLocation().getName() */ "Zweibrücken");
    model.addAttribute("MainCategories", mainCategoryWithSubCategories);

    List<ReportInfoDTO> reportInfoDTOs = reportService.getLatestReportsByReportingLocationId(/*
                                                                                              * userEntity.
                                                                                              * getAdminForLocation().
                                                                                              * getId()
                                                                                              */ 1);
    model.addAttribute("Reports", reportInfoDTOs);

    List<StatusEntity> statusEntities = statusService.getStatusByReportingLocationId(/*
                                                                                      * userEntity.getAdminForLocation()
                                                                                      * .getId()
                                                                                      */ 1);
    model.addAttribute("Status", statusEntities);
    return "overview";
  }

  @GetMapping("/aboutUs")
  public String aboutUs(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("aboutUs", true);
    return "aboutUs";
  }

  @GetMapping("/contact")
  public String contact(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("contact", true);
    return "contact";
  }

  @GetMapping("/status")
  public String status(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }

    List<StatusEntity> statusEntities =  statusService.getStatusByReportingLocationId(1);
    model.addAttribute("Status", statusEntities);
    
    model.addAttribute("status", true);
    return "status";
  }

  @GetMapping("/reports")
  public String reports(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if (userEntity != null) {
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("reports", true);
    logger.info("GET REPORTS");
    List<ReportEntity> reports = reportService.getReportEntitiesByReportingLocationId(1);
    if (reports != null) {
      List<ReportPictureDTO> reportListNew = new ArrayList<ReportPictureDTO>();
      List<ReportPictureDTO> reportListInProgress = new ArrayList<ReportPictureDTO>();
      List<ReportPictureDTO> reportListDone = new ArrayList<ReportPictureDTO>();
      List<ReportPictureDTO> reportListRejected = new ArrayList<ReportPictureDTO>();
      for (ReportEntity report : reports) {
        String iconBase64 = "X";
        Integer reportPictureId = null;
        /*
         * if (report.getIcon() != null) {
         * byte[] iconBytes = report.getIcon();
         * iconBase64 = Base64.getEncoder().encodeToString(iconBytes);
         * }
         */

        // ICON ist Noch nicht in der ReportEntity Klasse, ODER wo kommt das ICON her???

        if (report.getReportPictureId() != null) {
          reportPictureId = report.getReportPictureId();
        }
        String titleOrSubCategory;
        if (report.getTitle() == null || report.getTitle().equals("")) {
          titleOrSubCategory = report.getSubcategory().getTitle();
        } else {
          titleOrSubCategory = report.getTitle();
        }

        if (report.getStatus().getName().equals("Neu")) {
          reportListNew.add(new ReportPictureDTO(report.getId(), titleOrSubCategory, iconBase64,
              report.getReportingTimestamp(), reportPictureId, report.getLongitude(), report.getLatitude(),
              report.getStatus()));
        } else if (report.getStatus().getName().equals("In Bearbeitung")) {
          reportListInProgress.add(new ReportPictureDTO(report.getId(), titleOrSubCategory, iconBase64,
              report.getReportingTimestamp(), reportPictureId, report.getLongitude(), report.getLatitude(),
              report.getStatus()));
        } else if (report.getStatus().getName().equals("Abgeschlossen")) {
          reportListDone.add(new ReportPictureDTO(report.getId(), titleOrSubCategory, iconBase64,
              report.getReportingTimestamp(), reportPictureId, report.getLongitude(), report.getLatitude(),
              report.getStatus()));
        } else if (report.getStatus().getName().equals("Abgelehnt")) {
          reportListRejected.add(new ReportPictureDTO(report.getId(), titleOrSubCategory, iconBase64,
              report.getReportingTimestamp(), reportPictureId, report.getLongitude(), report.getLatitude(),
              report.getStatus()));
        }

      }
      model.addAttribute("ReportsNew", reportListNew);
      model.addAttribute("ReportsInProgress", reportListInProgress);
      model.addAttribute("ReportsDone", reportListDone);
      model.addAttribute("ReportsRejected", reportListRejected);
    } else {
      logger.info("Reports not found.");
    }
    return "reports";
  }

  @GetMapping("/login")
  public String showLoginForm(Model model) {
    model.addAttribute("login", true);
    return "login";
  }

}
