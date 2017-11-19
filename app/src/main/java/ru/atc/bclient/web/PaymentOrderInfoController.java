package ru.atc.bclient.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.atc.bclient.model.entity.PaymentOrder;
import ru.atc.bclient.service.PaymentOrderService;
import ru.atc.bclient.service.dto.PaymentOrderForm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentOrderInfoController extends WebMvcConfigurerAdapter {
    @Autowired
    PaymentOrderService paymentOrderService;

    @RequestMapping(value="/paymentorderinfo", method = RequestMethod.GET)
    public String paymentOrderInfoForm(Model model) {
        return "paymentorderinforequest";
    }

    @RequestMapping(value="/paymentorderinfo", method = RequestMethod.POST)
    public String paymentOrderInfoSubmit(Model model, @ModelAttribute("datefrom") String stringDateFrom, @ModelAttribute("dateto") String stringDateTo)
    throws ParseException{
        model.addAttribute("paymentOrderForms", paymentOrderService.getPaymentOrderForms(stringDateFrom, stringDateTo));
        return "paymentorderinforesponse";
    }
}
