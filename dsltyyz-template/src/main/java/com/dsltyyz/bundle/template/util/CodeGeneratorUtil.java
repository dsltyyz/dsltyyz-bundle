package com.dsltyyz.bundle.template.util;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.dsltyyz.bundle.common.util.DateUtils;
import com.dsltyyz.bundle.common.util.YamlUtils;
import com.dsltyyz.bundle.office.excel.entity.Excel;
import com.dsltyyz.bundle.office.excel.entity.ExcelSheet;
import com.dsltyyz.bundle.office.excel.util.ExcelUtils;
import com.dsltyyz.bundle.template.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Description:
 * 代码生成器工具类
 *
 * @author: dsltyyz
 * @since: 2019-2-20
 */
@Slf4j
public class CodeGeneratorUtil {

    public static final String YML_SUFFIX = ".yml";
    public static final String YAML_SUFFIX = ".yaml";
    public static final String XML_SUFFIX = ".xml";
    public static final String FILE_NAME = "code-generator";

    private static MybatisPlusCodeGeneratorXml mybatisPlusCodeGeneratorConfig = null;

    private static String autoFindConfigFile() {
        //1.查找默认yml文件
        Resource resourceYml = new ClassPathResource(FILE_NAME + YML_SUFFIX);
        if (resourceYml.exists()) {
            return FILE_NAME + YML_SUFFIX;
        }
        //2.查找默认xml文件
        Resource resourceXml = new ClassPathResource(FILE_NAME + XML_SUFFIX);
        if (resourceXml.exists()) {
            return FILE_NAME + XML_SUFFIX;
        }
        return null;
    }

    private static Boolean init(String configFile) {
        Assert.isTrue(!StringUtils.isEmpty(configFile), "配置文件不能为空");
        Assert.isTrue(configFile.endsWith(XML_SUFFIX) || configFile.endsWith(YML_SUFFIX) || configFile.endsWith(YAML_SUFFIX), "配置文件类型不正确");
        Resource resource= new ClassPathResource(configFile);
        Assert.isTrue(resource.exists(), "配置文件不存在");
        if (configFile.endsWith(XML_SUFFIX)) {
            return initXml(configFile);
        } else if (configFile.endsWith(YML_SUFFIX) || configFile.endsWith(YAML_SUFFIX)) {
            return initYaml(configFile);
        }
        return false;
    }

    private static Boolean initYaml(String configFile) {
        try {
            mybatisPlusCodeGeneratorConfig = YamlUtils.getObject(new ClassPathResource(configFile).getInputStream(), new TypeReference<MybatisPlusCodeGeneratorXml>(){});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 初始化数据
     *
     * @return
     */
    private static Boolean initXml(String configFile) {
        try {
            // 读取XML文件
            Resource resource = new ClassPathResource(configFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
            mybatisPlusCodeGeneratorConfig = XmlBuilderUtil.xmlStrToObject(MybatisPlusCodeGeneratorXml.class, buffer.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void generator(Class clazz) {
        String path = clazz.getResource(File.separator).getPath();
        String projectPath = path.substring(0, path.indexOf("/target"));
        DataSourceXml dataSource = mybatisPlusCodeGeneratorConfig.getDataSource();
        StrategyXml strategy = mybatisPlusCodeGeneratorConfig.getStrategy();
        // 定义输出目录
        String outPath = (projectPath + "/src/main/java/" + strategy.getParentPackage() + "/" + strategy.getModuleName()).replaceAll("\\.", "\\" + File.separator);
        FastAutoGenerator.create(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword())
                //全局配置
                .globalConfig(builder -> {
                    builder.author(mybatisPlusCodeGeneratorConfig.getAuthor())
                        .outputDir(projectPath + "/src/main/java");
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent(strategy.getParentPackage())
                            .moduleName(strategy.getModuleName())
                            .entity("domain.entity")
                            .mapper("dao");
                })
                //模板配置
                .templateConfig(builder -> {
                    builder.entity("user-defined/entity.java")
                            .mapper("user-defined/dao.java")
                            .service("user-defined/service.java")
                            .serviceImpl("user-defined/serviceImpl.java")
                            .controller("user-defined/controller.java")
                            .mapperXml(null)
                            .build();
                })
                //自定义注入配置
                .injectionConfig(builder -> {
                    Map<String, String> outputMap = new HashMap<>();
                    builder.beforeOutputFile((tableInfo, stringObjectMap) -> {
                        //文件全路径
                        outputMap.put(outPath + "/domain/dto/" + tableInfo.getEntityName() + "DTO" + StringPool.DOT_JAVA, "user-defined/dto.java.ftl");
                        outputMap.put(outPath + "/domain/dto/" + tableInfo.getEntityName() + "PageDTO" + StringPool.DOT_JAVA, "user-defined/pagedto.java.ftl");
                        outputMap.put(outPath + "/domain/vo/" + tableInfo.getEntityName() + "VO" + StringPool.DOT_JAVA, "user-defined/vo.java.ftl");
                    })
                    .customFile(outputMap)
                    .build();
                })
                //自定义策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(strategy.getIncludeTable().getTable())
                    .enableSkipView();
                    //Mapper文件替换问DAO文件
                    builder.mapperBuilder().formatMapperFileName("%sDAO");
                })
                //引擎默认输出到other 重写输出 不同的模板输出到不同的包
                .templateEngine(new FreemarkerTemplateEngine(){
                    @Override
                    protected void outputCustomFile(Map<String, String> customFile, TableInfo tableInfo, Map<String, Object> objectMap) {
                        customFile.forEach((key, value) -> {
                            outputFile(new File(key), objectMap, value);
                        });
                    }
                })
                .execute();
        ;
    }

    /**
     * 代码生成
     *
     * @param clazz 项目执行代码生成类
     */
    public static void autoRun(Class clazz) {
        //初始化数据
        autoRun(clazz, autoFindConfigFile());
    }


    /**
     * 代码生成
     *
     * @param clazz      项目执行代码生成类
     * @param configFile 配置文件
     *                   参考jar下
     *                   XML resources/code-generator.xml.example
     *                   YML resources/code-generator.yml.example
     */
    public static void autoRun(Class clazz, String configFile) {
        //初始化数据
        //configFile参考resources/code-generator.xml.example
        if (init(configFile)) {
            generator(clazz);
        }
    }

    private static void dictionary(Class clazz) {
        DataSourceXml dataSource = mybatisPlusCodeGeneratorConfig.getDataSource();
        DataSourceConfig.Builder builder = new DataSourceConfig.Builder(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword()).dbQuery(new DsltyyzMySqlQuery());
        ConfigBuilder configBuilder = new ConfigBuilder(null, builder.build(), null, null, null, null);
        List<DsltyyzTable> list = new ArrayList<>();
        for (TableInfo tableInfo : configBuilder.getTableInfoList()) {
            DsltyyzTable dsltyyzTable = new DsltyyzTable();
            dsltyyzTable.setName(tableInfo.getName());
            dsltyyzTable.setComment(tableInfo.getComment());
            for (TableField tableField : tableInfo.getFields()) {
                DsltyyzTableField dsltyyzTableField = new DsltyyzTableField();
                BeanUtils.copyProperties(tableField, dsltyyzTableField);
                Map<String, Object> customMap = tableField.getCustomMap();
                dsltyyzTableField.setNullFlag("YES".equals(customMap.get("Null").toString()));
                dsltyyzTableField.setDefaultValue(customMap.get("Default") == null ? "" : customMap.get("Default").toString());
                dsltyyzTable.getFields().add(dsltyyzTableField);
            }
            list.add(dsltyyzTable);
        }
        Excel excel = new Excel();

        //1.修订日志
        ExcelSheet excelSheet1 = new ExcelSheet();
        excelSheet1.setSheetName("修订日志");
        excelSheet1.setHeadList(Arrays.asList("修订日志"));
        excelSheet1.setList(Arrays.asList(
                new ModifyLog("v1.0", "数据库设计", mybatisPlusCodeGeneratorConfig.getAuthor(), mybatisPlusCodeGeneratorConfig.getAuthor(), DateUtils.format(new Date(), "yyyy-MM-dd"))
        ));
        excel.getExcelSheetList().add(excelSheet1);

        //2.数据库表目录
        ExcelSheet excelSheet2 = new ExcelSheet();
        excelSheet2.setSheetName("表目录");
        excelSheet2.setHeadList(Arrays.asList("表目录"));
        excelSheet2.setList(list);
        excel.getExcelSheetList().add(excelSheet2);

        //3.数据库表结构
        list.forEach(tableInfo -> {
            ExcelSheet es = new ExcelSheet();
            es.setSheetName(tableInfo.getName() + "表");
            es.setHeadList(Arrays.asList(tableInfo.getName() + tableInfo.getComment()));
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
     *
     * @param clazz 项目执行代码生成类
     */
    public static void databaseDictionary(Class clazz) {
        databaseDictionary(clazz, autoFindConfigFile());
    }

    /**
     * 数据库字典
     *
     * @param clazz      项目执行代码生成类
     * @param configFile 配置文件
     *                   参考jar下
     *                   XML resources/code-generator.xml.example
     *                   YML resources/code-generator.yml.example
     */
    public static void databaseDictionary(Class clazz, String configFile) {
        //初始化数据
        //configFile参考resources/code-generator.xml.example
        if (init(configFile)) {
            dictionary(clazz);
        }
    }

}
