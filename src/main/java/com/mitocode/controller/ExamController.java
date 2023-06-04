package com.mitocode.controller;

import com.mitocode.dto.ExamDTO;
import com.mitocode.model.Exam;
import com.mitocode.service.impl.ExamServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamServiceImpl examService;

    @Qualifier("modelMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExamDTO>> findAll(){
        List<ExamDTO> exams = examService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(exams, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id){
        Exam Exam = examService.findById(id);
        return new ResponseEntity<>(this.convertToDTO(Exam), OK);
    }

    @PostMapping
    public ResponseEntity<Void> save( @Valid @RequestBody ExamDTO dto){
        Exam obj = examService.save(this.convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdExam()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody ExamDTO dto){
        dto.setIdExam(id);
        Exam obj = examService.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDTO(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        this.examService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<ExamDTO> findByIdHateoas(@PathVariable("id") Integer id) {
        Exam Exam = examService.findById(id);
        EntityModel<ExamDTO> resource = EntityModel.of(this.convertToDTO(Exam));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLanguage("EN"));

        resource.add(link1.withRel("Exam-info"));
        resource.add(link1.withRel("language-info"));

        return resource;
    }

    private ExamDTO convertToDTO(Exam exam) {
        return mapper.map(exam, ExamDTO.class);
    }

    private Exam convertToEntity(ExamDTO dto) {
        return mapper.map(dto, Exam.class);
    }
}
