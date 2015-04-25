package com.young.tools.export.template.execl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * execl模板解析器
 * 
 * @author yangy
 * 
 */
public class ExcelTemplateParser {

	/** 初始行的下标(指的是填充数据的第一个单元格下标) */
	private int initRowIndex;
	/** 初始列的下标(指的是填充数据的第一个单元格下标) */
	private int initColIndex;
	/** 当前行的下标(指的是填充数据的当前单元格下标) */
	private int currRowIndex;
	/** 当前列的下标(指的是填充数据的当前单元格下标) */
	private int currColIndex;
	/** 最后一行的下标 */
	private int lastRowIndex;
	/** 默认行高(指的是填充数据的第一个单元格的行高) */
	private float defaultRowHeight;
	/**
	 * 用来保存每列的属性样式
	 */
	private Map<Integer, CellStyle> allCellStyle = new HashMap<Integer, CellStyle>();

	private Map<Integer, Integer> allCellType = new HashMap<Integer, Integer>();

	private Map<Integer, String> allCellFormula = new HashMap<Integer, String>();

	private Row currRow;
	private Sheet template_sheet;
	private Workbook template_wb;
	private Map<Integer, Sheet> dest_sheets = new HashMap<Integer, Sheet>();
	/** 存放模板中所有表格样式(键为1000表示表格的默认样式) */
	private static final int default_style_id = 1000;

	private Map<Integer, String> sheet_names = new HashMap<Integer, String>();

	private ExcelTemplateParserConfig parserConfig;

	public ExcelTemplateParser(ExcelTemplateParserConfig parserConfig) {
		this.parserConfig = parserConfig;
	}

	private void rewind() {
		initRowIndex = 0;
		initColIndex = 0;
		currRowIndex = 0;
		currColIndex = 0;
		lastRowIndex = 0;
		allCellStyle.clear();
		allCellType.clear();
		allCellFormula.clear();
		currRow = null;
	}

	private void init() throws InvalidFormatException, FileNotFoundException,
			IOException {
		if (template_wb == null) {
			template_wb = WorkbookFactory.create(new FileInputStream(
					parserConfig.getREPORT_TEMPLATE_FILE_PATH()));
			template_sheet = template_wb.getSheetAt(parserConfig
					.getTemplate_sheet_index());
		}
	}

	public void createSheet(String sheet_name, int index)
			throws InvalidFormatException, FileNotFoundException, IOException {
		init();
		rewind();
		// Sheet sheet = template_wb.createSheet(sheet_name);
		template_wb.setSheetName(parserConfig.getTemplate_sheet_index(),
				sheet_name);
		Sheet sheet = template_wb.cloneSheet(parserConfig
				.getTemplate_sheet_index());
		sheet_names.put(template_wb.getSheetIndex(sheet), sheet_name);
		Row temp = null;
		for (Row row : template_sheet) {
			temp = sheet.createRow(row.getRowNum());
			temp.setHeight(row.getHeight());
			Cell tempCell = null;
			for (Cell cell : row) {
				// 报表模板文件default.xls中约定序号和SERIAL_NO和DATA_BEGIN都是String类型的
				if (Cell.CELL_TYPE_STRING != cell.getCellType()) {
					continue;
				}
				tempCell = temp.createCell(cell.getColumnIndex(),
						cell.getRowIndex());
				tempCell.setCellValue(cell.getStringCellValue());
				tempCell.setCellStyle(cell.getCellStyle());
				tempCell.setCellType(cell.getCellType());
				String str = cell.getStringCellValue().trim();
				// 收集默认的表格样式
				if (parserConfig.getDEFAULT_STYLES().equals(str)) {
					this.allCellStyle
							.put(default_style_id, cell.getCellStyle());
					allCellType.put(default_style_id, cell.getCellType());
					if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
						allCellFormula.put(default_style_id,
								cell.getCellFormula());
				}
				// 收集除默认表格样式以外的所有表格样式
				if (parserConfig.getUSE_STYLES().equals(str)) {
					this.allCellStyle.put(cell.getColumnIndex(),
							cell.getCellStyle());
					allCellType.put(cell.getColumnIndex(), cell.getCellType());
					if(cell.getCellType()==Cell.CELL_TYPE_FORMULA)
					allCellFormula.put(cell.getColumnIndex(),
							cell.getCellFormula());
				}

				// 定位开始填充数据的第一个单元格的下标
				if (parserConfig.getDATA_BEGIN().equals(str)) {
					this.initColIndex = cell.getColumnIndex();
					this.initRowIndex = row.getRowNum();
					this.currColIndex = this.initColIndex;
					this.currRowIndex = this.initRowIndex;
					this.lastRowIndex = template_sheet.getLastRowNum();
					this.defaultRowHeight = row.getHeightInPoints();
				}
			}
		}
		dest_sheets.put(index, sheet);
	}

	/**
	 * 创建行
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 */
	public void createNewRow(int sheet_index) throws InvalidFormatException,
			FileNotFoundException, IOException {
		if (!dest_sheets.containsKey(sheet_index)) {
			createSheet("sheet_" + sheet_index, sheet_index);
		}
		Sheet dest_sheet = dest_sheets.get(sheet_index);
		// 下移行的条件有2个:当前行非初始行,且当前行没有超过最后一行
		if (this.currRowIndex != this.initRowIndex
				&& this.lastRowIndex > this.currRowIndex) {
			// 将指定的几行进行下移一行
			dest_sheet.shiftRows(this.currRowIndex, this.lastRowIndex, 1, true,
					true);
			// 既然下移了那么最后一行下标就也要增大了
			this.lastRowIndex++;
		}
		// 在指定的行上创建一个空行(如果此行原本有单元格和数据,那么也会被空行覆盖,且创建出来的空行是没有单元格的)
		this.currRow = dest_sheet.createRow(this.currRowIndex);
		this.currRow.setHeightInPoints(this.defaultRowHeight);
		this.currRowIndex++;
		this.currColIndex = this.initColIndex;
	}

	/**
	 * 构造单元格(包括创建单元格和填充数据)
	 */
	public void buildCell(Object value) {
		Cell cell = this.currRow.createCell(this.currColIndex);
		if (this.allCellStyle.containsKey(this.currColIndex)) {

			cell.setCellStyle(this.allCellStyle.get(this.currColIndex));
			cell.setCellType(this.allCellType.get(this.currColIndex));
			cell.setCellFormula(allCellFormula.get(this.currColIndex));

		} else {
			cell.setCellStyle(this.allCellStyle.get(default_style_id));
			cell.setCellType(this.allCellType.get(default_style_id));
			cell.setCellFormula(this.allCellFormula.get(default_style_id));

		}
		if (value instanceof Long)
			cell.setCellValue(Long.parseLong(value.toString()));
		else
			cell.setCellValue(value==null?"null":value.toString());
		this.currColIndex++;
	}

	/**
	 * 替换模板文件中的常量
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 */
	public void replaceConstantData(Map<String, Object> constantData,
			int sheet_index) throws InvalidFormatException,
			FileNotFoundException, IOException {
		if (!dest_sheets.containsKey(sheet_index)) {
			createSheet("sheet_" + sheet_index, sheet_index);
		}
		Sheet dest_sheet = dest_sheets.get(sheet_index);
		for (Row row : dest_sheet) {
			for (Cell cell : row) {
				if (Cell.CELL_TYPE_STRING != cell.getCellType()) {
					continue;
				}
				String str = cell.getStringCellValue().trim();
				if (str.startsWith(parserConfig.getPlaceholder())) {
					if (constantData.containsKey(str.substring(1))) {
						cell.setCellValue(constantData.get(str.substring(1))
								.toString());
					}
				}
			}
		}
	}

	/**
	 * 将生成的excel文件写到输出流中
	 * 
	 * @see 适用于文件下载
	 */
	public void writeToStream(OutputStream os) {
		try {
			template_wb.setSheetName(parserConfig.getTemplate_sheet_index(),
					"template");

			for (Map.Entry<Integer, String> entry : sheet_names.entrySet())
				template_wb.setSheetName(entry.getKey(), entry.getValue());
			template_wb.removeSheetAt(parserConfig.getTemplate_sheet_index());
			template_wb.write(os);
		} catch (IOException e) {
			throw new RuntimeException("写入流失败", e);
		}
	}

}
