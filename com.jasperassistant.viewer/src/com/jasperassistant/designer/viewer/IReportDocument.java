package com.jasperassistant.designer.viewer;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.OrientationEnum;

import com.jasperassistant.designer.viewer.pdf.PDFReader;


public abstract class IReportDocument {
    
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
	
	/**
	 * Retorna o {@link JasperPrint} que essa classe contém.
	 * @return o {@link JasperPrint} ou <code>null</code>, caso
	 * ela represente um arquivo PDF.
	 */
	public JasperPrint getJasper() {
	    return isJasper() ? JasperPrint.class.cast(getUnderlying()) : null;
	}
	
	/**
	 * Retorna o {@link PDFReader} que essa classe possui.
	 * @return o {@link PDFReader} ou <code>null</code>, caso ela
	 * represente um {@link JasperPrint}.
	 */
	public PDFReader getPDF() {
	    return isPDF() ? PDFReader.class.cast(getUnderlying()) : null;
	}
	
}
