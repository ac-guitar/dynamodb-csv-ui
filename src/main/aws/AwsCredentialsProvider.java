package main.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

public class AwsCredentialsProvider implements AWSCredentialsProvider{

	private String accessKey;
	private String secretKey;
	private Regions region;
		
	public AwsCredentialsProvider(String accessKeyId, String secretAccessKey, Regions region){
		this.accessKey = accessKeyId;
		this.secretKey = secretAccessKey;
		this.region = region;
	}

	@Override
	public AWSCredentials getCredentials() {
		return new BasicAWSCredentials(this.accessKey, this.secretKey);
	}
	
	public Regions getRegion(){
		return this.region;
	}

	@Override
	public void refresh() {
	}
}
