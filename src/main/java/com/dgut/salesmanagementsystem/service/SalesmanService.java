package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.SalesmanDAO;
import com.dgut.salesmanagementsystem.pojo.ContactInfo;
import com.dgut.salesmanagementsystem.pojo.PaginatedResult;
import com.dgut.salesmanagementsystem.pojo.Salesman;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

public class SalesmanService {
    private final SalesmanDAO salesmanDAO = new SalesmanDAO();

    // 添加销售人员
    public void addSalesman(String name, String email, String phone, BigDecimal totalSales, BigDecimal commission) {
        Salesman salesman = new Salesman();
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(email);
        contactInfo.setPhone(phone);

        salesman.setName(name);
        salesman.setContactInfo(contactInfo);
        salesman.setTotalSales(totalSales);
        salesman.setCommission(commission);

        setJsonInfo(salesman);

        salesmanDAO.addSalesman(salesman);
    }

    // 更新销售人员
    public void updateSalesman(int salesmanID, String name, String email, String phone, BigDecimal totalSales, BigDecimal commission) {
        Salesman salesman = new Salesman();
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(email);
        contactInfo.setPhone(phone);

        salesman.setSalesmanID(salesmanID);
        salesman.setName(name);
        salesman.setContactInfo(contactInfo);
        salesman.setTotalSales(totalSales);
        salesman.setCommission(commission);

        setJsonInfo(salesman);

        salesmanDAO.updateSalesman(salesman);
    }

    // 获取单个销售人员
    public Salesman getSalesmanById(int salesmanID) {
        Salesman salesman = salesmanDAO.getSalesmanById(salesmanID);
        // 将json格式的字符串转成对象
        salesman.setContactInfo(mapJsonToContactInfo(salesman.getContactInfoJson()));

        return salesman;
    }

    // 获取所有销售人员，不使用，因为缺少页码信息
    public List<Salesman> getAllSalesmen() {
        List<Salesman> salesmanList = salesmanDAO.getAllSalesmen();
        for(Salesman salesman : salesmanList) {
            // 将json格式的字符串转成对象
            salesman.setContactInfo(mapJsonToContactInfo(salesman.getContactInfoJson()));
        }
        return salesmanList;
    }

    // 删除销售人员
    public void deleteSalesman(int salesmanID) {
        salesmanDAO.deleteSalesman(salesmanID);
    }

    private void setJsonInfo(Salesman salesman)
    {
        // 将 contactInfo 和 performanceStats 转换为 JSON 字符串
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String contactInfoJson = objectMapper.writeValueAsString(salesman.getContactInfo());
            salesman.setContactInfoJson(contactInfoJson);  // 将对应的json数据存到对象中

            // 如果 performanceStats 不为空，转换成 JSON 字符串
            String performanceStatsJson = objectMapper.writeValueAsString(salesman.getPerformanceStats());
            salesman.setPerformanceStatsJson(performanceStatsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ContactInfo mapJsonToContactInfo(String jsonData)
    {
        ContactInfo contactInfo = new ContactInfo();
        ObjectMapper mapper = new ObjectMapper();
        try {
            contactInfo = mapper.readValue(jsonData, ContactInfo.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return contactInfo;
    }

    public List<Salesman> searchSalesmen(String searchKeyword, int pageNum, int pageSize) {
        List<Salesman> salesmanList = salesmanDAO.searchSalesmen(searchKeyword, pageNum, pageSize);
        for(Salesman salesman : salesmanList) {
            // 将json格式的字符串转成对象
            salesman.setContactInfo(mapJsonToContactInfo(salesman.getContactInfoJson()));
        }
        return salesmanList;
    }

    public int getTotalPages(String searchKeyword,int pageSize) {

        int totalRecords = salesmanDAO.countSalesmen(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        return totalPages;
    }


    public PaginatedResult<Salesman> getSalesmenByPage(String searchKeyword, int curPage, int pageSize, List<Salesman> salesmanList) {
        int totalRecords = salesmanDAO.countSalesmen(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        // 设置分页相关属性
        PaginatedResult<Salesman> result = new PaginatedResult<>();
        result.setElementList(salesmanList);
        result.setCurrentPage(curPage);
        result.setTotalPages(totalPages);
        result.setTotalRecords(totalRecords);

        return result;
    }

    public Salesman getSalesmanByName(String name) {
        Salesman salesman = salesmanDAO.getSalesmanByName(name);
        // 将json格式的字符串转成对象
        salesman.setContactInfo(mapJsonToContactInfo(salesman.getContactInfoJson()));

        return salesman;
    }

    public BigDecimal getSalesAmountBySalesmanAndDateRange(int salesmanID, String startTimeStr, String endTimeStr) {
        return salesmanDAO.getSalesAmountBySalesmanAndDateRange(salesmanID, startTimeStr, endTimeStr);
    }
}
