package com.dsltyyz.bundle.template.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.dsltyyz.bundle.common.util.DateUtils;
import com.dsltyyz.bundle.office.excel.entity.Excel;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheet;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheetColumnProperty;
import com.dsltyyz.bundle.office.excel.util.ExcelUtils;
import com.dsltyyz.bundle.template.bean.DataSourceXml;
import com.dsltyyz.bundle.template.bean.ModityLog;
import com.dsltyyz.bundle.template.bean.MybatisPlusCodeGeneratorXml;
import com.dsltyyz.bundle.template.bean.StrategyXml;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * 代码生成器工具类
 *
 * @author: dsltyyz
 * @date: 2019/2/20
 */
@Slf4j
public class CodeGeneratorUtil {

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
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String path = clazz.getResource(File.separator).getPath();
        String projectPath = path.substring(0, path.indexOf("/target"));
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(mybatisPlusCodeGeneratorXml.getAuthor());

        //重新定义类命名
        gc.setMapperName("%sDAO");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        mpg.setGlobalConfig(gc);

        //配置数据源
        DataSourceXml dateSourceXml = mybatisPlusCodeGeneratorXml.getDateSourceXml();
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dateSourceXml.getUrl());
        dsc.setDriverName(dateSourceXml.getDriverName());
        dsc.setUsername(dateSourceXml.getUsername());
        dsc.setPassword(dateSourceXml.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        StrategyXml strategyXml = mybatisPlusCodeGeneratorXml.getStrategyXml();
        PackageConfig pc = new PackageConfig();
        pc.setParent(strategyXml.getParentPackage());
        pc.setModuleName(strategyXml.getModuleName());
        pc.setEntity("domain.entity");
        pc.setMapper("dao");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 定义输出目录
        String outPath = (projectPath + "/src/main/java/" + strategyXml.getParentPackage() + "/" + strategyXml.getModuleName()).replaceAll("\\.", "\\" + File.separator);
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        // 自定义模板
        String dtoTemplate = "user-defined/dto.java.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(dtoTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return outPath + "/domain/dto/" + tableInfo.getEntityName() + "DTO" + StringPool.DOT_JAVA;
            }
        });

        String pageDtoTemplate = "user-defined/pagedto.java.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(pageDtoTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return outPath + "/domain/dto/" + tableInfo.getEntityName() + "PageDTO" + StringPool.DOT_JAVA;
            }
        });

        String voTemplate = "user-defined/vo.java.ftl";
        focList.add(new FileOutConfig(voTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return outPath + "/domain/vo/" + tableInfo.getEntityName() + "VO" + StringPool.DOT_JAVA;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        templateConfig.setEntity("user-defined/entity.java");
        templateConfig.setMapper("user-defined/dao.java");
        templateConfig.setService("user-defined/service.java");
        templateConfig.setServiceImpl("user-defined/serviceImpl.java");
        templateConfig.setController("user-defined/controller.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        String[] array = new String[strategyXml.getIncludeTableXML().getTable().size()];
        strategy.setInclude(strategyXml.getIncludeTableXML().getTable().toArray(array));
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * 代码生成
     */
    public static void autorun(Class clazz) {
        //初始化数据
        if (init()) {
            generator(clazz);
        }
    }


    /**
     * 生成数据字典
     */
    private static void dictionary(Class clazz) {
        //配置数据源
        DataSourceXml dateSourceXml = mybatisPlusCodeGeneratorXml.getDateSourceXml();
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dateSourceXml.getUrl());
        dsc.setDriverName(dateSourceXml.getDriverName());
        dsc.setUsername(dateSourceXml.getUsername());
        dsc.setPassword(dateSourceXml.getPassword());

        ConfigBuilder configBuilder = new ConfigBuilder(null, dsc, null, null, null);

        Excel excel = new Excel();

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
                new ModityLog("v1.0", "初始化数据库设计", mybatisPlusCodeGeneratorXml.getAuthor(), mybatisPlusCodeGeneratorXml.getAuthor(), DateUtils.format(new Date(), "yyyy-MM-dd"))
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
            es.setSheetName(tableInfo.getName());
            es.setHeadList(Arrays.asList(tableInfo.getName()+tableInfo.getComment()));
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
     * 数据库字典
     * @param clazz
     */
    public static void databaseDictionary(Class clazz){
        //初始化数据
        if (init()) {
            dictionary(clazz);
        }
    }
}
