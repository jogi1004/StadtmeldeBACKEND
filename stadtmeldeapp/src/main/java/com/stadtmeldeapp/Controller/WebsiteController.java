package com.stadtmeldeapp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.DTO.ReportPictureDTO;
import com.stadtmeldeapp.service.CategoryService;
import com.stadtmeldeapp.service.ReportService;
import com.stadtmeldeapp.service.UserService;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.Entity.UserEntity;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.Base64;

@Controller
public class WebsiteController {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private ReportService reportService;

  @Autowired
  private UserService userService;

  @GetMapping("/")
  public String index(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if(userEntity != null){
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("home", true);
    return "landingPage";
  }

  @GetMapping("/cityInfo")
  public String cityInfo(RedirectAttributes redirectAttributes, HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if(userEntity != null){
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("cityInfo", true);
    logger.info("GET CATEGORYS");
    List<MaincategoryEntity> mainCategory = categoryService.getMaincategoriesByLocationName("Zweibrücken");
    List<SubcategoryEntity> subCategory = new ArrayList<SubcategoryEntity>();
    ArrayList<SubcategoryEntity> subCategoryList = new ArrayList<SubcategoryEntity>();
    for (MaincategoryEntity maincategoryEntity : mainCategory) {
      int mainCategoryId = (int)maincategoryEntity.getId();
      subCategory = categoryService.getSubCategoriesByMainCategoryId(mainCategoryId);
      for (SubcategoryEntity subcategoryEntity : subCategory) {
        subCategoryList.add(subcategoryEntity);
      }   
    } 
    if (mainCategory != null) {
      model.addAttribute("MainCategories", mainCategory);
      model.addAttribute("SubCategories", subCategoryList);
      return "cityInfo";
    }
    logger.info("Main Category not found.");
    return "cityInfo";
  }

  @GetMapping("/services")
  public String services(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if(userEntity != null){
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("services", true);
    return "services";
  }

  @GetMapping("/aboutUs")
  public String aboutUs(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if(userEntity != null){
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("aboutUs", true);
    return "aboutUs";
  }

  @GetMapping("/contact")
  public String contact(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if(userEntity != null){
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("token", session.getAttribute("token"));
    model.addAttribute("contact", true);
    return "contact";
  }

  @GetMapping("/reports")
  public String reports(HttpSession session, Model model) throws NotFoundException {
    UserEntity userEntity = userService.getUserByAuthentication();
    if(userEntity != null){
      model.addAttribute("User", userEntity);
    }
    model.addAttribute("reports", true);
    logger.info("GET REPORTS");
    List<ReportInfoDTO> reports = reportService.getReportsByReportingLocationId(1);
    if (reports != null) {
      List<ReportPictureDTO> reportList = new ArrayList<ReportPictureDTO>();
      for (ReportInfoDTO reportInfo : reports) {
        String iconBase64 = "X";
        String imageBase64 = "X";
        if (reportInfo.getIcon() != null) {
          byte[] iconBytes = reportInfo.getIcon();
          iconBase64 = Base64.getEncoder().encodeToString(iconBytes);
        }
        if (reportInfo.getImage() != null){
          byte[] imageBytes = reportInfo.getImage();
          imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
        }
        reportList.add(new ReportPictureDTO(reportInfo.getTitleOrsubcategoryName(), iconBase64,
            reportInfo.getTimestamp(), imageBase64, reportInfo.getLongitude(), reportInfo.getLatitude()));
      }
      model.addAttribute("Reports", reportList);
    }
    else {
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
