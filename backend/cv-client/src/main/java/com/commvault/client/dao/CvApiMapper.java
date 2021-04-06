package com.commvault.client.dao;

import com.commvault.client.entity.CvApi;
import com.commvault.client.util.CvYamlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class CvApiMapper {

    private static final Logger logger = LoggerFactory.getLogger(CvApiMapper.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    public CvApi queryByNameAndVersion(String apiName, String version){
        CvApi cvApi = new CvApi();

        List<Map<String, Object>> apis = CvYamlProperties.getInstance().getProperty("apis");
        Map<String, Object> apiEntry = apis.stream().filter(api -> apiName.equals(api.get("name")) && version.equals(api.get("version"))).findAny().orElse(null);
        if(apiEntry != null){
            cvApi.setName((String) apiEntry.get("name"));
            cvApi.setType((String) apiEntry.get("type"));
            cvApi.setUrl((String) apiEntry.get("url"));
            cvApi.setHeader((String) apiEntry.get("header"));
            cvApi.setVersion((String) apiEntry.get("version"));

            cvApi.setBody(loadApiBody("classpath:cvtemplates/" + cvApi.getVersion() + "/" + cvApi.getName() + ".xml"));
        } else {
            return null;
        }
        return cvApi;
    }

    private String loadApiBody(String path){
        String result = null;

        Resource resource = resourceLoader.getResource(path);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (IOException e) {
            logger.debug("Failed to load API body from {}", path, e);
        }

        return result;
    }
}
