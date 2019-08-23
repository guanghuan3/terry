package com.terry.archer.excel;

import com.terry.archer.excel.annotation.ExcelField;
import com.terry.archer.excel.annotation.ExcelFields;
import com.terry.archer.excel.cellstyle.ExcelCellStyle;
import com.terry.archer.excel.cellstyle.impl.DefaultCellStyle;
import com.terry.archer.excel.enums.AlignType;
import com.terry.archer.excel.enums.ExcelAction;
import com.terry.archer.excel.enums.ExcelFileType;
import com.terry.archer.utils.ApplicationContextUtil;
import com.terry.archer.utils.CommonUtil;
import com.terry.archer.utils.DateUtil;
import com.terry.archer.utils.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
public class ExcelExportor {

    private static Logger logger = LoggerFactory.getLogger(ExcelExportor.class);

    private static final String EXCEL_EXT_XLSX = ".xlsx";

    public static final Integer DEFAULT_PER_PAGE_SIZE = 5000;

    /**
     * 导出的文件名称
     */
    private String fileName;

    /**
     * 每个sheet的记录数
     */
    private Integer sizePerPage;

    private Workbook workbook;

    public ExcelExportor() {
        this(DateUtil.getFormattedTime(new Date()), DEFAULT_PER_PAGE_SIZE);
    }

    public ExcelExportor(String fileName) {
        this(fileName, DEFAULT_PER_PAGE_SIZE);
    }

    public ExcelExportor(String fileName, Integer sizePerPage) {
        this.fileName = fileName;
        this.sizePerPage = CommonUtil.isNotEmpty(sizePerPage) && sizePerPage > 0 ? sizePerPage : DEFAULT_PER_PAGE_SIZE;

        this.workbook = createWorkbook(fileName);
    }

    private Workbook createWorkbook(String fileName) {
        if (CommonUtil.isNotEmpty(fileName)) {
            String ext = fileName.substring(fileName.lastIndexOf("."));
            return createWorkbook(EXCEL_EXT_XLSX.equals(ext) ? ExcelFileType.XLSX : ExcelFileType.XLS);
        } else {
            // 默认生成HSSFWorkBook
            return new HSSFWorkbook();
        }
    }

    private Workbook createWorkbook(ExcelFileType excelFileTypeEnum) {
        if (CommonUtil.isNotEmpty(excelFileTypeEnum)
                && ExcelFileType.XLSX.equals(excelFileTypeEnum)) {
            return new XSSFWorkbook();
        } else {
            return new HSSFWorkbook();
        }
    }

    /**
     * 输出文件到指定目录对象
     * @param datas
     * @param clazz
     * @param os
     * @throws Exception
     */
    public void exportExcel(List<?> datas, Class<?> clazz, OutputStream os) throws Exception {
        if (CommonUtil.isNotEmpty(datas)
                && CommonUtil.isNotEmpty(clazz)
                && CommonUtil.isNotEmpty(os)) {

            // 根据导出数据的model获取导出数据的字段信息列表
            List<ExcelField> excelFieldList = handleExcelFieldList(clazz);

            if (CommonUtil.isNotEmpty(excelFieldList)) {
                // 将数据按照excelFieldList进行处理
                handleExcelDatas(datas, excelFieldList, clazz);

                // 输出excel
                writeExcel(os);
            }

        } else {
            // 入参为空，日志提示
            logger.info("执行：ExcelExportSupport.exportExcel(List, Class)未成功！入参为空");
        }
    }

    /**
     * 处理excel导出的model类，获取需要处理的字段集合
     * @param clazz
     * @return
     */
    private static List<ExcelField> handleExcelFieldList(Class<?> clazz) {
        List<ExcelField> excelFields = null;
        if (CommonUtil.isNotEmpty(clazz)) {
            // 类的excel集合注解
            ExcelFields efs = clazz.getDeclaredAnnotation(ExcelFields.class);
            // 解析excel集合注解
            if (CommonUtil.isNotEmpty(efs)) {
                excelFields = new ArrayList<>();
                // 数组转化成集合，方便之后的操作
                ExcelField[] fileds = efs.fields();
                for (ExcelField ef : fileds) {
                    // 不止只支持导入操作
                    if (!ef.action().equals(ExcelAction.IMPORT)) {
                        excelFields.add(ef);
                    }
                }

            } else {
                // 获取所有声明的字段
                Field[] fields = clazz.getDeclaredFields();
                // 搜集field的注解
                excelFields = new ArrayList<>();
                ExcelField ef = null;
                for (Field f : fields) {
                    ef = f.getDeclaredAnnotation(ExcelField.class);
                    // 不为空且不是只支持导入操作
                    if (CommonUtil.isNotEmpty(ef)
                            && !ef.action().equals(ExcelAction.IMPORT)) {
                        excelFields.add(f.getAnnotation(ExcelField.class));
                    }
                }
            }

            // 将字段excel注解排序后返回
            if (CommonUtil.isNotEmpty(excelFields)) {
                Collections.sort(excelFields, (Object tar1, Object tar2) -> ((ExcelField) tar1).sort() - ((ExcelField) tar2).sort());
            }
        }

        return excelFields;
    }

    private void handleExcelDatas(List<?> datas, List<ExcelField> excelFieldList, Class clazz) throws Exception {

        Row row = null;
        Cell cell = null;
        // 临时i存放字段属性获取方法
        Method m = null;
        // 临时存放字段值
        Object value = null;
        // 临时存放数据对象
        Object data = null;
        // 临时存放ExcelField对象
        ExcelField anno = null;
        // 分页的临时数据集合
        List<?> subDatas = null;

        // 根据记录数和每页sheet显示数得出sheet页总数
        int page = CommonUtil.isNotEmpty(datas)
                // 通过总数与每页显示数取模判断。1、多出数据不满一页：总数/每页显示数+1；2、刚好满页：总数/每页显示数
                ? (datas.size() % sizePerPage > 0) ? datas.size() / sizePerPage + 1 : datas.size() / sizePerPage
                // 数据为空则默认1页
                : 1;
        for (int k = 0; k < page; k ++) {
            // 将数据按照每页显示书分割
            subDatas = datas.subList(k*sizePerPage, ((k+1)*sizePerPage) > datas.size() ? datas.size() : (k+1)*sizePerPage);
            // 按照页数生成Sheet
            Sheet sheet = workbook.createSheet("Sheet_"+String.valueOf(k));

            // 表头格式
            CellStyle headStyle = workbook.createCellStyle();
            headStyle.setAlignment(HorizontalAlignment.CENTER);
            // 输出表头，第一行
            Row head = sheet.createRow(0);
            for (int i = 0; i < excelFieldList.size(); i++) {
                cell = head.createCell(i);
                // 输出表头文字
                cell.setCellValue(excelFieldList.get(i).label());
                cell.setCellStyle(headStyle);
            }

            // 创建行数据，从excel第二行开始
            for (int i = 1; i <= subDatas.size(); i++) {
                data = subDatas.get(i - 1);
                if (!data.getClass().equals(clazz)) {
                    logger.info("导出数据对象不匹配：[{}]不能转换为[{}]", new Object[]{data.getClass(), clazz});
                    continue;
                }
                row = sheet.createRow(i);

                // 创建行内字段数据
                for (int j = 0; j < excelFieldList.size(); j++) {
                    anno = excelFieldList.get(j);

                    // 字段原始值
                    value = clazz.getMethod(
                            StringUtil.getMethodByAttr(
                                    anno.attr())).invoke(data);

                    // 整理输出格式
                    value = CommonUtil.isNotEmpty(ApplicationContextUtil.getBean(anno.format()))
                            ? ApplicationContextUtil.getBean(anno.format()).format(value, excelFieldList.get(j).pattern())
//                            : anno.format().newInstance().format(value, excelFieldList.get(j).pattern());
                            : "";

                    // 渲染单元格
                    cell = row.createCell(j);
                    renderCell(cell, anno, value);
                }
            }
        }
    }

    private void renderCell(Cell cell, ExcelField anno, Object value) {
        if (CommonUtil.isNotEmpty(value)) {
            CellStyle cs = null;
            // 如果是默认的无样式单元格
            if (DefaultCellStyle.class.equals(anno.cellStyle())) {
                // 如果不是默认的水平靠左，则重新创建
                if (!AlignType.LEFT.equals(anno.align())) {
                    // 设置单元格水平位置
                    cs = workbook.createCellStyle();
                    createAlignCellStyle(anno.align(), cs);
                }
            }
            // 获取自定义样式，输出自定义渲染效果
            else {
                ExcelCellStyle ecs = ApplicationContextUtil.getBean(anno.cellStyle());
                if (CommonUtil.isNotEmpty(ecs)) {
                    cs = ecs.createCellStyle(workbook, cell);
                }
            }

            // 样式存在时设置变更的渲染样式
            if (CommonUtil.isNotEmpty(cs)) {
                cell.setCellStyle(cs);
            }

            // 设置单元格内容
            cell.setCellValue(String.valueOf(value));
        }
    }

    private void createAlignCellStyle(AlignType type, CellStyle cs) {
        switch (type) {
            case CENTER:
                cs.setAlignment(HorizontalAlignment.CENTER);
                break;
            case RIGHT:
                cs.setAlignment(HorizontalAlignment.RIGHT);
                break;
            default:
                cs.setAlignment(HorizontalAlignment.LEFT);
        }
    }

    private void writeExcel(OutputStream os) {
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("执行导出excel方法失败：{}", new Object[]{e.toString()});
        } finally {
            if (CommonUtil.isNotEmpty(os)) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (CommonUtil.isNotEmpty(workbook)) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
