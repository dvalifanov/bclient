package ru.atc.bclient.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.atc.bclient.model.entity.*;
import ru.atc.bclient.service.dto.PaymentOrderForm;
import ru.atc.bclient.model.repository.*;

@Component
public class PaymentOrderValidator implements Validator {

    @Autowired
    LegalEntityRepository legalEntityRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserLegalEntityRepository userLegalEntityRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    AccountBalanceRepository accountBalanceRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return PaymentOrderForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PaymentOrderForm paymentOrderForm = (PaymentOrderForm) o;

        //getting entities that will be checked
        int i = 0;
        LegalEntity senderLegalEntity = legalEntityRepository.findByShortName(paymentOrderForm.getSenderLegalEntityShortName());
        LegalEntity recipientLegalEntity = legalEntityRepository.findByShortName(paymentOrderForm.getRecipientLegalEntityShortName());
        Account senderAccount = accountRepository.findAccountByNum(paymentOrderForm.getSenderAccountNum());
        Account recipientAccount = accountRepository.findAccountByNum(paymentOrderForm.getRecipientAccountNum());
        Contract contract = contractRepository.findContractByNum(paymentOrderForm.getContractNum());

        // if necessary fields doesn't correlates with entities validation is stopping
        if ((senderLegalEntity == null) || (senderAccount == null) || (recipientLegalEntity == null) || (recipientAccount == null) || (contract == null))
            return;

        //get logged in User
        org.springframework.security.core.userdetails.User loggedUserDetails = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = loggedUserDetails.getUsername();
        User loggedUser = new User();
        for(User user : userRepository.findAll())
            if (user.getLogin().equals(name)) loggedUser = user;

        // 1. verify that sender legal entity belongs to user, who logged in
        for(UserLegalEntity userLegalEntity : userLegalEntityRepository.findAll())
            if (userLegalEntity.getLegalEntity().equals(senderLegalEntity) && userLegalEntity.getUser().equals(loggedUser)) i++;

        if (i ==0) {
            errors.rejectValue("senderLegalEntityShortName", "", "Организация не относится к текущему пользователю");
            return;
        }

        // 2. verify that sender account belongs to sender legal entity
        if (!senderLegalEntity.equals(senderAccount.getLegalEntity())) {
            errors.rejectValue("senderAccountNum", "", "Счет не принадлежит организации");
            return;
        }

        // 3. verify that recipient account belongs to recipient legal entity
        if (!recipientLegalEntity.equals(recipientAccount.getLegalEntity())) {
            errors.rejectValue("recipientAccountNum", "", "Счет не принадлежит организации");
            return;
        }

        // 4. verify that sender account is not equal recipient account
        if (senderAccount.equals(recipientAccount)) {
            errors.rejectValue("recipientAccountNum", "", "Счет списания не может совпадать со счетом зачисления");
            return;
        }

        // 5. verify that contract relates to sender and recipient legal entities
        if (!((contract.getLegalEntityIssuer().equals(senderLegalEntity) && contract.getLegalEntitySigner().equals(recipientLegalEntity))
            || (contract.getLegalEntityIssuer().equals(recipientLegalEntity) && contract.getLegalEntitySigner().equals(senderLegalEntity)))){
            errors.rejectValue("contractNum", "", "Указан неверный контракт");
            return;
        }

//        // 6. verify that amount is not rather than account balance
//        if(accountBalanceRepository.findFirstByAccountOrderByDateDesc(senderAccount).getAmt().compareTo(paymentOrderForm.getAmt()) == -1)
//            errors.rejectValue("amt", "", "Указанная сумма превышает текущий баланс на счете");
    }
}
