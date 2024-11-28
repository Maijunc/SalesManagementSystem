package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ContractDAO;
import com.dgut.salesmanagementsystem.pojo.Contract;
import com.dgut.salesmanagementsystem.pojo.ContractSearchCriteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContractService {
    private ContractDAO contractDAO;

    public ContractService() {
        contractDAO = new ContractDAO();
    }

    public List<Contract> searchContracts(ContractSearchCriteria contractSearchCriteria, int pageNum, int pageSize) {
        return contractDAO.searchContracts(contractSearchCriteria, pageNum, pageSize);
    }

    public int countCustomers(ContractSearchCriteria contractSearchCriteria) {
        return contractDAO.countCustomers(contractSearchCriteria);
    }
}
