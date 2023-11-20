package com.allur.wbtemplatesservice.service;

import com.allur.wbtemplatesservice.model.Template;
import com.allur.wbtemplatesservice.model.TemplateDTO;
import com.allur.wbtemplatesservice.repositories.TemplateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public ResponseEntity<?> createTemplate(TemplateDTO templateDTO){
        Template template = new Template();
        template.setTemplate(templateDTO.getTemplate());
        template.setAnswer(templateDTO.getAnswer());
        addTemplate(template);

        return ResponseEntity.ok(new Template(template.getId(), template.getTemplate(), template.getAnswer()));
    }

    public ResponseEntity<?> deleteTemplate(Integer id){
        Template template = templateRepository.findById(id).orElse(null);
        templateRepository.deleteById(id);

        assert template != null;
        return ResponseEntity.ok(new Template(template.getId(), template.getTemplate(), template.getAnswer()));
    }

    public ResponseEntity<?> updateTemplate(Template template, Integer id){
        template.setId(id);
        templateRepository.save(template);
        return ResponseEntity.ok(new Template(template.getId(), template.getTemplate(), template.getAnswer()));
    }

    @Transactional
    public Collection<Template> getAllTemplate(){
        return templateRepository.findAll();
    }

    public void addTemplate(Template template){
        templateRepository.save(template);
    }
}
