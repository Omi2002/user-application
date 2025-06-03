package user.com.user.handler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RestClient {
    private String requestURL;
    private String requestMethod;
    private Map<String, String> requestHeader;
    private String requestPayload;
    private Integer apiConnTimeout = 180;
    private Integer apiReadTimeout = 180;

    private Map<String, List<String>> responseHeader;
    private Integer responseCode;
    private String response;

    public RestClient() {
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public Map<String, List<String>> getResponseHeader() {
        return responseHeader;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setApiConnTimeout(Integer apiConnTimeout) {
        this.apiConnTimeout = apiConnTimeout;
    }

    public void setApiReadTimeout(Integer apiReadTimeout) {
        this.apiReadTimeout = apiReadTimeout;
    }

    /**
     * Set HTTP request header
     *
     * @param httpURLConnection
     * @return
     */
    private HttpURLConnection setHeader(HttpURLConnection httpURLConnection) {
        if (requestHeader == null || requestHeader.isEmpty()) {
            return httpURLConnection;
        }

        for (Map.Entry<String, String> header : requestHeader.entrySet()) {
            String key = header.getKey();
            String value = header.getValue();
            httpURLConnection.setRequestProperty(key, value);
        }
        return httpURLConnection;
    }

    /**
     * Set request body
     *
     * @param httpURLConnection
     * @return
     * @throws IOException
     */
    private HttpURLConnection setPayload(HttpURLConnection connection) throws IOException {
        if (requestPayload == null || requestPayload.isEmpty()) {
            return connection;
        }
        // ----------------------------------------------------------------

        connection.setDoOutput(true);
        // ----------------------------------------------------------------

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new DataOutputStream(connection.getOutputStream()), "UTF-8"));
        writer.write(requestPayload);
        // ----------------------------------------------------------------

        writer.flush();
        writer.close();
        // ----------------------------------------------------------------
        return connection;
    }

    /**
     * read http response code
     *
     * @param httpURLConnection
     * @throws Exception
     */
    private void readResponseCode(HttpURLConnection httpURLConnection) throws Exception {
        responseCode = httpURLConnection.getResponseCode();
    }

    /**
     * read response headers
     *
     * @param httpURLConnection
     */
    private void readResponseHeader(HttpURLConnection connection) {
        // get response headers
        responseHeader = connection.getHeaderFields();
    }

    /**
     * read http response
     *
     * @param connection
     * @throws IOException
     */
    private void readResponse(HttpURLConnection connection) throws IOException {
        String inputLine = null;
        StringBuffer responseData = new StringBuffer();
        // ----------------------------------------------------------------

        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (Exception e) {
            inputStream = connection.getErrorStream();
        }
        // ----------------------------------------------------------------

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        while ((inputLine = reader.readLine()) != null) {
            responseData.append(inputLine);
        }
        reader.close();
        // ----------------------------------------------------------------

        // set response
        response = responseData.toString();
    }

    /**
     * call the api
     *
     * @throws Exception
     */
    public void callAPI() throws Exception {
        if (requestURL == null || requestURL.isEmpty()) {
            throw new Exception("request URL should not null/empty");
        }

        URL url = new URL(requestURL);
        // ----------------------------------------------------------------

        // create new instance of URLConnection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (apiConnTimeout != null) {
            connection.setConnectTimeout(apiConnTimeout * 1000);
        }

        if (apiReadTimeout != null) {
            connection.setReadTimeout(apiReadTimeout * 1000);
        }
        // ----------------------------------------------------------------

        // set the method for the URL request
        if (requestMethod == null || requestMethod.isEmpty()) {
            throw new Exception("request method should not null/empty");
        }
        connection.setRequestMethod(requestMethod);
        // ----------------------------------------------------------------

        // sets the general request property
        connection = setHeader(connection);
        // ----------------------------------------------------------------

        // set payload
        connection = setPayload(connection);
        // ----------------------------------------------------------------

        // read http response status code
        readResponseCode(connection);
        // ----------------------------------------------------------------

        // read response headers
        readResponseHeader(connection);
        // ----------------------------------------------------------------

        // read http response
        readResponse(connection);
        // ----------------------------------------------------------------

        // disconnect stream
        connection.disconnect();
    }
}
