package com.stadtmeldeapp.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "reportimages")
@Data
@NoArgsConstructor
public class ReportImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private byte[] image;
}
