package com.mitocode.controller;

import com.mitocode.config.MapperConfig;
import com.mitocode.dto.PatientDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.exception.NewModelNotFoundException;
import com.mitocode.model.Patient;
import com.mitocode.service.impl.PatientServiceImpl;
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

import static org.springframework.http.HttpStatus.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
// @CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private final PatientServiceImpl patientService;

    @Qualifier("modelMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll(){
        List<PatientDTO> patients = patientService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(patients, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id){
        Patient patient = patientService.findById(id);
        /*
        if (patient == null) {
            throw new ModelNotFoundException("ID NOT FOUND : " + id);
            // throw new NewModelNotFoundException("ID NOT FOUND" + id);
        }*/
        return new ResponseEntity<>(this.convertToDTO(patient), OK);
    }

    /* @PostMapping
    public ResponseEntity<Patient> save(@RequestBody Patient patient){
        Patient obj = patientService.save(patient);
        return new ResponseEntity<>(obj, CREATED);
    } */

    @PostMapping
    public ResponseEntity<Void> save( @Valid @RequestBody PatientDTO dto){
        Patient obj = patientService.save(this.convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPatient()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody PatientDTO dto){
        dto.setIdPatient(id);
        Patient obj = patientService.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDTO(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        // Patient patient = patientService.findById(id);
        /*if (patient == null) {
            throw new ModelNotFoundException("ID NOT FOUND : " + id);
            // throw new NewModelNotFoundException("ID NOT FOUND" + id);
        }*/
        this.patientService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<PatientDTO> findByIdHateoas(@PathVariable("id") Integer id) {
        Patient patient = patientService.findById(id);
        EntityModel<PatientDTO> resource = EntityModel.of(this.convertToDTO(patient));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLanguage("EN"));

        resource.add(link1.withRel("patient-info"));
        resource.add(link1.withRel("language-info"));

        return resource;
    }

    private PatientDTO convertToDTO(Patient patient) {
        return mapper.map(patient, PatientDTO.class);
    }

    private Patient convertToEntity(PatientDTO dto) {
        return mapper.map(dto, Patient.class);
    }
}
