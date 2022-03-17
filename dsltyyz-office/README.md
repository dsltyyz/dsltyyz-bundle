# dsltyyz-office
- OFFICE包
  - 当前版本: 2.1.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-office ___office办公模块___
  - zip ___zip文件工具包(渲染word和EXCEL模板)___
  - excel ___excel工具包___
    - annotation ___注解___
    - entity ___实体___
    - util ___工具类___
  - pdf ___PDF工具包___
## 2 快速入门
### 2.0 pom.xml配置
~~~
 <dependencies>
        ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-office</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
### 2.1 导出对象增加注解@ExportExcel 排除字段增加@ExcelColumn(false)
~~~
/**
 * 表
 * @author dsltyyz
 */
@ExportExcel
@ApiModel(description = "表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsltyyzTable {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ExcelColumn(false)
    @ApiModelProperty(value = "字段")
    private List<DsltyyzTableField> fields = new ArrayList<>();
}
~~~
### 2.2 Excel输出
~~~
    //创建一个EXCEL
    Excel excel = new Excel();
    excel.setFileName("文件名称");

    //创建一个Sheet工作表
    ExcelSheet excelSheet = new ExcelSheet();
    excelSheet.setSheetName("工作表名称");
    //可选
    //excelSheet.setHeadList(Arrays.asList("头部显示内容"));
    excelSheet.setList(返回结果list);
    //将工作表添加到EXCEL
    excel.getExcelSheetList().add(excelSheet);
    
    //1.将Excel导出到指定目录
    ExcelUtils.exportExcelFile(excel, new File("绝对路径/xxx.xls"));
    //2.将Excel通过浏览器下载 HttpServletResponse
    ExcelUtils.exportExcel(excel, response);
    //3.将Excel转输出流 可用于OSS上传
    InputStream inputStream = ExcelUtils.exportExcelStream(excel);
~~~
### 2.3 Excel输入
~~~
    //注意:sheet第一行为业务名称 第二行为对应解析实体定义字段 数据从第三行开始 可参考导出 debug=true输出
    //1.将Excel输入流解析返回标准单个ExcelSheet
    ExcelSheet excelSheet = ExcelUtils.importExcel(excelInputStream, "对应解析实体.class", excelType);
    //2.将Excel输入流解析返回标准多个ExcelSheet列表
    List<ExcelSheet> list = ExcelUtils.importExcel(excelInputStream, "对应解析实体.class列表", excelType);
~~~