package com.mitocode.controller;

import com.mitocode.dto.SpecialtyDTO;
import com.mitocode.model.Specialty;
import com.mitocode.service.impl.SpecialtyServiceImpl;
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
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyServiceImpl specialtyService;

    @Qualifier("modelMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> findAll(){
        List<SpecialtyDTO> specialties = specialtyService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(specialties, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id){
        Specialty specialty = specialtyService.findById(id);
        return new ResponseEntity<>(this.convertToDTO(specialty), OK);
    }

    @PostMapping
    public ResponseEntity<Void> save( @Valid @RequestBody SpecialtyDTO dto){
        Specialty obj = specialtyService.save(this.convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSpecialty()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody SpecialtyDTO dto){
        dto.setIdSpecialty(id);
        Specialty obj = specialtyService.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDTO(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        this.specialtyService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<SpecialtyDTO> findByIdHateoas(@PathVariable("id") Integer id) {
        Specialty Specialty = specialtyService.findById(id);
        EntityModel<SpecialtyDTO> resource = EntityModel.of(this.convertToDTO(Specialty));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLanguage("EN"));

        resource.add(link1.withRel("Specialty-info"));
        resource.add(link1.withRel("language-info"));

        return resource;
    }

    private SpecialtyDTO convertToDTO(Specialty specialty) {
        return mapper.map(specialty, SpecialtyDTO.class);
    }

    private Specialty convertToEntity(SpecialtyDTO dto) {
        return mapper.map(dto, Specialty.class);
    }
}
