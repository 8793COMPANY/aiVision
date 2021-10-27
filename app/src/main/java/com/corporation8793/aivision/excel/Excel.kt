package com.corporation8793.aivision.excel

import android.content.Context
import android.util.Log
import jxl.Sheet
import jxl.Workbook
import java.lang.StringBuilder

/**
 * 엑셀 파일(.xls)을 제어하는 클래스 입니다.
 * @author  두동근
 * @param   context 애플리케이션 콘텍스트.
 * @param   xlsFileNameWithExtension assets 폴더에 위치한 엑셀 파일. 확장자(xls) 포함.
 * @return
 * @see     jxl
 */
class Excel(context: Context, xlsFileNameWithExtension:String) {
    /**
     * 엑셀 파일의 [java.io.InputStream] 인스턴스
     */
    private val isis = context.resources.assets.open(xlsFileNameWithExtension)
    /**
     * [Workbook] 인스턴스
     * @see jxl.Workbook
     */
    private var wb : Workbook = Workbook.getWorkbook(isis)
    /**
     * [Sheet] 인스턴스
     * @see jxl.Sheet
     */
    private var sheet : Sheet = wb.getSheet(0)
    /**
     * [Sheet] 인스턴스의 전체 열 개수(columns)
     * @see jxl.Sheet.getColumns
     */
    private var colTotal = sheet.columns
    /**
     * [Sheet] 인스턴스의 전체 행 개수(rows)
     * @see jxl.Sheet.getRows
     */
    private var rowTotal = sheet.rows

    /**
     * 엑셀 시트를 String 타입 리스트로 반환합니다.
     * @author  두동근
     * @param   data 열. arrayOf("A", "B", "C" ... ).
     * @return  List<Array<String>>
     * @see     jxl
     */
    fun extractTotalSheet(data : Array<String>): List<Array<String>> {
        var list : MutableList<Array<String>> = mutableListOf()

        for (row in 1 until rowTotal) {
            var insertData : Array<String> = data.copyOf()

            for (col in data.indices) {
                if (sheet.getCell(col, row).contents != null) {
                    data[col] = sheet.getCell(col, row).contents
                    insertData[col] = data[col]
                    Log.i("Excel.kt", "extractTotalSheet: DATA(${data[col]}) in row : $row col : ${sheet.getCell(col, 0).contents}")
                } else {
                    data[col] = "NULL"
                    insertData[col] = data[col]
                    Log.e("Excel.kt", "extractTotalSheet: NULL in row : $row col : ${sheet.getCell(col, 0).contents}")
                }
            }

            list.add(insertData)
        }

        return list
    }

    /**
     * 엑셀 시트 전체를 Log.i 로 출력합니다.
     * @author  두동근
     * @param
     * @return
     * @see     jxl
     */
    fun printTotalSheet() {
        val stringBuilder = StringBuilder("")

        for (row in 1..rowTotal) {
            for (col in 0..colTotal) {
                if (sheet.getCell(col, row).contents != null) {
                    stringBuilder.append("${sheet.getCell(col, 0).contents} : ${sheet.getCell(col, row).contents} ")
                } else {
                    stringBuilder.append("${sheet.getCell(col, 0).contents} : NULL")
                }
            }
            Log.i("Excel.kt", "printTotalSheet: ${row}row : [ $stringBuilder ]")
        }
    }

    /**
     * 엑셀 시트의 특정 Row(행)을 String 타입 어레이로 반환합니다.
     * @author  두동근
     * @param   row 특정 Row(행).
     * @param   data 열. arrayOf("A", "B", "C" ... ).
     * @return  Array<String>
     * @see     jxl
     */
    fun extractRow(row : Int, data : Array<String>): Array<String> {
        for (col in data.indices) {
            if (sheet.getCell(col, row).contents != null) {
                data[col] = sheet.getCell(col, row).contents
                Log.i("Excel.kt", "extractRow: DATA(${data[col]}) in row : $row col : ${sheet.getCell(col, 0).contents}")
            } else {
                data[col] = "NULL"
                Log.e("Excel.kt", "extractRow: NULL in row : $row col : ${sheet.getCell(col, 0).contents}")
            }
        }
        return data
    }

    /**
     * 엑셀 시트의 특정 Row(행)을 Log.i 로 출력합니다.
     * @author  두동근
     * @param   row 특정 Row(행).
     * @return
     * @see     jxl
     */
    fun printRow(row : Int) {
        for (col in 0..colTotal) {
            if (sheet.getCell(col, row).contents != null) {
                Log.i("Excel.kt", "printRow: DATA(${sheet.getCell(col, row).contents}) in row : $row col : ${sheet.getCell(col, 0).contents}")
            } else {
                Log.e("Excel.kt", "printRow: NULL in row : $row col : ${sheet.getCell(col, 0).contents}")
            }
        }
    }
}