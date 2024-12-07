package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ContractDAO;
import com.dgut.salesmanagementsystem.pojo.Contract;
import com.dgut.salesmanagementsystem.pojo.ContractItem;
import com.dgut.salesmanagementsystem.pojo.ContractModifyCriteria;
import com.dgut.salesmanagementsystem.pojo.ContractSearchCriteria;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContractService {
    private final ContractDAO contractDAO;

    public ContractService() {
        contractDAO = new ContractDAO();
    }

    public List<Contract> searchContracts(ContractSearchCriteria contractSearchCriteria, int pageNum, int pageSize) {
        return contractDAO.searchContracts(contractSearchCriteria, pageNum, pageSize);
    }

    public int getTotalPages(ContractSearchCriteria contractSearchCriteria, int pageSize) {
        int totalRecords = contractDAO.countContracts(contractSearchCriteria);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        return totalPages = totalPages > 0 ? totalPages : 1;
    }

    public void addContract(ContractModifyCriteria criteria) {
        List<ContractItem> contractItemList = criteria.getContractItemList();
        // 计算总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(ContractItem contractItem : contractItemList) {
            contractItem.setTotalPrice(contractItem.getUnitPrice().multiply(BigDecimal.valueOf(contractItem.getQuantity())));
            totalPrice = totalPrice.add(contractItem.getTotalPrice());
        }

        criteria.setTotalPrice(totalPrice);

        try {
            contractDAO.addContract(criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
