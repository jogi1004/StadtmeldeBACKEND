package com.stadtmeldeapp.Entity;

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
@Table(name = "maincategories")
@Data
public class MaincategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String title;

    @OneToOne
    @JoinColumn(name = "reporting_location_id", nullable = false)
    private ReportingLocationEntity reportingLocationEntity;

    @Column(name = "icon_id", nullable = true)
    private Integer iconId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id", referencedColumnName = "id", nullable = true, insertable=false, updatable=false)
    private IconEntity iconEntity;
}
