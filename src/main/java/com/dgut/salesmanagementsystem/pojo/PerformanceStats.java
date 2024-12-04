package com.dgut.salesmanagementsystem.pojo;
import java.math.BigDecimal;
import java.util.Map;

public class PerformanceStats {
    private Map<String, BigDecimal> monthlySales;
    private Map<String, BigDecimal> quarterlySales;
    private Map<String, BigDecimal> yearlySales;

    public Map<String, BigDecimal> getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(Map<String, BigDecimal> monthlySales) {
        this.monthlySales = monthlySales;
    }

    public Map<String, BigDecimal> getQuarterlySales() {
        return quarterlySales;
    }

    public void setQuarterlySales(Map<String, BigDecimal> quarterlySales) {
        this.quarterlySales = quarterlySales;
    }

    public Map<String, BigDecimal> getYearlySales() {
        return yearlySales;
    }

    public void setYearlySales(Map<String, BigDecimal> yearlySales) {
        this.yearlySales = yearlySales;
    }
}
