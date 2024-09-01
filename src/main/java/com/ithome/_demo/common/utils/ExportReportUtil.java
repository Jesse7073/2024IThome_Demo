package com.ithome._demo.common.utils;

import com.ithome._demo.common.consts.FileType;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.util.List;
import java.util.Map;

public class ExportReportUtil {

    public static byte[] templateToPdfByteSimple(List dataSourceList, String reportPath, Map<String, Object> parametersMap) throws Exception {

        // https://blog.csdn.net/tang199115/article/details/7554026
        // https://topic.alibabacloud.com/tc/a/using-jasperreport-and-ireport-to-make-java-reports-rpm_1_27_20223505.html
        try {

            // 以JasperCompileManager將jrxml模板編譯成jasper文件
            // https://blog.csdn.net/caolaosanahnu/article/details/7402959
            JasperReport jasperReport = JasperCompileManager.compileReport(ExportExcelUtil.class.getResourceAsStream(reportPath));

            // 將Java集合資料來源與Jasper報表進行綁定
            // https://cloud.tencent.cn/developer/information/%E4%BD%BF%E7%94%A8JRBeanCollectionDataSource%E5%90%88%E5%B9%B6Jasper%E6%8A%A5%E8%A1%A8%E4%B8%AD%E7%9A%84%E8%A1%A8%E6%A0%BC%E8%A1%8C%E5%8D%95%E5%85%83%E6%A0%BC%EF%BC%9F
            JRDataSource dataSource = new JRBeanCollectionDataSource(dataSourceList, true);

            // 將資料填入報表
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametersMap, dataSource);

            // 匯出為pdf
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public static byte[] templateToPdfByte(List dataSourceList, String reportPath, Map<String, Object> parametersMap) throws Exception {

        // https://blog.csdn.net/tang199115/article/details/7554026
        // https://topic.alibabacloud.com/tc/a/using-jasperreport-and-ireport-to-make-java-reports-rpm_1_27_20223505.html
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            // 以JasperCompileManager將jrxml模板編譯成jasper文件
            // https://blog.csdn.net/caolaosanahnu/article/details/7402959
            JasperReport jasperReport = JasperCompileManager.compileReport(ExportExcelUtil.class.getResourceAsStream(reportPath));

            // 將Java集合資料來源與Jasper報表進行綁定
            // https://cloud.tencent.cn/developer/information/%E4%BD%BF%E7%94%A8JRBeanCollectionDataSource%E5%90%88%E5%B9%B6Jasper%E6%8A%A5%E8%A1%A8%E4%B8%AD%E7%9A%84%E8%A1%A8%E6%A0%BC%E8%A1%8C%E5%8D%95%E5%85%83%E6%A0%BC%EF%BC%9F
            JRDataSource dataSource = new JRBeanCollectionDataSource(dataSourceList, true);

            // 將資料填入報表
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametersMap, dataSource);

            // 匯出為pdf
            // 匯出pdf沒有中文字的原因可能為字型
            // https://ithelp.ithome.com.tw/articles/10210719
            // https://blog.csdn.net/Mrqiang9001/article/details/113778799
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
            exporter.exportReport();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public static byte[] templateToByteByFileType(List dataSourceList, String reportPath, Map<String, Object> parametersMap, String fileType) throws Exception {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // 以JasperCompileManager將jrxml模板編譯成jasper文件
            JasperReport jasperReport = JasperCompileManager.compileReport(ExportExcelUtil.class.getResourceAsStream(reportPath));

            // 將Java集合資料來源與Jasper報表進行綁定
            JRDataSource dataSource = new JRBeanCollectionDataSource(dataSourceList, true);

            // 將資料填入報表
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametersMap, dataSource);

            // 匯出
            exportReportByFileType(fileType, print, byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * 匯出不同格式檔案
     * https://blog.csdn.net/linbren/article/details/68067288
     */
    private static void exportReportByFileType(String fileType, JasperPrint print, ByteArrayOutputStream byteArrayOutputStream) throws JRException {

        switch (fileType.toLowerCase()) {
            case FileType.XLSX:
                exportXlsx(print, byteArrayOutputStream);
                break;

            case FileType.XLS:
                exportXls(print, byteArrayOutputStream);
                break;

            case FileType.PDF:
                exportPdf(print, byteArrayOutputStream);
                break;

            case FileType.DOCX:
                exportDocx(print, byteArrayOutputStream);
                break;

            default:
                throw new IllegalArgumentException("不支援的檔案類型: " + fileType);
        }
    }

    /**
     * 匯出 pdf
     */
    private static void exportPdf(JasperPrint print, ByteArrayOutputStream byteArrayOutputStream) throws JRException {
        SimplePdfReportConfiguration pdfReportConfiguration = new SimplePdfReportConfiguration();
        // 頁面尺寸自動調整以適應內容的大小
        pdfReportConfiguration.setSizePageToContent(true);

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setConfiguration(pdfReportConfiguration);
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();
    }

    /**
     * 匯出 xlsx
     */
    private static void exportXlsx(JasperPrint print, ByteArrayOutputStream byteArrayOutputStream) throws JRException {
        SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
        // setDetectCellType使excel偵測這個值的型別並轉換為對應的格式
        xlsxReportConfiguration.setDetectCellType(true);

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setConfiguration(xlsxReportConfiguration);
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();
    }

    /**
     * 匯出 xls
     */
    private static void exportXls(JasperPrint print, ByteArrayOutputStream byteArrayOutputStream) throws JRException {
        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
        // setDetectCellType使excel偵測這個值的型別並轉換為對應的格式
        xlsReportConfiguration.setDetectCellType(true);

        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setConfiguration(xlsReportConfiguration);
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();
    }

    /**
     * 匯出 docx
     */
    private static void exportDocx(JasperPrint print, ByteArrayOutputStream byteArrayOutputStream) throws JRException {
        SimpleDocxReportConfiguration docxReportConfiguration = new SimpleDocxReportConfiguration();
        // 列高自動調整以適應內容
        docxReportConfiguration.setFlexibleRowHeight(true);

        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setConfiguration(docxReportConfiguration);
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();
    }
}
