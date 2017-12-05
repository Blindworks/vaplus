package de.vaplus.pdf;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.PdfControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileSystemFile;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.InvoiceItem;

@Stateless
public class PdfController implements PdfControllerInterface{

	@EJB
	private StockControllerInterface stockController;

	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private FileControllerInterface fileController;
	
	private static final float RE_COL_0 = 0;
	private static final float RE_COL_1 = 30;
	private static final float RE_COL_2 = 40;
	private static final float RE_COL_3 = 280;
	private static final float RE_COL_4 = 60;
	private static final float RE_COL_5 = 105;

	private static final float DOC_FONT_SIZE_S = 6;
	private static final float DOC_FONT_SIZE_M = 10;
	private static final float DOC_FONT_SIZE_L = 12;
	private static final float DOC_FONT_SIZE_XL = 14;

	private static final float FOOTER_POS_Y = 40;

	private final URL regularFontURL;
	private final URL semiboldFontURL;
	
	NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMAN);
	
	private void drawBackgroundImage(PDPageContentStream contentStream, PDImageXObject bgImage, float pageHeight, float pageWidth) throws IOException{
		
		if(bgImage == null)
			return;
		
		float scale = pageWidth / bgImage.getWidth();
		
		float imageHeight = bgImage.getHeight() * scale;
		
		contentStream.drawImage(bgImage, 0, pageHeight - imageHeight, pageWidth, imageHeight);
		
	}
	
	private void drawAddressfield(PDPageContentStream contentStream, float x, float y, PDFont font, String drafterLine,  String[] addresslines) throws IOException{
		
		if(drafterLine == null)
			drafterLine = "";
		
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_S );
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(drafterLine);
        contentStream.endText();

        contentStream.setLineWidth(new Float(0.5));
        contentStream.moveTo(x, y -3);
        contentStream.lineTo(x + 250, y -3);
        contentStream.stroke();
		
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_M );
        contentStream.setLeading(14);
        contentStream.newLineAtOffset(x, y - 20);
        
        for (String addressline: addresslines) {    
        	if(addressline == null)
        		continue;
            contentStream.showText(addressline);
            contentStream.newLine();
        }
        
        contentStream.endText();
	}
	
	private void drawDocumentTitle(PDPageContentStream contentStream, float x, float y, PDFont font, String title, String number) throws IOException{
		
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_L );
        contentStream.setLeading(20);
        contentStream.newLineAtOffset(x, y);
        
        contentStream.showText(title);
        contentStream.newLine();
        
        contentStream.showText(number);
        
        contentStream.endText();
	}
	
	private void drawDocumentPageNumber(PDPageContentStream contentStream, float x, float y, PDFont font, int pageNumber, int numberOfPages) throws IOException{
		
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_L );
        contentStream.setLeading(20);
        contentStream.newLineAtOffset(x, y);
        
        contentStream.showText("Seite");
        contentStream.newLine();
        
        contentStream.showText(String.valueOf(pageNumber)+ " / "+String.valueOf(numberOfPages));
        
        contentStream.endText();
	}

	private void drawDocumentFooter(PDPageContentStream contentStream, float x, float y, float pageWidth, PDFont font, String[] footerLines) throws IOException{
	
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_S );

        contentStream.newLineAtOffset(pageWidth / 2, y);
        
        float halfTextWidth;
        
        for(String s : footerLines){
        	
        	if(s == null || s.length() == 0)
        		continue;
        	
        	halfTextWidth = textWidth(s, font, DOC_FONT_SIZE_S) / 2;

            contentStream.newLineAtOffset( -1 * halfTextWidth, 0);
            contentStream.showText(s);
            contentStream.newLineAtOffset( halfTextWidth , -1 * (DOC_FONT_SIZE_S + 2));
            
        }
        

        contentStream.endText();
	}

	private void drawInvoiceItemHead(PDPageContentStream contentStream, float x, float y, float contentWidth, PDFont font) throws IOException{
		
		String[] heads = new String[5];

		heads[0] = "ArtNr.";
		heads[1] = "Bezeichnung";
		heads[2] = "Menge";
		heads[3] = "Preis";
		heads[4] = "Gesamtpreis";

        contentStream.setLineWidth(new Float(0.5));
        contentStream.moveTo(x - 5, y -4);
        contentStream.lineTo(x + contentWidth + 5, y -4);
        contentStream.stroke();
	
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_M );

        contentStream.newLineAtOffset(x, y);
        
        contentStream.newLineAtOffset(RE_COL_0, 0);
        contentStream.showText("Pos.");
        
        contentStream.newLineAtOffset(RE_COL_1, 0);
        contentStream.showText("ArtNr.");
        
        contentStream.newLineAtOffset(RE_COL_2, 0);
        contentStream.showText("Bezeichnung");

        contentStream.newLineAtOffset( textOffsetRight(RE_COL_3, heads[2], font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(heads[2]);
        contentStream.newLineAtOffset( textWidth(heads[2], font, DOC_FONT_SIZE_M), 0);

        contentStream.newLineAtOffset( textOffsetRight(RE_COL_4, heads[3], font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(heads[3]);
        contentStream.newLineAtOffset( textWidth(heads[3], font, DOC_FONT_SIZE_M), 0);
        
        contentStream.newLineAtOffset( textOffsetRight(RE_COL_5, heads[4], font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(heads[4]);
        contentStream.newLineAtOffset( textWidth(heads[4], font, DOC_FONT_SIZE_M), 0);

        contentStream.endText();
	}
	
	private void drawInvoiceSubTotal(PDPageContentStream contentStream, float x, float y, float contentWidth, PDFont font, BigDecimal sum) throws IOException{
		
		y += DOC_FONT_SIZE_M - 2;
		
        contentStream.setLineWidth(new Float(0.5));
        contentStream.moveTo(x - 5, y);
        contentStream.lineTo(x + contentWidth + 5, y);
        contentStream.stroke();
	
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_M );

        contentStream.newLineAtOffset(x, y - DOC_FONT_SIZE_M - 2);
        
        String s = "Zwischensumme";
        
        contentStream.newLineAtOffset(textOffsetRight( RE_COL_0 + RE_COL_1 + RE_COL_2 + RE_COL_3 + RE_COL_4, s, font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);
        
        s = formatter.format(sum) + " €";
        
        contentStream.newLineAtOffset( textOffsetRight(RE_COL_5, s, font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);

        contentStream.endText();
	}
	
	private float drawInvoiceTotal(PDPageContentStream contentStream, float x, float y, float contentWidth, PDFont font,  PDFont fontBold, BigDecimal nettoTotal, Map<BigDecimal,BigDecimal> vatList, BigDecimal grossTotal) throws IOException{
		
		float currentY = y + DOC_FONT_SIZE_M - 2;

		contentStream.setLineWidth(new Float(0.5));
        contentStream.moveTo(x - 5, currentY);
        contentStream.lineTo(x + contentWidth + 5, currentY);
        contentStream.stroke();
        
        currentY -= DOC_FONT_SIZE_M + 2;
	
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_M );

        contentStream.newLineAtOffset(x, currentY);
        
        String s = "Summe";
        
        contentStream.newLineAtOffset(textOffsetRight( RE_COL_0 + RE_COL_1 + RE_COL_2 + RE_COL_3 + RE_COL_4, s, font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);
        
        s = formatter.format(nettoTotal.setScale(2, RoundingMode.HALF_UP)) + " €";
        
        contentStream.newLineAtOffset( textOffsetRight(RE_COL_5, s, font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);

        contentStream.endText();

        
        Iterator<BigDecimal> taxIterator = vatList.keySet().iterator();
        BigDecimal tax;
        while(taxIterator.hasNext()){
        	tax = taxIterator.next();

            currentY -= DOC_FONT_SIZE_M + 2;
    	
	        contentStream.beginText();
	        contentStream.setFont( font, DOC_FONT_SIZE_M );
	
	        contentStream.newLineAtOffset(x, currentY);
	        
	        s = tax.setScale(0, RoundingMode.HALF_UP)+"% MwSt.";
	        
	        contentStream.newLineAtOffset(textOffsetRight( RE_COL_0 + RE_COL_1 + RE_COL_2 + RE_COL_3 + RE_COL_4, s, font, DOC_FONT_SIZE_M), 0);
	        contentStream.showText(s);
	        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);
	        
	        s = formatter.format(vatList.get(tax).setScale(2, RoundingMode.HALF_UP)) + " €";
	        
	        contentStream.newLineAtOffset( textOffsetRight(RE_COL_5, s, font, DOC_FONT_SIZE_M), 0);
	        contentStream.showText(s);
	        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);
	
	        contentStream.endText();
        
        }

        currentY -= 4;


		contentStream.setLineWidth(new Float(0.5));
        contentStream.moveTo(x + RE_COL_0 + RE_COL_1 + RE_COL_2 + RE_COL_3 , currentY);
        contentStream.lineTo(x + contentWidth + 5, currentY);
        contentStream.stroke();

        currentY -= 1;

		contentStream.setLineWidth(new Float(0.5));
        contentStream.moveTo(x + RE_COL_0 + RE_COL_1 + RE_COL_2 + RE_COL_3 , currentY);
        contentStream.lineTo(x + contentWidth + 5, currentY);
        contentStream.stroke();

        currentY -= DOC_FONT_SIZE_M + 2;
    	
        contentStream.beginText();
        contentStream.setFont( font, DOC_FONT_SIZE_M );

        contentStream.newLineAtOffset(x, currentY);
        
        s = "Betrag";
        
        contentStream.newLineAtOffset(textOffsetRight( RE_COL_0 + RE_COL_1 + RE_COL_2 + RE_COL_3 + RE_COL_4, s, fontBold, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);
        
        s = formatter.format(grossTotal.setScale(2, RoundingMode.HALF_UP)) + " €";
        
        contentStream.newLineAtOffset( textOffsetRight(RE_COL_5, s, fontBold, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);

        contentStream.endText();

        currentY -= DOC_FONT_SIZE_M + 2;
        currentY -= DOC_FONT_SIZE_M + 2;
        
        return y - currentY;
	}
	
	private float drawInvoiceItem(PDPageContentStream contentStream, float x, float y, float contentWidth, PDFont font, InvoiceItem item, int pos) throws IOException{
		
		float titleFieldWidth = RE_COL_3 - RE_COL_2;
		
		
		// Check Title for Linebreaks
		List<String> titleList = new LinkedList<String>();
		
		if(this.textWidth(item.getTitle(), font, DOC_FONT_SIZE_M) < titleFieldWidth){
			titleList.add(item.getTitle());
		}else{
			String[] titleSplits = item.getTitle().split(" ");

			String title = "";
			String title_tmp = "";
			
			String titleSplit;
			for(int i=0; i< titleSplits.length; i++){
				
				titleSplit = titleSplits[i];
				
				title = title_tmp;
				
				if(title_tmp.length() > 0)
					title_tmp += " ";
				
				title_tmp += titleSplit;
				
				if(this.textWidth(title_tmp, font, DOC_FONT_SIZE_M) < titleFieldWidth){

					// check if this was the last element
					if(i == titleSplits.length - 1){
						titleList.add(title_tmp);
					}
					
					continue;
				}

				// string is with new word too long
				
				// check if single word is too long!!!
				if(title.length() == 0){
					//title_tmp is too long!

					// use this in a single line
					titleList.add(title_tmp);
					title_tmp = "";
				}else{
					
					// take old string
					titleList.add(title);
					
					// set new word on next line
					title_tmp = titleSplit;
				
				}
				
				
				// check if this was the last element
				if(i == titleSplits.length - 1){
					titleList.add(title_tmp);
				}
			}
			
		}
		
		Iterator<String> titleListIterator = titleList.iterator();
		
		

		// check subtitle for linbreaks
		List<String> subtitleList = new LinkedList<String>();
		Iterator<String> subtitleListIterator = null;
		
		if(item.getSubTitle() != null){
			String[] subtitlelines = item.getSubTitle().split("\\r|\\n");
			
			
			
			
	    	for(String line : subtitlelines){
	    		
			
				if(this.textWidth(line, font, DOC_FONT_SIZE_S) < titleFieldWidth){
					subtitleList.add(line);
				}else{
					String[] subtitleSplits = line.split(" ");
		
					String subtitle = "";
					String subtitle_tmp = "";
					
					String subtitleSplit;
					for(int i=0; i< subtitleSplits.length; i++){
						
						subtitleSplit = subtitleSplits[i];
						
						subtitle = subtitle_tmp;
						
						if(subtitle_tmp.length() > 0)
							subtitle_tmp += " ";
						
						subtitle_tmp += subtitleSplit;
						
						if(this.textWidth(subtitle_tmp, font, DOC_FONT_SIZE_S) < titleFieldWidth){
		
							// check if this was the last element
							if(i == subtitleSplits.length - 1){
								subtitleList.add(subtitle_tmp);
							}
							
							continue;
						}
		
						// string is with new word too long
						
						// check if single word is too long!!!
						if(subtitle.length() == 0){
							//title_tmp is too long!
		
							// use this in a single line
							subtitleList.add(subtitle_tmp);
							subtitle_tmp = "";
						}else{
							
							// take old string
							subtitleList.add(subtitle);
							
							// set new word on next line
							subtitle_tmp = subtitleSplit;
						
						}
						
						
						// check if this was the last element
						if(i == subtitleSplits.length - 1){
							subtitleList.add(subtitle_tmp);
						}
					}
					
				}
	    	}
			
			
		}
		
		if(item.getSerial() != null && item.getSerial().length() > 0){
			subtitleList.add("Serial: "+item.getSerial());
		}
		
		subtitleListIterator = subtitleList.iterator();
		
		
		
		
		
		float elementHeight = 0;
		
		float lineHeight = DOC_FONT_SIZE_M + 2;
		
		String s;
        contentStream.setFont( font, DOC_FONT_SIZE_M );
	
        contentStream.beginText();

        contentStream.newLineAtOffset(x, y);
        
        contentStream.newLineAtOffset(RE_COL_0, 0);
        contentStream.showText(""+pos);
        
        contentStream.newLineAtOffset(RE_COL_1, 0);
        if(item.getProduct() != null)
        	contentStream.showText(""+item.getProduct().getId());
        
        contentStream.newLineAtOffset(RE_COL_2, 0);
        contentStream.showText(titleListIterator.next());

        s = formatter.format(item.getQuantity());
        contentStream.newLineAtOffset( textOffsetRight(RE_COL_3, s, font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);

        s = formatter.format(item.getUnitPrice().setScale(2, RoundingMode.HALF_UP)) + " €";
        contentStream.newLineAtOffset( textOffsetRight(RE_COL_4, s, font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);

        s = formatter.format(item.getCommission().getPrice().setScale(2, RoundingMode.HALF_UP)) + " €";
        contentStream.newLineAtOffset( textOffsetRight(RE_COL_5, s, font, DOC_FONT_SIZE_M), 0);
        contentStream.showText(s);
        contentStream.newLineAtOffset( textWidth(s, font, DOC_FONT_SIZE_M), 0);

        contentStream.endText();
        
        elementHeight += lineHeight;
        
        //print other title lines
        while(titleListIterator.hasNext()){
//        	System.out.println("next text: "+titleListIterator.next());

            contentStream.beginText();

            contentStream.newLineAtOffset(x, y - elementHeight);
            
            contentStream.newLineAtOffset(RE_COL_0 + RE_COL_1 + RE_COL_2, 0);
            contentStream.showText(titleListIterator.next());

            contentStream.endText();

            elementHeight += lineHeight;
        }

        contentStream.setFont( font, DOC_FONT_SIZE_S );
        
        if(subtitleListIterator != null  && subtitleList.size() > 0){
        	
        	while(subtitleListIterator.hasNext()){
        	
        	
	            contentStream.beginText();
	
	            contentStream.newLineAtOffset(x, y - elementHeight);
	            
	            contentStream.newLineAtOffset(RE_COL_0 + RE_COL_1 + RE_COL_2, 0);
	            contentStream.showText(subtitleListIterator.next());
	
	            contentStream.endText();
	
	            elementHeight += lineHeight;
        	}
        	
        }
        
        return elementHeight;
	}
	
	private void drawDocumentReferences(PDPageContentStream contentStream, float x, float y, PDFont font, Map<String,String> items) throws IOException{
		
	
        
        contentStream.setFont( font, DOC_FONT_SIZE_M );

        
        String name, value;
        float yPos = y;
        
        for (Map.Entry<String, String> entry : items.entrySet()) {
        	contentStream.beginText();
            contentStream.newLineAtOffset(x, yPos);
        	
            name = entry.getKey();
            value = entry.getValue();
            
        	contentStream.showText(name);

            contentStream.newLineAtOffset( textOffsetRight(215, value, font, DOC_FONT_SIZE_M), 0);
        	contentStream.showText(value);
            contentStream.newLineAtOffset( textWidth(value, font, DOC_FONT_SIZE_M), 0);

            contentStream.endText();
            
            yPos -= 16;
        }
        
	}
	
	private float textOffsetRight(float x, String text, PDFont font, float fontSize){
		return x - textWidth(text, font, fontSize);
	}
	
	private float textWidth(String text, PDFont font, float fontSize){
		float stringWidth = 0;
		
		try {
			stringWidth = font.getStringWidth( text )*fontSize/1000f;
		} catch (IOException e) {
		}
		
		return stringWidth;
	}
	
	public void initInvoicePage(PDPageContentStream contentStream, float x, float y, float pageHeight, float pageWidth, PDFont font, PDImageXObject bgImage, Invoice invoice) throws IOException{

		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		
		drawBackgroundImage(contentStream, bgImage, pageHeight, pageWidth);
		
        String[] addresslines = new String[5];
        addresslines[0] = invoice.getRecipientLine1();
        addresslines[1] = invoice.getRecipientLine2();
        addresslines[2] = invoice.getRecipientLine3();
        addresslines[3] = invoice.getRecipientLine4();
        addresslines[4] = invoice.getRecipientLine5();

        drawAddressfield(contentStream, x, y, font, invoice.getShop().getDrafterLine(), addresslines);
        
        
        Map<String,String> docReferenceMap = new LinkedHashMap<String,String>();
        
        if(! invoice.isCancelation()){
	        docReferenceMap.put("Rechnungsnummer:", invoice.getNumber() );
	        docReferenceMap.put("Rechnungsdatum:", String.valueOf( formatter.format( invoice.getEffectiveDate() )));
        }else{
            docReferenceMap.put("Gutschriftnummer:", invoice.getNumber() );
	        docReferenceMap.put("Gutschriftdatum:", String.valueOf( formatter.format( invoice.getEffectiveDate() )));
        	
        }
        
        docReferenceMap.put("Lieferscheinnummer:", invoice.getStockPickslip().getNumber() );
        
        docReferenceMap.put("Lieferscheindatum:", String.valueOf( formatter.format( invoice.getStockPickslip().getEffectiveDate() )));
        
        if(invoice.getCustomer() != null)
        	docReferenceMap.put("Kundennummer:", String.valueOf( invoice.getCustomer().getId() ));
        
        docReferenceMap.put("Buchungskonto:", String.valueOf( invoice.getCustomerAccount().getId() ));
        
        docReferenceMap.put("Zahlungsart:", String.valueOf( invoice.getPayment().getDescription() ));
        
        drawDocumentReferences(contentStream, x + 300, y - 20, font, docReferenceMap);

        if(! invoice.isCancelation()){
        	drawDocumentTitle(contentStream, x, y - 150, font, "Rechnung", invoice.getNumber());
        }else{
        	drawDocumentTitle(contentStream, x, y - 150, font, "Gutschrift", invoice.getNumber());
        }
		
        String[] footerLines = new String[3];
        footerLines[0] = invoice.getShop().getDocFooterLine1();
        footerLines[1] = invoice.getShop().getDocFooterLine2();
        footerLines[2] = invoice.getShop().getDocFooterLine3();
        
        drawDocumentFooter(contentStream, x, FOOTER_POS_Y, pageWidth, font, footerLines);
	}
	
	public PdfController(){
		regularFontURL = Thread.currentThread().getContextClassLoader().getResource("OpenSans-Regular.ttf");
		semiboldFontURL = Thread.currentThread().getContextClassLoader().getResource("OpenSans-Semibold.ttf");
		formatter.setMinimumFractionDigits(2);
	}
	
	@Override
	public java.io.File writeInvoiceTmpFile(Invoice invoice) throws Exception
    {
		
		java.io.File tmpFile = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat formatterWithTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        
        PDDocument doc = new PDDocument();

        PDFont regularFont = PDType0Font.load(doc, regularFontURL.openStream());
        PDFont semiboldFont = PDType0Font.load(doc, semiboldFontURL.openStream());
        
        PDFont font  = regularFont;
        PDImageXObject bgImage = null;
        
        PDStream pdStream  = new PDStream(doc);
        try
        {
        	
        	
            // a valid PDF document requires at least one page
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            
            DBFile docBgImage = stockController.getDocImage();
            if(docBgImage != null){
    			InputStream in = new ByteArrayInputStream(docBgImage.getImageFile());
    			BufferedImage bufferedBgImage = ImageIO.read(in);
                bgImage = LosslessFactory.createFromImage(doc, bufferedBgImage);
            }


	        
            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();
            float fontSize = 12;
            float startX = 40;
            float startY = 130;
            float contentWidth = pageWidth - startX - startX;
            float currentYPos;
            float contentStart = pageHeight - startY - 200;
            
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.OVERWRITE, false);
            contentStream.setFont( font, fontSize );
            currentYPos = pageHeight - startY;
            
            initInvoicePage(contentStream, startX, pageHeight - startY, pageHeight, pageWidth, font, bgImage, invoice);

            currentYPos = contentStart;
            
            currentYPos -= drawParagraph(contentStream, startX, currentYPos, contentWidth, font, invoice.getIntroduction());
            
            drawInvoiceItemHead(contentStream, startX, currentYPos, contentWidth, font);

            currentYPos -= DOC_FONT_SIZE_M + 6;
            Iterator<? extends InvoiceItem> i = invoice.getInvoiceItemList().iterator();
            InvoiceItem item;
            int pos = 0;
            BigDecimal sum = new BigDecimal(0);
            while(i.hasNext()){
            	item = i.next();
            	pos++;
            	currentYPos -= drawInvoiceItem(contentStream, startX, currentYPos, contentWidth, font, item, pos);
            	
            	sum = sum.add(new BigDecimal(1));
            	
            	if(currentYPos < FOOTER_POS_Y + 50){
            		
            		drawInvoiceSubTotal(contentStream, startX, currentYPos, contentWidth, font, sum);
            		
            		contentStream.close();
            		
            		// next page
            		page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    contentStream = new PDPageContentStream(doc, page, AppendMode.OVERWRITE, false);
                    contentStream.setFont( font, fontSize );
                    currentYPos = pageHeight - startY;
                    
                    initInvoicePage(contentStream, startX, pageHeight - startY, pageHeight, pageWidth, font, bgImage, invoice);

                    currentYPos = contentStart;
                    drawInvoiceItemHead(contentStream, startX, currentYPos, contentWidth, font);
                    currentYPos -= DOC_FONT_SIZE_M + 6;
            	}
            	
            }
            

            BigDecimal nettoTotal = invoice.getCommission().getPrice();
            Map<BigDecimal,BigDecimal> vatList = invoice.getVatList();
            BigDecimal grossTotal = nettoTotal.add( invoice.getCommission().getVat());
            
            currentYPos -= drawInvoiceTotal(contentStream, startX, currentYPos, contentWidth, font, semiboldFont, nettoTotal, vatList, grossTotal);

            currentYPos -= drawParagraph(contentStream, startX, currentYPos, contentWidth, font, invoice.getClosing());
            
            if(invoice.getPayment().getInvoiceText() != null && invoice.getPayment().getInvoiceText().trim().length() > 0){
            	currentYPos -= drawHeadline(contentStream, startX, currentYPos, contentWidth, font, "Zahlungsinformation:");
            	currentYPos -= drawParagraph(contentStream, startX, currentYPos, contentWidth, font, invoice.getPayment().getInvoiceText());
            }
            
            contentStream.close();
            
            addPageNumber(doc,font,startX + 300,  pageHeight - startY - 150);
            
//            replaceText(doc, "##pagecount##", String.valueOf( doc.getNumberOfPages() ));
            
//            x(doc);
            
//            doc.save(os);
            
                // creates temporary file
            tmpFile = fileController.getTempFile(".pdf");
            
            doc.save(tmpFile);

            
            FileSystemFile invoiceFile = fileController.saveInvoice(tmpFile, invoice.getNumber());
            
        }
        finally
        {
            doc.close();
        }
        
        return tmpFile;
    }
	

	private float drawParagraph(PDPageContentStream contentStream, float x, float y, float contentWidth, PDFont font,  String text) throws IOException{
		
		if(text == null || text.trim().length() == 0)
			return 0;
		
		float elementHeight = 0;
		float lineHeight = DOC_FONT_SIZE_M + 2;
        
		contentStream.beginText();
		contentStream.setFont(font, DOC_FONT_SIZE_M);
        contentStream.newLineAtOffset(x, y);
        contentStream.setLeading(lineHeight);
        
    	String[] lines = text.split("\\r|\\n");
    	
    	for(String line : lines){
    		
    		if(line.trim().length() == 0)
    			continue;
    		
    		if(elementHeight > 0)
    			contentStream.newLine();
    		
            contentStream.showText(line);

            elementHeight += lineHeight;
    	}

        contentStream.endText();
        
        if(elementHeight > 0)
        	elementHeight += lineHeight;

        return elementHeight;
	}
	


	private float drawHeadline(PDPageContentStream contentStream, float x, float y, float contentWidth, PDFont font,  String text) throws IOException{
		
		if(text == null || text.trim().length() == 0)
			return 0;
		
		float elementHeight = 0;
		float lineHeight = DOC_FONT_SIZE_L + 2;
        
		contentStream.beginText();
		contentStream.setFont(font, DOC_FONT_SIZE_L);
        contentStream.newLineAtOffset(x, y);
        contentStream.setLeading(lineHeight);
		
        contentStream.showText(text);

        elementHeight += lineHeight;

        contentStream.endText();

        return elementHeight;
	}
	
	private void addPageNumber(PDDocument doc, PDFont font, float x, float y) throws IOException{
        // Add Pagenumbers
	    PDPageTree pages = doc.getDocumentCatalog().getPages();
	    int pageNum = 0;
	    PDPageContentStream contentStream;
	    for (PDPage p : pages) {
	    	pageNum++;
            
	    	contentStream = new PDPageContentStream(doc, p, AppendMode.APPEND, false);
	    	
	    	drawDocumentPageNumber(contentStream, x, y, font, pageNum, doc.getNumberOfPages());

	    	contentStream.close();
            
        }
	}
}
