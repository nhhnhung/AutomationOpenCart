package com.opencart.helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelHelper {
    private FileInputStream fis;
    private FileOutputStream fio;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private DataFormatter df;
    private String excelFilePath;

    public void setExcelFile(String path, String sheetName){
        try {
            fis = new FileInputStream(path);
            wb = WorkbookFactory.create(fis);
            sh = wb.getSheet(sheetName);
            this.excelFilePath = path;

            if (sh == null) {
                throw new RuntimeException("Sheet không tồn tại: " + sheetName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc file Excel", e);
        }
    }

    public String getCellData(int rowNum, int colNum){ //Lấy 1 ô khi biết index hàng và cột
        df = new DataFormatter();
        row = sh.getRow(rowNum);
        if (row == null) return "";

        cell = row.getCell(colNum);
        if (cell == null) return "";

        return df.formatCellValue(cell).trim();
    }

    public String getCellData(String columnName, int rowIndex) {
        df = new DataFormatter();
        try {
            row = sh.getRow(0);
            if (row == null) return "";

            int colNum = -1;

            for(Cell cell : row){
                if(cell.getStringCellValue().trim().equalsIgnoreCase(columnName.trim())){
                    colNum = cell.getColumnIndex();
                    break;
                }
            }
            if (colNum == -1){
                throw new RuntimeException("Không tìm thầy cột: " + columnName);
            }
            return getCellData(rowIndex,colNum);
        }catch (Exception e){
            return "";
        }

    }

    //set by column index
    public void setCellData(String text, int columnIndex, int rowIndex) {
        try {
            row = sh.getRow(rowIndex);
            if (row == null) {
                row = sh.createRow(rowIndex);
            }
            cell = row.getCell(columnIndex);

            if (cell == null) {
                cell = row.createCell(columnIndex);
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            style.setFillPattern(FillPatternType.NO_FILL);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fio = new FileOutputStream(excelFilePath);
            wb.write(fio);
            fio.flush();
            fio.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //set by column name
    public void setCellData(String text, String columnName, int rowIndex) {
        try {
            // 1. Lấy dòng header
            Row headerRow = sh.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found");
            }

            // 2. Tìm index của cột
            int colIndex = -1;
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().trim()
                        .equalsIgnoreCase(columnName.trim())) {
                    colIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (colIndex == -1) {
                throw new RuntimeException("Không tìm thấy cột: " + columnName);
            }
            Row row = sh.getRow(rowIndex);
            if (row == null) {
                row = sh.createRow(rowIndex);
            }

            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }

            cell.setCellValue(text);

            CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            cell.setCellStyle(style);

            if (text.equalsIgnoreCase("PASS")) {
                style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            } else if (text.equalsIgnoreCase("FAIL")) {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }

            FileOutputStream fos = new FileOutputStream(excelFilePath);
            wb.write(fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Đọc excel theo dong trên 1 sheet
    public Object[][] getExcelData(String filePath, String sheetName) {
        Object[][] data = null;
        Workbook workbook = null;
        try {
            // load the file
            FileInputStream fis = new FileInputStream(filePath);

            // load the workbook
            workbook = new XSSFWorkbook(fis);

            // load the sheet
            Sheet sh = workbook.getSheet(sheetName);

            // load the row
            Row row = sh.getRow(0);

            //
            int noOfRows = sh.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();

            System.out.println(noOfRows + " - " + noOfCols);

            Cell cell;
            data = new Object[noOfRows - 1][noOfCols];

            for (int i = 1; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols; j++) {
                    row = sh.getRow(i);
                    cell = row.getCell(j);

                    switch (cell.getCellType()) {
                        case STRING:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i - 1][j] = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BLANK:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                        default:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("The exception is:" + e.getMessage());
            throw new RuntimeException(e);
        }
        return data;
    }

    //Lấy dòng có lọc
    public Object[][] getExcelData(String filePath, String sheetName, String... testTypes) {
        Workbook workbook = null;
        List<Object[]> filteredData = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            Sheet sh = workbook.getSheet(sheetName);

            Row headerRow = sh.getRow(0);
            int noOfCols = headerRow.getLastCellNum();
            int noOfRows = sh.getPhysicalNumberOfRows();

            // Xác định cột TestType
            int testTypeColIndex = -1;
            for (int c = 0; c < noOfCols; c++) {
                if (headerRow.getCell(c).getStringCellValue().trim().equalsIgnoreCase("TestType")) {
                    testTypeColIndex = c;
                    break;
                }
            }

            if (testTypeColIndex == -1) {
                throw new RuntimeException("Không tìm thấy cột TestType trong Excel");
            }

            for (int i = 1; i < noOfRows; i++) {
                Row row = sh.getRow(i);
                if (row == null) continue;

                Cell testTypeCell = row.getCell(testTypeColIndex);
                String testType = testTypeCell.getStringCellValue().trim();

                // lọc theo TestType
                if (!Arrays.asList(testTypes).contains(testType)) {
                    continue;
                }

                Object[] rowData = new Object[noOfCols];

                for (int j = 0; j < noOfCols; j++) {
                    Cell cell = row.getCell(j);

                    if (cell == null) {
                        rowData[j] = "";
                        continue;
                    }

                    switch (cell.getCellType()) {
                        case STRING:
                            rowData[j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            rowData[j] = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BLANK:
                            rowData[j] = "";
                            break;
                        default:
                            rowData[j] = cell.toString();
                            break;
                    }
                }
                filteredData.add(rowData);
            }

        } catch (Exception e) {
            System.out.println("The exception is: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return filteredData.toArray(new Object[0][]);
    }
}
