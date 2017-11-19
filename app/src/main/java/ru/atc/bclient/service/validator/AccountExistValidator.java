package ru.atc.bclient.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.atc.bclient.service.annotation.AccountExist;
import ru.atc.bclient.model.repository.AccountRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountExistValidator implements ConstraintValidator<AccountExist, String> {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void initialize(AccountExist accountExist) {

    }


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(accountRepository == null)
            return true;
        return !(accountRepository.findAccountByNum(s) == null);

    }
}
