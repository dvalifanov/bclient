package ru.atc.bclient.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class AccountForm {
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String num;

    @Getter @Setter
    private BigDecimal balanceAmt;
}
