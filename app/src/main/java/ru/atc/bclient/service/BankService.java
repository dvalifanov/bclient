package ru.atc.bclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atc.bclient.model.entity.Bank;
import ru.atc.bclient.model.repository.BankRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {
    @Autowired
    BankRepository bankRepository;

    public List<String> getBankNameList() {
        List<String> bankNameList = new ArrayList<>();
        for (Bank bank : bankRepository.findAll()) {
            bankNameList.add(bank.getName());
        }
        return bankNameList;
    }
}
