package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.Contract;
import com.dgut.salesmanagementsystem.pojo.ContractSearchCriteria;
import com.dgut.salesmanagementsystem.pojo.ContractStatus;
import com.dgut.salesmanagementsystem.pojo.Customer;
import com.dgut.salesmanagementsystem.service.ContractService;
import com.dgut.salesmanagementsystem.service.CustomerService;
import com.dgut.salesmanagementsystem.service.SalesmanService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

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
        if("add".equals(action))
            addContract(req, resp);
        else
            searchContract(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("../contract/manage_contracts.jsp").forward(req, resp);
    }

    private void searchContract(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contractName = req.getParameter("contractName");
        String contractIDStr = req.getParameter("contractID");
        String status = req.getParameter("status");
        String startTimeStr = req.getParameter("start_time");
        String endTimeStr = req.getParameter("end_time");

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

        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));
        // 获取总记录数以计算总页数
        int totalPages = contractService.getTotalPages(new ContractSearchCriteria(contractName, contractID, status, startTimeStr, endTimeStr), pageSize);

        // 创建 ContractSearchCriteria 对象时，避免空值字段影响查询
        ContractSearchCriteria criteria = new ContractSearchCriteria();
        criteria.setContractName(contractName != null && !contractName.isEmpty() ? contractName : null);
        criteria.setContractID(contractID); // 如果为空则不设置
        criteria.setStatus(status != null && !status.isEmpty() ? ContractStatus.fromInt(Integer.parseInt(status)).getValue() : null);
        criteria.setStartDateStr(startTimeStr != null && !startTimeStr.isEmpty() ? startTimeStr : null);
        criteria.setEndDateStr(endTimeStr != null && !endTimeStr.isEmpty() ? endTimeStr : null);

        List<Contract> contractList = contractService.searchContracts(criteria, pageNum, pageSize);
        HttpSession session = req.getSession();

        session.setAttribute("contractList", contractList);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("pageSize", pageSize);

        // 设置分页相关属性
        session.setAttribute("contractName", contractName);
        session.setAttribute("contractID", contractIDStr);
        session.setAttribute("status", status);
        session.setAttribute("start_time", startTimeStr);
        session.setAttribute("end_time", endTimeStr);


        resp.sendRedirect("contract/manage_contracts.jsp");
    }

    private void addContract(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}
