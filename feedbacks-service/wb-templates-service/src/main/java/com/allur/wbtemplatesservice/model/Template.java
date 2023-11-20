package com.allur.wbtemplatesservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "answer_template")
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String template;
    private String answer;

    public Template(String template, String answer) {
        this.template = template;
        this.answer = answer;
    }
}
