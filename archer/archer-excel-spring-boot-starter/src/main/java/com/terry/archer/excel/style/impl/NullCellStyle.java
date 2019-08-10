package com.terry.archer.excel.style.impl;

import com.terry.archer.excel.style.FieldCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by Administrator
 * 返回单元格空样式，不做任何处理
 * on 2019/8/5.
 */
public class NullCellStyle implements FieldCellStyle {
    @Override
    public CellStyle cellStyle(Workbook wb, int row, int column) {
        return null;
    }
}
