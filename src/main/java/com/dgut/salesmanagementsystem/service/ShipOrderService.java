package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ShipOrderDAO;
import com.dgut.salesmanagementsystem.pojo.ShipOrder;

public class ShipOrderService {
    private ShipOrderDAO shipOrderDAO = new ShipOrderDAO();
    public void createShipOrder(ShipOrder shipOrder) {
        shipOrderDAO.createShipOrder(shipOrder);
    }

    public boolean checkIfShipOrderExists(int purchaseListItemID) {
        return shipOrderDAO.checkShipOrderExists(purchaseListItemID) > 0;
    }
}
