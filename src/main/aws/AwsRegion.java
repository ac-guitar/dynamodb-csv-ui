package main.aws;

import com.amazonaws.regions.Regions;

public class AwsRegion {
	
	public static Regions getAmazonRegion(String cloudRegion){
			
		Regions region = null;
		
		switch(cloudRegion){
			
			case "us-east-1" : region = Regions.US_EAST_1;
				break;
			
			case "us-east-2" : region = Regions.US_EAST_2;
				break;
				
			case "us-west-1" : region = Regions.US_WEST_1;
				break;
			
			case "us-west-2" : region = Regions.US_WEST_2;
				break;
				
			case "ca-central-1" : region = Regions.CA_CENTRAL_1;
				break;
				
			case "eu-west-1" : region = Regions.EU_WEST_1;
				break;
				
			case "eu-central-1" : region = Regions.EU_CENTRAL_1;
				break;
				
			case "eu-west-2" : region = Regions.EU_WEST_2;
				break;
				
			case "ap-northeast-1" : region = Regions.AP_NORTHEAST_1;
				break;
				
			case "ap-northeast-2" : region = Regions.AP_NORTHEAST_2;
				break;	
				
			case "ap-southeast-1" : region = Regions.AP_SOUTHEAST_1;
				break;	
				
			case "ap-southeast-2" : region = Regions.AP_SOUTHEAST_2;
				break;	
				
			case "ap-south-1" : region = Regions.AP_SOUTH_1;
				break;	
				
			case "sa-east-1" : region = Regions.SA_EAST_1;
				break;
				
			case "eu-north-1" : region = Regions.EU_NORTH_1;
		
		}
		
		return region;
	}
	
}

