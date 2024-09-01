package com.ithome._demo.common.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
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
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){

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
}
