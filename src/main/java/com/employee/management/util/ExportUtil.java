package com.employee.management.util;

import com.employee.management.entity.Employee;
import com.employee.management.entity.Payslip;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Map<String, String> FIELD_LABELS = new HashMap<>();
    private static final Map<String, String> PAYSLIP_FIELD_LABELS = new HashMap<>();        
    
    static {
        FIELD_LABELS.put("name", "姓名");
        FIELD_LABELS.put("gender", "性别");
        FIELD_LABELS.put("age", "年龄");
        FIELD_LABELS.put("birthDate", "出生日期");
        FIELD_LABELS.put("height", "身高(cm)");
        FIELD_LABELS.put("weight", "体重(kg)");
        FIELD_LABELS.put("position", "职位");
        FIELD_LABELS.put("email", "邮箱");
        FIELD_LABELS.put("phone", "电话");
        FIELD_LABELS.put("employeeCode", "员工编码");
        
        PAYSLIP_FIELD_LABELS.put("employeeCode", "员工编码");
        PAYSLIP_FIELD_LABELS.put("employeeName", "姓名");
        PAYSLIP_FIELD_LABELS.put("year", "年份");
        PAYSLIP_FIELD_LABELS.put("monthNum", "月份");
        PAYSLIP_FIELD_LABELS.put("basicSalary", "基本工资");
        PAYSLIP_FIELD_LABELS.put("positionSalary", "岗位工资");
        PAYSLIP_FIELD_LABELS.put("performanceBonus", "绩效奖金");
        PAYSLIP_FIELD_LABELS.put("allowances", "津贴");
        PAYSLIP_FIELD_LABELS.put("deductions", "扣款");
        PAYSLIP_FIELD_LABELS.put("socialInsurance", "社保");
        PAYSLIP_FIELD_LABELS.put("housingFund", "公积金");
        PAYSLIP_FIELD_LABELS.put("individualTax", "个税");
        PAYSLIP_FIELD_LABELS.put("grossSalary", "应发工资");
        PAYSLIP_FIELD_LABELS.put("netSalary", "实发工资");
        PAYSLIP_FIELD_LABELS.put("status", "状态");
    }

    public static byte[] exportToExcel(List<Employee> employees, List<String> fields) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("员工数据");
            
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < fields.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(FIELD_LABELS.getOrDefault(fields.get(i), fields.get(i)));
                cell.setCellStyle(headerStyle);
            }
            
            int rowNum = 1;
            for (Employee employee : employees) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                
                for (String field : fields) {
                    Cell cell = row.createCell(colNum++);
                    setCellValue(cell, employee, field);
                }
            }
            
            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public static byte[] exportToCsv(List<Employee> employees, List<String> fields) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(outputStream)) {
            
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < fields.size(); i++) {
                if (i > 0) {
                    header.append(",");
                }
                header.append(FIELD_LABELS.getOrDefault(fields.get(i), fields.get(i)));
            }
            writer.println(header.toString());
            
            for (Employee employee : employees) {
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < fields.size(); i++) {
                    if (i > 0) {
                        row.append(",");
                    }
                    String value = getFieldValue(employee, fields.get(i));
                    if (value.contains(",")) {
                        row.append("\"").append(value).append("\"");
                    } else {
                        row.append(value);
                    }
                }
                writer.println(row.toString());
            }
            
            writer.flush();
            return outputStream.toByteArray();
        }
    }

    public static byte[] exportPayslipsToExcel(List<Payslip> paylips, List<String> fields) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("工资条数据");
            
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < fields.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(PAYSLIP_FIELD_LABELS.getOrDefault(fields.get(i), fields.get(i)));
                cell.setCellStyle(headerStyle);
            }
            
            int rowNum = 1;
            for (Payslip payslip : paylips) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                
                for (String field : fields) {
                    Cell cell = row.createCell(colNum++);
                    setPayslipCellValue(cell, payslip, field);
                }
            }
            
            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public static byte[] exportPayslipsToCsv(List<Payslip> paylips, List<String> fields) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(outputStream)) {
            
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < fields.size(); i++) {
                if (i > 0) {
                    header.append(",");
                }
                header.append(PAYSLIP_FIELD_LABELS.getOrDefault(fields.get(i), fields.get(i)));
            }
            writer.println(header.toString());
            
            for (Payslip payslip : paylips) {
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < fields.size(); i++) {
                    if (i > 0) {    
                        row.append(",");
                    }
                    String value = getPayslipFieldValue(payslip, fields.get(i));
                    if (value.contains(",")) {
                        row.append("\"").append(value).append("\"");
                    } else {
                        row.append(value);
                    }
                }
                writer.println(row.toString());
            }
            
            writer.flush();
            return outputStream.toByteArray();
        }
    }

    private static void setCellValue(Cell cell, Employee employee, String field) {
        String value = getFieldValue(employee, field);
        cell.setCellValue(value);
    }

    private static String getFieldValue(Employee employee, String field) {
        switch (field) {
            case "name":
                return employee.getName() != null ? employee.getName() : "";
            case "gender":
                return employee.getGender() != null ? employee.getGender() : "";
            case "age":
                return employee.getAge() != null ? String.valueOf(employee.getAge()) : "";
            case "birthDate":
                return employee.getBirthDate() != null ? employee.getBirthDate().format(DATE_FORMATTER) : "";
            case "height":
                return employee.getHeight() != null ? String.valueOf(employee.getHeight()) : "";
            case "weight":
                return employee.getWeight() != null ? String.valueOf(employee.getWeight()) : "";
            case "position":
                return employee.getPosition() != null ? employee.getPosition() : "";
            case "email":
                return employee.getEmail() != null ? employee.getEmail() : "";
            case "phone":
                return employee.getPhone() != null ? employee.getPhone() : "";
            case "employeeCode":
                return employee.getEmployeeCode() != null ? employee.getEmployeeCode() : "";
            default:
                return "";
        }
    }

    private static void setPayslipCellValue(Cell cell, Payslip payslip, String field) {
        String value = getPayslipFieldValue(payslip, field);
        cell.setCellValue(value);
    }

    private static String getPayslipFieldValue(Payslip payslip, String field) {
        switch (field) {
            case "employeeCode":
                return payslip.getEmployeeCode() != null ? payslip.getEmployeeCode() : "";
            case "employeeName":
                return payslip.getEmployeeName() != null ? payslip.getEmployeeName() : "";
            case "year":
                return payslip.getYear() != null ? String.valueOf(payslip.getYear()) : "";
            case "monthNum":
                return payslip.getMonthNum() != null ? String.valueOf(payslip.getMonthNum()) : "";
            case "basicSalary":
                return payslip.getBasicSalary() != null ? payslip.getBasicSalary().toString() : "";
            case "positionSalary":
                return payslip.getPositionSalary() != null ? payslip.getPositionSalary().toString() : "";
            case "performanceBonus":
                return payslip.getPerformanceBonus() != null ? payslip.getPerformanceBonus().toString() : "";
            case "allowances":
                return payslip.getAllowances() != null ? payslip.getAllowances().toString() : "";
            case "deductions":
                return payslip.getDeductions() != null ? payslip.getDeductions().toString() : "";
            case "socialInsurance":
                return payslip.getSocialInsurance() != null ? payslip.getSocialInsurance().toString() : "";
            case "housingFund":
                return payslip.getHousingFund() != null ? payslip.getHousingFund().toString() : "";
            case "individualTax":
                return payslip.getIndividualTax() != null ? payslip.getIndividualTax().toString() : "";
            case "grossSalary":
                return payslip.getGrossSalary() != null ? payslip.getGrossSalary().toString() : "";
            case "netSalary":
                return payslip.getNetSalary() != null ? payslip.getNetSalary().toString() : "";
            case "status":
                return payslip.getStatus() != null ? payslip.getStatus() : "";
            default:
                return "";
        }
    }
}

