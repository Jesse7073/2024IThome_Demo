package com.ithome._demo.facade.Impl;

import com.ithome._demo.facade.IWorkbookDemoFacade;
import com.ithome._demo.model.report.StudentCourseScoreReportModel;
import com.ithome._demo.model.report.common.CommonReportModel;
import com.ithome._demo.service.IReportDemoService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkbookDemoFacadeImpl implements IWorkbookDemoFacade {
    @Autowired
    private IReportDemoService reportDemoService;

    @Override
    // 匯出excel
    public void exportXSSFExcel(int columnNumber, int rowNumber) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             Workbook xssfWorkbook = new XSSFWorkbook();) {
            // 建立excel sheet(參數為sheetname)
            Sheet sheet = xssfWorkbook.createSheet("測試XSSFWorkbook");

            // 建立列物件(參數為列數，從0開始)
            Row titleRow = sheet.createRow(0);
            // 建立此列的單元格物件
            Cell titleCell = titleRow.createCell(0);
            // 設定cell的內容
            titleCell.setCellValue("測試XSSFWorkbook");

            // XSSFWorkbook
            // 最大列數：1,048,576 列（從第 0 列到第 1,048,575 列）
            // 最大欄數：16,384 (從 A 到 XFD）
            for (int i = 0; i < rowNumber; i++) {
                Row contentRow = sheet.createRow(i + 1);
                for (int j = 0; j < columnNumber; j++) {
                    Cell contentCell = contentRow.createCell(j);
                    // 寬度(width > 65280 會有IllegalArgumentException)
                    // Excel 的欄寬最大是 255 個字元，每個字元寬度是 256 單位，所以 255 個字元的欄寬等於 65280 單位。
                    // API文件 https://poi.apache.org/apidocs/dev/org/apache/poi/ss/usermodel/Sheet.html#setColumnWidth-int-int-
                    //            sheet.setColumnWidth(i, 500);
                    contentCell.setCellValue("abcdefghijk");
                }
            }

            // 匯出Excel
            xssfWorkbook.write(bos);
        } catch (Exception e) {
            // 寫入失敗
            throw new RuntimeException(e);
        }
    }

    @Override
    // 匯出excel
    public void exportSXSSFExcel(int columnNumber, int rowNumber) {
        // SXSSFWorkbook使用完後要close釋放資源，有實作AutoCloseable介面因此可以用try-with-resources
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             Workbook sxssfWorkbook = new SXSSFWorkbook(10000)) {

            // 建立excel sheet(參數為sheetname)
            Sheet sheet = sxssfWorkbook.createSheet("測試SXSSFWorkbook");

            // 建立列物件(參數為列數，從0開始)
            Row titleRow = sheet.createRow(0);
            // 建立此列的單元格物件
            Cell titleCell = titleRow.createCell(0);
            // 設定cell的內容
            titleCell.setCellValue("測試SXSSFWorkbook");

            // SXSSFWorkbook
            // 最大列數：1,048,576 列（從第 0 列到第 1,048,575 列）
            // 最大欄數：16,384 (從 A 到 XFD）
            // IllegalArgumentException
            for (int i = 0; i <= rowNumber; i++) {
                Row contentRow = sheet.createRow(i + 1);
                for (int j = 0; j < columnNumber; j++) {
                    Cell contentCell = contentRow.createCell(j);
                    // 寬度(width > 65280 會有IllegalArgumentException)
                    // Excel 的欄寬最大是 255 個字元，每個字元寬度是 256 單位，所以 255 個字元的欄寬等於 65280 單位。
                    // API文件 https://poi.apache.org/apidocs/dev/org/apache/poi/ss/usermodel/Sheet.html#setColumnWidth-int-int-
//                    sheet.setColumnWidth(i, 500);
                    contentCell.setCellValue("abcdefghijk");
                }
            }

            sxssfWorkbook.write(bos);
        } catch (Exception e) {
            // 寫入失敗
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommonReportModel demoExcel() {
        // 欄位名稱
        String[] columnNames = {"學號", "科系", "年級", "姓名", "課程", "成績", "考試日期"};

        // 查詢學生考試成績資料資料
        List<StudentCourseScoreReportModel> studentCourseScoreReportModelList = reportDemoService.getStudentCourseScoreData();

        CommonReportModel commonReportModel;
        // XSSFWorkbook使用完後要close釋放資源，有實作AutoCloseable介面因此可以用try-with-resources
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // 1.建立Workbook
            Workbook workbook = new XSSFWorkbook();) {
            // 2.建立excel sheet(參數為sheetname)
            Sheet sheet = workbook.createSheet("學生考試成績表");
            sheet.setDefaultColumnWidth(10);

            // 設定Cell的驗證
            this.setDataValidation(sheet);

            // 3.建立列物件(參數為列數，從0開始)
            Row titleRow = sheet.createRow(0);
            // 4.建立此列的單元格物件
            Cell titleCell = titleRow.createCell(0);
            // 設定cell的內容
            titleCell.setCellValue("XX大學 學生考試成績表");

            // 標題 合併儲存格 (起始行, 結束行, 起始列, 結束列)
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 6);
            sheet.addMergedRegion(cellRangeAddress);

            // 設定文字置中樣式
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);


            // 設定文字靠右樣式
            CellStyle rightStyle = workbook.createCellStyle();
            rightStyle.setAlignment(HorizontalAlignment.RIGHT);

            titleCell.setCellStyle(centerStyle);

            // 建立標題樣式
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.cloneStyleFrom(titleCell.getCellStyle());

            // 設定標題文字粗體、顏色為藍色
            Font titleFont = titleRow.getSheet().getWorkbook().createFont();
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.BLUE.getIndex());
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);


            // 5.第2列作為欄位名稱
            Row columnTitleRow = sheet.createRow(1);
            // 建立欄位名稱樣式
            CellStyle columnTitleStyle = workbook.createCellStyle();
            // 設定欄位名稱的文字粗體
            Font columnTitleFont = columnTitleRow.getSheet().getWorkbook().createFont();
            columnTitleFont.setBold(true);

            for (int k = 0; k < columnNames.length; k++) {
                Cell contentCell = columnTitleRow.createCell(k);
                contentCell.setCellValue(columnNames[k]);

                if (k == 0) {
                    // 取得欄位名稱的樣式
                    columnTitleStyle.cloneStyleFrom(contentCell.getCellStyle());
                    // 設定欄位名稱的背景顏色
                    columnTitleStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(197, 232, 181), null));
                    columnTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    columnTitleStyle.setFont(columnTitleFont);

                    // 加上邊框
                    columnTitleStyle.setBorderBottom(BorderStyle.THICK);
                    columnTitleStyle.setBorderLeft(BorderStyle.THICK);
                    columnTitleStyle.setBorderRight(BorderStyle.THICK);
                    columnTitleStyle.setBorderTop(BorderStyle.THICK);
                    // 設定邊框顏色
                    columnTitleStyle.setBottomBorderColor(IndexedColors.GREEN.getIndex());
                    columnTitleStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
                    columnTitleStyle.setRightBorderColor(IndexedColors.GREEN.getIndex());
                    columnTitleStyle.setTopBorderColor(IndexedColors.GREEN.getIndex());
                }
                contentCell.setCellStyle(columnTitleStyle);
            }

            // 6.遍歷列的資料
            for (int i = 0; i < studentCourseScoreReportModelList.size(); i++) {
                StudentCourseScoreReportModel model = studentCourseScoreReportModelList.get(i);

                // i+2因為第1列是標題，第2列是欄位名稱
                Row contentRow = sheet.createRow(i + 2);

                // 7.行的資料
                for (int j = 0; j < columnNames.length; j++) {
                    Cell contentCell = contentRow.createCell(j);
                    switch (j) {
                        case 0:
                            contentCell.setCellValue(model.getStudentNumber());
                            contentCell.setCellStyle(centerStyle);
                            break;
                        case 1:
                            contentCell.setCellValue(model.getDepartmentDesc());
                            break;
                        case 2:
                            contentCell.setCellValue(model.getGrade());
                            contentCell.setCellStyle(rightStyle);
                            break;
                        case 3:
                            contentCell.setCellValue(model.getFullName());
                            break;
                        case 4:
                            contentCell.setCellValue(model.getCourseDesc());
                            break;
                        case 5:
                            contentCell.setCellValue(model.getScore());
                            break;
                        case 6:
                            contentCell.setCellValue(model.getTestDate());
                            break;
                    }
                }
            }

            // 建立小結欄位名稱
            Row summaryRow = sheet.getRow(1);
            Cell courseNameCell = summaryRow.createCell(8);
            courseNameCell.setCellValue("課程名稱");
            courseNameCell.setCellStyle(columnTitleStyle);

            Cell averageScoreCell = summaryRow.createCell(9);
            averageScoreCell.setCellValue("平均成績");
            averageScoreCell.setCellStyle(columnTitleStyle);

            // 國文
            Row chineseRow = sheet.getRow(2);
            Cell chineseCell = chineseRow.createCell(8);
            chineseCell.setCellValue("國文");
            Cell chineseAvgCell = chineseRow.createCell(9);
            chineseAvgCell.setCellFormula("AVERAGE(F3:F127)");

            // 數學
            Row mathRow = sheet.getRow(3);
            Cell mathCell = mathRow.createCell(8);
            mathCell.setCellValue("數學");
            Cell mathAvgCell = mathRow.createCell(9);
            mathAvgCell.setCellFormula("AVERAGE(F128:F252)");

            // 英文
            Row englishRow = sheet.getRow(4);
            Cell englishCell = englishRow.createCell(8);
            englishCell.setCellValue("英文");
            Cell englishAvgCell = englishRow.createCell(9);
            englishAvgCell.setCellFormula("AVERAGE(F253:F377)");

            // 體育
            Row sportRow = sheet.getRow(5);
            Cell sportCell = sportRow.createCell(8);
            sportCell.setCellValue("體育");
            Cell sportAvgCell = sportRow.createCell(9);
            sportAvgCell.setCellFormula("AVERAGE(F378:F502)");

            // 計算公式
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            evaluator.evaluateFormulaCell(chineseAvgCell);
            evaluator.evaluateFormulaCell(mathAvgCell);
            evaluator.evaluateFormulaCell(englishAvgCell);
            evaluator.evaluateFormulaCell(sportAvgCell);

            // 做直條圖
            this.createBarChart(sheet);


            // 8.寫入ByteArrayOutputStream 匯出Excel
            workbook.write(bos);
            // 加密
            ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());

            String encodedFilename = URLEncoder.encode("學生考試成績表.xlsx", StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(resource.getByteArray(), encodedFilename);
        } catch (Exception e) {
            // 寫入失敗
            throw new RuntimeException(e);
        }

        return commonReportModel;
    }

    @Override
    // 讀取Excel
    public void readExcel(String filePath) {
        // 欄位名稱
        String[] columnNames = {"學號", "科系", "年級", "姓名", "課程", "成績", "考試日期"};

        List<StudentCourseScoreReportModel> modelList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(Paths.get(filePath).toFile());
            // 解密
//            Workbook workbook = this.decryptExcel(fileInputStream, "password");
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            // 取得sheet
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet != null) {
                // 有內容的最後一列index
                int lastRowNumber = sheet.getLastRowNum();
                // 第1列為標題，從第2列開始讀取
                for (int i = 2; i <= lastRowNumber; i++) {
                    // 取得row
                    Row row = sheet.getRow(i);

                    StudentCourseScoreReportModel model = new StudentCourseScoreReportModel();
                    for (int j = 0; j < columnNames.length; j++) {
                        if (row != null) {
                            // 取得cell
                            Cell cell = row.getCell(j);
                            if (cell == null) {
                                continue;
                            }
                            // 按照欄位名稱取值，放入model
                            switch (columnNames[j]) {
                                case "學號":
                                    if (cell.getCellType() == CellType.STRING) {
                                        String studentNumber = cell.getStringCellValue();
                                        if (studentNumber != null) {
                                            model.setStudentNumber(studentNumber);
                                        }
                                    }
                                    break;

                                case "科系":
                                    if (cell.getCellType() == CellType.STRING) {
                                        String departmentDesc = cell.getStringCellValue();
                                        if (departmentDesc != null) {
                                            model.setDepartmentDesc(departmentDesc);
                                        }
                                    }
                                    break;

                                case "年級":
                                    if (cell.getCellType() == CellType.STRING) {
                                        String grade = cell.getStringCellValue();
                                        if (grade != null) {
                                            model.setGrade(grade);
                                        }
                                    }
                                    break;

                                case "姓名":
                                    if (cell.getCellType() == CellType.STRING) {
                                        String fullName = cell.getStringCellValue();
                                        if (fullName != null) {
                                            model.setFullName(fullName);
                                        }
                                    }
                                    break;

                                case "課程":
                                    if (cell.getCellType() == CellType.STRING) {
                                        String courseDesc = cell.getStringCellValue();
                                        if (courseDesc != null) {
                                            model.setCourseDesc(courseDesc);
                                        }
                                    }
                                    break;

                                case "成績":
                                    if (cell.getCellType() == CellType.NUMERIC) {
                                        Double score = cell.getNumericCellValue();
                                        if (score != null) {
                                            model.setScore(score.intValue());
                                        }
                                    }
                                    break;

                                case "考試日期":
                                    if (cell.getCellType() == CellType.STRING) {
                                        String testDate = cell.getStringCellValue();
                                        if (testDate != null) {
                                            model.setTestDate(testDate);
                                        }
                                    }
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                    modelList.add(model);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(modelList);
    }

    private void setDataValidation(Sheet sheet) {
        // 1. 取得 DataValidationHelper 來幫助設置驗證
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();

        // 2. 設定年級驗證條件
        // 年級欄位選項清單
        DataValidationConstraint gradeConstraint = validationHelper.createExplicitListConstraint(new String[] {"1", "2", "3", "4"});
        // 分數範圍驗證條件
        DataValidationConstraint scoreConstraint = validationHelper.createIntegerConstraint(
                DataValidationConstraint.OperatorType.BETWEEN, "0", "100");
        // 考試日期驗證條件
        DataValidationConstraint dateConstraint = validationHelper.createDateConstraint(
                DataValidationConstraint.OperatorType.GREATER_OR_EQUAL, "Date(2022, 0, 1)", null, "yyyy/MM/dd");

        // 3. 設置驗證範圍
        CellRangeAddressList gradeAddressList = new CellRangeAddressList(2, 1048575, 2, 2);  // 表示第3行，第3到最後列
        CellRangeAddressList scoreAddressList = new CellRangeAddressList(2, 1048575, 5, 5);  // 表示第5行，第3到最後列
        CellRangeAddressList dateAddressList = new CellRangeAddressList(2, 1048575, 6, 6);  // 第7行的日期欄位

        // 4. 建立資料驗證
        DataValidation gradeValidation = validationHelper.createValidation(gradeConstraint, gradeAddressList);
        DataValidation integerValidation = validationHelper.createValidation(scoreConstraint, scoreAddressList);
        DataValidation dateValidation = validationHelper.createValidation(dateConstraint, dateAddressList);

        // 5. 設置顯示提示框
        gradeValidation.setShowErrorBox(true);
        gradeValidation.createErrorBox("資料錯誤", "請選擇下拉選單範圍內的值");

        integerValidation.setShowErrorBox(true);
        integerValidation.createErrorBox("資料錯誤", "請輸入0~100正整數");

        dateValidation.setShowErrorBox(true);
        dateValidation.createErrorBox("資料錯誤", "請輸入2020/01/01之後的有效日期");

        // 6. 將資料驗證套用到工作表
        sheet.addValidationData(gradeValidation);
        sheet.addValidationData(integerValidation);
        sheet.addValidationData(dateValidation);
    }

    private byte[] setEncrypt(ByteArrayOutputStream bos) throws IOException, InvalidFormatException, GeneralSecurityException {
        POIFSFileSystem fs = new POIFSFileSystem();
        EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
        Encryptor encryptor = info.getEncryptor();
        // 設定加密密碼
        encryptor.confirmPassword("password");
        // 打開文件
        OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(bos.toByteArray()));

        OutputStream os = encryptor.getDataStream(fs);
        // 儲存加密內容
        opc.save(os);
        opc.close();
        // 匯出前一定要先關閉加密文件OutputStream，不然會顯示匯出的文件損壞
        os.close();
//        try (OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(bos.toByteArray()));
//             OutputStream os = encryptor.getDataStream(fs);) {
//
//            opc.save(os);
//        }

        // 再將已加密的資料寫入ByteArrayOutputStream
        ByteArrayOutputStream encryptedStream = new ByteArrayOutputStream();
        fs.writeFilesystem(encryptedStream);
        encryptedStream.close();

//        try (ByteArrayOutputStream encryptedStream = new ByteArrayOutputStream()) {
//            fs.writeFilesystem(encryptedStream);
//            fs.close();
//            return encryptedStream.toByteArray();
//        }
        return encryptedStream.toByteArray();
    }

    private Workbook decryptExcel(FileInputStream fileInputStream, String password) throws IOException, GeneralSecurityException {
        POIFSFileSystem fs = new POIFSFileSystem(fileInputStream);
        EncryptionInfo encInfo = new EncryptionInfo(fs);
        Decryptor decryptor = Decryptor.getInstance(encInfo);
        decryptor.verifyPassword(password);
        Workbook workbook = new XSSFWorkbook(decryptor.getDataStream(fs));
        return workbook;
    }

    private void createBarChart(Sheet sheet){
        // 創建繪圖區域(createDrawingPatriarch() 方法返回的結果是一個通用的介面型態，這樣它可以在不同格式的工作表中通用。如果你知道自己正在處理的是 .xlsx 格式，則可以將返回的物件進行轉型為 XSSFDrawing)
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        // https://poi.apache.org/apidocs/4.1/org/apache/poi/ss/usermodel/Drawing.html#createAnchor-int-int-int-int-int-int-int-int-
        // dx1, dy1：上左角的偏移量，分別表示距離第一個儲存格左邊界和上邊界的偏移，單位是 EMU (English Metric Units, 1 EMU = 1/36000 of a point)。
        // dx2, dy2：下右角的偏移量，表示距離第二個儲存格右邊界和下邊界的偏移，單位同樣是 EMU。
        // col1, row1, col2, row2 則控制它跨越的儲存格範圍
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 11, 1, 17, 15);

        // 創建圖表
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("學生課程平均成績");
        // 設定圖例與位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        // 3. 設定圖表資料範圍
        // X 軸標籤
        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange((XSSFSheet) sheet,
                new CellRangeAddress(2, 5, 8, 8));
        // Y 軸數值
        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) sheet,
                new CellRangeAddress(2, 5, 9, 9));

        // 4. 設定軸
        // X軸標籤
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("課程");
        // 設定X軸的刻度對齊方式

        // Y軸標籤
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("平均成績");
        // 設定Bar在刻度之間
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        // 設定Y軸的最大值與最小值
        leftAxis.setMinimum(50);
        leftAxis.setMaximum(100);

        // 4. 創建圖表資料
        // 設定圖表類型為Bar chart，設定軸標籤資料
        XDDFBarChartData barChartData = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        barChartData.setBarDirection(BarDirection.COL);

        // 5. 設定圖表資料
        XDDFBarChartData.Series series = (XDDFBarChartData.Series) barChartData.addSeries(categories, values);
        // 設定圖例標題
        series.setTitle("課程平均成績", null);

        chart.plot(barChartData);
    }


}
