package com.allur.wbstatserver.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class StatEntity {
    @Id
    @Column(name = "nmId")
    private int nmId;
    @Column(name = "prgroup")
    private String prGroup;
    @Column(name = "tp_stream")
    private String tpStream;
}
