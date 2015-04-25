package com.young.tools.common.util.poi.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.Bookmarks;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

import com.young.tools.common.util.poi.Doc;
import com.young.tools.common.util.poi.FileParser;

public class DocParser implements FileParser{

	@Override
	public Doc parse(File file, String encode) throws FileNotFoundException, IOException {
		HWPFDocument document = new HWPFDocument(new FileInputStream(file));
		Range range = document.getRange();
		int sectionNum = range.numSections();
		Section section = null;
		for(int i=0;i<sectionNum;i++){
			section = range.getSection(i);
			int paragraphNum = section.numParagraphs();
			Paragraph p = null;
			for(int j=0;j<paragraphNum;j++){
				p = section.getParagraph(j);
				
				System.out.println(p.text().trim());
				int chNum = p.numCharacterRuns();
				CharacterRun r = null;
				for(int x = 0;x<chNum;x++){
					r = p.getCharacterRun(x);
				}
			}
		}
		Bookmarks bookmarks = document.getBookmarks();
		Bookmark mark = null;
		for(int i=0;i<bookmarks.getBookmarksCount();i++){
			mark = bookmarks.getBookmark(i);
		}
		WordExtractor extractor = new WordExtractor(document);
//		System.out.println("header="+extractor.getHeaderText());
//		System.out.println("footer="+extractor.getFooterText());
//		System.out.println("comments="+extractor.getCommentsText()[0]);
//		System.out.println("meta="+extractor.getMetadataTextExtractor().getText());
//		System.out.println(extractor.getDocSummaryInformation().toString());
//		
//		System.out.println();
//		System.out.println(extractor.getParagraphText()[80]);
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		FileParser parser = new DocParser();
		//parser.parse(new File("G:\\工作\\捷云项目\\云南审计厅\\XXX人民政府2011年度地方财政收支决算情况.doc"), "utf-8");
		parser.parse(new File("G:\\工作\\捷云项目\\云南审计厅\\test.doc"), "utf-8");
	}

}
