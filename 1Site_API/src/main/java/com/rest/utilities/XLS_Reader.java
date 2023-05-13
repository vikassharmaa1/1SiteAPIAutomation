package com.rest.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLS_Reader {

	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	// Creating constructor for xls reader class
	public XLS_Reader(String path) {

		this.path = path;
		try {
			fis = new FileInputStream(path);
			System.out.println("Path is :" + fis);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This method will returns the row count in a sheet
	public int getRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if(index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}
	}

	// This method will returns the data from a cell
	@SuppressWarnings("deprecation")
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if(rowNum <= 0)
				return "";
			int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if(index == -1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					{
					col_Num = i;
					break;
					}
			}
			if(col_Num == -1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if(row == null)
				return "";
			cell = row.getCell(col_Num);
			if(cell == null)
				return "";
			// System.out.println(cell.getCellType());
			if(cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				if(HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
					// System.out.println(cellText);
				}
				return cellText;
			} else if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());

		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	public Object[] getTableArray(String SheetName, String testType) throws Exception {

		String[] tabArray = null;
		List<Object> dataList = null;
		try {
			int index = workbook.getSheetIndex(SheetName);
			sheet = workbook.getSheetAt(index);
			int colnum1 = -1, colnum2 = -1, colnum3 = -1;
			int totalRows = sheet.getLastRowNum();
			String col_name1 = "DataProvider";
			String col_name2 = "TestMethodName";
			String col_name3 = "RunMode";
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(col_name1.trim())) {
					colnum1 = i;
					break;
				}
			}

			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(col_name2.trim())) {
					colnum2 = i;
					break;
				}
			}
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(col_name3.trim())) {
					colnum3 = i;
					break;
				}
			}

			if(colnum1 == -1 || colnum2 == -1 || colnum3 == -1) {
				System.out.println("Either DataProvider/TestMethodName/RunMode columns are missing in excel sheet or invalid sheetname is provided in the xml");
			}

			dataList = new ArrayList<Object>();
			for (int i = 1; i <= totalRows; i++) {

				if(getCellData(i, colnum1).equalsIgnoreCase(testType)) {

					if(getCellData(i, colnum3).equalsIgnoreCase("yes")) {
						dataList.add(getCellData(i, colnum2));
					} else {
						System.out.println(getCellData(i, colnum2) + " : SKIPPED - Run Mode is set to false.");
					}
				}
			}

		} catch (FileNotFoundException e) {

			System.out.println("Could not read the Excel sheet");

			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Could not read the Excel sheet");

			e.printStackTrace();

		}
		int size = dataList.size();
		tabArray = new String[size];
		for (int i = 0; i < size; ++i) {
			tabArray[i] = dataList.get(i).toString();
		}

		return (tabArray);

	}

	public String getCellData(int RowNum, int ColNum) throws Exception {
		String CellData = "";
		try {
			row = sheet.getRow(RowNum);
			if(isRowEmpty(row)) {
				return CellData;
			} else {
				cell = row.getCell(ColNum);

				if(cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
					return "";
				} else {					
					DataFormatter objDefaultFormat = new DataFormatter();
					FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
					CellData = objDefaultFormat.formatCellValue( cell , formulaEvaluator );
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
		return CellData;
	}

	private static boolean isRowEmpty(XSSFRow row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if(row != null) {
			for (Cell cell : row) {
				if(dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}

	public Map<String, String> getTestData(String sheetName, int rowNum) {
		Map<String, String> testData = new HashMap<String, String>();
		try {
			int sheetIndex = workbook.getSheetIndex(sheetName);
			sheet = workbook.getSheetAt(sheetIndex);
			row = sheet.getRow(rowNum - 1);
			XSSFRow topRow = sheet.getRow(0);
			for (int i = 0; i < topRow.getLastCellNum(); i++) {
				testData.put(getCellData(0, i), getCellData(rowNum - 1, i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testData;
	}

}
