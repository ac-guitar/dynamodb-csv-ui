package main.aws;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.github.opendevl.JFlat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AwsToCsv
{

    public static void createCsvFile(JSONObject config, AmazonDynamoDB dynamoDbClient, String columName, String value) {

        if(validateConfig(config)) {

            try{

                JSONObject json = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                ScanRequest scanRequest = new ScanRequest();

                if(!value.equals("") && !columName.equals("")){
                    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
                    expressionAttributeValues.put(":" + columName, new AttributeValue().withS(value));
                    scanRequest.withExpressionAttributeValues(expressionAttributeValues);
                    scanRequest.withFilterExpression(columName + " = :" + columName);
                }

                Map<String, AttributeValue> lastKeyEvaluated = null;

                do {
                    scanRequest.withTableName(config.getString("tableName"));
                    scanRequest.withExclusiveStartKey(lastKeyEvaluated);
                    ScanResult result = dynamoDbClient.scan(scanRequest);

                    for (Map<String, AttributeValue> item : result.getItems()) {
                        json = new JSONObject(ItemUtils.toItem(item).toJSON());
                        jsonArray.put(json);
                    }

                    lastKeyEvaluated = result.getLastEvaluatedKey();

                } while (lastKeyEvaluated != null);

                System.out.println("Total itens: " + jsonArray.length());

                // JFlat to create CSV files with JSON
                JFlat flatMe = new JFlat(jsonArray.toString());
                flatMe
                        .json2Sheet()
                        .headerSeparator(";")
                        .write2csv(config.getString("tableName") + ".csv");

            }

            catch (Exception e){
                System.out.println("AwsToCsv.createCsvFile: " + e.getMessage());
            }

        }
    }

    private static boolean validateConfig(JSONObject config) {

        boolean valid = true;

        if (!config.has("accessKeyId")){
            System.out.println("accessKeyId is missing.");
            valid = false;
        }

        if (!config.has("secretAccessKey")){
            System.out.println("secretAccessKey is missing.");
            valid = false;
        }

        if (!config.has("region")){
            System.out.println("region is missing.");
            valid = false;
        }

        if (!config.has("tableName")){
            System.out.println("tableName is missing.");
            valid = false;
        }

        return valid;

    }
    
}
