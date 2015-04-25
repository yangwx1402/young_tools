package com.young.tools.common.util.tika;

import java.io.File;

import org.apache.tika.Tika;
import org.apache.tika.parser.microsoft.OfficeParser;

public class TikaWordTest {

	public static void main(String[] args) throws Exception {
		Tika tika = new Tika();
		String fileName = "G:\\工作\\捷云项目\\云南审计厅\\2010至2012年省级旅游发展专项资金管理使用况专项审计调查报告.doc";
		String content = tika.parseToString(new File(fileName));
		System.out.println(content);
		tika.parse(new File(fileName));
		
		OfficeParser parser = new OfficeParser();
		
	}
}
