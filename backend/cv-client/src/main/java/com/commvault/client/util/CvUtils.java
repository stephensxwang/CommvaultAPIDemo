package com.commvault.client.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.commvault.client.service.CvCommonService;
import org.springframework.lang.Nullable;

public class CvUtils {
	
	/**
	 * 从返回结果中获取错误信息
	 * @param response
	 * @return
	 */
	public static Map<String, String> getError(Map<String, Object> response) {
		Map<String, String> errorMap = new HashMap<>();
		
		Integer errorCode = getMapValue("errorCode", response);
		String errorMessage = getMapValue("errorMessage", response);
		if(errorMessage == null){
		    errorMessage = getMapValue("errLogMessage", response);
        }
		String errorString = getMapValue("errorString", response);
		
		errorMap.put("errorCode", errorCode.toString());
		errorMap.put("errorMessage", errorMessage == null ? errorString : errorMessage);
		
		return errorMap;
	}

    @Nullable
	public static String getErrorMessage(Map<String, Object> response) {
		String errorMessage = getMapValue("errorMessage", response);
		String errorString = getMapValue("errorString", response);

		return errorMessage == null ? errorString : errorMessage;
	}

	@Nullable
    public static Integer getErrorCode(Map<String, Object> response) {
        Integer errorCode = getMapValue("errorCode", response);

        return errorCode;
    }
	
	/**
	 * 生成错误信息
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 */
	public static Map<String, Object> genError(Integer errorCode, String errorMessage) {
		Map<String, Object> errorMap = new HashMap<>();
		
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorMessage", errorMessage);
		
		return errorMap;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getMapValue(String name, Map<String, Object> map) {
		T result = null;
		if(map == null) return null;
		for(String key : map.keySet()) {
			Object value = map.get(key);
			if(key.equals(name)) {
				result = (T) value;
				break;
			}
			
			if (value instanceof List) {
				for(Object child:(List<?>)value) {
					if(child instanceof Map) {
						result = getMapValue(name, (Map<String, Object>)child);
						if(result != null) break;
					}
				}
			}
			
			if (value instanceof Map) {				
				result = getMapValue(name, (Map<String, Object>)value);
				if(result != null) break;
			}
		}
		
		return result;
	}
	
	public static String doLogin(CvCommonService cvCommonService, String username, String password) throws Exception {
		String token = "";
		
		Map<String, Object> result = cvCommonService.login("", username, password, "");
		if(result != null && result.get("token") != null) {
			token = (String) result.get("token");
		} else {
		    throw new Exception((String) getMapValue("errLogMessage", result));
        }
		
		return token;
	}
	
	public static void doLogout(CvCommonService cvCommonService, String token) {
		try{
			cvCommonService.logout(token);
		} catch(Exception ignored) {
			
		}
	}
}
