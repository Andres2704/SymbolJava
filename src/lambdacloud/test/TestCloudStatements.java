package lambdacloud.test;

import lambdacloud.core.CloudConfig;
import lambdacloud.core.CloudVar;
import lambdacloud.core.CloudStatements;
import lambdacloud.core.CSD;
import static symjava.symbolic.Symbol.*;

public class TestCloudStatements {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CloudConfig.setTarget("server");
		
		CloudVar i = new CloudVar("i");
		CloudVar j = new CloudVar("j");
		CSD result = new CSD("TestCloudStatements.result");
		CSD input = new CSD("TestCloudStatements.input");
		input.init(new double[]{3.0,4.0});
		input.storeToCloud();
		
		CloudStatements s = new CloudStatements();
		s.append(i.assign(1)); //i=1
		s.append(j.assign(x*y)); //j=x*y
		s.append(result.assign(i+j)); //result=i+j
		
		s.apply(input);
		
		result.fetchToLocal();
		System.out.println(result.get(0));
	}

}