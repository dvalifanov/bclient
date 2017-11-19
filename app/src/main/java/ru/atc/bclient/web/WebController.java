package ru.atc.bclient.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.atc.bclient.service.*;
import ru.atc.bclient.service.test.TransactionTestClass;


@Controller
public class WebController {

    @Autowired
    LegalEntityService legalEntityService;
    @Autowired
    CacheService cacheService;
    @Autowired
    BankService bankService;
    @Autowired
    TransactionTestClass transactionTestClass;
    @Autowired
    PaymentOrderService PaymentOrderService;

    @RequestMapping("/info")
    public String  getInfo(Model model) {
        model.addAttribute("legalEntityForms", legalEntityService.getLegalEntityFormsOfCurrentUser());
        return "info";
    }

    @RequestMapping("/bank")
    // to check that cache for dim_bank is working
    public String checkInfo(Model model) {
        model.addAttribute("sizeBefore", cacheService.getSize("ru.atc.bclient.model.entity.Bank"));
        model.addAttribute("bankNameList", bankService.getBankNameList());
        model.addAttribute("sizeAfter", cacheService.getSize("ru.atc.bclient.model.entity.Bank"));
        //transactionTstClass.saveUser();
        return "bank";
    }

    @RequestMapping("/makepaymentorders")
    public String makePaymentOrders() {
        PaymentOrderService.make();
        return "makingpaymentorders";
    }
}