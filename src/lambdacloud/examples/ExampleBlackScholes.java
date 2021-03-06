package lambdacloud.examples;

import static symjava.math.SymMath.exp;
import static symjava.math.SymMath.log;
import static symjava.math.SymMath.pow;
import static symjava.math.SymMath.sqrt;
import static symjava.symbolic.Symbol.C0;
import static symjava.symbolic.Symbol.z;
import lambdacloud.core.CloudConfig;
import lambdacloud.core.CloudLib;
import lambdacloud.core.CloudSD;
import symjava.domains.Domain;
import symjava.domains.Interval;
import symjava.examples.Newton;
import symjava.relational.Eq;
import symjava.symbolic.Expr;
import symjava.symbolic.Integrate;
import symjava.symbolic.SymConst;
import symjava.symbolic.Symbol;

public class ExampleBlackScholes {

	public static void main(String[] args) {
		CloudConfig.setGlobalConfig("job_local.conf");
		CloudLib lib = new CloudLib();
		
		Symbol spot = new Symbol("spot"); //spot price
		Symbol strike = new Symbol("strike"); //strike price
		Symbol rd = new Symbol("rd");
		Symbol rf = new Symbol("rf");
		Symbol vol = new Symbol("sigma"); //volatility
		Symbol tau = new Symbol("tau");
		Symbol phi = new Symbol("phi");
		SymConst PI2 = new SymConst("2*pi", 2*Math.PI);
		
		Expr domDf = exp(-rd*tau); 
		Expr forDf = exp(-rf*tau);
		Expr fwd=spot*forDf/domDf;
		Expr stdDev=vol*sqrt(tau);
		//We use -10 instead of -oo for numerical computation
		double step = 1e-3;
		Domain I1 = Interval.apply(-10, phi*(log(fwd/strike)+0.5*pow(stdDev,2))/stdDev, z)
				.setStepSize(step); 
		Domain I2 = Interval.apply(-10, phi*(log(fwd/strike)-0.5*pow(stdDev,2))/stdDev, z)
				.setStepSize(step); 
		Expr cdf1 = Integrate.apply(exp(-0.5*pow(z,2)), I1)/sqrt(PI2);
		Expr cdf2 = Integrate.apply(exp(-0.5*pow(z,2)), I2)/sqrt(PI2);
		Expr res = phi*domDf*(fwd*cdf1-strike*cdf2);

		Expr[] freeVars = {vol};
		Expr[] params = {spot, strike, rd, rf, tau, phi};
		Eq[] eq = new Eq[] { new Eq(res-0.897865, C0, freeVars, params) };
		
		// Use Newton's method to find the root
		double[] guess = new double[]{ 0.10 };
		double[] constParams = new double[] {100.0, 110.0, 0.002, 0.01, 0.5, 1};
		//local
		Newton.solve(eq, guess, constParams, 100, 1e-5);		
		//sever
		CloudSD output = new CloudSD("bs_sol").init(1);
		lib.solverNewton(eq, guess, constParams, 100, 1e-5, output);
		output.fetch();
		System.out.println("Results from server: "+output.getData(0));

	}

}
