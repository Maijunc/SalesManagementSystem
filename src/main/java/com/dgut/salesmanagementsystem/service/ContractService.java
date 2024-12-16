package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ContractDAO;
import com.dgut.salesmanagementsystem.model.CustomerDAO;
import com.dgut.salesmanagementsystem.model.SalesmanDAO;
import com.dgut.salesmanagementsystem.model.ShipOrderDAO;
import com.dgut.salesmanagementsystem.pojo.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ContractService {
    private final ContractDAO contractDAO;
    private final CustomerDAO customerDAO;
    private final SalesmanDAO salesmanDAO;
    private final ShipOrderDAO shipOrderDAO;

    public ContractService() {
        contractDAO = new ContractDAO();
        customerDAO = new CustomerDAO();
        salesmanDAO = new SalesmanDAO();
        shipOrderDAO = new ShipOrderDAO();
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
        contractDetails.setTotalAmount(contract.getTotalAmount());
        contractDetails.setPaidAmount(contract.getPaidAmount());
        contractDetails.setRemainingAmount(contract.getRemainingAmount());

        List<ContractItem> contractItemList = contractDAO.getContractItemListById(contractID);
        contractDetails.setContractItemList(contractItemList);

        return contractDetails;
    }

    public String getContractName(int contractID) {
        return contractDAO.getContractName(contractID);
    }

    public Contract getContractByID(int contractID) {
        return contractDAO.getContractById(contractID);
    }


    public boolean updateContractStatus(Integer contractID, ContractStatus contractStatus) {
        return contractDAO.updateContractStatus(contractID, contractStatus);
    }

    public boolean checkIfAllItemsShipped(int contractID) {
        // 查询合同涉及的所有商品
        List<ContractItem> contractItems = contractDAO.getContractItemListById(contractID);

        for (ContractItem item : contractItems) {
            int productID = item.getProductID();
            int requiredQuantity = item.getQuantity();

            // 查询该合同下该商品的累计发货数量
            int shippedQuantity = shipOrderDAO.getShippedQuantityByContractAndProduct(contractID, productID);

            // 如果累计发货数量小于合同规定数量，返回 false
            if (shippedQuantity < requiredQuantity) {
                return false;
            }
        }

        // 如果所有商品的累计发货数量均满足合同要求，返回 true
        return true;
    }

    public List<Contract> searchContractsBySalesman(ContractSearchCriteria criteria, int pageNum, int pageSize, int salesmanID) {
        return contractDAO.searchContractsBySalesman(criteria, pageNum, pageSize, salesmanID);
    }

    public int getTotalPagesBySalesman(ContractSearchCriteria contractSearchCriteria, int pageSize, int salesmanID) {
        int totalRecords = contractDAO.countContractsBySalesman(contractSearchCriteria, salesmanID);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        return totalPages = totalPages > 0 ? totalPages : 1;
    }

    public boolean setEndDate(Integer contractID, LocalDateTime now) {
        return contractDAO.setEndDate(contractID, now);
    }

    public SalesStatistics getSalesStatistics(String startDate, String endDate, String customerIDStr, String productIDStr) {
        // 这里根据传入的参数进行数据库查询，查询销售数据
        SalesStatistics statistics = new SalesStatistics();

        // 查询销售总额、客户销售额、商品销售额
        statistics.setTotalSales(getTotalSales(startDate, endDate));
        statistics.setCustomerSales(getCustomerSales(startDate, endDate, customerIDStr));
        statistics.setProductSales(getProductSales(startDate, endDate, productIDStr));

        System.out.println(statistics);

        return statistics;
    }

    // 获取总销售额
    private BigDecimal getTotalSales(String startDate, String endDate) {
        // 调用DAO查询数据库
        return contractDAO.getTotalSales(startDate, endDate);
    }

    // 获取不同客户的销售额
    private List<CustomerSales> getCustomerSales(String startDate, String endDate, String customerIDStr) {
        // 调用DAO查询数据库
        return contractDAO.getCustomerSales(startDate, endDate, customerIDStr);
    }

    // 获取不同商品的销售额
    private List<ProductSales> getProductSales(String startDate, String endDate, String productIDStr) {
        // 调用DAO查询数据库
        return contractDAO.getProductSales(startDate, endDate, productIDStr);
    }
}
