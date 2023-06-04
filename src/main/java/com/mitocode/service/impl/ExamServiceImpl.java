package com.mitocode.service.impl;

import com.mitocode.model.Exam;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IExamRepository;
import com.mitocode.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends CRUDImpl<Exam, Integer> implements IExamService {

    private  final IExamRepository patientRepository;

    @Override
    protected IGenericRepo<Exam, Integer> getRepo() {
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
