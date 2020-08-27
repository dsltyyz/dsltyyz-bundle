package com.dsltyyz.bundle.office.excel.util;

import com.dsltyyz.bundle.common.util.ReflexUtils;
import com.dsltyyz.bundle.common.util.UUIDUtils;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheet;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheetColumnProperty;
import com.dsltyyz.bundle.office.excel.entity.Excel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Excel工具类
 *
 * @author: dsltyyz
 * @date: 2019/05/28
 */
public class ExcelUtils {

    public static final String XLS_TYPE = "xls";
    public static final String XLSX_TYPE = "xlsx";

    /**
     * 导入excel单sheet
     *
     * @param excelInputStream Excel输入流
     * @param clazz            sheet对应转对象类型
     * @param excelType        Excel类型
     * @return
     * @throws Exception
     */
    public static ExcelSheet importExcel(InputStream excelInputStream, Class clazz, String excelType) throws Exception {
        return importExcel(excelInputStream, new ArrayList<>(Arrays.asList(clazz)), excelType).get(0);
    }

    /**
     * 导入excel 多sheet
     *
     * @param excelInputStream Excel输入流
     * @param classList        每个sheet对应转对象类型
     * @param excelType        Excel类型
     * @return
     * @throws Exception
     */
    public static List<ExcelSheet> importExcel(InputStream excelInputStream, List<Class> classList, String excelType) throws Exception {
        List<ExcelSheet> excelSheetList = new ArrayList<>();
        Workbook workbook;
        if (XLS_TYPE.equals(excelType)) {
            workbook = new HSSFWorkbook(excelInputStream);
        } else if (XLSX_TYPE.equals(excelType)) {
            workbook = new XSSFWorkbook(excelInputStream);
        } else {
            throw new IllegalArgumentException("fileType格式不正确");
        }
        //获取excel中sheet数目
        int sheetNumber = workbook.getNumberOfSheets();
        if (classList.size() != sheetNumber) {
            throw new IllegalArgumentException("请检查sheet数目与标准模板是否相同");
        }
        //sheet表
        for (int i = 0; i < sheetNumber; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            ExcelSheet excelSheet = new ExcelSheet();
            //获取当前sheet名称
            excelSheet.setSheetName(sheet.getSheetName());
            //除了列明说明 每列对应字段无数据
            if (sheet.getPhysicalNumberOfRows() <= 2) {
                excelSheetList.add(excelSheet);
                break;
            }
            //行
            for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                Row row = sheet.getRow(j);
                //列
                for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                    Cell cell = row.getCell(k);
                    //第一行列名称 第二行对应组装字段
                    if (j == 0) {
                        ExcelSheetColumnProperty excelSheetColumnProperty = new ExcelSheetColumnProperty();
                        excelSheetColumnProperty.setColumnName(cell.getStringCellValue());
                        excelSheet.getPropertyList().add(excelSheetColumnProperty);
                    } else if (j == 1) {
                        ExcelSheetColumnProperty excelSheetColumnProperty = excelSheet.getPropertyList().get(k);
                        excelSheetColumnProperty.setColumnProperty(cell.getStringCellValue());
                    } else {
                        //实例化接收对象
                        Object object = ReflexUtils.instance(classList.get(i));
                        //对象属性对应业务名及属性
                        List<ExcelSheetColumnProperty> propertyList = excelSheet.getPropertyList();
                        //获取每列名称及对应属性
                        ExcelSheetColumnProperty excelSheetColumnProperty = propertyList.get(k);
                        //为对象属性设置值
                        ReflexUtils.setPropertyValueForObject(object, excelSheetColumnProperty.getColumnProperty(), cell.getStringCellValue());
                        excelSheet.getList().add(object);
                    }
                }
            }
            //设置当前读取Excel Sheet
            excelSheetList.add(excelSheet);
        }
        return excelSheetList;
    }

    /**
     * 导出Excel到输出流 单sheet
     *
     * @param excelSheet   需要导出的sheet
     * @param outputStream 输出流
     * @param excelType    Excel类型
     * @param debug        是否调试
     * @throws Exception
     */
    public static void exportExcel(ExcelSheet excelSheet, OutputStream outputStream, String excelType, Boolean debug) throws Exception {
        exportExcel(new ArrayList<>(Arrays.asList(excelSheet)), outputStream, excelType, debug);
    }

    /**
     * 导出EXCEL
     *
     * @param excel
     * @param response
     * @throws IOException
     */
    public static void exportExcel(Excel excel, HttpServletResponse response) throws Exception {
        Assert.notNull(excel, "excel不能为null");
        //文件名带类型
        String fileNameDotExcelType;
        //EXCEL类型
        String excelType;
        if (null != excel.getFileName()) {
            if (excel.getFileName().indexOf(XLS_TYPE) != -1) {
                fileNameDotExcelType = excel.getFileName();
                excelType = XLS_TYPE;
            } else if (excel.getFileName().indexOf(XLSX_TYPE) != -1) {
                fileNameDotExcelType = excel.getFileName();
                excelType = XLSX_TYPE;
            } else {
                //文件名不带EXCEL后缀默认为XLS_TYPE
                fileNameDotExcelType = excel.getFileName() + "." + XLS_TYPE;
                excelType = XLS_TYPE;
            }
        } else {
            //未配置名称 UUID+XLS_TYPE
            fileNameDotExcelType = UUIDUtils.getUUID() + "." + XLS_TYPE;
            excelType = XLS_TYPE;
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileNameDotExcelType.getBytes("utf-8"), "ISO-8859-1"));
        response.setContentType("application/msexcel");
        exportExcel(excel.getExcelSheetList(), response.getOutputStream(), excelType, excel.getDebug());
    }

    /**
     * 导出Excel到输出流 多sheet
     *
     * @param excelSheetList 需要导出的sheet列表
     * @param outputStream   输出流
     * @param excelType      Excel类型
     * @param debug          是否调试
     */
    public static void exportExcel(List<ExcelSheet> excelSheetList, OutputStream outputStream, String excelType, Boolean debug) throws Exception {
        Workbook workbook;
        if (XLS_TYPE.equals(excelType)) {
            workbook = new HSSFWorkbook();
        } else if (XLSX_TYPE.equals(excelType)) {
            workbook = new XSSFWorkbook();
        } else {
            throw new IllegalArgumentException("fileType格式不正确");
        }

        //设置单元格样式
        CellStyle greenStyle = workbook.createCellStyle();
        greenStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        CellStyle redStyle = workbook.createCellStyle();
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (ExcelSheet excelSheet : excelSheetList) {
            Sheet sheet = workbook.createSheet(excelSheet.getSheetName());

            //创建sheet行数
            List list = excelSheet.getList();
            //所有行 = 行业务名称+业务属性+所有数据数目
            //标题行数 行业务名称
            int titleRow = 1;
            //调试模式 行业务名称+业务属性
            if (debug) {
                titleRow = 2;
            }
            for (int j = 0; j < list.size() + titleRow; j++) {
                sheet.createRow(j);
            }

            //组装头
            List<ExcelSheetColumnProperty> propertyList = excelSheet.getPropertyList();
            for (int i = 0; i < propertyList.size(); i++) {
                ExcelSheetColumnProperty excelSheetColumnProperty = propertyList.get(i);
                Cell row0Cell = sheet.getRow(0).createCell(i);
                row0Cell.setCellValue(excelSheetColumnProperty.getColumnName());
                row0Cell.setCellStyle(greenStyle);
                //是否显示实体字段名称
                if (debug) {
                    Cell row1Cell = sheet.getRow(1).createCell(i);
                    row1Cell.setCellValue(excelSheetColumnProperty.getColumnProperty());
                    row1Cell.setCellStyle(redStyle);
                }
            }

            //组装数据
            for (int k = 0; k < list.size(); k++) {
                Row row = sheet.getRow(k + titleRow);
                Map<String, String> map = ReflexUtils.objectToMap(list.get(k));
                for (int kk = 0; kk < propertyList.size(); kk++) {
                    Cell rowCell = row.createCell(kk);
                    rowCell.setCellValue(map.get(propertyList.get(kk).getColumnProperty()));
                }
            }
        }
        workbook.write(outputStream);
    }

}
