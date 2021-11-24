package com.tcdt.qlnvsystem.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

public class ReportPrint {

	private static final Map<String, String> reportDirs = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("DxuatXdungKho", "/reports/DxuatXdungKho.jrxml");
		}
	};

	public static <T> JasperPrint jasperReport(Map<String, Object> parameters, Class<T> type, Collection<T> report)
			throws Exception {
		
		String dir = reportDirs.get(type.getSimpleName());
		if (StringUtils.isEmpty(dir))
			throw new UnsupportedOperationException("Report type unknown");
		InputStream inputStream = new ClassPathResource(dir).getInputStream();
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(report);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBean);

		return jasperPrint;
	}
	
	public static <T> JasperPrint jasperReportCus(Map<String, Object> parameters, String type, Collection<T> report)
			throws Exception {
		String dir = reportDirs.get(type);
		if (StringUtils.isEmpty(dir))
			throw new UnsupportedOperationException("Report type unknown");
		InputStream inputStream = new ClassPathResource(dir).getInputStream();
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(report);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBean);

		return jasperPrint;
	}

	public static String toHtmlString(JasperPrint jasperPrint) throws Exception {
		// Export the report to a HTML file
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		Exporter<ExporterInput, HtmlReportConfiguration, HtmlExporterConfiguration, HtmlExporterOutput> exporter;
		// HTML exporter
		exporter = new HtmlExporter();
		// Set output to byte array
		exporter.setExporterOutput(new SimpleHtmlExporterOutput(byteArray));
		// Set input source
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		// Export to HTML
		exporter.exportReport();
		byte[] bytes = byteArray.toByteArray();
		return new String(bytes, "UTF-8");
	}
	
	public static <T> byte[] jasperReportPdf(Map<String, Object> parameters, String type, Collection<T> report)
			throws Exception {
		String dir = reportDirs.get(type);
		if (StringUtils.isEmpty(dir))
			throw new UnsupportedOperationException("Report type unknown");
		InputStream inputStream = new ClassPathResource(dir).getInputStream();
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(report);
		return JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(jasperReport, parameters, jrBean));
	}
}
