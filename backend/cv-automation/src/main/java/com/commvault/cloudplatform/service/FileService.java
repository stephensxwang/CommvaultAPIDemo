package com.commvault.cloudplatform.service;

import com.commvault.cloudplatform.dto.BatchConfigView;
import com.commvault.cloudplatform.dto.BatchInstallView;
import com.commvault.cloudplatform.exception.ApplicationException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	public BatchConfigView readBatchConfigExcel(MultipartFile file) throws ApplicationException;

	public BatchInstallView readBatchInstallExcel(MultipartFile file) throws ApplicationException;

}
