package com.terry.archer.excel;

import com.terry.archer.excel.annotation.ExcelField;
import com.terry.archer.excel.annotation.ExcelFields;
import com.terry.archer.excel.format.FieldFormat;
import com.terry.archer.excel.format.UserAnno;
import com.terry.archer.utils.ApplicationContextUtil;
import com.terry.archer.utils.CommonUtil;
import com.terry.archer.utils.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
public class ExcelExportSupport {

    private static Logger logger = LoggerFactory.getLogger(ExcelExportSupport.class);

    public static void main(String[] args) {
        /*List<UserAnno> la = new ArrayList<>();
        UserAnno ua = new UserAnno("zhangsan", 15, new Date());
        la.add(ua);

        ua = new UserAnno("lisi", 17, new Date());
        la.add(ua);

        handleExcelFieldList(UserAnno.class);*/

        UserAnno ua = new UserAnno();
        System.err.println(ua.getClass().equals(UserAnno.class));
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
            ExcelFields efs = clazz.getAnnotation(ExcelFields.class);
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

        // 创建行数据
        for (int i = 0; i < datas.size(); i ++) {
            if (!datas.get(i).getClass().equals(clazz)) {
                logger.info("导出数据对象不匹配：[{}]不能转换为[{}]", new Object[]{datas.get(i).getClass(), clazz});
                continue;
            }
            row = sheet.createRow(i);

            // 创建行内字段数据
            for (int j = 0; j < excelFieldList.size(); j ++) {
                // 字段原始值
                value =  clazz.getMethod(
                        StringUtil.getMethodByAttr(
                                excelFieldList.get(j).attr())).invoke(datas.get(i));

                // 整理输出格式
                value = ((FieldFormat) ApplicationContextUtil.getBean(excelFieldList.get(j).format()))
                        .format(value, excelFieldList.get(j).pattern());

                // 为行内字段设置值
                cell = row.createCell(j);
            }
        }
    }

    private static void writeExcel(Workbook wb, OutputStream os) {
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("执行导出excel方法失败：{}", new Object[]{e.toString()});
        }
    }
}
