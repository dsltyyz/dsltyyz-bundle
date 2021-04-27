package com.dsltyyz.bundle.office.excel.util;

import com.dsltyyz.bundle.common.util.FileUtils;
import com.dsltyyz.bundle.common.util.ReflexUtils;
import com.dsltyyz.bundle.common.util.UUIDUtils;
import com.dsltyyz.bundle.office.excel.annotation.ExcelColumn;
import com.dsltyyz.bundle.office.excel.annotation.ExportExcel;
import com.dsltyyz.bundle.office.excel.entity.Excel;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheet;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheetColumnProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Excel工具类
 *
 * @author: dsltyyz
 * @since: 2019-05-28
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
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileNameDotExcelType,"UTF-8"));
        response.setContentType("application/msexcel");
        exportExcel(excel.getExcelSheetList(), response.getOutputStream(), excelType, excel.getDebug());
    }

    /**
     * 导出EXCEL到指定文件
     *
     * @param excel
     * @param file
     * @throws IOException
     */
    public static void exportExcelFile(Excel excel, File file) throws Exception {
        Assert.notNull(excel, "excel不能为null");
        FileUtils.checkFileExists(file);
        exportExcel(excel.getExcelSheetList(), new FileOutputStream(file), XLS_TYPE, excel.getDebug());
    }

    /**
     * 导出EXCEL输入流
     *
     * @param excel
     * @throws IOException
     */
    public static InputStream exportExcelStream(Excel excel) throws Exception {
        Assert.notNull(excel, "excel不能为null");
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        exportExcel(excel.getExcelSheetList(), bs, XLS_TYPE, excel.getDebug());
        return new ByteArrayInputStream(bs.toByteArray());
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
        CellStyle style1 = workbook.createCellStyle();
        //背景
        style1.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //边框
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        //居中
        style1.setAlignment(HorizontalAlignment.CENTER);
        //自动换行
        style1.setWrapText(true);

        CellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(IndexedColors.RED.getIndex());
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //边框
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        //居中
        style1.setAlignment(HorizontalAlignment.CENTER);
        //自动换行
        style3.setWrapText(true);

        CellStyle style4 = workbook.createCellStyle();
        style4.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //边框
        style4.setBorderBottom(BorderStyle.THIN);
        style4.setBorderLeft(BorderStyle.THIN);
        style4.setBorderTop(BorderStyle.THIN);
        style4.setBorderRight(BorderStyle.THIN);
        //居中
        style4.setAlignment(HorizontalAlignment.CENTER);
        //自动换行
        style4.setWrapText(true);

        //sheet表单
        for (ExcelSheet excelSheet : excelSheetList) {
            Sheet sheet = workbook.createSheet(excelSheet.getSheetName());
            if(excelSheet.getList() == null || excelSheet.getList().size() == 0){
                continue;
            }
            excelSheet.setPropertyList(getExcelSheetColumnPropertyList(excelSheet.getList().get(0)));
            //根据列数目设置宽度
            for (int i = 0; i < excelSheet.getPropertyList().size(); i++) {
                sheet.setColumnWidth(i, 5000);
            }
            //创建sheet行数
            //初始化包含1个调试列字段
            int totalRow = 1;
            if (excelSheet.getHeadList() != null) {
                totalRow = totalRow + excelSheet.getHeadList().size();
            }
            if (excelSheet.getPropertyList() != null && excelSheet.getPropertyList().size() > 0) {
                totalRow = totalRow + 1;
            }
            if (excelSheet.getList() != null) {
                totalRow = totalRow + excelSheet.getList().size();
            }
            for (int j = 0; j < totalRow; j++) {
                sheet.createRow(j);
            }

            //记录数据插入行
            int currentRow = 0;
            //1.插入头
            if (excelSheet.getHeadList() != null && excelSheet.getHeadList().size() > 0) {
                List<String> headList = excelSheet.getHeadList();
                //组装数据
                for (int k = 0; k < headList.size(); k++) {
                    Row row = sheet.getRow(currentRow);
                    for (int kk = 0; kk < excelSheet.getPropertyList().size(); kk++) {
                        Cell cell = row.createCell(kk);
                        cell.setCellValue(headList.get(k));
                        cell.setCellStyle(style1);
                    }
                    CellRangeAddress cellAddresses = new CellRangeAddress(currentRow, currentRow, 0, excelSheet.getPropertyList().size() - 1);
                    sheet.addMergedRegion(cellAddresses);
                    currentRow++;
                }
            }

            //2.插入列名及数据
            if (excelSheet.getList() != null && excelSheet.getList().size() > 0) {
                //2.1列名
                List<ExcelSheetColumnProperty> properties = excelSheet.getPropertyList();
                for (int i = 0; i < properties.size(); i++) {
                    ExcelSheetColumnProperty excelSheetColumnProperty = properties.get(i);
                    Cell row0Cell = sheet.getRow(currentRow).createCell(i);
                    row0Cell.setCellValue(excelSheetColumnProperty.getColumnName());
                    row0Cell.setCellStyle(style1);
                    //是否显示实体字段名称
                    if (debug) {
                        Cell row1Cell = sheet.getRow(currentRow + 1).createCell(i);
                        row1Cell.setCellValue(excelSheetColumnProperty.getColumnProperty());
                        row1Cell.setCellStyle(style3);
                    }
                }
                currentRow++;
                if (debug) {
                    currentRow++;
                }

                //2.2数据
                List list = excelSheet.getList();
                //组装数据
                for (int k = 0; k < list.size(); k++) {
                    Row row = sheet.getRow(currentRow);
                    Map<String, String> map = ReflexUtils.objectToMap(list.get(k));
                    for (int kk = 0; kk < properties.size(); kk++) {
                        Cell rowCell = row.createCell(kk);
                        rowCell.setCellValue(map.get(properties.get(kk).getColumnProperty()));
                        rowCell.setCellStyle(style4);
                    }
                    currentRow++;
                }
            }

        }
        workbook.write(outputStream);
    }

    private static List<ExcelSheetColumnProperty> getExcelSheetColumnPropertyList(Object obj) {
        List<ExcelSheetColumnProperty> list = new ArrayList<>();
        Class<?> objClass = obj.getClass();
        boolean flag = objClass.isAnnotationPresent(ExportExcel.class);
        for (Field field : objClass.getDeclaredFields()) {
            if (flag) {
                //字段有ExcelColumn注解，跳过标记为false的字段
                if(field.isAnnotationPresent(ExcelColumn.class) && !field.getAnnotation(ExcelColumn.class).value()){
                    continue;
                }
                ExcelSheetColumnProperty excelSheetColumnProperty = new ExcelSheetColumnProperty();
                excelSheetColumnProperty.setColumnProperty(field.getName());
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                if (apiModelProperty != null) {
                    excelSheetColumnProperty.setColumnName(apiModelProperty.value());
                } else {
                    excelSheetColumnProperty.setColumnName(field.getName());
                }
                list.add(excelSheetColumnProperty);
            }
        }
        return list;
    }
}
