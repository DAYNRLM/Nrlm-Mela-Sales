package com.nrlm.melasalesoffline.utils;

import com.nrlm.melasalesoffline.database.entity.ProductAddTemptableEntity;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class PdfUtils {
    List<ProductAddTemptableEntity> selectedItemList;
    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;
    DecimalFormat df = new DecimalFormat("0.00");
    private int totalAmount=0;

    public static com.nrlm.melasalesoffline.utils.PdfUtils pdfUtils = null;
    public static com.nrlm.melasalesoffline.utils.PdfUtils getInstance() {
        if (pdfUtils == null)
            pdfUtils = new com.nrlm.melasalesoffline.utils.PdfUtils();
        return pdfUtils;
    }

    public void createPdfFile(String finalPath, List<ProductAddTemptableEntity> selectedItemList , String invoiceNo,String dateTime){
           totalAmount=0;
        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();
        AppUtility.getInstance().showLog("Size of list" +selectedItemList.size(), com.nrlm.melasalesoffline.utils.PdfUtils.class);
        try {

            Font regular = new Font(Font.FontFamily.HELVETICA, 12);
            Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Rectangle small = new Rectangle(290,100);
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(finalPath));
            doc.addAuthor("NRLM");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("NRLM.com");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.A4);
            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            //create header
            Paragraph SUB_Heading = null;
            SUB_Heading = new Paragraph("SARAS AAJEEVIKA MELA \n Invoice Slip ");
            SUB_Heading.setAlignment(Element.ALIGN_CENTER);
            SUB_Heading.setSpacingAfter(10f);

          /*  Paragraph shg_name = new Paragraph("Shg name."+"dev kumar shastri"+"%10s"+"Date:-"+"10/11/1991");
            shg_name.setAlignment(Element.ALIGN_LEFT);
            shg_name.setSpacingAfter(10f);

            Paragraph gud = new Paragraph("Shg name."+"dev kumar shastri"+"%10s"+"Date:-"+"10/11/1991------");
            gud.setAlignment(Element.ALIGN_LEFT);
            gud.setSpacingAfter(10f);*/


            doc.add(SUB_Heading);
           /* doc.add(shg_name);
            doc.add(gud);*/


            PdfPTable top_table = new PdfPTable(4);
            float[] top_cw = new float[] { 1f,2f,1.5f,2f};
            top_table.setWidths(top_cw);
            PdfPCell top_cell;
            top_table.setWidthPercentage(100);
            top_table.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_table.setHorizontalAlignment(Element.ALIGN_MIDDLE);

            top_cell = new PdfPCell(new Paragraph("SHG Reg Id:"));
            top_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_cell.setBorderColor(BaseColor.BLACK);
            top_cell.setBackgroundColor(BaseColor.WHITE);
            top_table.addCell(top_cell);

            top_cell = new PdfPCell(new Paragraph(selectedItemList.get(0).getShg_reg_id()));
            top_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            top_cell.setBorderColor(BaseColor.BLACK);
            top_cell.setBackgroundColor(BaseColor.WHITE);
            top_table.addCell(top_cell);

            top_cell = new PdfPCell(new Paragraph("Invoice Number:"));
            top_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_cell.setBorderColor(BaseColor.BLACK);
            top_cell.setBackgroundColor(BaseColor.WHITE);
            top_table.addCell(top_cell);

            top_cell = new PdfPCell(new Paragraph(invoiceNo));
            top_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            top_cell.setBorderColor(BaseColor.BLACK);
            top_cell.setBackgroundColor(BaseColor.WHITE);
            top_table.addCell(top_cell);
            top_table.setSpacingAfter(4f);
            doc.add(top_table);



            PdfPTable top_second_table = new PdfPTable(4);
            float[] top_second_cw = new float[] { 1f,2f,1.5f,2f};
            top_second_table.setWidths(top_second_cw);
            PdfPCell top_second_cell;
            top_second_table.setWidthPercentage(100);
            top_second_table.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_second_table.setHorizontalAlignment(Element.ALIGN_MIDDLE);

            top_second_cell = new PdfPCell(new Paragraph("Stall Number:"));
            top_second_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_second_cell.setBorderColor(BaseColor.BLACK);
            top_second_cell.setBackgroundColor(BaseColor.WHITE);
            top_second_table.addCell(top_second_cell);

            top_second_cell = new PdfPCell(new Paragraph(selectedItemList.get(0).getStall_no()));
            top_second_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            top_second_cell.setBorderColor(BaseColor.BLACK);
            top_second_cell.setBackgroundColor(BaseColor.WHITE);
            top_second_table.addCell(top_second_cell);

            top_second_cell = new PdfPCell(new Paragraph("Date:"));
            top_second_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_second_cell.setBorderColor(BaseColor.BLACK);
            top_second_cell.setBackgroundColor(BaseColor.WHITE);
            top_second_table.addCell(top_second_cell);

            top_second_cell = new PdfPCell(new Paragraph(dateTime));
            top_second_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            top_second_cell.setBorderColor(BaseColor.BLACK);
            top_second_cell.setBackgroundColor(BaseColor.WHITE);
            top_second_table.addCell(top_second_cell);

           /* top_second_cell = new PdfPCell(new Paragraph("SHG Reg Id."));
            top_second_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_second_cell.setBorderColor(BaseColor.BLACK);
            top_second_cell.setBackgroundColor(BaseColor.WHITE);
            top_second_table.addCell(top_second_cell);

            top_second_cell = new PdfPCell(new Paragraph("18"));
            top_second_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            top_second_cell.setBorderColor(BaseColor.BLACK);
            top_second_cell.setBackgroundColor(BaseColor.WHITE);
            top_second_table.addCell(top_second_cell);*/

            top_second_table.setSpacingAfter(10f);
            doc.add(top_second_table);

           /* LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            doc.add(new Chunk(lineSeparator));*/





            // create table
            PdfPTable table = new PdfPTable(5);
            float[] columnWidths = new float[] { 0.5f,3f,1f,2f,2f };
            table.setWidths(columnWidths);
            PdfPCell cell = new PdfPCell(new Paragraph("Invoice"));
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setHorizontalAlignment(Element.ALIGN_MIDDLE);

            cell = new PdfPCell(new Paragraph("S.No."));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderColor(BaseColor.BLACK);
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Item name"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(BaseColor.BLACK);
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Quantity"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(BaseColor.BLACK);
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Unit Price"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(BaseColor.BLACK);
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Amount"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(BaseColor.BLACK);
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            for(int i=0,index=1;i<selectedItemList.size();i++,index++){

                AppUtility.getInstance().showLog("totalAmount="+totalAmount+"selectedItemList.get(i).getProduct_total_price()"+selectedItemList.get(i).getProduct_total_price(),PdfUtils.class);

                totalAmount=totalAmount+selectedItemList.get(i).getProduct_total_price();

                AppUtility.getInstance().showLog("totalAmount="+totalAmount,PdfUtils.class);
                cell = new PdfPCell(new Paragraph(Integer.toString(index)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderColor(BaseColor.BLACK);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(selectedItemList.get(i).getProduct_name()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderColor(BaseColor.BLACK);
                table.addCell(cell);

                int quantity=selectedItemList.get(i).getProduct_quantity();
                AppUtility.getInstance().showLog("quantity"+quantity,PdfUtils.class);
                cell = new PdfPCell(new Paragraph(quantity+""));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderColor(BaseColor.BLACK);
                table.addCell(cell);

                int unitPrice =selectedItemList.get(i).getProduct_unit_price();
                AppUtility.getInstance().showLog("unitPrice"+unitPrice,PdfUtils.class);
                cell = new PdfPCell(new Paragraph(unitPrice+""));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderColor(BaseColor.BLACK);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(selectedItemList.get(i).getProduct_total_price()+" Rs."));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorderColor(BaseColor.BLACK);
                table.addCell(cell);
            }
            table.setSpacingAfter(3f);
            doc.add(table);

            //--------total price formate-------------
            PdfPTable totalPriceTable = new PdfPTable(2);
            float[] totalPriceCW = new float[] {5f,1.55f };
            totalPriceTable.setWidths(totalPriceCW);
            PdfPCell totalPriceCell;
            totalPriceTable.setWidthPercentage(100);
            totalPriceTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalPriceTable.setHorizontalAlignment(Element.ALIGN_MIDDLE);

            totalPriceCell = new PdfPCell(new Paragraph("Total Amount"));
            totalPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalPriceCell.setBorderColor(BaseColor.BLACK);
            totalPriceCell.setBackgroundColor(BaseColor.WHITE);
            totalPriceTable.addCell(totalPriceCell);

            totalPriceCell = new PdfPCell(new Paragraph(totalAmount+" Rs."));
            totalPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalPriceCell.setBorderColor(BaseColor.BLACK);
            totalPriceCell.setBackgroundColor(BaseColor.WHITE);
            totalPriceTable.addCell(totalPriceCell);
            doc.add(totalPriceTable);




        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (doc != null)
            {
                doc.close();
            }
            if (docWriter != null)
            {
                docWriter.close();
            }
        }
    }

    public static void addCustomerReference(Document layoutDocument)
    {

       /* layoutDocument.add(new Paragraph("M/s Indian Convent School").setTextAlignment(TextAlignment.LEFT).setMultipliedLeading(0.2f));
        layoutDocument.add(new Paragraph("y Pocket-3, Sector-24, Rohini Delhi-110085").setMultipliedLeading(.2f));
        layoutDocument.add(new Paragraph("b 011-64660271").setMultipliedLeading(.2f));*/
    }

    private void generateLayout(Document doc, PdfContentByte cb)  {

        try {
            cb.setLineWidth(1f);
            // Invoice Header box layout
            cb.rectangle(22,700,200,100);

            cb.moveTo(22,720);
            cb.lineTo(222,720);
            cb.moveTo(22,740);
            cb.lineTo(222,740);
            cb.moveTo(22,760);
            cb.lineTo(222,760);
            cb.moveTo(22,780);
            cb.lineTo(222,780);
            cb.moveTo(102,700);
            cb.lineTo(102,800);


            cb.stroke();
            createHeadings(cb,24,783,"SHG Name.");
            createHeadings(cb,24,763,"Invoice No.");
            createHeadings(cb,24,743,"Invoice Date");
            createHeadings(cb,24,723,"Stall Number.");
            createHeadings(cb,24,703,"SHG Code.");


           /* cb.rectangle(20,350,550,400);
            cb.moveTo(20,620);
            cb.lineTo(570,620);

            cb.moveTo(20,400);
            cb.lineTo(570,400);*/
           /* cb.rectangle(20,200,550,600);
            cb.moveTo(20,630);
            cb.lineTo(570,630);

            cb.moveTo(50,50);
            cb.lineTo(50,650);

            cb.moveTo(150,50);
            cb.lineTo(150,650);

            cb.moveTo(430,50);
            cb.lineTo(430,650);

            cb.moveTo(500,50);
            cb.lineTo(500,650);


            //cb.stroke();

            // Invoice Detail box Text Headings
            createHeadings(cb,22,633,"No.");
            createHeadings(cb,52,633,"Item Number");
            createHeadings(cb,148,633,"Quantity");
            createHeadings(cb,152,633,"Item Description");
            createHeadings(cb,432,633,"Price");
            createHeadings(cb,502,633,"Ext Price");

            //add the images
            Image companyLogo = Image.getInstance("images/olympics_logo.gif");
            companyLogo.setAbsolutePosition(25,700);
            companyLogo.scalePercent(25);*/
            // doc.add(companyLogo);
        }

        catch (IllegalAccessError dex){
            dex.printStackTrace();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateHeader(Document doc, PdfContentByte cb)  {

        try {

          /*  createHeadings(cb,200,750,"Company Name");
            createHeadings(cb,200,735,"Address Line 1");
            createHeadings(cb,200,720,"Address Line 2");
            createHeadings(cb,200,705,"City, State - ZipCode");
            createHeadings(cb,200,690,"Country");*/

           /* createHeadings(cb,482,783,"SHG NAME");
            createHeadings(cb,482,763,"#001100223");
            createHeadings(cb,482,743,"06/10/1991");
            createHeadings(cb,482,723,"17");
            createHeadings(cb,482,703,"0987675");*/

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateDetail(Document doc, PdfContentByte cb, int index, int y)  {
        DecimalFormat df = new DecimalFormat("0.00");

        try {

            createContent(cb,48,y, String.valueOf(index+1), PdfContentByte.ALIGN_RIGHT);
            createContent(cb,52,y, "ITEM" + String.valueOf(index+1), PdfContentByte.ALIGN_LEFT);
            createContent(cb,152,y, "Product Description - SIZE " + String.valueOf(index+1), PdfContentByte.ALIGN_LEFT);

            double price = Double.valueOf(df.format(Math.random() * 10));
            double extPrice = price * (index+1) ;
            createContent(cb,498,y, df.format(price), PdfContentByte.ALIGN_RIGHT);
            createContent(cb,568,y, df.format(extPrice), PdfContentByte.ALIGN_RIGHT);

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


    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

   /* public void unusew(){

        Document document = new Document();
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(finalPath));
        document.open();
        // Document Settings
        PdfContentByte cb = pdfWriter.getDirectContent();

        //custom page size for bill slip
        // Rectangle pagesize = new Rectangle(164.41f, 14400);
        //document.setPageSize(pagesize);

        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("Android School");
        document.addCreator("Pratik Butani");



        BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
        float mHeadingFontSize = 20.0f;
        float mValueFontSize = 26.0f;

        //set text formate
        BaseFont urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));



        // Title Order Details...
        // Adding Title....
        Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderDetailsTitleChunk = new Chunk("Order Details", mOrderDetailsTitleFont);
        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(mOrderDetailsTitleParagraph);

        // Fields of Order Details...
        // Adding Chunks for Title and value
        //-------------invoice number-----------------
        Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
        Chunk mOrderIdChunk = new Chunk("Invoice Number:", mOrderIdFont);
        Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
        document.add(mOrderIdParagraph);

        Font mOrderIdValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderIdValueChunk = new Chunk("#123123", mOrderIdValueFont);
        Paragraph mOrderIdValueParagraph = new Paragraph(mOrderIdValueChunk);
        document.add(mOrderIdValueParagraph);

        // Adding Line Breakable Space....
        document.add(new Paragraph(""));
        // Adding Horizontal Line...
        document.add(new Chunk(lineSeparator));
        // Adding Line Breakable Space....
        document.add(new Paragraph(""));

        // Fields of Order Details...
        //------order date-------------------
        Font mOrderDateFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
        Chunk mOrderDateChunk = new Chunk("Order Date:", mOrderDateFont);
        Paragraph mOrderDateParagraph = new Paragraph(mOrderDateChunk);
        document.add(mOrderDateParagraph);

        Font mOrderDateValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderDateValueChunk = new Chunk("06/07/2017", mOrderDateValueFont);
        Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
        document.add(mOrderDateValueParagraph);

        document.add(new Paragraph(""));
        document.add(new Chunk(lineSeparator));
        document.add(new Paragraph(""));

        //---------shg name------------------
        Font mOrderSHGNameFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
        Chunk mOrderSHGNameChunk = new Chunk("SHG Name:", mOrderSHGNameFont);
        Paragraph mOrderSHGNameParagraph = new Paragraph(mOrderSHGNameChunk);
        document.add(mOrderSHGNameParagraph);

        Font mOrderSHGNameValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderSHGNameValueChunk = new Chunk("06/07/2017", mOrderSHGNameValueFont);
        Paragraph mOrderSHGNameValueParagraph = new Paragraph(mOrderSHGNameValueChunk);
        document.add(mOrderSHGNameValueParagraph);

        document.add(new Paragraph(""));
        document.add(new Chunk(lineSeparator));
        document.add(new Paragraph(""));

        //--shg code-------------
        Font mOrderSHGCode = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
        Chunk mOrderSHGCodeChunk = new Chunk("SHG Name:", mOrderSHGCode);
        Paragraph mOrderSHGCodeParagraph = new Paragraph(mOrderSHGCodeChunk);
        document.add(mOrderSHGCodeParagraph);

        Font mOrderSHGCodeValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderSHGCodeValueChunk = new Chunk("06/07/2017", mOrderSHGCodeValueFont);
        Paragraph mOrderSHGCodeValueParagraph = new Paragraph(mOrderSHGCodeValueChunk);
        document.add(mOrderSHGCodeValueParagraph);

        document.add(new Paragraph(""));
        document.add(new Chunk(lineSeparator));
        document.add(new Paragraph(""));


        // -----------stall number-------------------
        Font stallNumberFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
        Chunk stallNumberChunk = new Chunk("Account Name:", stallNumberFont);
        Paragraph stallNumberParagraph = new Paragraph(stallNumberChunk);
        document.add(stallNumberParagraph);


        Font stallNumberValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
        Chunk stallNumberValueChunk = new Chunk("Pratik Butani", stallNumberValueFont);
        Paragraph stallNumberValueParagraph = new Paragraph(stallNumberValueChunk);
        document.add(stallNumberValueParagraph);


        document.add(new Paragraph(""));
        document.add(new Chunk(lineSeparator));
        document.add(new Paragraph(""));




        //generateProductListLayout(document, cb);

        document.close();

        Toast.makeText(context, "Created... :)", Toast.LENGTH_SHORT).show();

        //   InternalFileCreation.openFile(context, new File(finalPath));

    }*/

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

}