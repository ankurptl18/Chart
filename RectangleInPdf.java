package com.ankur.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class RectangleInPdf {
    public static final String DEST = "D:/Passion/Office Work/pdfChart/Rectangle.pdf";

    public static final String DATE_PATTERN = "dd/MM/yyyy";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RectangleInPdf().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
    	 
    	SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
    	
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();
        
        float llx = 36; 
        float lly = 770;
        float urx = 200;
        float ury = 806;
        
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect1 = new Rectangle(llx, lly, urx-30, ury);
        rect1.setBorder(Rectangle.BOX);
        rect1.setBorderWidth(0.5f);
        canvas.rectangle(rect1);
        BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        canvas.setFontAndSize(bf, 8);
        canvas.beginText();
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "DIRECTION RECHERCHE ", llx + 10, 795, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "ET DEVELOPPEMENT", llx + 10, 785, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "DRD/DCTC/VCT/MESL/MEMU", llx + 10, 775, 0);
        canvas.endText();

        Rectangle rect2 = new Rectangle(llx + 140, lly, urx + 25, ury);
        rect2.setBorder(Rectangle.BOX);
        rect2.setBorderColor(BaseColor.BLACK);
        rect2.setBorderWidth(0.5f);
        canvas.rectangle(rect2);
        canvas.beginText();
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Courbes", llx + 140 + 25, 795, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "des", llx + 140 + 25, 785, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Polluants", llx + 140 + 25, 775, 0);
        canvas.endText();

        Rectangle rect3 = new Rectangle(llx + 140 + 55 , lly, urx + 120 , ury);
        rect3.setBorder(Rectangle.BOX);
        rect3.setBorderColor(BaseColor.BLACK);
        rect3.setBorderWidth(0.5f);
        canvas.rectangle(rect3);
        canvas.beginText();
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Periode de reference", llx + 140 + 100, 795, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Debut : " + formatter.format(new Date()), llx + 140 + 100, 785, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Fin      :  " + formatter.format(new Date()), llx + 140 + 100, 775, 0);
        canvas.endText();
        
        
        Rectangle rect4 = new Rectangle(llx + 140 + 55 + 95 , lly, urx + 120 + 125 , ury);
        rect4.setBorder(Rectangle.BOX);
        rect4.setBorderColor(BaseColor.BLACK);
        rect4.setBorderWidth(0.5f);
        canvas.rectangle(rect4);
        canvas.beginText();
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "PSA  PEUGEOT CITROEN", llx + 140 + 55 + 95 + 60, 795, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Site de Mulhouse", llx + 140 + 55 + 95 + 60, 785, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Date : " + formatter.format(new Date()) +  "  Heure:  23:33", llx + 140 + 55 + 95 + 60, 775, 0);
        canvas.endText();
        
        
        document.close();
        }
}