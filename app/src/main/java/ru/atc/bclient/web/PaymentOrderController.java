package ru.atc.bclient.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.atc.bclient.service.dto.PaymentOrderForm;
import ru.atc.bclient.service.PaymentOrderService;
import ru.atc.bclient.service.validator.PaymentOrderValidator;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class PaymentOrderController extends WebMvcConfigurerAdapter {

    @Autowired
    PaymentOrderValidator paymentOrderValidator;
    @Autowired
    PaymentOrderService paymentOrderService;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(paymentOrderValidator);
    }

    @RequestMapping(value="/paymentorder", method= RequestMethod.GET)
    public String paymentOrderForm(Model model) {
        model.addAttribute("paymentorderform", new PaymentOrderForm());
        return "paymentorder";
    }

    @RequestMapping(value="/paymentorder", method=RequestMethod.POST)
    public String paymentOrderSubmit(@Valid @ModelAttribute("paymentorderform") PaymentOrderForm paymentOrderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "paymentorder";

        paymentOrderService.savePaymentOrder(paymentOrderForm);

        return "result";
    }



}