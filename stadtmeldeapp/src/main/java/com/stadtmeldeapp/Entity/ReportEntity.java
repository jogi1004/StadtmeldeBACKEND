package com.stadtmeldeapp.Entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    @Column(name = "latitude", nullable = true)
    private Double latitude;
    
    @Column(name = "reporting_timestamp", nullable = false)
    @CreationTimestamp
    private Timestamp reportingTimestamp;

    @ManyToOne
    @JoinColumn(name = "reporting_location_id", referencedColumnName = "id", nullable = false)
    private ReportingLocationEntity reportingLocation;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StatusEntity status;

    @Column(name = "additional_picture", nullable = true)
    private String additionalPicture;
}

