package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ReportDTO;
import com.stadtmeldeapp.DTO.ReportDetailInfoDTO;
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.DTO.ReportUpdateDTO;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ProfilePictureEntity;
import com.stadtmeldeapp.Entity.ReportEntity;
import com.stadtmeldeapp.Entity.ReportPictureEntity;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.Repository.ProfilePictureRepository;
import com.stadtmeldeapp.Repository.ReportPictureRepository;
import com.stadtmeldeapp.Repository.MaincategoryRepository;
import com.stadtmeldeapp.Repository.ReportRepository;
import com.stadtmeldeapp.Repository.ReportingLocationRepository;
import com.stadtmeldeapp.Repository.StatusRepository;
import com.stadtmeldeapp.Repository.SubcategoryRepository;
import com.stadtmeldeapp.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportingLocationRepository reportingLocationRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private MaincategoryRepository maincategoryRepository;
    @Autowired
    private ProfilePictureRepository imageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportPictureRepository reportPictureRepository;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMMM yyyy, HH:mm 'Uhr'");

    public ReportEntity createReport(ReportDTO reportDto, String username)
            throws NotFoundException, IOException, NotAllowedException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
        ReportingLocationEntity reportingLocation = reportingLocationRepository
                .findReportingLocationByName(reportDto.reportingLocationName())
                .orElseThrow(() -> new NotFoundException("Meldeort nicht gefunden"));
        MaincategoryEntity maincategory = maincategoryRepository
                .findByTitleAndReportingLocationEntityId(reportDto.mainCategoryName(), reportingLocation.getId())
                .orElseThrow(() -> new NotFoundException("Hauptkategorie nicht gefunden"));
        SubcategoryEntity subcategory = subcategoryRepository
                .findByTitleAndMaincategoryEntityId(reportDto.subCategoryName(), maincategory.getId())
                .orElseThrow(() -> new NotFoundException("Unterkategorie nicht gefunden"));
        List<StatusEntity> status = statusRepository.findByReportingLocationEntityId(reportingLocation.getId());

        StatusEntity statusX = null;
        if (!status.isEmpty()) {
            statusX = status.get(0);
        }

        ReportEntity report = new ReportEntity();
        if (reportDto.additionalPicture() != null) {
            ReportPictureEntity reportPictureEntity = new ReportPictureEntity(reportDto.additionalPicture());
            reportPictureRepository.save(reportPictureEntity);
            report.setReportPictureEntity(reportPictureEntity);
            report.setReportPictureId(reportPictureEntity.getId());
        } else {
            report.setReportPictureEntity(null);
            report.setReportPictureId(null);
        }

        report.setSubcategory(subcategory);
        if (reportDto.title() != null)
            report.setTitle(reportDto.title());
        report.setDescription(reportDto.description());
        report.setLongitude(reportDto.longitude());
        report.setLatitude(reportDto.latitude());
        report.setReportingLocation(reportingLocation);
        report.setUser(user);
        report.setStatus(statusX);

        if (reportDto.additionalPicture() != null) {
            try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
                Image img = Image.newBuilder().setContent(ByteString.copyFrom(reportDto.additionalPicture())).build();
                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Feature.Type.FACE_DETECTION))
                        .setImage(img)
                        .build();
                BatchAnnotateImagesResponse response = vision.batchAnnotateImages(List.of(request));
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(reportDto.additionalPicture()));
                Graphics graphics = bufferedImage.getGraphics();
                for (AnnotateImageResponse res : response.getResponsesList()) {
                    if (res.getFaceAnnotationsList() != null) {
                        for (FaceAnnotation face : res.getFaceAnnotationsList()) {
                            int x = (int) face.getFdBoundingPoly().getVertices(0).getX();
                            int y = (int) face.getFdBoundingPoly().getVertices(0).getY();
                            int width = (int) (face.getFdBoundingPoly().getVertices(2).getX() - x);
                            int height = (int) (face.getFdBoundingPoly().getVertices(2).getY() - y);
                            BufferedImage faceRegion = bufferedImage.getSubimage(x, y, width, height);
                            BufferedImage blurredFace = pixelateImage(faceRegion, width/5);
                            graphics.drawImage(blurredFace, x, y, null);
                        }
                    }
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos);
                
                ReportPictureEntity reportPictureEntity = new ReportPictureEntity(baos.toByteArray());
                reportPictureRepository.save(reportPictureEntity);
                report.setReportPictureEntity(reportPictureEntity);
                report.setReportPictureId(reportPictureEntity.getId());
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return reportRepository.save(report);
    }

    public ReportEntity getReportById(int id) {
        return reportRepository.findById(id).orElse(null);
    }

    public List<ReportInfoDTO> getReportsByUserId(int userId) {
        return toInfoDTOList(reportRepository.findAllByUserId(userId));

    }

    public List<ReportInfoDTO> getReportsByUserId(HttpServletRequest request) throws NotFoundException {
        int userId = userService.getUserFromRequest(request).getId();
        return getReportsByUserId(userId);
    }

    public List<ReportInfoDTO> getReportsByReportingLocationId(int reportingLocationId) {
        return toInfoDTOList(reportRepository.findAllByReportingLocationId(reportingLocationId));
    }

    public List<ReportEntity> getReportEntitiesByReportingLocationId(int reportingLocationId) {
        return reportRepository.findAllByReportingLocationId(reportingLocationId);
    }

    public List<ReportInfoDTO> getReportsByReportingLocationName(String reportingLocationTitle) {
        return toInfoDTOList(reportRepository.findAllByReportingLocationName(reportingLocationTitle));
    }

    public ReportDetailInfoDTO getReportDetails(int id) throws NotFoundException {
        ReportEntity report = reportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));
        Optional<ProfilePictureEntity> userProfilePicture = imageRepository
                .findById(report.getUser().getProfilePictureId());
        return new ReportDetailInfoDTO(
                (report.getTitle() == null || report.getTitle().isBlank()) ? report.getSubcategory().getTitle()
                        : report.getTitle(),
                report.getDescription(),
                report.getSubcategory().getMaincategoryEntity().getIconEntity().getId(), report.getStatus(),
                dateFormat.format(report.getReportingTimestamp()), report.getReportPictureId(),
                report.getReportPictureEntity().getPicture(),
                report.getLongitude(),
                report.getLatitude(), report.getUser().getUsername(), report.getReportingLocation().getName(),
                userProfilePicture.isPresent() ? userProfilePicture.get().getImage() : null);
    }

    public List<ReportInfoDTO> getLatestReportsByReportingLocationId(int id) {
        return toInfoDTOList(reportRepository.findFirst10ByReportingLocationIdOrderByReportingTimestampDesc(id));
    }

    public ReportInfoDTO updateReport(int reportId, ReportUpdateDTO reportDto,
            HttpServletRequest request) throws NotAllowedException, NotFoundException {

        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));

        if (!report.getUser().equals(userService.getUserFromRequest(request))) {
            throw new NotAllowedException("Keine Berechtigung!");
        }
        if (reportDto.title() != null && !report.getSubcategory().getTitle().equals("Sonstiges")) {
            throw new NotAllowedException("Meldungen außerhalb der Kategorie 'Sonstiges' haben keinen Titel");
        }
        if (reportDto.additionalPicture() != null) {
            if (report.getReportPictureId() != null) {
                reportPictureRepository.delete(report.getReportPictureEntity());
            }
            ReportPictureEntity reportPictureEntity = new ReportPictureEntity(reportDto.additionalPicture());
            reportPictureRepository.save(reportPictureEntity);

            report.setReportPictureId(reportPictureEntity.getId());
            report.setReportPictureEntity(reportPictureEntity);
        }
        report.setTitle(reportDto.title());
        report.setDescription(reportDto.description());
        return toReportInfoDTO(reportRepository.save(report));
    }

    public ReportInfoDTO updateReportStatus(int reportId, String newStatus)
            throws NotAllowedException, NotFoundException {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));

        if (!report.getReportingLocation().equals(userService.getUserByAuthentication().getAdminForLocation())) {
            throw new NotAllowedException("Keine Berechtigung!");
        }
        StatusEntity status = statusRepository
                .findByReportingLocationEntityIdAndName(report.getReportingLocation().getId(), newStatus)
                .orElseThrow(() -> new NotFoundException("Status nicht gefunden"));
        report.setStatus(status);
        return toReportInfoDTO(report);
    }

    public void deleteReport(int reportId, HttpServletRequest request)
            throws NotFoundException, NotAllowedException {

        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));

        UserEntity user = userService.getUserFromRequest(request);

        if (!report.getUser().equals(user)) {
            throw new NotAllowedException("Keine Berechtigung");
        }

        reportRepository.delete(report);
    }

    public List<ReportInfoDTO> toInfoDTOList(List<ReportEntity> reports) {
        List<ReportInfoDTO> retReports = new ArrayList<>();
        for (ReportEntity r : reports) {
            retReports
                    .add(new ReportInfoDTO(
                            (r.getTitle() == null || r.getTitle().isBlank()) ? r.getSubcategory().getTitle()
                                    : r.getTitle(),
                            (r.getSubcategory().getMaincategoryEntity().getIconEntity() == null ? -1
                                    : r.getSubcategory().getMaincategoryEntity().getIconEntity().getId()),
                            r.getStatus(), dateFormat.format(r.getReportingTimestamp()), r.getReportPictureId(),
                            r.getLongitude(),
                            r.getLatitude()));
        }
        return retReports;
    }

    public ReportInfoDTO toReportInfoDTO(ReportEntity report) {
        return new ReportInfoDTO(
                (report.getTitle() == null || report.getTitle().isBlank()) ? report.getSubcategory().getTitle()
                        : report.getTitle(),
                (report.getSubcategory().getMaincategoryEntity().getIconEntity() == null ? -1
                        : report.getSubcategory().getMaincategoryEntity().getIconEntity().getId()),
                report.getStatus(), dateFormat.format(report.getReportingTimestamp()), report.getReportPictureId(),
                report.getLongitude(),
                report.getLatitude());
    }

    public ReportInfoDTO toReportInfoDTO(ReportDetailInfoDTO report) {
        return new ReportInfoDTO(
                report.titleOrsubcategoryName(),
                report.iconId(), report.status(), report.timestamp(), report.reportPictureId(),
                report.longitude(),
                report.latitude());
    }

    public BufferedImage blurImage(BufferedImage image) {
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {
                ninth, ninth, ninth,
                ninth, ninth, ninth,
                ninth, ninth, ninth
        };

        Map<RenderingHints.Key, Object> map = new HashMap<>();
        map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RenderingHints hints = new RenderingHints(map);
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
        return op.filter(image, null);
    }
    
    public BufferedImage pixelateImage(BufferedImage image, int pixelSize) {
        BufferedImage pixelatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    
        for (int y = 0; y < image.getHeight(); y += pixelSize) {
            for (int x = 0; x < image.getWidth(); x += pixelSize) {
                int averageColor = getAverageColor(image, x, y, pixelSize);
                for (int yd = y; (yd < y + pixelSize) && (yd < pixelatedImage.getHeight()); yd++) {
                    for (int xd = x; (xd < x + pixelSize) && (xd < pixelatedImage.getWidth()); xd++) {
                        pixelatedImage.setRGB(xd, yd, averageColor);
                    }
                }
            }
        }
    
        return pixelatedImage;
    }
    
    public int getAverageColor(BufferedImage image, int x, int y, int pixelSize) {
        long sumRed = 0;
        long sumGreen = 0;
        long sumBlue = 0;
        long count = 0;
    
        for (int yd = y; (yd < y + pixelSize) && (yd < image.getHeight()); yd++) {
            for (int xd = x; (xd < x + pixelSize) && (xd < image.getWidth()); xd++) {
                Color color = new Color(image.getRGB(xd, yd));
                sumRed += color.getRed();
                sumGreen += color.getGreen();
                sumBlue += color.getBlue();
                count++;
            }
        }
    
        int averageRed = (int) (sumRed / count);
        int averageGreen = (int) (sumGreen / count);
        int averageBlue = (int) (sumBlue / count);
    
        return new Color(averageRed, averageGreen, averageBlue).getRGB();
    }
}