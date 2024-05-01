package com.stadtmeldeapp.Entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reports")
@Data
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private SubcategoryEntity subcategory;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;
    
    @Column(name = "reporting_timestamp", nullable = false)
    @CreationTimestamp
    private Timestamp reportingTimestamp;

    @Column(name = "report_picture_id", nullable = true)
    private Integer reportPictureId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "report_picture_id", referencedColumnName = "id", nullable = true, insertable=false, updatable=false)
    private ReportPictureEntity reportPictureEntity;

    @ManyToOne
    @JoinColumn(name = "reporting_location_id", referencedColumnName = "id", nullable = false)
    private ReportingLocationEntity reportingLocation;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StatusEntity status;
}

