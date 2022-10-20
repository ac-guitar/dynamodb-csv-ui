package main.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwsToCsv {

    public static JSONArray createJson(String tableName, AmazonDynamoDB dynamoDbClient, String columName, String value) {

        JSONArray jsonArray = new JSONArray();

        try{
            ScanRequest scanRequest = new ScanRequest();

            if(!value.equals("") && !columName.equals("")){
                Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
                expressionAttributeValues.put(":" + columName, new AttributeValue().withS(value));
                scanRequest.withExpressionAttributeValues(expressionAttributeValues);
                scanRequest.withFilterExpression(columName + " = :" + columName);
            }

            Map<String, AttributeValue> lastKeyEvaluated = null;

            do {
                scanRequest.withTableName(tableName);
                scanRequest.withExclusiveStartKey(lastKeyEvaluated);
                ScanResult result = dynamoDbClient.scan(scanRequest);

                List<Item> items = InternalUtils.toItemList(result.getItems());
                for(Item item : items){
                    String deviceString = item.toJSON();
                    JSONObject json = new JSONObject(deviceString);
                    jsonArray.put(json);
                }

                lastKeyEvaluated = result.getLastEvaluatedKey();

            } while (lastKeyEvaluated != null);

        }

        catch (Exception e){
            System.out.println("AwsToCsv.createCsvFile: " + e.getMessage());
        }

        return jsonArray;

    }
    
}
