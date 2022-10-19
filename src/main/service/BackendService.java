package main.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import main.aws.AwsCredentialsProvider;
import main.aws.AwsToCsv;
import main.model.TableDetails;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BackendService {

	private static AmazonDynamoDB dynamoDbClient;
	private static DynamoDB dynamoDB = null;
	private static JSONObject config = null;
	private AmazonS3 s3client = null;

	private String accessKeyId = "ACCESSKEYID";
	private String secretAccessKey = "SECRETACCESSKEY";
	private String region = "REGION";

	public void configure() {

		try{

			config = new JSONObject();
			config.put("accessKeyId",accessKeyId);
			config.put("secretAccessKey",secretAccessKey);
			config.put("region",region);

		} catch (Exception e){
			e.printStackTrace();
		}

		AwsCredentialsProvider credentialsProvider = new AwsCredentialsProvider(accessKeyId, secretAccessKey, getAmazonRegion(region));

		// this service is needed to analyse data
		dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
				.withRegion(credentialsProvider.getRegion())
				.withCredentials(credentialsProvider)
				.withClientConfiguration(new ClientConfiguration().withRequestTimeout(50000))
				.build();

		// this service is needed to analyze the dynamodb
		dynamoDB = new DynamoDB(dynamoDbClient);

		// start S3
		s3client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(credentialsProvider)
				.withRegion(Regions.EU_CENTRAL_1)
				.build();

	}

	public List<TableDetails> getTableList() {

		Long id = 42L;

		ArrayList<TableDetails> tableList = new ArrayList<TableDetails>();

		TableCollection<ListTablesResult> awsTables = dynamoDB.listTables();
		Iterator<Table> iterator = awsTables.iterator();

		tableList = new ArrayList<TableDetails>();

		while (iterator.hasNext()) {
			Table table = iterator.next();
			tableList.add(new TableDetails(id++, table.getTableName()));
		}

		return tableList;
	}

	public void callAwsService(String tableName, String columnName, String value) {

		try{

			config.put("tableName", tableName);
			AwsToCsv.createCsvFile(config, dynamoDbClient, columnName, value);

			File file = new File(tableName.concat(".csv"));
			s3client.putObject("awsdb-csv-export", tableName.concat(".csv"), file);

			if(file.exists()) {
				file.deleteOnExit();
			}

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public static Regions getAmazonRegion(String cloudRegion){

		System.out.println("DynamoDbCon: getAmazonRegion");

		Regions region = null;

		switch(cloudRegion){

			case "us-east-1" : region = com.amazonaws.regions.Regions.US_EAST_1;
				break;

			case "us-east-2" : region = com.amazonaws.regions.Regions.US_EAST_2;
				break;

			case "us-west-1" : region = com.amazonaws.regions.Regions.US_WEST_1;
				break;

			case "us-west-2" : region = com.amazonaws.regions.Regions.US_WEST_2;
				break;

			case "ca-central-1" : region = com.amazonaws.regions.Regions.CA_CENTRAL_1;
				break;

			case "eu-west-1" : region = com.amazonaws.regions.Regions.EU_WEST_1;
				break;

			case "eu-central-1" : region = com.amazonaws.regions.Regions.EU_CENTRAL_1;
				break;

			case "eu-west-2" : region = com.amazonaws.regions.Regions.EU_WEST_2;
				break;

			case "ap-northeast-1" : region = com.amazonaws.regions.Regions.AP_NORTHEAST_1;
				break;

			case "ap-northeast-2" : region = com.amazonaws.regions.Regions.AP_NORTHEAST_2;
				break;

			case "ap-southeast-1" : region = com.amazonaws.regions.Regions.AP_SOUTHEAST_1;
				break;

			case "ap-southeast-2" : region = com.amazonaws.regions.Regions.AP_SOUTHEAST_2;
				break;

			case "ap-south-1" : region = com.amazonaws.regions.Regions.AP_SOUTH_1;
				break;

			case "sa-east-1" : region = com.amazonaws.regions.Regions.SA_EAST_1;
				break;

			case "eu-north-1" : region = com.amazonaws.regions.Regions.EU_NORTH_1;

		}

		return region;
	}

}
