package pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import vo.InvoiceItem;
import vo.Registration;
public class InvoiceToPDF {

	private BaseFont bfBold;
	 private BaseFont bf;
	 private int pageNumber = 0;
     private int penPosY = 0;
     private int lineHeight = 15;
     private int itemWidth = 130;
     private int hsnWidth = 50;
     private int qtyWidth = 20;
     private int priceWidth = 55;
     private int taxableWidth = 55;
     private int taxWidth = 45;
     private int cessWidth = 35;
     private  DecimalFormat df = new DecimalFormat("0.00");
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
		   penPosY = 0;
		   List<InvoiceItem> allItems = new ArrayList<InvoiceItem>();
		   allItems.addAll(invoiceDetails.getMyCart());
		   allItems.addAll(invoiceDetails.getMyCartManual());
		   int index = 0;
		   for(InvoiceItem aItem: allItems ){
		    if(beginPage){
		     beginPage = false;
		     generateLayout(doc, cb); 
		     generateHeader(doc, cb,invoiceDetails, registration);
		     penPosY = 570; 
		    }
		    int linesConsumed = generateDetail(doc, cb, ++index, penPosY, aItem);
		    
		    penPosY -= ++linesConsumed *lineHeight;
		    if(penPosY < 150){
		     printPageNumber(cb, false);
		     doc.newPage();
		     beginPage = true;
		    }
		   }
		   printPageNumber(cb, true);
		   printTotal(cb, invoiceDetails);
		   

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

		

		   // Invoice Header box Text Headings 
		   createHeadings(cb,422,743,"Invoice No:");
		   createHeadings(cb,422,723,"Invoice Date:");
		  
		   //createHeadings(cb,422,703,"");

		   // Invoice Detail box layout 
		   cb.rectangle(20,50,550,550);
		   cb.moveTo(20,585);
		   cb.lineTo(570,585);
		   
		   int verticalLineHeight = 600;
		   
		   
		   
		   int marginLeft = 1;
		   // Invoice Detail box Text Headings 
		   int headingY = 590;
		   int headingX = 22;
		   createHeadings(cb,headingX,headingY,"Item");
		   headingX += itemWidth;
		   createHeadings(cb,headingX,headingY,"HSN");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += hsnWidth;
		   createHeadings(cb,headingX,headingY,"Qty");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += qtyWidth;
		   createHeadings(cb,headingX,headingY,"Price");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += priceWidth;
		   createHeadings(cb,headingX,headingY,"Taxable value");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += taxableWidth;
		   createHeadings(cb,headingX,headingY,"CGST");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += taxWidth;
		   createHeadings(cb,headingX,headingY,"SGST");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += taxWidth;
		   createHeadings(cb,headingX,headingY,"IGST");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += taxWidth;
		   createHeadings(cb,headingX,headingY,"CESS");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);
		   headingX += cessWidth;
		   createHeadings(cb,headingX,headingY,"Total");
		   cb.moveTo(headingX-marginLeft,50);
		   cb.lineTo(headingX-marginLeft,verticalLineHeight);

		   cb.stroke();
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
		 private void printTotal(PdfContentByte cb, InvoiceDetails invoiceDetails){
			 
			 double totalTaxable = 0;
			 double totalIgst = 0;
			 double totalCgst =0;
			 double totalSgst = 0;
			 double totalCess = 0;
			 double totalInvoice = 0;
			 for (InvoiceItem item: invoiceDetails.getMyCart()){
				 totalTaxable += item.getTaxableValue();
				 totalIgst += item.getIgstApplied();
				 totalSgst += item.getSgstApplied();
				 totalCgst += item.getCgstApplied();
				 totalCess += item.getCessApplied();
			 }
			 for (InvoiceItem item: invoiceDetails.getMyCartManual()){
				 totalTaxable += item.getTaxableValue();
				 totalIgst += item.getIgstApplied();
				 totalSgst += item.getSgstApplied();
				 totalCgst += item.getCgstApplied();
				 totalCess += item.getCessApplied();
			 }
			 totalInvoice = totalTaxable +totalIgst+totalSgst+totalCgst+totalCess;
			 
			 //Draw Total line 
			 cb.moveTo(20,70);
			 cb.lineTo(570,70);
			 CMYKColor black = new CMYKColor(0.f, 0.f, 0.f, 0.34f);
			 cb.setColorStroke(black);
			 cb.stroke();
			 
			 int totalPosY = 55;
			 int totalPosX = 25;
			 createHeadings(cb,totalPosX,totalPosY,"Total");
			 totalPosX = 280;
			 createHeadings(cb,totalPosX,totalPosY,df.format(totalTaxable));
			 totalPosX += taxableWidth;
			 createHeadings(cb,totalPosX,totalPosY,df.format(totalCgst));
			 totalPosX += taxWidth;
			 createHeadings(cb,totalPosX,totalPosY,df.format(totalSgst));
			 totalPosX += taxWidth;
			 createHeadings(cb,totalPosX,totalPosY,df.format(totalIgst));
			 totalPosX += taxWidth;
			 createHeadings(cb,totalPosX,totalPosY,df.format(totalCess));
			 totalPosX += cessWidth;
			 createHeadings(cb,totalPosX,totalPosY,df.format(totalInvoice));
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
		 
		 private int generateDetail(Document doc, PdfContentByte cb, int index, int y, InvoiceItem aItem)  {
		 
		  int noOfLines = 0;
		  try {
		  int penPosX = 22;
		  String[] itemDescParts = (String.valueOf(index) +". "+ aItem.getItem()).split(" ");
		  String lineContent = "";
		  
		  for (String part: itemDescParts){
			  if ((lineContent+" "+part).length() < 30 ){
				  lineContent +=" "+part;
			  }else {
				 
				  createContent(cb,penPosX,(y- noOfLines++*lineHeight),lineContent,PdfContentByte.ALIGN_LEFT);
				  lineContent = part;
			  }
			  
		  }
		  createContent(cb,penPosX,(y- noOfLines++*lineHeight),lineContent,PdfContentByte.ALIGN_LEFT);
		  int marginLeft = 2;
		  penPosX += itemWidth;
		  createContent(cb,penPosX+marginLeft,y, aItem.getHsn() ,PdfContentByte.ALIGN_LEFT);
		  penPosX += hsnWidth;
		  createContent(cb,penPosX+marginLeft,y, aItem.getQuantity() ,PdfContentByte.ALIGN_LEFT);
		  
		  penPosX += qtyWidth;
		  createContent(cb,penPosX+marginLeft,y, df.format(aItem.getRate()) ,PdfContentByte.ALIGN_LEFT);
		  
		  penPosX += priceWidth;
		  createContent(cb,penPosX+marginLeft,y, df.format(aItem.getTaxableValue()) ,PdfContentByte.ALIGN_LEFT);
		 
		  penPosX += taxableWidth;
		  createContent(cb,penPosX+marginLeft,y, df.format(aItem.getCgstApplied()) ,PdfContentByte.ALIGN_LEFT);
		  createContent(cb,penPosX+marginLeft,y-lineHeight, "@ "+aItem.getCgst() +"%" ,PdfContentByte.ALIGN_LEFT);
		  
		  penPosX += taxWidth;
		  createContent(cb,penPosX+marginLeft,y, df.format(aItem.getSgstApplied()) ,PdfContentByte.ALIGN_LEFT);
		  createContent(cb,penPosX+marginLeft,y-lineHeight, "@ "+aItem.getSgst() +"%" ,PdfContentByte.ALIGN_LEFT);
		  
		  penPosX += taxWidth;
		  createContent(cb,penPosX+marginLeft,y, df.format(aItem.getIgstApplied()) ,PdfContentByte.ALIGN_LEFT);
		  createContent(cb,penPosX+marginLeft,y-lineHeight, "@ "+aItem.getIgst() +"%" ,PdfContentByte.ALIGN_LEFT);
		  
		  penPosX += taxWidth;
		  createContent(cb,penPosX+marginLeft,y, df.format(aItem.getCessApplied()) ,PdfContentByte.ALIGN_LEFT);
		  createContent(cb,penPosX+marginLeft,y-lineHeight, "@ "+aItem.getCess() +"%" ,PdfContentByte.ALIGN_LEFT);
		  
		  penPosX += cessWidth;
		  createContent(cb,penPosX+marginLeft,y, df.format(aItem.getRowTotal()) ,PdfContentByte.ALIGN_LEFT);
		  
		   
		  }

		  catch (Exception ex){
		   ex.printStackTrace();
		  }
		  if (noOfLines == 1){
			  noOfLines++;
		  }
		  return noOfLines;
		 }

		 private void createHeadings(PdfContentByte cb, float x, float y, String text){


		  cb.beginText();
		  cb.setFontAndSize(bfBold, 8);
		  cb.setTextMatrix(x,y);
		  cb.showText(text.trim());
		  cb.endText(); 

		 }
		 
		 private void printPageNumber(PdfContentByte cb, boolean isLastPage){


		  cb.beginText();
		  cb.setFontAndSize(bfBold, 8);
		  String nextPageMsg = " Cont...";
		  if (isLastPage){
			  nextPageMsg = " End.";
		  }
		  cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1)+nextPageMsg, 570 , 25, 0);
		  
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