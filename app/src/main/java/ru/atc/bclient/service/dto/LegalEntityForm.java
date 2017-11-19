package ru.atc.bclient.service.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class LegalEntityForm {
    @Getter @Setter
    private String fullName;

    @Getter @Setter
    private List<AccountForm> accountForms;
}
