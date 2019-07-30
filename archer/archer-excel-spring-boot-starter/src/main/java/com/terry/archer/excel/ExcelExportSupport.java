package com.terry.archer.excel;

import com.terry.archer.excel.annotation.ExcelField;
import com.terry.archer.excel.annotation.ExcelFields;
import com.terry.archer.excel.enums.ExcelFileType;
import com.terry.archer.excel.format.FieldFormat;
import com.terry.archer.utils.ApplicationContextUtil;
import com.terry.archer.utils.CommonUtil;
import com.terry.archer.utils.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
public class ExcelExportSupport {

    private static Logger logger = LoggerFactory.getLogger(ExcelExportSupport.class);

    private static final String EXCEL_EXT_XLSX = ".xlsx";

    /*public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExcelConfiguration.class);

        List<UserAnno> la = new ArrayList<>();
        UserAnno ua = new UserAnno("zhangsan", 15, new Date());
        la.add(ua);

        ua = new UserAnno("lisi", 17, new Date());
        la.add(ua);

        exportExcel(la, UserAnno.class, new File("D:/zhangwenbin/excel/test.xlsx"));
    }*/

    private static Workbook createWorkbook(String fileName) {
        if (CommonUtil.isNotEmpty(fileName)) {
            String ext = fileName.substring(fileName.lastIndexOf("."));
            return createWorkbook(EXCEL_EXT_XLSX.equals(ext) ? ExcelFileType.XLSX : ExcelFileType.XLS);
        } else {
            // 默认生成HSSFWorkBook
            return new HSSFWorkbook();
        }
    }

    private static Workbook createWorkbook(ExcelFileType excelFileTypeEnum) {
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
     * @param file
     * @throws Exception
     */
    public static void exportExcel(List<?> datas, Class<?> clazz, File file) throws Exception {
        if (!CommonUtil.isNotEmpty(file)) {
            return ;
        }
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        FileOutputStream f = new FileOutputStream(file);
        exportExcel(datas, clazz, createWorkbook(file.getName()), new FileOutputStream(file));
    }

    public static void exportExcel(List<?> datas, Class<?> clazz, ExcelFileType excelType, OutputStream os) throws Exception {
        exportExcel(datas, clazz, createWorkbook(excelType), os);
    }

    public static void exportExcel(List<?> datas, Class<?> clazz, Workbook wb, OutputStream os) throws Exception {
        if (CommonUtil.isNotEmpty(datas)
                && CommonUtil.isNotEmpty(clazz)
                && CommonUtil.isNotEmpty(wb)
                && CommonUtil.isNotEmpty(os)) {

            // 根据导出数据的model获取导出数据的字段信息列表
            List<ExcelField> excelFieldList = handleExcelFieldList(clazz);

            if (CommonUtil.isNotEmpty(excelFieldList)) {
                // 将数据按照excelFieldList进行处理
                handleExcelDatas(datas, excelFieldList, clazz, wb);

                // 输出excel
                writeExcel(wb, os);
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
                // 数组转化成集合，方便之后的操作
                excelFields = Arrays.asList(efs.fields());

            } else {
                // 获取所有声明的字段
                Field[] fields = clazz.getDeclaredFields();
                // 搜集field的注解
                excelFields = new ArrayList<>();
                for (Field f : fields) {
                    if (CommonUtil.isNotEmpty(f.getDeclaredAnnotation(ExcelField.class))) {
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

    private static void handleExcelDatas(List<?> datas, List<ExcelField> excelFieldList, Class clazz, Workbook wb) throws Exception {
        Sheet sheet = wb.createSheet();

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

        // 输出表头，第一行
        Row head = sheet.createRow(0);
        for (int i = 0; i < excelFieldList.size(); i ++) {
            cell = head.createCell(i);
            // 输出表头文字
            cell.setCellValue(excelFieldList.get(i).label());
        }

        // 创建行数据，从excel第二行开始
        for (int i = 1; i <= datas.size(); i ++) {
            data = datas.get(i - 1);
            if (!data.getClass().equals(clazz)) {
                logger.info("导出数据对象不匹配：[{}]不能转换为[{}]", new Object[]{data.getClass(), clazz});
                continue;
            }
            row = sheet.createRow(i);

            // 创建行内字段数据
            for (int j = 0; j < excelFieldList.size(); j ++) {
                anno = excelFieldList.get(j);

                // 字段原始值
                value =  clazz.getMethod(
                        StringUtil.getMethodByAttr(
                                anno.attr())).invoke(data);

                // 整理输出格式
                value = ((FieldFormat) ApplicationContextUtil.getBean(anno.format()))
                        .format(value, excelFieldList.get(j).pattern());

                // 设置excel展示内容
                row.createCell(j).setCellValue(String.valueOf(value));
            }
        }
    }

    private static void writeExcel(Workbook wb, OutputStream os) {
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("执行导出excel方法失败：{}", new Object[]{e.toString()});
        } finally {
            if (CommonUtil.isNotEmpty(wb)) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
