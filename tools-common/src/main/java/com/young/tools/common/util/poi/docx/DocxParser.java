package com.young.tools.common.util.poi.docx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.young.tools.common.util.poi.Doc;
import com.young.tools.common.util.poi.DocFragment;
import com.young.tools.common.util.poi.FileParser;

public class DocxParser implements FileParser {

	@Override
	public Doc parse(File file, String encode) throws FileNotFoundException,
			IOException {
		XWPFDocument document = new XWPFDocument(new FileInputStream(file));
		List<XWPFParagraph> paras = document.getParagraphs();
		Doc doc = new Doc();
		doc = parseNormal(document, doc);
		doc = parseFragment(paras, doc);
		return doc;
	}

	private Doc parseFragment(List<XWPFParagraph> paras, Doc doc) {
		int t1Num = 0;
		int t2Num = 0;
		int t3Num = 0;
		List<DocFragment> t1s = new ArrayList<DocFragment>();
		DocFragment frag1 = null;
		DocFragment frag2 = null;
		DocFragment frag3 = null;
		DocFragment zheng = null;
		for (XWPFParagraph p : paras) {
			if ("a9".equals(p.getStyle())) {
				if (doc.getTitle() == null) {
					doc.setTitle(p.getText());
				}
			} else if ("1".equals(p.getStyle())) {
				t1Num++;
				t2Num = 0;
				frag1 = new DocFragment();
				frag1.setText(p.getText());
				frag1.setType("tindex" + t1Num);
				t1s.add(frag1);
			} else if ("2".equals(p.getStyle())) {
				t2Num++;
				t3Num = 0;
				frag2 = new DocFragment();
				frag2.setText(p.getText());
				frag2.setType("tindex" + t1Num + "_" + t2Num);
				frag1.addFragment(frag2);
			} else if ("3".equals(p.getStyle())) {
				t3Num++;
				frag3 = new DocFragment();
				frag3.setText(p.getText());
				frag3.setType("tindex" + t1Num + "_" + t2Num + "_" + t3Num);
				frag2.addFragment(frag3);
			} else if (p.getStyle() == null) {
				zheng = new DocFragment();
				zheng.setText(p.getText());
				zheng.setType("cindex" + t1Num + "_" + t2Num + "_" + t3Num);
				if (frag3 != null)
					frag3.addFragment(zheng);
			}
		}
		doc.setFragments(t1s);
		return doc;
	}

	private Doc parseNormal(XWPFDocument document, Doc doc) {
		CoreProperties pros = document.getProperties().getCoreProperties();
		doc.setTitle(pros.getTitle());
		doc.setCreateTime(pros.getCreated());
		return doc;
	}

	public void printDoc(Doc doc) {
		System.out.println(doc.getTitle());
		if (doc.getFragments() != null) {
			for (DocFragment f : doc.getFragments()) {
				printFragment(f);
			}
		}
	}

	private void printFragment(DocFragment frag) {
		System.out.println(frag.getType() + ":::" + frag.getText());
		if (frag.getSubFragments() != null
				&& frag.getSubFragments().size() != 0) {
			for (DocFragment f : frag.getSubFragments()) {
				printFragment(f);
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		DocxParser parser = new DocxParser();
		// parser.parse(new
		// File("G:\\工作\\捷云项目\\云南审计厅\\Netflix公布个性化和推荐系统架构.docx"), "utf-8");
		// parser.test(new File("G:\\工作\\捷云项目\\云南审计厅\\test.docx"), "utf-8");
		Doc doc = parser.parse(
				new File("G:\\工作\\捷云项目\\云南审计厅\\安装配置维护交接文档.docx"), "utf-8");
		parser.printDoc(doc);
	}
}
