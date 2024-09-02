package com.ithome._demo.common.utils;

import com.ithome._demo.common.consts.FileType;
import com.ithome._demo.model.report.common.SubReportModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExportExcelUtil {
    /**
     * Jasper Export Excel XLSX XLS Factory
     * @param type -- OutputType.XLSX or OutputType.XLS
     * @return JRXlsExporter or JRXlsxExporter
     */
    public static JRXlsAbstractExporter buildJRExporter(String type) {
        if (FileType.XLSX.equals(type)) {
            SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
            // setDetectCellType使excel偵測這個值的型別並轉換為對應的格式
            xlsxReportConfiguration.setDetectCellType(true);
            JRXlsxExporter jrXlsxExporter = new JRXlsxExporter();
            jrXlsxExporter.setConfiguration(xlsxReportConfiguration);
            return jrXlsxExporter;
        } else if (FileType.XLS.equals(type)) {
            SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
            xlsReportConfiguration.setDetectCellType(true);
            JRXlsExporter jrXlsExporter = new JRXlsExporter();
            jrXlsExporter.setConfiguration(xlsReportConfiguration);
            return jrXlsExporter;
        } else {
            return null;
        }
    }

    /**
     * Jasper report 模板匯出excel
     */
    public static <T> Resource templateToExcel(List dataSource, List<SubReportModel> subReportDataList,
                                                      String reportPath, Map<String, Object> parametersMap,
                                                      String type) throws Exception {
        try (ByteArrayOutputStream byteArrOut = new ByteArrayOutputStream()) {
            // check if template file exists use Files.exists
            Resource templateResource = new InputStreamResource(Objects.requireNonNull(ExportExcelUtil.class.getResourceAsStream(reportPath)));
            JasperReport jasperReport = JasperCompileManager.compileReport(templateResource.getInputStream());
            if (CollectionUtils.isNotEmpty(subReportDataList)) {
                for (SubReportModel subReportModel : subReportDataList) {
                    // jasper文件 子報表
                    try {
                        Resource subReportTemplate = new InputStreamResource(Objects.requireNonNull(ExportExcelUtil.class
                                .getResourceAsStream(subReportModel.getSubJrxmlFilePath())));
                        JasperReport subReport = JasperCompileManager.compileReport(subReportTemplate.getInputStream());
                        // 將子報表放入主報表參數
                        parametersMap.put(subReportModel.getSubDataExpression(), subReport);
                        // 將子報表參數來源設置為子報表的參數來源
                        parametersMap.put(subReportModel.getSubParametersMapExpression(), subReportModel.getSubReportParametersMap());
                        // 將子報表資料來源設置為子報表的資料來源
                        parametersMap.put(subReportModel.getSubDataSourceExpression(),
                                CollectionUtils.isEmpty(subReportModel.getSubDataSource())
                                        ? new JREmptyDataSource()
                                        : new JRBeanCollectionDataSource(subReportModel.getSubDataSource()));
                    } catch (Exception e) {
                        throw new Exception();
                    }
                }
            }

            /**
             * 房屋與土地分類統計表的主報表為null，parametersMap中的子報表資料需要建立在JREmptyDataSource才可以正常顯示，
             * 因此改為下列三元運算來判斷。
             */
            JRDataSource jrDataSource = dataSource == null ? new JREmptyDataSource() : new JRBeanCollectionDataSource(dataSource);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametersMap, jrDataSource);

            JRXlsAbstractExporter exporter = buildJRExporter(type);
            if (exporter == null) {
                throw new Exception();
            }
            // set file name
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrOut));
            exporter.exportReport();

            return new ByteArrayResource(byteArrOut.toByteArray());
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * resource to byte[]
     * @param resource -- Resource
     * @return byte[]
     */
    public static byte[] resourceToByte(Resource resource) throws Exception {
        try {
            byte[] bytes = new byte[resource.getInputStream().available()];
            resource.getInputStream().read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new Exception();
        }
    }

    public static ByteArrayResource resourceToByteArrayResource(Resource resource) throws Exception {
        try {
            return new ByteArrayResource(IOUtils.toByteArray(resource.getInputStream()));
        } catch (IOException e) {
            throw new Exception();
        }
    }
}
