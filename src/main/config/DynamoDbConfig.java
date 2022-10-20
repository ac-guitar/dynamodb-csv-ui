package main.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import main.aws.AwsCredentialsProvider;
import main.aws.AwsRegion;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories
public class DynamoDbConfig {

    @Value("${amazon.aws.accessKey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretKey}")
    private String amazonAWSSecretKey;

    @Bean
    public DynamoDB dynamoDb() {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB());
        return dynamoDB;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AwsCredentialsProvider credentialsProvider = amazonAWSCredentials();
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(credentialsProvider.getRegion())
                .withCredentials(credentialsProvider)
                .withClientConfiguration(new ClientConfiguration().withRequestTimeout(50000))
                .build();

        return amazonDynamoDB;
    }

    @Bean
    public AmazonS3 amazonS3(){
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(amazonAWSCredentials())
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
        return amazonS3;
    }

    @Bean
    public AwsCredentialsProvider amazonAWSCredentials() {
        AwsCredentialsProvider credentialsProvider = new AwsCredentialsProvider(amazonAWSAccessKey, amazonAWSSecretKey, AwsRegion.getAmazonRegion("eu-central-1"));
        return credentialsProvider;
    }

}
