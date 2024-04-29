package com.stadtmeldeapp.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "report_visuals")
@Data
public class ReportVisualsEntity {

    @Id
    private int id;
    @OneToOne
    @MapsId
    private BasicReportEntity report;

    @ManyToOne
    @JoinColumn(name = "icons_id")
    private IconEntity icon;

    @OneToOne
    @JoinColumn(name = "reportimages_id")
    private ReportImageEntity reportImage;

}
