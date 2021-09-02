package com.corporation8793.aivision.excel

import android.content.Context
import android.util.Log
import jxl.Sheet
import jxl.Workbook

class Excel(context: Context, xlsFileNameWithExtension:String) {
    private val isis = context.resources.assets.open(xlsFileNameWithExtension)
    var wb : Workbook = Workbook.getWorkbook(isis)
    var sheet : Sheet = wb.getSheet(0)
    var colTotal = sheet.columns
    var row = 0

    fun extractRow(row : Int, data : Array<String>): Array<String> {
        for (col in data.indices) {
            if (sheet.getCell(col, row).contents != null) {
                data[col] = sheet.getCell(col, row).contents
                Log.i("Excel.kt", "extractRow: DATA(${data[col]}) in row : $row col : $col")
            } else {
                data[col] = "NO DATA"
                Log.e("Excel.kt", "extractRow: NO DATA in row : $row col : $col")
            }
        }
        return data
    }

    fun printRow(row : Int) {
        for (col in 0..colTotal) {
            if (sheet.getCell(col, row).contents != null) {
                Log.i("Excel.kt", "printLine: DATA(${sheet.getCell(col, row).contents}) in row : $row col : $col")
            } else {
                Log.e("Excel.kt", "printLine: NO DATA in row : $row col : $col")
            }
        }
    }
}