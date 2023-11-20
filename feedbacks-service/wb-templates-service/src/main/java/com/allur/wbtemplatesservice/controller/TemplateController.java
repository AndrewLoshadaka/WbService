package com.allur.wbtemplatesservice.controller;

import com.allur.wbtemplatesservice.model.Template;
import com.allur.wbtemplatesservice.model.TemplateDTO;
import com.allur.wbtemplatesservice.service.TemplateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@AllArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/all")
    public Collection<Template> getAll(){
        return templateService.getAllTemplate();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTemplate(@RequestBody TemplateDTO templateDTO){
        return templateService.createTemplate(templateDTO);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTemplate(@RequestBody Map<String, Integer> map){
        return templateService.deleteTemplate(map.get("id"));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTemplate(
            @RequestBody Template template,
            @RequestParam Integer id
    ){
        return templateService.updateTemplate(template, id);
    }
}
