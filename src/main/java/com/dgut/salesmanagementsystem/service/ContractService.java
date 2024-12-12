package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ContractDAO;
import com.dgut.salesmanagementsystem.model.CustomerDAO;
import com.dgut.salesmanagementsystem.model.SalesmanDAO;
import com.dgut.salesmanagementsystem.pojo.*;

import java.math.BigDecimal;
import java.util.List;

public class ContractService {
    private final ContractDAO contractDAO;
    private final CustomerDAO customerDAO;
    private final SalesmanDAO salesmanDAO;

    public ContractService() {
        contractDAO = new ContractDAO();
        customerDAO = new CustomerDAO();
        salesmanDAO = new SalesmanDAO();
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

    public void editContract(ContractModifyCriteria criteria) {
        List<ContractItem> contractItemList = criteria.getContractItemList();
        // 计算总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(ContractItem contractItem : contractItemList) {
            contractItem.setTotalPrice(contractItem.getUnitPrice().multiply(BigDecimal.valueOf(contractItem.getQuantity())));
            totalPrice = totalPrice.add(contractItem.getTotalPrice());
        }

        criteria.setTotalPrice(totalPrice);

        try {
            contractDAO.editContract(criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ContractDetails getContractDetailsById(Integer contractID) {
        ContractDetails contractDetails = new ContractDetails();
        Contract contract = contractDAO.getContractById(contractID);
        contractDetails.setContractName(contract.getContractName());
        // 这里要映射成数字，因为是要返回给前端的 即实现not_started -> 1
        contractDetails.setContractStatusInt(ContractStatus.getInt(contract.getContractStatus().getValue()));
        Customer customer = customerDAO.getCustomerById(contract.getCustomerID());
        Salesman salesman = salesmanDAO.getSalesmanById(contract.getSalesmanID());
        contractDetails.setCustomerName(customer.getCustomerName());
        contractDetails.setSalesmanName(salesman.getName());
        contractDetails.setCustomerID(customer.getCustomerID());
        contractDetails.setSalesmanID(salesman.getSalesmanID());
        contractDetails.setStartDate(contract.getStartDate());
        contractDetails.setEndDate(contract.getEndDate());

        List<ContractItem> contractItemList = contractDAO.getContractItemListById(contractID);
        contractDetails.setContractItemList(contractItemList);

        return contractDetails;
    }

    public String getContractName(int contractID) {
        return contractDAO.getContractName(contractID);
    }
}
