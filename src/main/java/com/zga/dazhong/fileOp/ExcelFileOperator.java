package com.zga.dazhong.fileOp;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhangguangan on 2020/3/27
 * description: excel转化为sql
 */
@Log4j2
public class ExcelFileOperator {
    private static final int DATA_TYPE_STRING = 0;
    private static final int DATA_TYPE_INTEGER = 1;
    private static final int DATA_TYPE_DATE = 2;
    private static final int DATA_TYPE_DECIMAL = 3;
    private final static int SQL_SIZE = 10000;
    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");


    // 规则文件路径
    private static final String RULE_FILE_PATH = "D:/需求/tidb数据切换/rule.txt";
    // excel文件路径
    private final static String EXCEL_FILE_PATH = "D:/需求/tidb数据切换/Fwd__申请导出pgxl数据/bbb.xlsx";
    // 输出sql文件定义
    private final static String SQL_OUTPUT_FILE_PATH = "D:/需求/tidb数据切换/sql输出/outSqlFile";


    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();

        // 执行处理
        ExcelFileOperator excelFileOperator = new ExcelFileOperator();
        excelFileOperator.execute(RULE_FILE_PATH, EXCEL_FILE_PATH, SQL_OUTPUT_FILE_PATH);
//        excelFileOperator.execute(System.getProperty("ruleFilePath"), System.getProperty("excelFilePath"), System.getProperty("sqlOutputFilePath"));

        log.info("excel转化sql完成，耗时：{}", System.currentTimeMillis() - beginTime);
    }

    /**
     * 执行sql转换入口
     *
     * @param ruleFilePath      规则文件路径
     * @param excelFilePath     excel文件路径
     * @param SqlOutPutFilePath 转化后的sql路径
     */
    private void execute(String ruleFilePath, String excelFilePath, String SqlOutPutFilePath) {

        // 1、获取规则
        String ruleString = getRuleString(ruleFilePath);
        JSONObject ruleJson = JSONObject.parseObject(ruleString);
        Map<Integer, RuleModel> ruleMap = getRule(ruleJson.getJSONObject("rule"));
        String baseSql = getSqlBaseStatement(ruleJson);

        // 2、将excel行转为sql并输出到文件
        excel2Sql(excelFilePath, SqlOutPutFilePath, baseSql, ruleMap);
    }


    /**
     * excel转化成sql
     *
     * @param excelFilePath
     * @param sqlOutputPath
     * @param baseSql
     * @param ruleMap
     */
    public void excel2Sql(String excelFilePath, String sqlOutputPath, String baseSql, Map<Integer, RuleModel> ruleMap) {
        // 1、获取workbook
        Workbook wb = getExcel(excelFilePath);

        // 2、读取workbook
        if (wb == null) {
            log.error("文件读入出错");
            throw new RuntimeException("文件读入出错");
        }
        analyzeExcel(wb, sqlOutputPath, baseSql, ruleMap);
    }


    /**
     * 解析工作空间
     *
     * @param wb
     * @param sqlOutputPath
     * @param baseSql
     * @param ruleMap
     */
    private void analyzeExcel(Workbook wb, String sqlOutputPath, String baseSql, Map<Integer, RuleModel> ruleMap) {
        //读取sheet(从0计数)
        Sheet sheet = wb.getSheetAt(0);
        //读取行数(从0计数)
        int rowNum = sheet.getLastRowNum();

        StringBuffer sql = new StringBuffer(baseSql);

        // 读取excel 舍弃第一行
        for (int i = 1; i <= rowNum; i++) {
            try {
                //获得行
                Row row = sheet.getRow(i);
                String result = handleRow(row, ruleMap);
                sql.append(result);

                // 满足最大行数 输出到文件
                if (i % SQL_SIZE == 0 || i == rowNum) {
                    String sqlString = sql.toString().substring(0, sql.length() - 2) + ";";
                    String outputPath = sqlOutputPath + i + ".txt";
                    handleSqlOutPut(outputPath, sqlString);

                    // 重新构建sql
                    sql = new StringBuffer(baseSql);
                }
            } catch (Exception e) {
                log.error("读取行遇到异常，异常行：{}", i);
            }
        }
    }


    /**
     * 处理excel的行
     *
     * @param row
     * @param ruleMap
     * @return
     */
    private String handleRow(Row row, Map<Integer, RuleModel> ruleMap) {
        StringBuffer sql = new StringBuffer("(");

        for (int excelColumnNo : ruleMap.keySet()) {
            //获取单元格
            Cell cell = row.getCell(excelColumnNo);
            // 空单元格处理
            if (cell == null || ruleMap.get(excelColumnNo) == null) {
                sql.append("NULL");
            } else {
                // 字段类型处理
                XSSFCell xssfCell = (XSSFCell) cell;
                String rawValue = xssfCell.getRawValue();
                if (StringUtils.isEmpty(rawValue)) {
                    sql.append("NULL");
                } else if (ruleMap.get(excelColumnNo).getFieldDataType() == DATA_TYPE_STRING) {
                    sql.append("'").append(cell.getStringCellValue()).append("'");
                } else if (ruleMap.get(excelColumnNo).getFieldDataType() == DATA_TYPE_INTEGER || ruleMap.get(excelColumnNo).getFieldDataType() == DATA_TYPE_DECIMAL) {
                    sql.append(rawValue);
                } else if (ruleMap.get(excelColumnNo).getFieldDataType() == DATA_TYPE_DATE) {
                    sql.append("'").append(SDF.format(cell.getDateCellValue())).append("'");

                }
            }


            if (excelColumnNo != ruleMap.size() - 1) {
                sql.append(",");
            }
        }
        sql.append("),\n");
        return sql.toString();
    }

    /**
     * 初始化规则映射
     *
     * @param rule 规则
     * @return
     */
    public Map<Integer, RuleModel> getRule(JSONObject rule) {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        Map<Integer, RuleModel> ruleMap = new TreeMap<>(comparator);
        for (String key : rule.keySet()) {
            RuleModel ruleModel = JSONObject.toJavaObject(rule.getJSONObject(key), RuleModel.class);
            ruleMap.put(Integer.parseInt(key), ruleModel);
        }

        return ruleMap;
    }

    /**
     * 获取基础insert语句
     *
     * @param ruleJson
     * @return
     */
    private String getSqlBaseStatement(JSONObject ruleJson) {
        StringBuffer baseSql = new StringBuffer("insert into ");

        Map<Integer, RuleModel> ruleMap = getRule(ruleJson.getJSONObject("rule"));

        baseSql.append(ruleJson.getString("tableName")).append(" (");
        for (int i = 0; i < ruleMap.size(); i++) {
            baseSql.append(ruleMap.get(i).getFieldName());
            if (i != ruleMap.size() - 1) {
                baseSql.append(", ");
            } else {
                baseSql.append(")values ");
            }
        }
        return baseSql.toString();
    }

    /**
     * 获取规则文件内容
     *
     * @param ruleFilePath
     * @return
     */
    public String getRuleString(String ruleFilePath) {
        File ruleFile = new File(ruleFilePath);
        if (!ruleFile.exists()) {
            throw new RuntimeException("规则文件不存在");
        }

        BufferedReader reader = null;
        StringBuffer ruleString = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(ruleFile));
            String lineTxt = "";
            while ((lineTxt = reader.readLine()) != null) {
                ruleString.append(lineTxt);
            }
        } catch (Exception e) {
            log.error("读取授权文件遇到异常，异常信息：", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error("关闭授权文件输入流遇到异常，异常信息：", e);
                }
            }
        }
        return ruleString.toString();
    }

    /**
     * sql输出到文件
     *
     * @param filePath
     * @param sql
     */
    private void handleSqlOutPut(String filePath, String sql) {
        FileWriter fw = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getPath(), true);
            fw.write(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取工作空间
     *
     * @param excelFilePath
     * @return
     */
    private Workbook getExcel(String excelFilePath) {
        Workbook wb = null;
        File file = new File(excelFilePath);
        if (!file.exists()) {
            log.error("excel文件不存在");
            throw new RuntimeException("excel文件读入出错");
        } else {
            String fileType = excelFilePath.substring(excelFilePath.lastIndexOf("."));//获得后缀名
            try {
                InputStream is = new FileInputStream(excelFilePath);
                if (".xls".equals(fileType)) {
                    wb = new HSSFWorkbook(is);
                } else if (".xlsx".equals(fileType)) {
                    wb = new XSSFWorkbook(is);
                } else {
                    log.error("excel格式不正确");
                    throw new RuntimeException("excel格式不正确");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wb;
    }
}
