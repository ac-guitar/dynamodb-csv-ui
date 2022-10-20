package main.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.s3.AmazonS3;
import com.github.opendevl.JFlat;
import main.model.TableDetails;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BackendService {

	Logger logger = LoggerFactory.getLogger(BackendService.class);

	@Autowired
	private AmazonDynamoDB dynamoDbClient;

	@Autowired
	private DynamoDB dynamoDB;

	@Autowired
	private AmazonS3 amazonS3;

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

			JSONArray jsonArray = AwsToCsv.createJson(tableName, dynamoDbClient, columnName, value);

			logger.info("Total itens: " + jsonArray.length());

			generateCsv(jsonArray, tableName);

			logger.info("Generated CSV file.");

			// Save file to S3
			File file = new File(tableName.concat(".csv"));
			amazonS3.putObject("awsdb-csv-export", tableName.concat(".csv"), file);

			logger.info("Uploaded CSV to S3.");

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public void generateCsv(JSONArray jsonArray, String fileName) throws Exception {
		// JFlat to create CSV files from JSON
		JFlat flatMe = new JFlat(jsonArray.toString());
		flatMe
				.json2Sheet()
				.headerSeparator(";")
				.write2csv(fileName + ".csv");
	}

}
