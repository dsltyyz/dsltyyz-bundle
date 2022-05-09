# dsltyyz-office
- OFFICE包
  - 当前版本: 2.3.0-SNAPSHOT
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
## 3 EXCEL导出
### 3.1 导出对象排除字段增加@ExcelColumn 默认为true 
~~~
@ApiModel(description = "表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsltyyzTable {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String comment;

    //排除
    @ExcelColumn(false)
    @ApiModelProperty(value = "字段")
    private List<DsltyyzTableField> fields = new ArrayList<>();
}
~~~
### 3.2 Excel输出
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
## 4 EXCEL导入
### 4.1 指定单元格解析字段模板导入
#### 4.1.1 excel模板示例
| 第一行  | 业务名称 | 姓名 |
------------- | :-------------: | :-------------:
| 第二行  | 对应解析实体字段  | name |
| 第三行  | 数据 | 张三 |
| 第N行  | 数据 | 李四 |
#### 4.1.2 excel导入调用示例
~~~
    //注意:sheet第一行为业务名称 第二行为对应解析实体定义字段 数据从第三行开始 可参考导出 debug=true输出
    //1.将Excel输入流解析返回标准单个ExcelSheet
    ExcelSheet excelSheet = ExcelUtils.importExcel("EXCEL文件流", "对应解析实体.class", "EXCEL文件类型");
    //2.将Excel输入流解析返回标准多个ExcelSheet列表
    List<ExcelSheet> list = ExcelUtils.importExcel("EXCEL文件流", "对应解析实体.class列表", "EXCEL文件类型");
~~~
### 4.2 固定行模板导入
#### 4.2.1 对象字段定位注解示例
~~~
@ApiModel(description = "销售单DTO")
@Data
public class SaleOrderDTO implements Serializable {

    @ApiModelProperty(value = "编号", hidden = true)
    private Long id;

    @ExcelColumnLocation(value=1)
    @ApiModelProperty(value = "送货时间 yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = DateUtils.PATTERN_DATE)
    private LocalDateTime deliveryTime;

    @ExcelColumnLocation(value=2)
    @ApiModelProperty(value = "客户名称", required = true)
    private String customerName;

    @ExcelColumnLocation(value=3)
    @ApiModelProperty(value = "送货单号", required = true)
    private String deliveryNum;

    @ExcelColumnLocation(value=4)
    @ApiModelProperty(value = "商务顾问", required = true)
    private String businessConsultant;

    @ExcelColumnLocation(value=5)
    @ApiModelProperty(value = "商品名称", required = true)
    private String productName;

    @ExcelColumnLocation(value=6)
    @ApiModelProperty(value = "单价-元")
    private BigDecimal pricePerUnit;

    @ExcelColumnLocation(value=7)
    @ApiModelProperty(value = "长")
    private BigDecimal length;

    @ExcelColumnLocation(value=8)
    @ApiModelProperty(value = "宽")
    private BigDecimal width;

    @ExcelColumnLocation(value=9)
    @ApiModelProperty(value = "高")
    private BigDecimal height;

    //自定义数据处理助手
    @ExcelColumnLocation(value=10, dataHandler = NumericToIntegerDataHandler.class)
    @ApiModelProperty(value = "数量")
    private Long amount;

    @ExcelColumnLocation(11)
    @ApiModelProperty(value = "重量-KG")
    private BigDecimal weight;

    @ExcelColumnLocation(12)
    @ApiModelProperty(value = "材料费-元")
    private BigDecimal materialExpense;

    @ExcelColumnLocation(13)
    @ApiModelProperty(value = "加工费-元")
    private BigDecimal processingCharges;

    @ExcelColumnLocation(14)
    @ApiModelProperty(value = "备注")
    private String remark;

    @ExcelColumnLocation(15)
    @ApiModelProperty(value = "是否含税")
    private Boolean includeTax;

    @ExcelColumnLocation(16)
    @ApiModelProperty(value = "规格")
    private String specification;

}
~~~
#### 4.2.2 自定义数据处理助手
~~~
public class NumericToIntegerDataHandler implements DataHandler {

    /**
     * 数据处理
     *
     * @param o
     * @return
     */
    @Override
    public String deal(String o, String param) {
        try {
            if (StringUtils.isEmpty(o)) {
                return null;
            }
            return new BigDecimal(o).setScale(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
~~~
#### 4.2.3 excel导入调用示例
~~~
    //注意:sheet第一行为业务名称 数据从第二行开始
    //1.将Excel输入流解析返回标准单个ExcelSheet 默认读取第一张
    ExcelSheet excelSheet = ExcelUtils.importExcelByLocation("EXCEL文件流", "对应解析实体.class", "EXCEL文件类型");
    //2.将Excel输入流解析返回标准单个ExcelSheet
    ExcelSheet excelSheet = ExcelUtils.importExcelByLocationWithSheetNum("EXCEL文件流", "指定实体对应sheet", "对应解析实体.class", "EXCEL文件类型");
    //3.将Excel输入流解析返回标准多个ExcelSheet列表
    List<ExcelSheet> list = ExcelUtils.importExcelByLocation("EXCEL文件流", "对应解析实体.class列表", "EXCEL文件类型");
~~~