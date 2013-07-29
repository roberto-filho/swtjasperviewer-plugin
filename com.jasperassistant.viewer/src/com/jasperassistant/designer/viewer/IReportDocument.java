package com.jasperassistant.designer.viewer;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.OrientationEnum;

import com.jasperassistant.designer.viewer.pdf.PDFReader;


public abstract class IReportDocument {
//    enum DocumentType {
//        JASPER,
//        PDF;
//    }
    
	public abstract int getPageWidth(int pageIndex);
	public abstract int getPageHeight(int pageIndex);
	public abstract int getPageCount();
	public abstract OrientationEnum getOrientation();
	public abstract String getName();
	protected abstract Object getUnderlying();
	
	public boolean isPDF() {
	    return getUnderlying() instanceof PDFReader;
    }
	
	public boolean isJasper() {
	    return getUnderlying() instanceof JasperPrint;
	}
	
	public JasperPrint getJasper() {
	    return isJasper() ? JasperPrint.class.cast(getUnderlying()) : null;
	}
	
	public PDFReader getPDF() {
	    return isPDF() ? PDFReader.class.cast(getUnderlying()) : null;
	}
	
}
