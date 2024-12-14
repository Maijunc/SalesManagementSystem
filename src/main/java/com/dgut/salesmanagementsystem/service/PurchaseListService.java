package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ContractDAO;
import com.dgut.salesmanagementsystem.model.ProductDAO;
import com.dgut.salesmanagementsystem.model.PurchaseListDAO;
import com.dgut.salesmanagementsystem.pojo.*;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseListService {
    private final PurchaseListDAO purchaseListDAO = new PurchaseListDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    public List<PurchaseList> getPurchaseListsByContractID(int contractID, int pageNum, int pageSize) {
        return purchaseListDAO.getPurchaseListsByContractID(contractID, pageNum, pageSize);
    }

    public int getTotalPages(int contractID, int pageSize) {
        int totalRecords = purchaseListDAO.countPurchaseLists(contractID);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        return totalPages;
    }

    public List<RemainingProduct> searchRemainingProducts(String searchKeyword, int pageNum, int pageSize, int contractID) {
        List<RemainingProduct> remainingProductList = purchaseListDAO.searchRemainingProducts(searchKeyword, pageNum, pageSize, contractID);
        for(RemainingProduct remainingProduct : remainingProductList) {
            remainingProduct.setStockQuantity(productDAO.getStockQuantityByID(remainingProduct.getProductID()));
        }
        return remainingProductList;
    }

    public PaginatedResult<RemainingProduct> getPaginatedResult(String searchKeyword, int curPage, int pageSize, List<RemainingProduct> remainingProductList, int contractID) {
        int totalRecords = purchaseListDAO.countRemainingProducts(searchKeyword, contractID);
        // 算出总共有几页
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        // 设置分页相关属性
        PaginatedResult<RemainingProduct> result = new PaginatedResult<>();
        result.setElementList(remainingProductList);
        result.setCurrentPage(curPage);
        result.setTotalPages(totalPages);
        result.setTotalRecords(totalRecords);

        return result;
    }

    public void addPurchaseList(int contractID, List<PurchaseListItem> purchaseListItems) {
        PurchaseList purchaseList = new PurchaseList();

        // 计算总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(PurchaseListItem purchaseListItem : purchaseListItems) {
            totalPrice = totalPrice.add(purchaseListItem.getUnitPrice().multiply(BigDecimal.valueOf(purchaseListItem.getQuantity())));
        }
        purchaseList.setContractID(contractID);
        purchaseList.setPurchaseListItems(purchaseListItems);
        purchaseList.setTotalPrice(totalPrice);

        purchaseListDAO.addPurchaseList(purchaseList);
    }

    public void payForPurchaseList(int purchaseListID) {
        // 修改状态
        purchaseListDAO.editPaymentStatus(purchaseListID);

        PurchaseList purchaseList = purchaseListDAO.getPurchaseListByID(purchaseListID);

        // 修改合同已付款金额 同时修改合同状态，如果合同是为开始状态则变成履行中状态
        contractDAO.updatePaidAmount(purchaseList.getContractID(), purchaseList.getTotalPrice());
    }

    public PaginatedResult<PurchaseList> getPurchaseListDetails(Integer purchaseListID, int pageNum, int pageSize) {
        PurchaseList purchaseList = purchaseListDAO.getPurchaseListByID(purchaseListID);
        purchaseList.setPurchaseListItems(purchaseListDAO.getPurchaseListItemsByPage(purchaseListID, pageNum, pageSize));

        int totalRecords = purchaseListDAO.countPurchaseListItems(purchaseListID);
        // 算出总共有几页
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        // 设置分页相关属性
        PaginatedResult<PurchaseList> result = new PaginatedResult<>();
        result.setElementToPass(purchaseList);
        result.setCurrentPage(pageNum);
        result.setTotalPages(totalPages);
        result.setTotalRecords(totalRecords);

        return result;
    }

    public ShipOrder getShipOrderInfo(Integer purchaseListItemID) {
        return purchaseListDAO.getShipOrderInfo(purchaseListItemID);
    }

    public boolean checkIfPaid(int purchaseListID) {
        return purchaseListDAO.checkIfPaid(purchaseListID);
    }
}
