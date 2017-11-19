package ru.atc.bclient.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.atc.bclient.service.annotation.LegalEntityExist;
import ru.atc.bclient.model.repository.LegalEntityRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LegalEntityExsistValidator implements ConstraintValidator<LegalEntityExist, String> {

    @Autowired
    LegalEntityRepository legalEntityRepository;

    @Override
    public void initialize(LegalEntityExist legalEntityExist) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(legalEntityRepository == null)
            return true;
        return !(legalEntityRepository.findByShortName(s) == null);
    }
}
