package com.stadtmeldeapp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stadtmeldeapp.DTO.LoginDataDTO;
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.DTO.ReportPictureDTO;
import com.stadtmeldeapp.service.CategoryService;
import com.stadtmeldeapp.service.JwtService;
import com.stadtmeldeapp.service.ReportService;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.Base64;

@Controller
public class WebsiteController {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private ReportService reportService;

  /*
   * @Autowired
   * private ReportingLocationService reportingLocationService;
   */
  @GetMapping("/")
  public String index(HttpSession session, Model model) {
    model.addAttribute("token", session.getAttribute("token"));
    return "landingPage"; // This maps to index.html inside src/main/resources/templates
  }

  @GetMapping("/cityInfo")
  public String cityInfo(RedirectAttributes redirectAttributes, HttpSession session, Model model) {
    model.addAttribute("token", session.getAttribute("token"));
    logger.info("GET MAIN CATEGORY BY ID");
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
    logger.info("Subcategory." + subCategory.toString());
    if (mainCategory != null) {
      logger.info("Main Category found." + mainCategory.toString());
      model.addAttribute("MainCategories", mainCategory);
      model.addAttribute("SubCategories", subCategoryList);
      return "cityInfo";
    }
    logger.info("Main Category not found.");
    return "cityInfo"; // This maps to index.html inside src/main/resources/templates
  }

  @GetMapping("/services")
  public String services(HttpSession session, Model model) {
    model.addAttribute("token", session.getAttribute("token"));
    return "services"; // This maps to index.html inside src/main/resources/templates
  }

  @GetMapping("/aboutUs")
  public String aboutUs(HttpSession session, Model model) {
    model.addAttribute("token", session.getAttribute("token"));
    return "aboutUs"; // This maps to index.html inside src/main/resources/templates
  }

  @GetMapping("/contact")
  public String contact(HttpSession session, Model model) {
    model.addAttribute("token", session.getAttribute("token"));
    return "contact"; // This maps to index.html inside src/main/resources/templates
  }

  @GetMapping("/reports")
  public String reports(HttpSession session, Model model) {
    model.addAttribute("token", session.getAttribute("token"));
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
      logger.info("ReportList." + reportList.toString());
      model.addAttribute("Reports", reportList);
    }
    else {
      logger.info("Reports not found.");
    }
    return "reports"; // This maps to index.html inside src/main/resources/templates
  }


  @GetMapping("/login")
  public String showLoginForm(Model model) {
    model.addAttribute("LoginDataDTO", new LoginDataDTO());
    return "login";
  }

  @PostMapping("/loginWebsite")
  public String loginWebsite(@ModelAttribute("LoginDataDTO") LoginDataDTO request,
      RedirectAttributes redirectAttributes, HttpSession session) {
    logger.info("LOGIN");
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    logger.info("User " + request.getUsername() + " authenticated.");
    String token = jwtService.generateToken(userDetailsServiceImpl.loadUserByUsername(request.getUsername()));
    redirectAttributes.addFlashAttribute("User", request.getUsername());
    session.setAttribute("token", token);
    session.setAttribute("test", "test");
    return "redirect:/";
  }


}
