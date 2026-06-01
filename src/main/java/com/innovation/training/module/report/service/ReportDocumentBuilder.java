package com.innovation.training.module.report.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class ReportDocumentBuilder {

    public byte[] buildDocx(String title, List<String> sections) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(18);
            titleRun.setText(title);

            for (String section : sections) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.setSpacingAfter(160);
                XWPFRun run = paragraph.createRun();
                run.setFontSize(11);
                run.setText(section == null ? "" : section);
            }
            document.write(out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("生成 Word 文档失败", ex);
        }
    }

    public byte[] buildPdf(String title, List<String> sections) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            Font titleFont = new Font(resolveChineseBaseFont(), 18, Font.BOLD);
            Font bodyFont = new Font(resolveChineseBaseFont(), 11, Font.NORMAL);
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingAfter(18);
            document.add(titleParagraph);
            for (String section : sections) {
                Paragraph paragraph = new Paragraph(section == null ? "" : section, bodyFont);
                paragraph.setLeading(18);
                paragraph.setSpacingAfter(10);
                document.add(paragraph);
            }
            document.close();
            return out.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("生成 PDF 文档失败", ex);
        }
    }

    private BaseFont resolveChineseBaseFont() throws Exception {
        List<String> candidates = List.of(
                "C:/Windows/Fonts/simsun.ttc,0",
                "C:/Windows/Fonts/msyh.ttc,0"
        );
        for (String candidate : candidates) {
            String path = candidate.substring(0, candidate.indexOf(','));
            if (Files.exists(Path.of(path))) {
                return BaseFont.createFont(candidate, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
        }
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
    }
}
