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
@Table(name = "basic_reports")
@Data
public class BasicReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title_or_subcategory_name", nullable = false)
    private String titleOrSubcategoryName;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;
    
    @Column(name = "reporting_timestamp", nullable = false)
    @CreationTimestamp
    private Timestamp reportingTimestamp;

    @ManyToOne
    @JoinColumn(name = "reporting_location_id", referencedColumnName = "id", nullable = false)
    private ReportingLocationEntity reportingLocation;
}

