package com.jasperassistant.designer.viewer.documents;


import org.eclipse.swt.graphics.Point;

import net.sf.jasperreports.engine.type.OrientationEnum;

import com.jasperassistant.designer.viewer.IReportDocument;
import com.jasperassistant.designer.viewer.pdf.PDFReader;

public class PDFDocumentWrapper extends IReportDocument {

    private PDFReader reader;
    
    public PDFDocumentWrapper(PDFReader reader) {
        super();
        this.reader = reader;
    }

    @Override
    public int getPageWidth(int pageIndex) {
        return reader.getDimensions(pageIndex).x;
    }

    @Override
    public int getPageHeight(int pageIndex) {
        return reader.getDimensions(pageIndex).y;
    }

    @Override
    public int getPageCount() {
        return reader.getPageCount();
    }

    @Override
    public String getName() {
        return reader.getName();
    }

    @Override
    protected Object getUnderlying() {
        return reader;
    }

    @Override
    public OrientationEnum getOrientation() {
        Point dimensions = reader.getDimensions(0);
        // Lógica simples... se a largura for maior que a altura, é paisagem
        // caso contrário é retrato.
        if(dimensions.x > dimensions.y)
            return OrientationEnum.LANDSCAPE;
        return OrientationEnum.PORTRAIT;
    }

}
