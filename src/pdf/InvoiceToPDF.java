package pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import java.text.DecimalFormat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import vo.InvoiceDetails;

import vo.Registration;
public class InvoiceToPDF {

	private BaseFont bfBold;
	 private BaseFont bf;
	 private int pageNumber = 0;

	public ByteArrayOutputStream getPdfBytes(Registration registration, InvoiceDetails invoiceDetails) throws DocumentException, IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 Document doc = new Document();
		  PdfWriter docWriter = null;
		  initializeFonts();

		  try {
		   
		   docWriter = PdfWriter.getInstance(doc , bos);
		   doc.addAuthor(registration.getbName());
		   doc.addCreationDate();
		   doc.addProducer();
		   doc.addCreator(registration.getbName());
		   doc.addTitle(""+invoiceDetails.getInvoiceNo());
		   doc.setPageSize(PageSize.LETTER);

		   doc.open();
		   PdfContentByte cb = docWriter.getDirectContent();
		   
		   boolean beginPage = true;
		   int y = 0;
		   
		   for(int i=0; i < 100; i++ ){
		    if(beginPage){
		     beginPage = false;
		     generateLayout(doc, cb); 
		     generateHeader(doc, cb,invoiceDetails, registration);
		     y = 570; 
		    }
		    generateDetail(doc, cb, i, y);
		    y = y - 15;
		    if(y < 50){
		     printPageNumber(cb);
		     doc.newPage();
		     beginPage = true;
		    }
		   }
		   printPageNumber(cb);

		  }
		  catch (DocumentException dex)
		  {
		   dex.printStackTrace();
		  }
		  catch (Exception ex)
		  {
		   ex.printStackTrace();
		  }
		  finally
		  {
		   if (doc != null)
		   {
		    doc.close();
		   }
		   if (docWriter != null)
		   {
		    docWriter.close();
		   }
		  }
		  return bos;
	}

		 private void generateLayout(Document doc, PdfContentByte cb)  {

		  try {

		   cb.setLineWidth(1f);

		   // Invoice Header box layout
		 /*  cb.rectangle(420,700,150,60);
		   cb.moveTo(420,720);
		   cb.lineTo(570,720);
		   cb.moveTo(420,740);
		   cb.lineTo(570,740);
		   cb.moveTo(480,700);
		   cb.lineTo(480,760);
		   cb.stroke();*/

		   // Invoice Header box Text Headings 
		   createHeadings(cb,422,743,"Invoice No:");
		   createHeadings(cb,422,723,"Invoice Date:");
		   //createHeadings(cb,422,703,"");

		   // Invoice Detail box layout 
		   cb.rectangle(20,50,550,550);
		   cb.moveTo(20,585);
		   cb.lineTo(570,585);
		   //cb.moveTo(50,50);
		   //cb.lineTo(50,600);
		   int colSepY = 600;
		   cb.moveTo(150,50);
		   cb.lineTo(150,colSepY);
		   cb.moveTo(430,50);
		   cb.lineTo(430,colSepY);
		   cb.moveTo(500,50);
		   cb.lineTo(500,colSepY);
		   cb.stroke();

		   // Invoice Detail box Text Headings 
		   int headingY = 590;
		   createHeadings(cb,22,headingY,"Item");
		   createHeadings(cb,152,headingY,"HSN");
		   createHeadings(cb,182,headingY,"Quantity");
		   createHeadings(cb,220,headingY,"Price");
		   createHeadings(cb,270,headingY,"Taxable value");
		   createHeadings(cb,350,headingY,"CGST");
		   createHeadings(cb,400,headingY,"SGST");
		   createHeadings(cb,450,headingY,"IGST");
		   createHeadings(cb,500,headingY,"CESS");
		   createHeadings(cb,550,headingY,"Total");

		   //add the images
		   ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		   String path = classLoader.getResource("images/durga.png").getPath();
		   Image companyLogo = Image.getInstance(path);
		   companyLogo.setAbsolutePosition(20,675);
		   companyLogo.scalePercent(25);
		   doc.add(companyLogo);

		  }

		  catch (DocumentException dex){
		   dex.printStackTrace();
		  }
		  catch (Exception ex){
		   ex.printStackTrace();
		  }

		 }
		 
		 private void generateHeader(Document doc, PdfContentByte cb, InvoiceDetails invoiceDetails, Registration registration)  {

		  try {
		   int registerationDetailsX = 130;
		   createHeadings(cb,registerationDetailsX,750,registration.getbName());
		   createHeadings(cb,registerationDetailsX,735,"GSTIN: "+registration.getGSTIN());
		   createHeadings(cb,registerationDetailsX,720,"State: "+registration.getState());
		   createHeadings(cb,registerationDetailsX,705,"PAN: "+registration.getPan());
		   
		   
		  
		   createHeadings(cb,482,743 ,""+invoiceDetails.getInvoiceNo());
		   createHeadings(cb,482,723,invoiceDetails.getInvoiceDateFormatted());
		   CMYKColor magentaColor = new CMYKColor(0.f, 1.f, 0.f, 0.f);
		   cb.setColorStroke(magentaColor);
		   cb.setLineWidth(1f);
		  
		   // Invoice Header box layout
		 
		   cb.moveTo(20,670);
		   cb.lineTo(570,670);
		   cb.stroke();
		   
		   int customerDetailsX = 20;
		   createHeadings(cb,customerDetailsX,660,"Customer Name: "+invoiceDetails.getCustomerName());
		   createHeadings(cb,customerDetailsX,650,"Address: "+invoiceDetails.getShippingAddress());
		   createHeadings(cb,customerDetailsX,640,"State: "+invoiceDetails.getShippingState());
		   createHeadings(cb,customerDetailsX,630,"Contact: "+invoiceDetails.getCustomerPhone() +" "+invoiceDetails.getCustomerEmail());
		   
		   cb.moveTo(20,620);
		   cb.lineTo(570,620);
		   cb.stroke();

		  }

		  catch (Exception ex){
		   ex.printStackTrace();
		  }

		 }
		 
		 private void generateDetail(Document doc, PdfContentByte cb, int index, int y)  {
		  DecimalFormat df = new DecimalFormat("0.00");
		  
		  try {

		   createContent(cb,48,y,String.valueOf(index+1),PdfContentByte.ALIGN_RIGHT);
		   createContent(cb,52,y, "ITEM" + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);
		   createContent(cb,152,y, "Product Description - SIZE " + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);
		   
		   double price = Double.valueOf(df.format(Math.random() * 10));
		   double extPrice = price * (index+1) ;
		   createContent(cb,498,y, df.format(price),PdfContentByte.ALIGN_RIGHT);
		   createContent(cb,568,y, df.format(extPrice),PdfContentByte.ALIGN_RIGHT);
		   
		  }

		  catch (Exception ex){
		   ex.printStackTrace();
		  }

		 }

		 private void createHeadings(PdfContentByte cb, float x, float y, String text){


		  cb.beginText();
		  cb.setFontAndSize(bfBold, 8);
		  cb.setTextMatrix(x,y);
		  cb.showText(text.trim());
		  cb.endText(); 

		 }
		 
		 private void printPageNumber(PdfContentByte cb){


		  cb.beginText();
		  cb.setFontAndSize(bfBold, 8);
		  cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 25, 0);
		  cb.endText(); 
		  
		  pageNumber++;
		  
		 }
		 
		 private void createContent(PdfContentByte cb, float x, float y, String text, int align){


		  cb.beginText();
		  cb.setFontAndSize(bf, 8);
		  cb.showTextAligned(align, text.trim(), x , y, 0);
		  cb.endText(); 

		 }

		 private void initializeFonts(){


		  try {
		   bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		   bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		  } catch (DocumentException e) {
		   e.printStackTrace();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }


		 }

		}