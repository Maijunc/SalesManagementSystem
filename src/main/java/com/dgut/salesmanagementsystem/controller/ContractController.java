package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.service.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(value = "/ContractController")
public class ContractController extends HttpServlet{
    private final ContractService contractService = new ContractService();
    private int pageSize;

    @Override
    public void init() throws ServletException {
        super.init();
        // 在这里获取Servlet上下文参数
        this.pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");


        if("ajax".equals(action))
            getContractDetailsById(req, resp);
        else if("salesman".equals(action)) {
            searchContractBySalesman(req, resp);
        }
        else
            searchContract(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        Map<String, String> result = null;
        if(action == null) {
            // 获取请求体数据
            BufferedReader reader = req.getReader();
            StringBuilder jsonRequest = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonRequest.append(line);
            }

            result = parseMultipartData(jsonRequest.toString());
            action = result.get("action");
        }

        if("add".equals(action)) {
            addContract(req, resp);
        }
        else if("edit".equals(action)) {
            editContract(req, resp);
        }
        else if(action.equals(action)) {
            statSales(req, resp, result);
        }
    }

    private void statSales(HttpServletRequest req, HttpServletResponse resp, Map<String, String> result) throws IOException, ServletException{
        // 1. 获取请求参数

        String startDate = result.get("startDate");
        String endDate = result.get("endDate");
        String customerIDStr = result.get("customerID");
        String productIDStr = result.get("productID");

        // 2. 调用服务层方法获取统计数据
        SalesStatistics statistics = contractService.getSalesStatistics(startDate, endDate, customerIDStr, productIDStr);

        // 3. 返回结果
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // 转换统计数据为JSON格式
//        Gson gson = new Gson();
//        out.print(gson.toJson(statistics));
//        out.flush();

        // 返回的是合同的细节信息，以json串的形式
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(statistics);
        resp.getWriter().write(jsonResult);
    }

    private void searchContractBySalesman(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        // 查询条件
        String contractName = req.getParameter("contractName");
        String contractIDStr = req.getParameter("contractID");
        String status = req.getParameter("status");
        String startTimeStr = req.getParameter("start_time");
        String endTimeStr = req.getParameter("end_time");
        int salesmanID = Integer.parseInt(req.getParameter("salesmanID"));
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));

        // 检查 contractID 是否为空，空的话不传递或设为 null
        Integer contractID = null;
        if (contractIDStr != null && !contractIDStr.isEmpty()) {
            try {
                contractID = Integer.parseInt(contractIDStr);
            } catch (NumberFormatException e) {
                // 如果转换失败，可以选择记录日志或返回错误信息
                e.printStackTrace();
            }
        }

        // 获取总记录数以计算总页数
        int totalPages = contractService.getTotalPagesBySalesman(new ContractSearchCriteria(contractName, contractID, status, startTimeStr, endTimeStr), pageSize, salesmanID);

        // 创建 ContractSearchCriteria 对象时，避免空值字段影响查询
        ContractSearchCriteria criteria = new ContractSearchCriteria();
        criteria.setContractName(contractName != null && !contractName.isEmpty() ? contractName : null);
        criteria.setContractID(contractID); // 如果为空则不设置
        criteria.setStatus(status != null && !status.isEmpty() ? ContractStatus.fromInt(Integer.parseInt(status)).getValue() : null);
        // 合同开始日期的起始日期
        criteria.setStartDateStr(startTimeStr != null && !startTimeStr.isEmpty() ? startTimeStr : null);
        // 合同开始日期的终止日期
        criteria.setEndDateStr(endTimeStr != null && !endTimeStr.isEmpty() ? endTimeStr : null);

        List<Contract> contractList = contractService.searchContractsBySalesman(criteria, pageNum, pageSize, salesmanID);
        HttpSession session = req.getSession();

        session.setAttribute("contractList", contractList);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("pageSize", pageSize);

        // 设置分页相关属性
        if(contractName != null)
            session.setAttribute("contractNameFilter", contractName);
        if(contractIDStr != null)
            session.setAttribute("contractIDFilter", contractIDStr);
        if(status != null)
            session.setAttribute("statusFilter", status);
        if(startTimeStr != null)
            session.setAttribute("startTimeFilter", startTimeStr);
        if(endTimeStr != null)
            session.setAttribute("endTimeFilter", endTimeStr);


        resp.sendRedirect("contract/manage_contracts_salesman.jsp");
    }

    private void searchContract(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 查询条件
        String contractName = req.getParameter("contractName");
        String contractIDStr = req.getParameter("contractID");
        String status = req.getParameter("status");
        String startTimeStr = req.getParameter("start_time");
        String endTimeStr = req.getParameter("end_time");
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));

        // 检查 contractID 是否为空，空的话不传递或设为 null
        Integer contractID = null;
        if (contractIDStr != null && !contractIDStr.isEmpty()) {
            try {
                contractID = Integer.parseInt(contractIDStr);
            } catch (NumberFormatException e) {
                // 如果转换失败，可以选择记录日志或返回错误信息
                e.printStackTrace();
            }
        }

        // 获取总记录数以计算总页数
        int totalPages = contractService.getTotalPages(new ContractSearchCriteria(contractName, contractID, status, startTimeStr, endTimeStr), pageSize);

        // 创建 ContractSearchCriteria 对象时，避免空值字段影响查询
        ContractSearchCriteria criteria = new ContractSearchCriteria();
        criteria.setContractName(contractName != null && !contractName.isEmpty() ? contractName : null);
        criteria.setContractID(contractID); // 如果为空则不设置
        criteria.setStatus(status != null && !status.isEmpty() ? ContractStatus.fromInt(Integer.parseInt(status)).getValue() : null);
        // 合同开始日期的起始日期
        criteria.setStartDateStr(startTimeStr != null && !startTimeStr.isEmpty() ? startTimeStr : null);
        // 合同开始日期的终止日期
        criteria.setEndDateStr(endTimeStr != null && !endTimeStr.isEmpty() ? endTimeStr : null);

        List<Contract> contractList = contractService.searchContracts(criteria, pageNum, pageSize);
        HttpSession session = req.getSession();

        session.setAttribute("contractList", contractList);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("pageSize", pageSize);

        // 设置分页相关属性
        if(contractName != null)
            session.setAttribute("contractNameFilter", contractName);
        if(contractIDStr != null)
            session.setAttribute("contractIDFilter", contractIDStr);
        if(status != null)
            session.setAttribute("statusFilter", status);
        if(startTimeStr != null)
            session.setAttribute("startTimeFilter", startTimeStr);
        if(endTimeStr != null)
            session.setAttribute("endTimeFilter", endTimeStr);


        resp.sendRedirect("contract/manage_contracts.jsp");
    }

    private void editContract(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contractName = req.getParameter("contractName");
        String status = req.getParameter("contractStatus");
        String startTimeStr = req.getParameter("startDate");
        String endTimeStr = req.getParameter("endDate");
        String customerIDStr = req.getParameter("customerID");
        String salesmanIDStr = req.getParameter("salesmanID");
        String contractIDStr = req.getParameter("contractID");

        Integer customerID = null;
        Integer salesmanID = null;
        Integer contractID = null;
        // 客户ID
        if (customerIDStr != null && !customerIDStr.isEmpty()) {
            try {
                customerID = Integer.parseInt(customerIDStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        // 销售人员ID
        if (salesmanIDStr != null && !salesmanIDStr.isEmpty()) {
            try {
                salesmanID = Integer.parseInt(salesmanIDStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        // 合同ID
        if (contractIDStr != null && !contractIDStr.isEmpty()) {
            try {
                contractID = Integer.parseInt(contractIDStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Map<String, String[]> parameterMap = req.getParameterMap();
        List<ContractItem> contractItems = new ArrayList<>();
        Map<Integer, Integer> indexMap = new HashMap<>(); // 存储 index 与在 contractItems 中的下标映射
        // 遍历所有参数，提取商品数据
        parameterMap.forEach((key, value) -> {
            if (key.startsWith("products[")) { // 检查是否是商品参数
                // 提取索引
                String indexStr = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
                int index = Integer.parseInt(indexStr);
                // 如果此 index 没有出现过，扩充 contractItems 的容量并更新 indexMap
                if (!indexMap.containsKey(index)) {
                    indexMap.put(index, contractItems.size());
                    contractItems.add(new ContractItem()); // 添加新的 ContractItem
                }

                ContractItem contractItem = contractItems.get(indexMap.get(index));

                // 填充商品属性
                if (key.endsWith(".productID")) {
                    try {
                        contractItem.setProductID(Integer.parseInt(value[0]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (key.endsWith(".productName")) {
                    contractItem.setProductName(value[0]);
                } else if (key.endsWith(".quantity")) {
                    try {
                        contractItem.setQuantity(Integer.parseInt(value[0]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (key.endsWith(".unitPrice")) {
                    contractItem.setUnitPrice(new BigDecimal(value[0]));
                }
            }
        });
        ContractModifyCriteria criteria = new ContractModifyCriteria();
        criteria.setContractName(contractName);
        criteria.setStatus(status);
        criteria.setSalesmanID(salesmanID);
        criteria.setCustomerID(customerID);
        criteria.setContractItemList(contractItems);
        criteria.setStartDateStr(startTimeStr);
        criteria.setEndDateStr(endTimeStr);
        criteria.setContractID(contractID);

        contractService.editContract(criteria);

        resp.sendRedirect("../ContractController?pageNum=1");
    }

    private void addContract(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contractName = req.getParameter("contractName");
        String status = req.getParameter("contractStatus");
        String startTimeStr = req.getParameter("startDate");
        String endTimeStr = req.getParameter("endDate");
        String customerIDStr = req.getParameter("customerID");
        String salesmanIDStr = req.getParameter("salesmanID");

        Integer customerID = null;
        Integer salesmanID = null;
        // 客户ID
        if (customerIDStr != null && !customerIDStr.isEmpty()) {
            try {
                customerID = Integer.parseInt(customerIDStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        // 销售人员ID
        if (salesmanIDStr != null && !salesmanIDStr.isEmpty()) {
            try {
                salesmanID = Integer.parseInt(salesmanIDStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Map<String, String[]> parameterMap = req.getParameterMap();
        List<ContractItem> contractItems = new ArrayList<>();
        Map<Integer, Integer> indexMap = new HashMap<>(); // 存储 index 与在 contractItems 中的下标映射
        // 遍历所有参数，提取商品数据
        parameterMap.forEach((key, value) -> {
            if (key.startsWith("products[")) { // 检查是否是商品参数
                // 提取索引
                String indexStr = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
                int index = Integer.parseInt(indexStr);
                // 如果此 index 没有出现过，扩充 contractItems 的容量并更新 indexMap
                if (!indexMap.containsKey(index)) {
                    indexMap.put(index, contractItems.size());
                    contractItems.add(new ContractItem()); // 添加新的 ContractItem
                }

                ContractItem contractItem = contractItems.get(indexMap.get(index));

                // 填充商品属性
                if (key.endsWith(".productID")) {
                    try {
                        contractItem.setProductID(Integer.parseInt(value[0]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (key.endsWith(".productName")) {
                    contractItem.setProductName(value[0]);
                } else if (key.endsWith(".quantity")) {
                    try {
                        contractItem.setQuantity(Integer.parseInt(value[0]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (key.endsWith(".unitPrice")) {
                    contractItem.setUnitPrice(new BigDecimal(value[0]));
                }
            }
        });
        ContractModifyCriteria criteria = new ContractModifyCriteria();
        criteria.setContractName(contractName);
        criteria.setStatus(status);
        criteria.setSalesmanID(salesmanID);
        criteria.setCustomerID(customerID);
        criteria.setContractItemList(contractItems);
        criteria.setStartDateStr(startTimeStr);
        criteria.setEndDateStr(endTimeStr);

        contractService.addContract(criteria);

        resp.sendRedirect("../ContractController?pageNum=1");
    }

    public void getContractDetailsById(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String contractIDStr = req.getParameter("contractID");
        Integer contractID = 0;
        if (contractIDStr != null && !contractIDStr.isEmpty()) {
            try {
                contractID = Integer.parseInt(contractIDStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("getContractDetailsById: invalid contractID");

            return;
        }
        ContractDetails contractDetails = contractService.getContractDetailsById(contractID);

        // 返回的是合同的细节信息，以json串的形式
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(contractDetails);
        resp.getWriter().write(jsonResult);
    }

    public static Map<String, String> parseMultipartData(String data) {
        // 定义正则表达式，提取字段名和值
        String pattern = "Content-Disposition: form-data; name=\"(.*?)\"\\s*(.*?)\\s*(?=------|$)";

        // 创建一个正则表达式模式
        Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = r.matcher(data);

        // 存储解析结果
        Map<String, String> resultMap = new HashMap<>();

        // 遍历所有匹配项
        while (m.find()) {
            String fieldName = m.group(1); // 字段名
            String fieldValue = m.group(2).trim(); // 字段值，去除前后空白字符
            resultMap.put(fieldName, fieldValue);
        }

        return resultMap;
    }
}
