package ru.atc.bclient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.bclient.model.entity.*;
import ru.atc.bclient.service.dto.PaymentOrderForm;
import ru.atc.bclient.model.repository.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentOrderService {
    @Autowired
    PaymentOrderRepository paymentOrderRepository;
    @Autowired
    LegalEntityRepository legalEntityRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountStatusRepository accountStatusRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    PaymentOrderStatusRepository paymentOrderStatusRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountBalanceRepository accountBalanceRepository;
    @Autowired
    OperationRepository operationRepository;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    private static final Logger log = LoggerFactory.getLogger(PaymentOrderService.class);

    // list of new account balances
    Map<Account, AccountBalance> newAccountBalances;
    List<Operation> newOperations;
    PaymentOrderStatus executedPaymentOrderStatus;

    // it takes data from paymentOrderForm and save them as PaymentOrder
    public void savePaymentOrder(PaymentOrderForm paymentOrderForm){

        Contract contract = contractRepository.findContractByNum(paymentOrderForm.getContractNum());
        PaymentOrder paymentOrder = new PaymentOrder();

        paymentOrder.setId(paymentOrderRepository.findMaxId() + 1);
        paymentOrder.setNum(paymentOrderRepository.findMaxNum() + 100);
        paymentOrder.setDate(new java.sql.Date(new Date().getTime()));
        paymentOrder.setCurrencyCode(contract.getCurrencyCode());
        paymentOrder.setAmt(paymentOrderForm.getAmt());
        paymentOrder.setPaymentReason(paymentOrderForm.getPaymentReason());
        paymentOrder.setPaymentPriorityCode(paymentOrderForm.getPaymentPriorityCode());
        paymentOrder.setLegalEntitySender(legalEntityRepository.findByShortName(paymentOrderForm.getSenderLegalEntityShortName()));
        paymentOrder.setAccountSender(accountRepository.findAccountByNum(paymentOrderForm.getSenderAccountNum()));
        paymentOrder.setLegalEntityRecipient(legalEntityRepository.findByShortName(paymentOrderForm.getRecipientLegalEntityShortName()));
        paymentOrder.setAccountRecipient(accountRepository.findAccountByNum(paymentOrderForm.getRecipientAccountNum()));
        paymentOrder.setContract(contract);
        paymentOrder.setPaymentOrderStatus(paymentOrderStatusRepository.findByCode("TAKEN"));
        paymentOrder.setRejectReason(null);

        paymentOrderRepository.save(paymentOrder);
    }

    // it returns payment order forms which belong to a current user's organisations in the given date interval
    public List<PaymentOrderForm> getPaymentOrderForms(String stringDateFrom, String stringDateTo) throws ParseException{
        List<LegalEntity> legalEntities = new ArrayList<>();

        // transform String to Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
        Date dateFrom = format.parse(stringDateFrom);
        Date dateTo = format.parse(stringDateTo);

        // get logged in user
        org.springframework.security.core.userdetails.User loggedUserDetails = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = loggedUserDetails.getUsername();
        User loggedUser = userRepository.findUserByLogin(login);

        // get legal entities of logged in user
        for(UserLegalEntity userLegalEntity : loggedUser.getUserLegalEntities()) {
            legalEntities.add(userLegalEntity.getLegalEntity());
        }

        //get list of payment orders in received interval and belong to current user legal entities
        List<PaymentOrder> paymentOrders = paymentOrderRepository.findByDateBetweenAndLegalEntitySenderInOrderByDate(dateFrom, dateTo, legalEntities);

        List<PaymentOrderForm> paymentOrderForms = new ArrayList<>();

        // transform payment order to payment order form
        for(PaymentOrder paymentOrder : paymentOrders){
            PaymentOrderForm paymentOrderForm = new PaymentOrderForm();
            paymentOrderForm.setNum(paymentOrder.getNum().toString());
            paymentOrderForm.setDate(paymentOrder.getDate().toString());
            paymentOrderForm.setSenderLegalEntityShortName(paymentOrder.getLegalEntitySender().getShortName());
            paymentOrderForm.setSenderAccountNum(paymentOrder.getAccountSender().getNum());
            paymentOrderForm.setRecipientLegalEntityShortName(paymentOrder.getLegalEntityRecipient().getShortName());
            paymentOrderForm.setRecipientAccountNum(paymentOrder.getAccountRecipient().getNum());
            paymentOrderForm.setContractNum(paymentOrder.getContract().getNum());
            paymentOrderForm.setCurrencyCode(paymentOrder.getCurrencyCode());
            paymentOrderForm.setAmt(paymentOrder.getAmt());
            paymentOrderForm.setPaymentReason(paymentOrder.getPaymentReason());
            paymentOrderForm.setStatusCode(paymentOrder.getPaymentOrderStatus().getCode());
            paymentOrderForm.setPaymentReason(paymentOrder.getPaymentReason());
            paymentOrderForm.setRejectReason(paymentOrder.getRejectReason());
            paymentOrderForms.add(paymentOrderForm);
        }

        return paymentOrderForms;
        }
        catch (Exception e) {
            List<PaymentOrderForm> paymentOrderForms = new ArrayList<>();
            return paymentOrderForms;
        }
    }

    @Transactional
    @Scheduled(cron="0 0 18 * * *")
    // it makes all new payment orders
    // scheduled on 18:00 every day
    public void make() {
        log.info("Start Initialization");

        List<PaymentOrder> executedPaymentOrders = new ArrayList<>();
        List<PaymentOrder> rejectedByBalancePaymentOrders = new ArrayList<>();
        newAccountBalances = new HashMap<>();
        newOperations = new ArrayList<>();
        AccountStatus activeAccountStatus = accountStatusRepository.findByCode("ACTIVE");
        PaymentOrderStatus rejectedPaymentOrderStatus = paymentOrderStatusRepository.findByCode("REJECTED");
        executedPaymentOrderStatus = paymentOrderStatusRepository.findByCode("EXECUTED");
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, -1);
        Date yesterday = c.getTime();

        // Begin transaction to lock fct_payment_order
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("LOCK TABLE postgres.bclient3.fct_payment_order IN EXCLUSIVE MODE")
                .executeUpdate();

        // get payment orders in the interval with status "TAKEN" ordered by priority code and id
        List<PaymentOrder> allPaymentOrders =
                paymentOrderRepository.findByDateBetweenAndPaymentOrderStatusOrderByPaymentPriorityCodeAscIdAsc
                        (yesterday, today, paymentOrderStatusRepository.findByCode("TAKEN"));

        log.info("Initialization was ended. Payment orders was received. Count  of payment orders is {}",
                allPaymentOrders.size());

        if (allPaymentOrders.size() == 0) {
            // remove lock from fct_payment_order
            entityManager.getTransaction().commit();
            entityManager.close();

            log.info("There is no new orders. Procedure is finished.");
            return;
        }

        // main handle of payment orders
        for(PaymentOrder paymentOrder : allPaymentOrders) {
            // FIRST. if account is inactive, then payment order is rejected without adding at
            // rejectedByBalancePaymentOrders list
            if((!paymentOrder.getAccountSender().getAccountStatus().equals(activeAccountStatus))
                    ||(!paymentOrder.getAccountRecipient().getAccountStatus().equals(activeAccountStatus))) {
                paymentOrder.setRejectReason("Inactive account");
                paymentOrder.setPaymentOrderStatus(rejectedPaymentOrderStatus);
                paymentOrderRepository.save(paymentOrder);
                log.info("paymentOrder id = {} is inactive. save it in repository", paymentOrder.getId());
                continue;
            }
            // SECOND. try to make current payment order
            if(isEnoughBalanceAmtForPayment(paymentOrder)) {
                makePaymentOrder(paymentOrder);
                executedPaymentOrders.add(paymentOrder);
                log.info("paymentOrder id = {} is successfully completed and added to executedPaymentOrders"
                        , paymentOrder.getId());
            }
            // else transfer payment order to rejected list without changing status
            else {
                rejectedByBalancePaymentOrders.add(paymentOrder);
                log.info("paymentOrder id = {} is rejected by balance and added to rejectedByBalancePaymentOrders"
                        , paymentOrder.getId());
                continue;
            }
            // THIRD. try to make each rejected payment order in rejectedByBalancePaymentOrders list
            for(PaymentOrder rejectedPaymentOrder : rejectedByBalancePaymentOrders)
            {
                if(isEnoughBalanceAmtForPayment(rejectedPaymentOrder)
                        &&(!rejectedPaymentOrder.getPaymentOrderStatus().equals(executedPaymentOrderStatus))) {
                    makePaymentOrder(rejectedPaymentOrder);
                    executedPaymentOrders.add(rejectedPaymentOrder);
                    log.info("paymentOrder id = {}, that was rejected by balance before," +
                                    " now is successfully completed and added to executedPaymentOrders"
                            , rejectedPaymentOrder.getId());
                }
            }
        }

        log.info("Main handle is finished");
        log.info("Size of executedPaymentOrders is {}", executedPaymentOrders.size());
        log.info("Size of rejectedByBalancePaymentOrders is {}", rejectedByBalancePaymentOrders.size());
        log.info("Size of newAccountBalances is {}", newAccountBalances.size());
        log.info("Size of newOperations is {}", newOperations.size());

        // remove lock from fct_payment_order
        entityManager.getTransaction().commit();
        entityManager.close();

        paymentOrderRepository.save(executedPaymentOrders);

        // save rejected payment orders
        for(PaymentOrder rejectedPaymentOrder : rejectedByBalancePaymentOrders) {
            if (!rejectedPaymentOrder.getPaymentOrderStatus().equals(executedPaymentOrderStatus))  {
                rejectedPaymentOrder.setRejectReason("Not enough balance amt");
                rejectedPaymentOrder.setPaymentOrderStatus(rejectedPaymentOrderStatus);
                paymentOrderRepository.save(rejectedPaymentOrder);
            }
        }

        // save account balances
        Integer maxAccountBalanceId = accountBalanceRepository.findMaxId();
        for (Map.Entry<Account, AccountBalance> pair : newAccountBalances.entrySet()) {
            maxAccountBalanceId ++;
            AccountBalance accountBalance = pair.getValue();
            accountBalance.setId(maxAccountBalanceId);
            accountBalanceRepository.save(accountBalance);
        }

        // save operations
        Integer maxOperationId = operationRepository.findMaxId();
        for (Operation operation : newOperations) {
            maxOperationId ++;
            operation.setId(maxOperationId);
            operationRepository.save(operation);
        }
    }

    // it checks that account balance amt is enough
    public boolean isEnoughBalanceAmtForPayment(PaymentOrder paymentOrder)
    {
        // if we already change account balance, than we get it from newAccountBalances
        if(newAccountBalances.containsKey(paymentOrder.getAccountSender())){
            return newAccountBalances.get(paymentOrder.getAccountSender()).getAmt()
                    .compareTo(paymentOrder.getAmt()) >= 0;
        }
        // else we get it from database
        else {
            return accountBalanceRepository.findFirstByAccountOrderByDateDesc(paymentOrder.getAccountSender()).getAmt()
                    .compareTo(paymentOrder.getAmt()) >= 0;
        }
    }

    public void updateAccountBalance(Account account, BigDecimal paymentAmt, Boolean isSenderAccount) {
        // if account balance is already in map newAccountBalances, than update it
        if (newAccountBalances.containsKey(account)) {
            AccountBalance accountBalance = newAccountBalances.get(account);
            BigDecimal originalBalanceAmt = accountBalance.getAmt();
            // update account balance. if account is sender than subtract, else add
            if (isSenderAccount) {
                accountBalance.setAmt(originalBalanceAmt.subtract(paymentAmt));
            }
            else {
                accountBalance.setAmt(originalBalanceAmt.add(paymentAmt));
            }
            newAccountBalances.put(account, accountBalance);
        }
        // else create new account balance and put it in newAccountBalances
        else {
            // original balance amount of account
            BigDecimal originalBalanceAmt = accountBalanceRepository.findFirstByAccountOrderByDateDesc(account).getAmt();

            // create new account balance without id. id is filled later in the end of "make" method
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.setDate(new java.sql.Date(new Date().getTime()));
            // set new balance amt
            if (isSenderAccount) {
                accountBalance.setAmt(originalBalanceAmt.subtract(paymentAmt));
            }
            else {
                accountBalance.setAmt(originalBalanceAmt.add(paymentAmt));
            }
            accountBalance.setAccount(account);
            newAccountBalances.put(account, accountBalance);
        }
    }

    // create operation without id. id is filled later in the end of "make" method
    public void createOperation(Account debetAccount, Account kreditAccount, BigDecimal amt, String Description) {
        Operation operation = new Operation();
        operation.setDate(new java.sql.Date(new Date().getTime()));
        operation.setAmt(amt);
        operation.setAccountDebet(debetAccount);
        operation.setAccountKredit(kreditAccount);
        operation.setDescr(Description);
        newOperations.add(operation);
    }

    // it makes one payment order
    public void makePaymentOrder(PaymentOrder paymentOrder) {
        // update sender account balance
        updateAccountBalance(paymentOrder.getAccountSender(), paymentOrder.getAmt(), true);
        // update recipient account balance
        updateAccountBalance(paymentOrder.getAccountRecipient(), paymentOrder.getAmt(), false);

        createOperation(paymentOrder.getAccountSender(), paymentOrder.getAccountRecipient()
                , paymentOrder.getAmt(), paymentOrder.getPaymentReason());

        paymentOrder.setPaymentOrderStatus(executedPaymentOrderStatus);
    }
}
