package com.quaza.solutions.qpalx.elearning.web.service.utils.excel;

import com.quaza.solutions.qpalx.elearning.domain.subscription.PrepaidSubscriptionStatistic;
import com.quaza.solutions.qpalx.elearning.domain.subscription.SubscriptionCodeBatchSession;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.SubscriptionManagementAttributeE;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author manyce400
 */
public class PrepaidSubscriptionStatisticExcelView extends AbstractExcelView {


    private final DateTimeFormatter dateTimeFormatter;

    public PrepaidSubscriptionStatisticExcelView(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PrepaidSubscriptionStatisticExcelView.class);

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook hssfWorkbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        SubscriptionCodeBatchSession subscriptionCodeBatchSession = (SubscriptionCodeBatchSession) model.get(SubscriptionManagementAttributeE.SubscriptionCodeBatchSession.toString());
        List<PrepaidSubscriptionStatistic> prepaidSubscriptionStatisticList = (List<PrepaidSubscriptionStatistic>) model.get(SubscriptionManagementAttributeE.PrepaidSubscriptionStatisticList.toString());

        // Title of Excel document will be the unique Batch Session UID
        HSSFSheet sheet = hssfWorkbook.createSheet(subscriptionCodeBatchSession.getSubscriptionCodeBatchSessionUID());
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle cellStyle = buildExcelCellStyle(hssfWorkbook);

        // create header row
        HSSFRow headerRow = buildExcelHeaderRow(sheet, cellStyle);

        // build all rows
        int rowCount = 1;
        for(PrepaidSubscriptionStatistic prepaidSubscriptionStatistic : prepaidSubscriptionStatisticList) {
            HSSFRow hssfRow = sheet.createRow(rowCount++);
            hssfRow.createCell(0).setCellValue(prepaidSubscriptionStatistic.getUniqueID());
            hssfRow.createCell(1).setCellValue(prepaidSubscriptionStatistic.getSubscriptionName());
            hssfRow.createCell(2).setCellValue(prepaidSubscriptionStatistic.getSubscriptionCost());
            hssfRow.createCell(3).setCellValue(prepaidSubscriptionStatistic.getMunicipalityName());
            hssfRow.createCell(4).setCellValue(prepaidSubscriptionStatistic.isAlreadyUsed());
            hssfRow.createCell(5).setCellValue(prepaidSubscriptionStatistic.getDateCreated().toString(dateTimeFormatter));

            if (prepaidSubscriptionStatistic.getDateRedeemed() != null) {
                hssfRow.createCell(6).setCellValue(prepaidSubscriptionStatistic.getDateRedeemed().toString(dateTimeFormatter));
            }

            hssfRow.createCell(7).setCellValue(prepaidSubscriptionStatistic.getRedemptionUserEmail());
        }
    }

    protected CellStyle buildExcelCellStyle(HSSFWorkbook hssfWorkbook) {
        CellStyle cellStyle = hssfWorkbook.createCellStyle();
        Font font = hssfWorkbook.createFont();
        font.setFontName("Arial");
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);
        return cellStyle;
    }

    protected HSSFRow buildExcelHeaderRow(HSSFSheet sheet, CellStyle style) {
        HSSFRow header = sheet.createRow(0);

        header.createCell(0).setCellValue("Unique ID");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Subscription Name");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("Subscription Cost");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Municipality Name");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Already Redeemed");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("Date Created");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("Date Redeemed");
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue("Redemption User Email");
        header.getCell(7).setCellStyle(style);

        return header;
    }
}
