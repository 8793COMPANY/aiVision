package com.corporation8793.aivision.excel

import android.content.Context
import android.util.Log
import jxl.Sheet
import jxl.Workbook
import java.lang.StringBuilder

class Excel(context: Context, xlsFileNameWithExtension:String) {
    private val isis = context.resources.assets.open(xlsFileNameWithExtension)
    private var wb : Workbook = Workbook.getWorkbook(isis)
    private var sheet : Sheet = wb.getSheet(0)
    private var colTotal = sheet.columns
    private var rowTotal = sheet.rows

    fun extractTotalSheet(data : Array<String>): Array<String> {
        for (row in 1..rowTotal) {
            for (col in data.indices) {
                if (sheet.getCell(col, row).contents != null) {
                    data[col] = sheet.getCell(col, row).contents
                    Log.i("Excel.kt", "extractTotalSheet: DATA(${data[col]}) in row : $row col : ${sheet.getCell(col, 0).contents}")
                } else {
                    data[col] = "NULL"
                    Log.e("Excel.kt", "extractTotalSheet: NULL in row : $row col : ${sheet.getCell(col, 0).contents}")
                }
            }
        }
        return data
    }

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