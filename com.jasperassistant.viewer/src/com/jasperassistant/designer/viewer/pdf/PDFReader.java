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

	public static void main(String[] args) {
		PDFReader pdf;
        pdf = new PDFReader(new File("/home/filho/Desktop/PGDAS/docs-pgdas/GUIA_DAS-05-2013-BRANCALHAO HIDRAULICA COMERCIO DE PECAS LTDA.pdf"));
        pdf.read();
//		PDFReader pdf = new PDFReader(new File("/home/filho/Documents/reducao-icms-sn-2013.pdf"), null);
	}
	
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
	
	public ImageData getPageImageData(int index) {
		Assert.isNotNull(document, "document não pode ser null");
		if(document == null)
			throw new RuntimeException("Documento não lido ainda. Chame o método \"read\" antes de iniciar a manipulação.");
		// Pega a imagem da página
		java.awt.Image pageImg = document.getPageImage(index, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
		// Converte para imagem SWT e adiciona na lista
		return SWTUtil.convertToSWT(BufferedImage.class.cast(pageImg));
	}
	
	public int getPageCount() {
		Assert.isNotNull(document, "document não pode ser null");
		return document.getNumberOfPages();
	}
	
	public void read() {
		try {
			document.setInputStream(new ByteArrayInputStream(payload), null);
		} catch (PDFException ex) {
			System.err.println("Error parsing PDF document." + ex);
		} catch (PDFSecurityException ex) {
			System.err.println("Error encryption not supported. " + ex);
		} catch (FileNotFoundException ex) {
			System.err.println("Error file not found. " + ex);
		} catch (IOException ex) {
			System.err.println("Error handling PDF document. " + ex);
		}
		
		/*
		Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(IMAGE_TYPE);
		if (!iterator.hasNext()) {
			System.err.println("ImageIO missing required plug-in to write PNG files.");
		}

		List<ImageData> images = new ArrayList<ImageData>();
		try {
			document.setInputStream(streamArquivoPdf, null);
			// Percorrer as páginas e 
			for (int i = 0; i < document.getNumberOfPages(); i++) {
				// Pega a imagem da página
				java.awt.Image pageImg = document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
				// Converte para imagem SWT e adiciona na lista
				images.add(SWTUtil.convertToSWT(BufferedImage.class.cast(pageImg)));
			}
		} catch (PDFException ex) {
			System.out.println("Error parsing PDF document." + ex);
		} catch (PDFSecurityException ex) {
			System.out.println("Error encryption not supported. " + ex);
		} catch (FileNotFoundException ex) {
			System.out.println("Error file not found. " + ex);
		} catch (IOException ex) {
			System.out.println("Error handling PDF document. " + ex);
		}
		
		try {
			// save page captures to file.
			ImageWriter writer = iterator.next();

			// Paint each pages content to an image and write the image to file
			for (int i = 0; i < document.getNumberOfPages(); i++) {
				final double targetDPI = dpi;
				float rotation = 0f;
				
				// Cria os streams
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//				FileOutputStream outStream = new FileOutputStream(String.format("/home/filho/Desktop/teste%s.png", i));
				ImageOutputStream ios = ImageIO.createImageOutputStream(outStream);
				writer.setOutput(ios);

				// Given no initial zooming, calculate our natural DPI when
				// printed to standard US Letter paper
				PDimension size = document.getPageDimension(i, rotation, scale);
				double dpi = Math.sqrt((size.getWidth() * size.getWidth()) + (size.getHeight() * size.getHeight())) / Math.sqrt((8.5 * 8.5) + (11 * 11));

				// Calculate scale required to achieve at least our target DPI
				if (dpi < (targetDPI - 0.1)) {
					scale = (float) (targetDPI / dpi);
					size = document.getPageDimension(i, rotation, scale);
				}

				int pageWidth = (int) size.getWidth();
				int pageHeight = (int) size.getHeight();

				BufferedImage image = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();//image.createGraphics();
				document.paintPage(i, g, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, rotation, scale);
				g.dispose();

				// capture the page image to file
				IIOImage img = new IIOImage(image, null, null);
				ImageWriteParam param = writer.getDefaultWriteParam();
				// Writes the data to the image stream wrapper object
				writer.write(null, img, param);
				
				image.flush();
				ios.flush();
				// Adiciona a imagem na lista
				images.add(new ImageData(new ByteArrayInputStream(outStream.toByteArray())));
				
				ios.close();
			}

			writer.dispose();
		} catch (Exception e) {
			System.out.println("Error saving file  " + e);
			e.printStackTrace();
		}
		*/
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
