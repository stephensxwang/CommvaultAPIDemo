package com.commvault.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestfulUtils {

    private static Logger logger = LoggerFactory.getLogger(RestfulUtils.class);

    public static String doPostAPIRequest(String targetURL, Map<String, Object> headers, String body) throws IOException {
        logger.debug("Send POST http request to server, URL: {}, Header: {}, Body: {}", targetURL, headers, body);
        String returnresult = "";
        InputStream response = null;
        try {
            StringBuilder result = new StringBuilder();
            targetURL = targetURL.replace(" ", "%20");
            URL targetUrl = new URL(targetURL);

            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");

            for (String key : headers.keySet()) {
                httpConnection.setRequestProperty(key, headers.get(key).toString());
            }

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(body.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            logger.debug("Response Code: {}", httpConnection.getResponseCode());
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                httpConnection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED ||
                httpConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {

                response = httpConnection.getInputStream();
            } else {
                response = httpConnection.getErrorStream();
            }

            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((response), StandardCharsets.UTF_8));

            String output;

            while ((output = responseBuffer.readLine()) != null) {
                result.append(output + "\n");
            }

            responseBuffer.close();
            returnresult = result.toString();
            //httpConnection.disconnect();
            logger.debug("Response Content: {}", returnresult);
        } catch (MalformedURLException e) {
            logger.error("URL format is incorrect :{}", targetURL);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
        }

        return returnresult;
    }

    public static String doPutAPIRequest(String targetURL, Map<String, Object> headers, String body) throws IOException {
        logger.debug("Send PUT http request to server, URL: {}, Header: {}, Body: {}", targetURL, headers, body);
        String returnresult = "";
        InputStream response = null;
        try {
            StringBuilder result = new StringBuilder();
            targetURL = targetURL.replace(" ", "%20");
            URL targetUrl = new URL(targetURL);

            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");

            for (String key : headers.keySet()) {
                httpConnection.setRequestProperty(key, headers.get(key).toString());
            }

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(body.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            logger.debug("Response Code: {}", httpConnection.getResponseCode());
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                httpConnection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED ||
                httpConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {

                response = httpConnection.getInputStream();
            } else {
                response = httpConnection.getErrorStream();
            }

            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                (response), StandardCharsets.UTF_8));

            String output;
            while ((output = responseBuffer.readLine()) != null) {
                result.append(output + "\n");
            }

            responseBuffer.close();
            returnresult = result.toString();
            //httpConnection.disconnect();

            logger.debug("Response Content: {}", returnresult);
        } catch (MalformedURLException e) {
            logger.error("URL format is incorrect :{}", targetURL);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
        }

        return returnresult;
    }

    public static String doDeleteAPIRequest(String targetURL, Map<String, Object> headers) throws IOException {
        logger.debug("Send DELETE http request to server, URL: {}, Header: {}", targetURL, headers);
        String returnresult = "";
        InputStream response = null;
        try {
            StringBuilder result = new StringBuilder();
            targetURL = targetURL.replace(" ", "%20");
            URL restServiceURL = new URL(targetURL);

            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
            httpConnection.setRequestMethod("DELETE");

            for (String key : headers.keySet()) {
                httpConnection.setRequestProperty(key, headers.get(key).toString());
            }

            logger.debug("Response Code: {}", httpConnection.getResponseCode());
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                    httpConnection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED ||
                    httpConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {

                response = httpConnection.getInputStream();
            } else {
                response = httpConnection.getErrorStream();
            }
            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                    (response), StandardCharsets.UTF_8));

            String output;

            while ((output = responseBuffer.readLine()) != null) {
                result.append(output + "\n");
            }
            responseBuffer.close();
            returnresult = result.toString();
            logger.debug("Response Content: {}", returnresult);
            //httpConnection.disconnect();

        } catch (MalformedURLException e) {
            logger.error("URL format is incorrect :{}", targetURL);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
        }

        return returnresult;
    }

    public static String doGetAPIRequest(String targetURL, Map<String, Object> headers) throws IOException {
        logger.debug("Send GET http request to server, URL: {}, Header: {}", targetURL, headers);
        String returnresult = "";
        InputStream response = null;
        try {
            StringBuilder result = new StringBuilder();
            targetURL = targetURL.replace(" ", "%20");
            URL restServiceURL = new URL(targetURL);

            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
            httpConnection.setRequestMethod("GET");

            for (String key : headers.keySet()) {
                httpConnection.setRequestProperty(key, headers.get(key).toString());
            }

            logger.debug("Response Code: {}", httpConnection.getResponseCode());
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                    httpConnection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED ||
                    httpConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {

                response = httpConnection.getInputStream();
            } else {
                response = httpConnection.getErrorStream();
            }
            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                (response), StandardCharsets.UTF_8));

            String output;

            while ((output = responseBuffer.readLine()) != null) {
                result.append(output + "\n");
            }
            responseBuffer.close();
            returnresult = result.toString();
            logger.debug("Response Content: {}", returnresult);
            //httpConnection.disconnect();

        } catch (MalformedURLException e) {
            logger.error("URL format is incorrect :{}", targetURL);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
        }

        return returnresult;
    }
}
