package com.terry.archer.excel.cellstyle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelCellStyle {

    CellStyle createCellStyle(Workbook wb, Cell cell);
}
