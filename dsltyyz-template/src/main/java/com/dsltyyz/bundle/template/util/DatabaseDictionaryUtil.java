package com.dsltyyz.bundle.template.util;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.dsltyyz.bundle.office.excel.entity.Excel;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheet;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheetColumnProperty;
import com.dsltyyz.bundle.office.excel.util.ExcelUtils;
import com.dsltyyz.bundle.template.bean.DataSourceXml;
import com.dsltyyz.bundle.template.bean.ModityLog;
import com.dsltyyz.bundle.template.bean.MybatisPlusCodeGeneratorXml;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;

/**
 * Description:
 * 数据字典工具类
 *
 * @author: dsltyyz
 * @date: 2019/2/20
 */
@Slf4j
public class DatabaseDictionaryUtil {

    private static MybatisPlusCodeGeneratorXml mybatisPlusCodeGeneratorXml = null;

    /**
     * 初始化数据
     *
     * @return
     */
    private static Boolean init() {
        try {
            // 读取XML文件
            Resource resource = new ClassPathResource("code-generator.xml");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
            mybatisPlusCodeGeneratorXml = XmlBuilderUtil.xmlStrToObject(MybatisPlusCodeGeneratorXml.class, buffer.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 自动生成
     */
    private static void generator(Class clazz) {
        //配置数据源
        DataSourceXml dateSourceXml = mybatisPlusCodeGeneratorXml.getDateSourceXml();
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dateSourceXml.getUrl());
        dsc.setDriverName(dateSourceXml.getDriverName());
        dsc.setUsername(dateSourceXml.getUsername());
        dsc.setPassword(dateSourceXml.getPassword());

        ConfigBuilder configBuilder = new ConfigBuilder(null, dsc, null, null, null);

        Excel excel = new Excel();
//        excel.setFileName("数据库设计");
        excel.setDebug(true);

        //1.修订日志
        ExcelSheet excelSheet1 = new ExcelSheet();
        excelSheet1.setSheetName("修订日志");
        excelSheet1.setPropertyList(Arrays.asList(
                new ExcelSheetColumnProperty("版本号", "version"),
                new ExcelSheetColumnProperty("修订内容", "modifyContent"),
                new ExcelSheetColumnProperty("修订人", "author"),
                new ExcelSheetColumnProperty("审核人", "auditor"),
                new ExcelSheetColumnProperty("修订日期", "modifyTime")
        ));
        excelSheet1.setList(Arrays.asList(
                new ModityLog("v1.0", "初始化数据库设计", mybatisPlusCodeGeneratorXml.getAuthor(), mybatisPlusCodeGeneratorXml.getAuthor(), new Date())
        ));
        excel.getExcelSheetList().add(excelSheet1);

        //2.数据库表目录
        ExcelSheet excelSheet2 = new ExcelSheet();
        excelSheet2.setSheetName("数据库表目录");
        excelSheet2.setPropertyList(Arrays.asList(
                new ExcelSheetColumnProperty("表名", "name"),
                new ExcelSheetColumnProperty("备注", "comment")
        ));
        excelSheet2.setList(configBuilder.getTableInfoList());
        excel.getExcelSheetList().add(excelSheet2);

        //3.数据库表结构
        configBuilder.getTableInfoList().forEach(tableInfo -> {
            ExcelSheet es = new ExcelSheet();
            es.setSheetName(tableInfo.getName() + "[" + tableInfo.getComment()+ "]");
            es.setPropertyList(Arrays.asList(
                    new ExcelSheetColumnProperty("字段", "name"),
                    new ExcelSheetColumnProperty("数据类型", "type"),
                    new ExcelSheetColumnProperty("主键", "keyFlag"),
                    new ExcelSheetColumnProperty("自增", "keyIdentityFlag"),
                    new ExcelSheetColumnProperty("填充", "fill"),
                    new ExcelSheetColumnProperty("备注", "comment")
            ));
            es.setList(tableInfo.getFields());
            excel.getExcelSheetList().add(es);
        });

        //创建文件并输出
        String path = clazz.getResource(File.separator).getPath();
        String filePath = path.substring(0, path.indexOf("/target")) + "/src/main/resources/database.xls";
        try {
            ExcelUtils.exportExcelFile(excel, new File(filePath));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 跑流程
     */
    public static void autorun(Class clazz) {
        //初始化数据
        if (init()) {
            generator(clazz);
        }
    }
}
