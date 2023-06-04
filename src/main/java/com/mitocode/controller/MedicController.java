package com.mitocode.controller;

import com.mitocode.dto.MedicDTO;
import com.mitocode.model.Medic;
import com.mitocode.service.impl.MedicServiceImpl;
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
@RequestMapping("/medics")
@RequiredArgsConstructor
public class MedicController {

    private final MedicServiceImpl medicService;

    @Qualifier("medicMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<MedicDTO>> findAll(){
        List<MedicDTO> medics = medicService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(medics, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id){
        Medic medic = medicService.findById(id);
        return new ResponseEntity<>(this.convertToDTO(medic), OK);
    }

    @PostMapping
    public ResponseEntity<Void> save( @Valid @RequestBody MedicDTO dto){
        Medic obj = medicService.save(this.convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMedic()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody MedicDTO dto){
        dto.setIdMedic(id);
        Medic obj = medicService.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDTO(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        this.medicService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<MedicDTO> findByIdHateoas(@PathVariable("id") Integer id) {
        Medic Medic = medicService.findById(id);
        EntityModel<MedicDTO> resource = EntityModel.of(this.convertToDTO(Medic));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLanguage("EN"));

        resource.add(link1.withRel("Medic-info"));
        resource.add(link1.withRel("language-info"));

        return resource;
    }

    private MedicDTO convertToDTO(Medic medic) {
        return mapper.map(medic, MedicDTO.class);
    }

    private Medic convertToEntity(MedicDTO dto) {
        return mapper.map(dto, Medic.class);
    }
}
