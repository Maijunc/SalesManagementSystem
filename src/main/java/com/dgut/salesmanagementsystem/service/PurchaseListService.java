package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.PurchaseListDAO;
import com.dgut.salesmanagementsystem.pojo.PaginatedResult;
import com.dgut.salesmanagementsystem.pojo.Product;
import com.dgut.salesmanagementsystem.pojo.PurchaseList;
import com.dgut.salesmanagementsystem.pojo.RemainingProduct;

import java.util.List;

public class PurchaseListService {
    private final PurchaseListDAO purchaseListDAO = new PurchaseListDAO();
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
        return purchaseListDAO.searchRemainingProducts(searchKeyword, pageNum, pageSize, contractID);
    }

    public PaginatedResult<RemainingProduct> getPaginatedResult(int curPage, int pageSize, List<RemainingProduct> remainingProductList, int contractID) {
        int totalRecords = purchaseListDAO.countPurchaseLists(contractID);
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
}
