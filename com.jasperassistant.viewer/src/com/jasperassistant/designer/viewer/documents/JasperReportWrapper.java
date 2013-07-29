package com.jasperassistant.designer.viewer.documents;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.OrientationEnum;

import com.jasperassistant.designer.viewer.IReportDocument;

public class JasperReportWrapper extends IReportDocument {

    private JasperPrint document;
    
    public JasperReportWrapper(JasperPrint document) {
        super();
        this.document = document;
    }

    @Override
    public int getPageWidth(int pageIndex) {
        return document.getPageWidth();
    }

    @Override
    public int getPageHeight(int pageIndex) {
        return document.getPageHeight();
    }

    @Override
    public int getPageCount() {
        return document.getPages().size();
    }

    @Override
    public String getName() {
        return document.getName();
    }
    
    @Override
    protected Object getUnderlying() {
        return document;
    }
    
    @Override
    public OrientationEnum getOrientation() {
        return document.getOrientationValue();
    }
}
