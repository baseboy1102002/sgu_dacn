package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.StudentInfoDto;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class EventStudentsExcelExporter {

    private List<StudentInfoDto> studentInfos;
    private String eventName;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public EventStudentsExcelExporter(List<StudentInfoDto> studentInfos, String eventName) {
        this.studentInfos = studentInfos;
        this.eventName = eventName;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet(eventName);
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "STT", style);
        createCell(row, 1, "Họ tên sinh viên", style);
        createCell(row, 2, "Mã số sinh viên", style);
        createCell(row, 3, "Điểm diện", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        int stt = 1;
        for (StudentInfoDto record: this.studentInfos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, stt++, style);
            createCell(row, columnCount++, record.getStudentInfoUserFullName(), style);
            createCell(row, columnCount++, record.getStudentInfoStudentCode(), style);
            createCell(row, columnCount++, record.getCheckAttended() ? "X":"", style);
        }
    }

    public void exportExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
