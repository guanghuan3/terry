package com.terry.archer.excel.cellstyle.impl;

import com.terry.archer.excel.cellstyle.ExcelCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class DefaultCellStyle implements ExcelCellStyle{
    @Override
    public CellStyle createCellStyle(Workbook wb, Cell cell) {
        return null;
    }
}
