package team.aliens.dms.thirdparty.parser.port

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import team.aliens.dms.domain.file.FileExtension.XLS
import team.aliens.dms.domain.file.FileExtension.XLSX
import team.aliens.dms.thirdparty.parser.exception.BadExcelFormatException
import team.aliens.dms.thirdparty.parser.exception.ExcelExtensionMismatchException
import team.aliens.dms.thirdparty.parser.exception.ExcelInvalidFileException
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.function.Function

abstract class ExcelPort {
    fun <T> getExcelInfo(worksheet: Sheet, function: Function<Row, T>): List<T?> {

        val invalidRowIdxes = mutableListOf<Int>()

        val results = (1..worksheet.lastRowNum).map { i ->
            val row = worksheet.getRow(i)
            if (row.isFirstCellBlank()) return@map null

            try {
                function.apply(row)
            } catch (e: Exception) {
                invalidRowIdxes.add(i + 1) // poi idx 0부터 시작, 엑셀 행은 1부터 시작
                null
            }
        }

        if (invalidRowIdxes.isNotEmpty()) {
            throw BadExcelFormatException(invalidRowIdxes = invalidRowIdxes)
        }

        return results
    }

    fun Row.isFirstCellBlank() = cellIterator().next().cellType == Cell.CELL_TYPE_BLANK

    fun Row.getStringValue(idx: Int) = getCell(idx, RETURN_BLANK_AS_NULL).stringCellValue

    fun Row.getIntValue(idx: Int) = getCell(idx, RETURN_BLANK_AS_NULL).numericCellValue.toInt()

    fun transferToExcel(file: File): Workbook {
        val inputStream = file.inputStream()

        return inputStream.use {
            runCatching {
                when (file.extension.lowercase()) {
                    XLS -> HSSFWorkbook(inputStream)
                    XLSX -> XSSFWorkbook(inputStream)
                    else -> throw ExcelExtensionMismatchException
                }
            }.also {
                file.delete()
            }.onFailure {
                it.printStackTrace()
                throw ExcelInvalidFileException
            }.getOrThrow()
        }
    }

    fun createExcelSheet(
        attributes: List<String>,
        dataList: List<List<String?>>
    ): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet()

        val headerRow = sheet.createRow(0)
        insertDataAtRow(headerRow, attributes, getHeaderCellStyle(workbook))

        dataList.forEachIndexed { idx, data ->
            val row = sheet.createRow(idx + 1)
            insertDataAtRow(row, data, getDefaultCellStyle(workbook))
        }
        formatWorkSheet(sheet)

        ByteArrayOutputStream().use { stream ->
            workbook.write(stream)
            return stream.toByteArray()
        }
    }

    fun createGroupExcelSheet(
        attributes: List<String>,
        dataListSet: List<List<List<String?>>>,
        colors: List<IndexedColors> = listOf(IndexedColors.AUTOMATIC)
    ): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet()

        val headerRow = sheet.createRow(0)
        insertDataAtRow(headerRow, attributes, getHeaderCellStyle(workbook))

        var idx = 1

        dataListSet.forEachIndexed { setIdx, dataList -> // 각 그룹마다 지정된 색을 받음.
            val color = colors[setIdx % colors.size]

            dataList.forEach { data ->
                val row = sheet.createRow(idx++)
                insertDataAtRow(
                    row = row,
                    data = data,
                    style = getDefaultCellStyle(workbook),
                    color = color
                )
            }
        }
        formatOutingWorkSheet(sheet)

        ByteArrayOutputStream().use { stream ->
            workbook.write(stream)
            return stream.toByteArray()
        }
    }

    fun formatOutingWorkSheet(
        worksheet: Sheet,
    ) {
        val lastCellNum = worksheet.getRow(0).lastCellNum.toInt()
        worksheet.apply {
            // 정렬 필터 적용
            setAutoFilter(CellRangeAddress(0, 0, 0, lastCellNum - 1))
            createFreezePane(0, 1)
            // 데이터에 맞춰 폭 조정
            (0 until lastCellNum)
                .map {
                    autoSizeColumn(it)
                    val width = getColumnWidth(it)
                    setColumnWidth(it, ((width * 1.3) - 50).toInt())
                }
        }
    }

    fun formatWorkSheet(
        worksheet: Sheet,
    ) {
        val lastCellNum = worksheet.getRow(0).lastCellNum.toInt()
        worksheet.apply {
            // 정렬 필터 적용
            setAutoFilter(CellRangeAddress(0, 0, 0, lastCellNum - 1))
            createFreezePane(0, 1)
            // 데이터에 맞춰 폭 조정
            (0 until lastCellNum)
                .map {
                    autoSizeColumn(it)
                    val width = getColumnWidth(it)
                    setColumnWidth(it, width + 900)
                }
        }
    }

    fun insertDataAtRow(
        row: Row,
        data: List<String?>,
        style: CellStyle,
        color: IndexedColors = IndexedColors.WHITE,
        startIdx: Int = 0
    ) {
        style.fillPattern = CellStyle.SOLID_FOREGROUND
        if (style.fillForegroundColor == IndexedColors.AUTOMATIC.index) style.fillForegroundColor = color.index

        style.setBorder()

        data.forEachIndexed { i, item ->
            val cell = row.createCell(i + startIdx)
            item?.toDoubleOrNull()?.let {
                cell.setCellValue(it)
            } ?: cell.setCellValue(item)
            cell.cellStyle = style
        }
    }

    fun getHeaderCellStyle(workbook: Workbook): CellStyle {
        val borderStyle = CellStyle.BORDER_THIN
        val borderColor = IndexedColors.BLACK.index

        return workbook.createCellStyle()
            .apply {
                fillForegroundColor = IndexedColors.YELLOW.index
                fillPattern = CellStyle.SOLID_FOREGROUND
                alignment = HorizontalAlignment.LEFT.ordinal.toShort()
                verticalAlignment = VerticalAlignment.CENTER.ordinal.toShort()
                borderLeft = borderStyle
                borderTop = borderStyle
                borderRight = borderStyle
                borderBottom = borderStyle
                leftBorderColor = borderColor
                topBorderColor = borderColor
                rightBorderColor = borderColor
                bottomBorderColor = borderColor
            }
    }

    fun getDefaultCellStyle(workbook: Workbook): CellStyle =
        workbook.createCellStyle()
            .apply {
                alignment = HorizontalAlignment.LEFT.ordinal.toShort()
                verticalAlignment = VerticalAlignment.CENTER.ordinal.toShort()
            }

    fun CellStyle.setBorder() {
        val borderStyle = CellStyle.BORDER_THIN
        val borderColor = IndexedColors.BLACK.index

        borderLeft = borderStyle
        borderTop = borderStyle
        borderRight = borderStyle
        borderBottom = borderStyle
        leftBorderColor = borderColor
        topBorderColor = borderColor
        rightBorderColor = borderColor
        bottomBorderColor = borderColor
    }
}
