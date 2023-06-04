package com.mitocode.service.impl;

import com.mitocode.model.Specialty;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IPatientRepository;
import com.mitocode.repo.ISpecialtyRepository;
import com.mitocode.service.IPatientService;
import com.mitocode.service.ISpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl extends CRUDImpl<Specialty, Integer> implements ISpecialtyService {

    private  final ISpecialtyRepository patientRepository;

    @Override
    protected IGenericRepo<Specialty, Integer> getRepo() {
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
