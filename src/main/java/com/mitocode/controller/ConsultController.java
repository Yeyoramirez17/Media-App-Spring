package com.mitocode.controller;

import com.mitocode.dto.ConsultDTO;
import com.mitocode.dto.ConsultListExamDTO;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.service.impl.ConsultServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
@RequestMapping("/consults")
@RequiredArgsConstructor
public class ConsultController {

    private final ConsultServiceImpl consultService;

    @Qualifier("modelMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ConsultDTO>> findAll(){
        List<ConsultDTO> consults = consultService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(consults, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultDTO> findById(@PathVariable("id") Integer id){
        Consult Consult = consultService.findById(id);
        return new ResponseEntity<>(this.convertToDTO(Consult), OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ConsultListExamDTO dto){
        Consult consult = this.convertToEntity(dto.getConsult());
        List<Exam> exams = mapper.map(dto.getLstExams(), new TypeToken<Exam>(){}.getType());
        Consult obj = consultService.saveTransactional(consult, exams);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult()).toUri();
        return ResponseEntity.created(location).build();
    }

    /*@PostMapping
    public ResponseEntity<Void> save( @Valid @RequestBody ConsultDTO dto){
        Consult obj = consultService.save(this.convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult()).toUri();
        return ResponseEntity.created(location).build();
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<ConsultDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody ConsultDTO dto){
        dto.setIdConsult(id);
        Consult obj = consultService.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDTO(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        this.consultService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<ConsultDTO> findByIdHateoas(@PathVariable("id") Integer id) {
        Consult consult = consultService.findById(id);
        EntityModel<ConsultDTO> resource = EntityModel.of(this.convertToDTO(consult));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLanguage("EN"));

        resource.add(link1.withRel("Consult-info"));
        resource.add(link1.withRel("language-info"));

        return resource;
    }

    private ConsultDTO convertToDTO(Consult consult) {
        return mapper.map(consult, ConsultDTO.class);
    }

    private Consult convertToEntity(ConsultDTO dto) {
        return mapper.map(dto, Consult.class);
    }
}
