package ru.atc.bclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.atc.bclient.model.entity.*;
import ru.atc.bclient.model.repository.AccountBalanceRepository;
import ru.atc.bclient.model.repository.UserRepository;
import ru.atc.bclient.service.dto.AccountForm;
import ru.atc.bclient.service.dto.LegalEntityForm;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegalEntityService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountBalanceRepository accountBalanceRepository;

    public List<LegalEntityForm>  getLegalEntityFormsOfCurrentUser() {
        List<LegalEntityForm> legalEntityForms = new ArrayList<>();

        // get logged in user
        org.springframework.security.core.userdetails.User loggedUserDetails = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = loggedUserDetails.getUsername();
        User loggedUser = userRepository.findUserByLogin(login);

        // it fills array of legal entity forms. Each form contains information about name organisation, account and balance.
        for(UserLegalEntity userLegalEntity : loggedUser.getUserLegalEntities()) {
            LegalEntityForm legalEntityForm = new LegalEntityForm();
            legalEntityForm.setFullName(userLegalEntity.getLegalEntity().getFullName());
            List<AccountForm> accountForms = new ArrayList<>();
            for (Account account : userLegalEntity.getLegalEntity().getAccounts()) {
                AccountForm accountForm = new AccountForm();
                accountForm.setName(account.getName());
                accountForm.setNum(account.getNum());
                accountForm.setBalanceAmt(accountBalanceRepository.findFirstByAccountOrderByDateDesc(account).getAmt());
                accountForms.add(accountForm);
            }
            legalEntityForm.setAccountForms(accountForms);
            legalEntityForms.add(legalEntityForm);
        }
        return legalEntityForms;
    }
}
