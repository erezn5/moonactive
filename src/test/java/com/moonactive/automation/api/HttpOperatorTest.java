package com.moonactive.automation.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moonactive.automation.conf.EnvConf;
import com.moonactive.automation.utils.FileUtil;
import com.moonactive.automation.utils.JsonHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static com.moonactive.automation.logger.LoggerFactory.LOG;

public class HttpOperatorTest {
    private HashMap<String, String> headersMap = new HashMap<>();
    private static final String BASIC_HOST_HEADER = "jokes.p.rapidapi.com";
    private static final String BASIC_KEY_HEADER = "56d7a4653emsh4c19b463b18e6b7p144eb7jsn030e478c59b2";
    private static final String BASIC_URL = EnvConf.getProperty("api.test.basic.url");
    private static final String TEST_FILES_FOLDER = EnvConf.getProperty("test_output.files.folder");
    private static final String CATEGORIES_PATH = "contents.categories";
    private static final String JOKES_PATH = "contents.jokes";

    private HashMap<String, String> jokeItemsMap = new HashMap<>();
    private SimpleHttpClient simpleHttpClient;
    private String TEST_FILE_PATH;
    private File TEST_FILE;

    @BeforeClass
    public void setUp() {
        headersMap.put("x-rapidapi-host", BASIC_HOST_HEADER);
        headersMap.put("x-rapidapi-key", BASIC_KEY_HEADER);
        FileUtil.createFolder(new File(TEST_FILES_FOLDER), false);

    }
    @Test(priority =10)
    public void getTestRequest() throws Exception {
        simpleHttpClient = new SimpleHttpClient();
        String response = simpleHttpClient.sendGetRequest(BASIC_URL.concat("/test"), headersMap);
        Assert.assertFalse(assertGetRequest(response), String.format("Get Request should be different from status code=[%s]", response.split("\"code\":")[1].substring(0,3)));
    }

    private boolean assertGetRequest(String response){
        return response.contains("\"code\":404") && response.contains("\"message\":\"Not Found\"");
    }

    @Test(priority =20, dataProvider ="getCategoriesProvider")
    public void getJokeDetails(String categoriesUrl, String path, String categoryUrl, String categoryPath) throws Exception {
        JsonArray array = getJsonArrayFromGetRequest(categoriesUrl, path);
        String category = JsonHandler.getItemStringFromJsonArray(2,"name", array);
        LOG.info(String.format("Getting category is successful, category name is=[%s]",category));

        array = getJsonArrayFromGetRequest(String.format(categoryUrl, category), categoryPath);
        JsonObject jsonObject = JsonHandler.getJsonObjectFromJsonArray(0, "joke", array);
        saveJokeContentToFileAndValidate(array, jsonObject);
        LOG.info("File created as expected, file content is: \n");
        FileUtil.printFileContent(TEST_FILE_PATH);
    }

    private JsonArray getJsonArrayFromGetRequest(String url, String path) throws IOException {
        simpleHttpClient = new SimpleHttpClient();
        String response = simpleHttpClient.sendGetRequest(url, headersMap);
        return JsonHandler.asList(response, path);
    }

    @DataProvider(name = "getCategoriesProvider")
    public Object[][] getRequestItems(){
        return new Object[][] {
                {
                        BASIC_URL.concat("/categories"),
                        CATEGORIES_PATH,
                        BASIC_URL.concat("?category=%s"),
                        JOKES_PATH
                }
        };
    }

    private void saveJokeContentToFileAndValidate(JsonArray jsonArray, JsonObject jsonObject){
        String name = JsonHandler.getItemStringFromJsonObject(jsonObject, "id");
        jokeItemsMap.put("category", JsonHandler.getItemStringFromJsonArray(0, "category", jsonArray));
        jokeItemsMap.put("title", JsonHandler.getItemStringFromJsonObject(jsonObject, "title"));
        jokeItemsMap.put("text", JsonHandler.getItemStringFromJsonObject(jsonObject, "text"));
        jokeItemsMap.put("description", JsonHandler.getItemStringFromJsonArray(0,"description", jsonArray));
        String fileName = TEST_FILES_FOLDER.concat("/").concat(name).concat(".txt");

        FileUtil.writeToFile(fileName, jokeItemsMap);
        TEST_FILE_PATH = fileName;
        TEST_FILE = new File(TEST_FILE_PATH);
        Assert.assertTrue(TEST_FILE.exists(), String.format("File=[%s] not found", TEST_FILE_PATH));
    }

    @Test(priority =50)
    public void tearDown(){
        Assert.assertTrue(FileUtil.delete(TEST_FILE), String.format("File still exist in =[%s]", TEST_FILE_PATH));
        LOG.info(String.format("File =[%s] deleted successfully", TEST_FILE_PATH));
    }

}
