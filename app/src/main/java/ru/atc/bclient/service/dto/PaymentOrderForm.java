package ru.atc.bclient.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import ru.atc.bclient.service.annotation.AccountExist;
import ru.atc.bclient.service.annotation.ContractExist;
import ru.atc.bclient.service.annotation.LegalEntityExist;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


public class PaymentOrderForm {

    @Getter @Setter
    private String num;

    @Getter @Setter
    private String date;

    @Getter @Setter
    @LegalEntityExist(message = "Организация не была найдена")
    private String senderLegalEntityShortName;

    @Getter @Setter
    @AccountExist(message = "Счет не был найден")
    private String senderAccountNum;

    @Getter @Setter
    @LegalEntityExist(message = "Организация не была найдена")
    private String recipientLegalEntityShortName;

    @Getter @Setter
    @AccountExist(message = "Счет не был найден")
    private String recipientAccountNum;

    @Getter @Setter
    @ContractExist(message = "Договор не был найден")
    private String contractNum;

    @Getter @Setter
    @NotNull
    private BigDecimal amt;

    @Getter @Setter
    @NotEmpty
    @Size(min =3)
    private String paymentReason;

    @Getter @Setter
    private String currencyCode;

    @Getter @Setter
    @NotNull
    @Pattern(regexp = "^[0][1-5]")
    private String paymentPriorityCode;

    @Getter @Setter
    private String statusCode;

    @Getter @Setter
    private String rejectReason;
}
