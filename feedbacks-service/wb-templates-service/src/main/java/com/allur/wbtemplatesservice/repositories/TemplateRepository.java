package com.allur.wbtemplatesservice.repositories;

import com.allur.wbtemplatesservice.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
}
