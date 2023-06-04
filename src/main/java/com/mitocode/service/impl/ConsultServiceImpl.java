package com.mitocode.service.impl;

import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.repo.IConsultExamRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IConsultRepository;
import com.mitocode.service.IConsultService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultServiceImpl extends CRUDImpl<Consult, Integer> implements IConsultService {

    private  final IConsultRepository consultRepository;
    private final IConsultExamRepo ceRepository;

    @Override
    protected IGenericRepo<Consult, Integer> getRepo() {
        return consultRepository;
    }

    @Override
    @Transactional
    public Consult saveTransactional(Consult consult, List<Exam> exams) {
        consultRepository.save(consult);
        exams.forEach(exam -> ceRepository.saveExam(consult.getIdConsult(), exam.getIdExam()));
        return consult;
    }
}
