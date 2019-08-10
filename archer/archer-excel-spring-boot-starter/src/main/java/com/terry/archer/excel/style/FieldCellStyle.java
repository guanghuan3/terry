package com.terry.archer.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by Administrator
 * on 2019/8/5.
 */
public interface FieldCellStyle {

    CellStyle cellStyle(Workbook wb, int row, int column);

}
