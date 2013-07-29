package com.jasperassistant.designer.viewer.pdf;

import static java.lang.String.format;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.jasperassistant.designer.viewer.pdf.image.SWTUtil;
import com.jasperassistant.designer.viewer.util.StreamUtil;


public class PDFReader {

//	private static final double PRINTER_RESOLUTION = 200; //150, 200, 300, 600, 1200
//	private static final String IMAGE_TYPE = "png";

	@SuppressWarnings("unused")
	private double dpi     = 200;
	private float scale    = 1.0f;
	private float rotation = 0f;
	
	private byte[] payload;
	private Document document = new Document();
	
//	Logger log = Logger.getAnonymousLogger("br.com.gtech.pdf");
	
	public PDFReader(File file) {
		this(loadFromFile(file));
	}
	
    public PDFReader(InputStream pdfInputStream) {
        try {
            this.payload = StreamUtil.toByteArray(pdfInputStream, 1024);
            // Ler o arquivo
            read();
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao ler o input stream de pdf.", e);
        }
    }
	
    private static FileInputStream loadFromFile(File file) {
		// Carregar o file em um inputStream
		if(file.isFile()) {
			// Carrega
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// Nunca vai acontecer, o file.isFile vai dar pau ou ñ vai passar
				return null;
			}
		} else
			throw new IllegalArgumentException(format("O caminho [%s] não representa um arquivo.", file.getAbsoluteFile()));
	}
	
	public ImageData getPageImageData(int pageIndex) {
		java.awt.Image pageImg = getPageImage(pageIndex);
		// Converte para imagem SWT e adiciona na lista
		return SWTUtil.convertToSWT(BufferedImage.class.cast(pageImg));
	}

    public java.awt.image.BufferedImage getPageImage(int pageIndex) {
        Assert.isNotNull(document, "document não pode ser null");
		if(document == null || document.getNumberOfPages() <= 0)
			throw new RuntimeException("Documento não lido ainda. Chame o método \"read\" antes de iniciar a manipulação.");
		// Pega a imagem da página, retorna um buffered image
		java.awt.Image pageImg = document.getPageImage(pageIndex, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
        return (BufferedImage) pageImg;
    }
	
	public int getPageCount() {
		Assert.isNotNull(document, "document não pode ser null");
		return document.getNumberOfPages();
	}
	
	/**
	 * Seta o inputStream do documento para que ele possa ser lido.
	 */
	private void read() {
		try {
			document.setInputStream(new ByteArrayInputStream(payload), null);
		} catch (PDFException ex) {
			System.err.println("Error parsing PDF document." + ex);
		} catch (PDFSecurityException ex) {
			System.err.println("Error encryption not supported. " + ex);
		} catch (IOException ex) {
			System.err.println("Error handling PDF document. " + ex);
		}
	}

	public PDFReader setDpi(double dpi) {
		this.dpi = dpi;
		return this;
	}
	
	public PDFReader setZoom(float scale) {
		this.scale = scale;
		return this;
	}
	
	public PDFReader setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}
	
	public ByteArrayInputStream getInputStream() {
	    return new ByteArrayInputStream(payload);
	}
	
	public Point getDimensions(int pageIndex) {
	    PDimension dimensions = document.getPageDimension(pageIndex, rotation, scale);
        return new Point(new Float(dimensions.getWidth()).intValue(), new Float(dimensions.getHeight()).intValue());
	}
	
	public String getName() {
	    Assert.isNotNull(document);
	    
	    return document.getInfo() == null ? "" : document.getInfo().getTitle();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// clean up resources
		if(document != null)
			document.dispose();
	}
}
