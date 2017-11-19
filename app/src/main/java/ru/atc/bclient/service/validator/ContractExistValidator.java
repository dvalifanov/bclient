package ru.atc.bclient.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.atc.bclient.service.annotation.ContractExist;
import ru.atc.bclient.model.repository.ContractRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContractExistValidator implements ConstraintValidator<ContractExist, String> {
    @Autowired
    ContractRepository contractRepository;

    @Override
    public void initialize(ContractExist contractExist) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !(contractRepository.findContractByNum(s) == null);
    }
}
