package com.mitocode.service.impl;

import com.mitocode.model.Patient;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IPatientRepository;
import com.mitocode.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl extends CRUDImpl<Patient, Integer> implements IPatientService {

    private  final IPatientRepository patientRepository;

    @Override
    protected IGenericRepo<Patient, Integer> getRepo() {
        return patientRepository;
    }

    /*
    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findById(Integer id) {
        Optional<Patient> opPatient = patientRepository.findById(id);
        return opPatient.isPresent() ? opPatient.get() : new Patient();
        // return patientRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        patientRepository.deleteById(id);
    }
     */
}
