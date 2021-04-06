package com.commvault.cloudplatform.service.impl;

import com.commvault.cloudplatform.dto.*;
import com.commvault.cloudplatform.exception.ApplicationException;
import com.commvault.cloudplatform.service.FileService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

	private final static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
    public BatchConfigView readBatchConfigExcel(MultipartFile file) throws ApplicationException {
        BatchConfigView batchConfigView = new BatchConfigView();
        List<BatchConfigTitleView> titleList = new ArrayList();
        List<BatchConfigContentView> contentList = new ArrayList();

        try {
            logger.debug("Reading excel: {}, size: {}, content type: {}", file.getOriginalFilename(), file.getBytes().length, file.getContentType());

            Workbook wb = null;
            if (file.getContentType().equals("application/vnd.ms-excel")){
                wb = new HSSFWorkbook(file.getInputStream());
            } else if(file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                logger.error("Invalid content type");
                throw new ApplicationException("Content type not allowed");
            }

            Sheet sheet = wb.getSheetAt(0);
            logger.debug("Sheet " + 0 + " \"" + wb.getSheetName(0) + "\" has " + sheet.getPhysicalNumberOfRows() + " row(s).");

            int firstRowNum = sheet.getFirstRowNum();
            Row titleRow = sheet.getRow(firstRowNum);
            short minColIx = titleRow.getFirstCellNum();
            short maxColIx = titleRow.getLastCellNum();
            for (short colIx=minColIx; colIx<maxColIx; colIx++){
                Cell cell = titleRow.getCell(colIx);
                CellReference cellRef = new CellReference(titleRow.getRowNum(), cell.getColumnIndex());
                String text = new DataFormatter().formatCellValue(cell);
                logger.debug("Got title: {} from column: {}", text, cellRef.formatAsString());

                BatchConfigTitleEnum titleEnum = BatchConfigTitleEnum.fromValue(text);
                BatchConfigTitleView title = new BatchConfigTitleView();
                title.setLabel(text);
                title.setProp(titleEnum == null ? "" : titleEnum.getProp());
                title.setType("normal");
                titleList.add(title);
            }

            for (int i=firstRowNum + 1; i <= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                BatchConfigContentView contentView = new BatchConfigContentView();

                for (int j=minColIx; j < maxColIx; j++){
                    Cell cell = row.getCell(j);
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());

                    //get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
                    String text = new DataFormatter().formatCellValue(cell);
                    logger.debug("Got value: {} from column: {}", text, cellRef.formatAsString());

                    BatchConfigTitleView title = titleList.get(j);
                    BatchConfigTitleEnum titleEnum = BatchConfigTitleEnum.fromValue(title.getLabel());

                    if (titleEnum == null) continue;
                    switch(titleEnum){
                        case CLIENT_NAME:
                            contentView.setClientName(text);
                            break;
                        case SUBCLIENT_NAME:
                            contentView.setSubclientName(text);
                            break;
                        case PATH:
                            contentView.setPath(text);
                            break;
                        case STORAGE_POLICY:
                            contentView.setStoragePolicy(text);
                            break;
                        case SCHEDULE_POLICY:
                            contentView.setSchedulePolicy(text);
                            break;
                    }
                }

                contentList.add(contentView);
            }
            batchConfigView.setTitleList(titleList);
            batchConfigView.setContentList(contentList);

            wb.close();
        } catch (IOException e) {
            logger.error("Error occurred while reading the excel file", e);
            throw new ApplicationException(e.getMessage());
        }
        return batchConfigView;
    }

    @Override
    public BatchInstallView readBatchInstallExcel(MultipartFile file) throws ApplicationException {
        BatchInstallView batchInstallView = new BatchInstallView();
        List<BatchInstallTitleView> titleList = new ArrayList();
        List<BatchInstallContentView> contentList = new ArrayList();

        try {
            logger.debug("Reading excel: {}, size: {}, content type: {}", file.getOriginalFilename(), file.getBytes().length, file.getContentType());

            Workbook wb = null;
            if (file.getContentType().equals("application/vnd.ms-excel")){
                wb = new HSSFWorkbook(file.getInputStream());
            } else if(file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                logger.error("Invalid content type");
                throw new ApplicationException("Content type not allowed");
            }

            Sheet sheet = wb.getSheetAt(0);
            logger.debug("Sheet " + 0 + " \"" + wb.getSheetName(0) + "\" has " + sheet.getPhysicalNumberOfRows() + " row(s).");

            int firstRowNum = sheet.getFirstRowNum();
            Row titleRow = sheet.getRow(firstRowNum);
            short minColIx = titleRow.getFirstCellNum();
            short maxColIx = titleRow.getLastCellNum();
            for (short colIx=minColIx; colIx<maxColIx; colIx++){
                Cell cell = titleRow.getCell(colIx);
                CellReference cellRef = new CellReference(titleRow.getRowNum(), cell.getColumnIndex());
                String text = new DataFormatter().formatCellValue(cell);
                logger.debug("Got title: {} from column: {}", text, cellRef.formatAsString());

                BatchInstallTitleEnum titleEnum = BatchInstallTitleEnum.fromValue(text);
                BatchInstallTitleView title = new BatchInstallTitleView();
                title.setLabel(text);
                title.setProp(titleEnum == null ? "" : titleEnum.getProp());
                title.setType("normal");
                titleList.add(title);
            }

            for (int i=firstRowNum + 1; i <= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                BatchInstallContentView contentView = new BatchInstallContentView();

                for (int j=minColIx; j < maxColIx; j++){
                    Cell cell = row.getCell(j);
                    //CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());

                    //get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
                    String text = new DataFormatter().formatCellValue(cell);
                    //logger.debug("Got value: {} from column: {}", text, cellRef.formatAsString());

                    BatchInstallTitleView title = titleList.get(j);
                    BatchInstallTitleEnum titleEnum = BatchInstallTitleEnum.fromValue(title.getLabel());

                    if (titleEnum == null) continue;
                    switch(titleEnum){
                        case CLIENT_NAME:
                            contentView.setClientName(text);
                            break;
                        case COMMCELL_NAME:
                            contentView.setCommCellName(text);
                            break;
                        case USERNAME:
                            contentView.setUserName(text);
                            break;
                        case PASSWORD:
                            contentView.setPassword(text);
                            break;
                        case OS_TYPE:
                            contentView.setOsType(text);
                            break;
                        case COMMENT:
                            contentView.setComment(text);
                            break;
                    }
                }

                contentList.add(contentView);
            }
            batchInstallView.setTitleList(titleList);
            batchInstallView.setContentList(contentList);

            wb.close();
        } catch (IOException e) {
            logger.error("Error occurred while reading the excel file", e);
            throw new ApplicationException(e.getMessage());
        }
        return batchInstallView;
    }
}
