package com.web.common.web.common.util.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;



public class ExcelUtil {

  private static final Logger logger = Logger.getLogger(ExcelUtil.class);

  // 替换生成的excel对象
  HSSFWorkbook workbook = null;
  // xsl模板文件
  String templateFile = "";
  // 目标文件
  String xslFile = "";
  // 模板文件位置
  String tmpDir = ""; // SystemParameter.getInstance().getRptTemplate()

  // 输出xsl路径
  String xlsDir = ""; // SystemParameter.getInstance().getRptOutFile();

  InputStream fs;


  // 数据集合
  List dataList = null;
  // 数据Map
  Map dataMap = null;
  // excel模版里需要替换的CodeField信息集合
  ArrayList codes = null;
  // 错误信息
  String errMsg = "err";


  HSSFCellStyle cellStyle2;

  HSSFDataFormat format;

  HSSFFont font;

  private int styleType = 0; // 采用的显示样式类型

  private void setErrMsg(String errMsg) {
    this.errMsg = "err-->>" + errMsg;
  }

  /**
   * 得到模版文件标记域信息
   *
   * @param xlsName
   * @throws Exception
   */
  private void getExcelCodeList(String xlsName) throws Exception {
    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(xlsName));
    HSSFWorkbook outwb = new HSSFWorkbook(fs);

    String cellmsg = "";
    codes = new ArrayList();
    // 遍历工作薄
    for (int i = 0; i < outwb.getNumberOfSheets(); i++) {// one sheet
      HSSFSheet outsheet = outwb.getSheetAt(i);

      // 遍历工作行
      for (int j = 0; j <= outsheet.getLastRowNum(); j++) {

        HSSFRow outrow = (HSSFRow) outsheet.getRow(j);
        if (outrow == null)
          continue;
        // 获取列
        for (int k = 0; k <= outrow.getLastCellNum(); k++) {

          HSSFCell outcell = (HSSFCell) outrow.getCell((short) k);
          cellmsg = getExcelCellValue(outcell);
          if (cellmsg.startsWith("#") || cellmsg.startsWith("%") || cellmsg.startsWith("&")) {
            codes.add(new CodeField(cellmsg, i, j, k));
          }
        }
      }
    }

  }

  /**
   * 将模版文件存成副本
   */
  private void doSaveAs(String source, String dest) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(dest);
      POIFSFileSystem fs = new POIFSFileSystem(this.fs);
      HSSFWorkbook wb = new HSSFWorkbook(fs);
      wb.write(fos);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        fos.close();
      } catch (Exception ei) {
        ei.printStackTrace();
      }
    }
  }



  /**
   * 替换baseinfo信息 # 表示单值，在模版中不允许重复
   *
   * @param strDest
   * @param codes
   */

  private void exportBaseInfo() {
    cellStyle2 = workbook.createCellStyle();
    for (int i = 0; i < codes.size(); i++) {

      CodeField field = (CodeField) codes.get(i);
      if (field.getCode().startsWith("#")) {
        Object obj = dataMap.get(field.getCode().substring(1));
        String newValue = "";
        if (obj != null) {
          newValue = obj.toString();
        }
        // String newValue = obj == null ? "" : obj.toString();
        // 写入列
        setExcelCell(field.getSheet(), field.getRow(), field.getCol(), newValue);
      }
    }
  }

  private void setExcelCell(int isheet, int irow, int icol, String newValue) {
    try {
      HSSFCell cell = null;
      HSSFSheet sheet = workbook.getSheetAt(isheet);
      // 判断行是否存在,如果不存在则建立一行
      HSSFRow row = sheet.getRow(irow);

      if (row == null) {
        row = sheet.createRow(irow);
      }
      cell = row.getCell((short) icol);
      if (cell == null) {
        cell = row.createCell((short) icol);
      }
      setCellStyle(irow, icol, styleType);
      setExcelCellValue(cell, newValue);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 功能：设置显示属性（根据row、col来定位,样式需要自己定义类型并扩展功能代码） 作者：魏殿猛 修改时间：2011-8-25 下午02:10:06
   *
   * @param irow
   * @param col
   */
  private void setCellStyle(int irow, int col, int styleType) {

    switch (styleType) {
      case 0: // 默认的样式(第一行标题居中加粗加大字体，其他左对其)
        if (irow == 0) { // 第一行，一般是标题
          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
          font.setFontHeightInPoints((short) 20);
          cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
          cellStyle2.setFont(font);
        } else {
          font.setFontName("宋体");
          font.setFontHeightInPoints((short) 9);
          cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
          cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
          cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
          cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
          cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
          if (col == 0) {
            cellStyle2.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
          } else if (col == 12 || col == 15) {
            cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
          } else if (col == 50) {
            cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0000"));
          } else {
            cellStyle2.setDataFormat(format.getFormat("@"));
          }
          cellStyle2.setFont(font);
        }
        break;
      case 1: // 默认的样式(第一行标题居中加粗加大字体，其他右对其)【如：银行支付报表】
        if (irow == 0) { // 第一行，一般是标题
          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
          font.setFontHeightInPoints((short) 20);
          cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
          cellStyle2.setFont(font);
        } else {
          font.setFontName("宋体");
          font.setFontHeightInPoints((short) 9);
          cellStyle2 = workbook.createCellStyle();
          cellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
          cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
          cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
          cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
          cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
          if (col == 0) {
            cellStyle2.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
          } else if (col == 12 || col == 15) {
            cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
          } else if (col == 50) {
            cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0000"));
          } else {
            cellStyle2.setDataFormat(format.getFormat("@"));
          }
          cellStyle2.setFont(font);
        }
        break;
    }

  }

  private void setExcelCellValue(HSSFCell outcell, String newValue) {

    if (outcell == null) {
      return;
    }

    boolean flag = false;
    double value = 0;
    // try {
    // value = Double.parseDouble(newValue);
    // flag = true;
    // outcell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    // } catch (Exception e) {
    // outcell.setCellType(HSSFCell.CELL_TYPE_STRING);
    // flag = false;
    // }
    // 创建一个DataFormat对象 6 HSSFDataFormat format = wb.createDataFormat(); 7
    // //这样才能真正的控制单元格格式，@就是指文本型，具体格式的定义还是参考上面的原文吧 8 c
    // cellStyle2.setDataFormat(format.getFormat("@"));


    outcell.setCellStyle(cellStyle2);

    // outcell.setEncoding(outcell.ENCODING_UTF_16);
    if (flag == true) {
      outcell.setCellValue(value);
    } else {
      outcell.setCellValue(newValue);
    }
  }

  private String getExcelCellValue(HSSFCell outcell) {
    String cellmsg = "";
    if (outcell == null) {
      return "";
    }
    switch (outcell.getCellType()) {
      case HSSFCell.CELL_TYPE_BLANK:
        cellmsg = outcell.getStringCellValue();
        break;
      case HSSFCell.CELL_TYPE_BOOLEAN:
        cellmsg = String.valueOf(outcell.getBooleanCellValue());
        break;
      case HSSFCell.CELL_TYPE_ERROR:
        break;
      case HSSFCell.CELL_TYPE_FORMULA:
        break;
      case HSSFCell.CELL_TYPE_NUMERIC:
        cellmsg = String.valueOf(outcell.getNumericCellValue());
        break;
      case HSSFCell.CELL_TYPE_STRING:
        cellmsg = outcell.getStringCellValue();
        break;
      default:
        cellmsg = outcell.getStringCellValue();
    }
    // end sheet
    return cellmsg;
  }

  /**
   * 打开要替换的excel文件
   *
   * @param excelName
   * @throws Exception
   */
  private void openExcel(String excelName) throws Exception {
    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelName));
    workbook = new HSSFWorkbook(fs);
    cellStyle2 = workbook.createCellStyle();
    format = workbook.createDataFormat();
    font = workbook.createFont();
  }

  /**
   * 保存需要替换的excel文件
   *
   * @param excelName
   */
  private void saveExcel(String excelName) throws Exception {
    FileOutputStream fileOut = new FileOutputStream(excelName);
    workbook.write(fileOut);
    fileOut.close();

  }

  /**
   * 将excel输出到制定的流中
   *
   * @param os
   * @throws Exception
   */
  private void saveExcel(OutputStream os) throws Exception {
    workbook.write(os);
  }

  /**
   * @param os
   * @param fileName
   */
  public void writeExcel(OutputStream os, String fileName) {
    try {
      POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
      HSSFWorkbook wb = new HSSFWorkbook(fs);
      wb.write(os);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        os.close();
      } catch (Exception ei) {
        ei.printStackTrace();
      }
    }
  }

  /**
   * 加入制定sheet的一个空行
   *
   * @param isheet
   * @param beginRow
   */
  private void insertBlankRow(int isheet, int beginRow) {
    // sheet循环
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      HSSFSheet sheet = workbook.getSheetAt(i);

      if (isheet == i) {
        // 1,指定行后插入移行
        int lastRowNum = sheet.getLastRowNum();
        HSSFRow lastRow_1 = sheet.getRow(lastRowNum);
        // 创建新的最后一行，将最后一行内容复制到新的行
        HSSFRow lastRow = sheet.createRow(beginRow + 1);
        for (int l = 0; l < 20; l++) {
          HSSFCell lcell = lastRow_1.getCell((short) l);
          HSSFCell ncell = lastRow.createCell((short) l);
          // setExcelCellValue(ncell,getExcelCellValue(lcell));
        }
      }

    }
  }

  private HSSFCell preAppendBlankRow(int isheet, int irow, int newRowNum) {

    try {
      HSSFSheet sheet = workbook.getSheetAt(isheet);

      // 检查表单中的行数是否需要扩充
      if (sheet.getLastRowNum() < irow + 1) {
        return appendBlankRow(isheet, newRowNum);
      }
    } catch (RuntimeException e) {
      logger.error("preAppendBlank Except:" + e.getMessage());
    }
    return null;
  }

  /**
   * 加入制定sheet的一个空行
   *
   * @param isheet
   * @param beginRow
   */
  private HSSFCell appendBlankRow(int isheet, int newRowNum) {
    // sheet循环
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      HSSFSheet sheet = workbook.getSheetAt(i);

      if (isheet == i) {
        // 1,建立一个新的最后一行
        int lastRowNum = sheet.getLastRowNum();
        HSSFRow lastRow_1 = sheet.getRow(lastRowNum);
        // HSSFCell lcell = lastRow_1.getCell((short)l);
        int lastRowColNum = lastRow_1.getLastCellNum() - lastRow_1.getFirstCellNum();

        // 创建新的最后一行，将最后一行内容复制到新的行
        for (int appRow = 1; appRow <= newRowNum; appRow++) {
          HSSFRow lastRow = sheet.createRow(lastRowNum + appRow);
          for (int l = 0; l < lastRowColNum; l++) {
            HSSFCell ncell = lastRow.createCell((short) l);
            return ncell;
          }
        }
      }

    }
    return null;
  }

  /**
   * 替换Detail域的信息 循环数据处理，第一层
   */
  private void exportDetail() {

    CodeField onefield = null; // detail中%的域
    ArrayList rows = new ArrayList();

    // 纵向输出取标签集合
    for (int i = 0; i < codes.size(); i++) {
      CodeField field = (CodeField) codes.get(i);
      // 循环体节点
      if (field.getCode().startsWith("%")) {
        onefield = field;
      }

      // 纵向明细输出
      if (field.getCode().startsWith("&")) {

        rows.add(field);
      }
    }
    // 替换掉%的域
    if (onefield != null) {
      setExcelCell(onefield.getSheet(), onefield.getRow(), onefield.getCol(), "");
    }

    exportDataList(dataList, rows);

  }

  /**
   * 替换Detail域的信息 循环数据处理，第2层
   */
  private void exportDataList(List dList, ArrayList rows) {

    // 取出欲替换的行数 details.length,替换模版位置
    // 定位起始坐标
    CodeField field = null;
    if (rows.size() > 0) {
      field = (CodeField) rows.get(0);
    }

    for (int i = 0; i < dList.size(); i++) {

      HashMap hash = (HashMap) dList.get(i);

      for (int l = 0; l < rows.size(); l++) {
        CodeField filed = (CodeField) rows.get(l);
        // 工作薄
        int sheet = filed.getSheet();
        // 当前行
        int setRows = filed.getRow() + i;
        // 当前列
        int setCols = filed.getCol();
        // 列头变量
        String code = filed.getCode();
        code = code.substring(1);
        // 获取值
        String value = "";
        if (hash.get(code) != null) {
          value = hash.get(code).toString();
        }

        setExcelCell(sheet, setRows, setCols, value);

      }
    }
  }

  /**
   *
   */
  public ExcelUtil() {
    super();
    if (logger.isDebugEnabled()) {
      logger.debug("TradeEntityToExcel 构造成功，模板路径：" + tmpDir + " 输出路径：" + xlsDir);
    }

  }

  public static String getCurDateStr() {
    Calendar cal = GregorianCalendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat();
    try {

      String pattern = "yyyyMMddhhmmss";
      sdf.applyPattern(pattern);
      String s = sdf.format(cal.getTime());
      return s + File.separator;

    } catch (Exception e) {

      e.printStackTrace();
      return "19000101";
    }

  }

  /**
   * 构造函数
   *
   * @param tmpDir ：报表模板的路径
   * @param xlsDir ：报表输出文件的路径
   */
  public ExcelUtil(String xlsDir, InputStream fs) {

    super();
    // 模板路径
    // this.tmpDir = tmpDir;
    this.xlsDir = xlsDir;
    this.fs = fs;
    if (logger.isDebugEnabled()) {
      logger.debug("TradeEntityToExcel 构造成功，模板路径：" + tmpDir + " 输出路径：" + xlsDir);
    }
  }

  /**
   * 程序入口 ：
   * <p>
   * 根据模板文件将交易数据 TrEntity 转换XSL文件
   *
   * @param trData
   * @param templateFile
   * @param xslFile
   * @return 结果文件
   */
  public String generateExcelByTemplate(Map dataMap, List dataList, String templateFile,
      String FExcelName) {
    if (!IsFolderExists(tmpDir)) {
      createFolder(tmpDir);
    }
    if (!IsFolderExists(xlsDir)) {
      createFolder(xlsDir);
    }
    // 源数据
    this.dataList = dataList;
    this.dataMap = dataMap;

    // 模板文件
    String srcfile = tmpDir + templateFile;

    String strDest = "";
    // 输出文件
    if ("".equals(FExcelName) || FExcelName == null) {
      FExcelName = getCurDateStr() + templateFile;
    } else {
      // FExcelName += ".xls";
    }
    strDest = xlsDir + FExcelName;
    // 另存模板excel
    doSaveAs(srcfile, strDest);

    // logger.info("doSaveAs---->>");
    try {
      // 获取输出变量的行
      getExcelCodeList(strDest);

      // 打开另存的excel
      openExcel(strDest);

      // 写入头信息
      exportBaseInfo();

      exportDetail();
      saveExcel(strDest);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("xls输出异常:" + e.getMessage());
    }
    return FExcelName;
  }

  /**
   * 文件的写入 将要分行的数组以数组的形式传入
   *
   * @param filePath(文件路径)
   * @param fileName(文件名)
   * @param args[]
   * @throws IOException
   */
  public void writeFile(String filePath, String[] args) throws IOException {
    FileWriter fw = new FileWriter(filePath);
    PrintWriter out = new PrintWriter(fw);
    for (int i = 0; i < args.length; i++) {
      out.write(args[i]);
      out.println();
    }
    fw.close();
    out.close();
  }

  public boolean IsFolderExists(String filePath) {
    File file = new File(filePath);
    return file.exists();
  }

  /**
   * 新建目录
   *
   * @param folderPath 目录
   * @return 返回目录创建后的路径
   */

  public String createFolder(String folderPath) {
    String txt = folderPath;
    try {
      java.io.File myFilePath = new java.io.File(txt);
      txt = folderPath;
      if (!myFilePath.exists()) {
        myFilePath.mkdir();
      }
    } catch (Exception e) {
    }
    return txt;
  }

  /**
   * 判断文件是否存在
   *
   * @return
   */

  public boolean IsFileExists(String filePath) {
    File f = new File(filePath);
    return f.exists();
  }

  /**
   * 创建XLS文件
   *
   * @param outputFile
   */
  public void CreateXL(String outputFile) {
    try {
      // 创建新的Excel 工作簿
      if (workbook == null) {
        workbook = new HSSFWorkbook();
      }
      HSSFSheet sheet = workbook.createSheet();
      // 在索引0的位置创建行（最顶端的行）
      HSSFRow row = sheet.createRow((short) 0);
      // 在索引0的位置创建单元格（左上端）
      HSSFCell cell = row.createCell((short) 0);
      // 定义单元格为字符串类型
      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
      // 在单元格中输入一些内容
      cell.setCellValue("");
      // 新建一输出文件流
      FileOutputStream fOut = new FileOutputStream(outputFile);
      // 把相应的Excel 工作簿存盘

      // 加入空行
      for (int i = 0; i < 2; i++) {
        insertBlankRow(0, i);// 加入一个新行
      }

      // appendBlankRow(0,20) ;
      workbook.write(fOut);
      fOut.flush();
      // 操作结束，关闭文件
      fOut.close();
      logger.info("XLS文件创建成功！FILE:" + outputFile);
    } catch (Exception e) {
      logger.error("创建xls文件失败:" + e.getMessage());
    }
  }

  public int getStyleType() {
    return styleType;
  }

  public void setStyleType(int styleType) {
    this.styleType = styleType;
  }

}


/**
 * 说明:得出excel模版的替换域
 *
 * @author Administrator
 *         <p>
 *         Preferences - Java - Code Style - Code Templates
 */
class CodeField {

  public CodeField(String code, int sheet, int row, int col) {
    this.sheet = sheet;
    this.code = code;
    this.row = row;
    this.col = col;
  }

  private int sheet = 0;
  private int row = 0;
  private int col = 0;
  private String code;

  /**
   * @return Returns the sheet.
   */
  public int getSheet() {
    return sheet;
  }

  /**
   * @param sheet The sheet to set.
   */
  public void setSheet(int sheet) {
    this.sheet = sheet;
  }

  /**
   * @return Returns the code.
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code The code to set.
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return Returns the col.
   */
  public int getCol() {
    return col;
  }

  /**
   * @param col The col to set.
   */
  public void setCol(int col) {
    this.col = col;
  }

  /**
   * @return Returns the row.
   */
  public int getRow() {
    return row;
  }

  /**
   * @param row The row to set.
   */
  public void setRow(int row) {
    this.row = row;
  }

}
