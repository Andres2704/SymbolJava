package lambdacloud.core;

import lambdacloud.net.CloudClient;

public class CloudConfig {
	private static String target;
	private static CloudClient client;

	/**
	 * Parameter target can be
	 * 1. "local": Locally compile and run
	 * 2. "<lambda_cloud_auth_file>.lca": Compile and run on www.lambdacloud.io
	 * 
	 * @param target
	 */
	public static void setTarget(String target) {
		CloudConfig.target = target;
		client = new CloudClient();
		try {
			client.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isLocal() {
		return target.equalsIgnoreCase("local");
	}

	/**
	 * Print the configuration of target environment
	 * @return
	 */
	public static String printTargetInfo() {
		return "16 CPU, 64GB RAM";
	}
	
	public static String getHost() {
		return "127.0.0.1";
	}
	
	public static int getPort() {
		return 8322;
		
	}
	
	public static CloudClient getClient() {
		return client;
	}
	
	public static void shutDown() {
		client.shutDown();
	}
}