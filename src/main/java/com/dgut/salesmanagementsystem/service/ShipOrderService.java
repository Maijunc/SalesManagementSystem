package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ShipOrderDAO;
import com.dgut.salesmanagementsystem.pojo.ShipOrder;

import java.util.List;

public class ShipOrderService {
    private ShipOrderDAO shipOrderDAO = new ShipOrderDAO();
    public void createShipOrder(ShipOrder shipOrder) {
        shipOrderDAO.createShipOrder(shipOrder);
    }

    public boolean checkIfShipOrderExists(int purchaseListItemID) {
        return shipOrderDAO.checkShipOrderExists(purchaseListItemID) > 0;
    }

    public List<ShipOrder> getShipOrderListByPage(int pageNum, int pageSize) {
        return shipOrderDAO.getShipOrdersWithPagination(pageNum, pageSize);
    }

    public int getTotalPages(int pageSize) {
        int totalRecords =  shipOrderDAO.getShipOrderCount();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        return totalPages;
    }

    public ShipOrder getShipOrderById(int shipOrderID) {
        return shipOrderDAO.getShipOrderById(shipOrderID);
    }

    public boolean updateShipOrder(ShipOrder shipOrder) {
        return shipOrderDAO.updateShipOrder(shipOrder);
    }
}
