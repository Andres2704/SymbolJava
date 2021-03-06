package symjava.examples;

import static symjava.symbolic.Symbol.C0;

import java.util.ArrayList;
import java.util.List;

import symjava.relational.Eq;
import symjava.relational.Ge;
import symjava.relational.Gt;
import symjava.relational.Le;
import symjava.relational.Lt;
import symjava.relational.Relation;
import symjava.symbolic.Expr;
import symjava.symbolic.Symbols;
import symjava.symbolic.utils.AddList;
import symjava.symbolic.utils.Utils;

public class OptSolver {
	Expr obj;
	List<Relation> constrs = new ArrayList<Relation>();
	
	public OptSolver() {
		
	}
	public static OptSolver min(Expr obj) {
		OptSolver s = new OptSolver();
		s.obj = obj;
		return s;
	}
	
	public OptSolver subjectTo(Relation ...constrs) {
		for(Relation r : constrs)
			this.constrs.add(r);
		return this;
	}
	
	double[] solve() {
		Symbols lmd = new Symbols("\\lambda");
		Symbols c = new Symbols("c");
		AddList addList = new AddList();
		addList.add(obj);
		int idxLmd = 1;
		int idxC = 1;
		for(int i=0; i<constrs.size(); i++) {
			Relation e = constrs.get(i);
			if(e instanceof Eq) {
				addList.add(lmd.get(idxLmd++)*(e.lhs()-e.rhs()));
			} else if(e instanceof Ge || e instanceof Gt) {
				addList.add(lmd.get(idxLmd++)*(e.lhs()-e.rhs()-c.get(idxC)*c.get(idxC)));
				idxC++;
			} else if(e instanceof Le || e instanceof Lt) {
				addList.add(lmd.get(idxLmd++)*(e.lhs()-e.rhs()+c.get(idxC)*c.get(idxC)));
				idxC++;
			} else {
				throw new RuntimeException("Unsupported relation: " + e);
			}
		}
		
		Expr L = addList.toExpr();
		Expr[] freeVars = Utils.extractSymbols(L).toArray(new Expr[0]);
		double[] x0 = new double[freeVars.length];
		Eq eq = new Eq(L, C0, freeVars);
		System.out.println(eq);

		return NewtonOptimization.solve(eq, x0, 1000, 1e-6, false);
		
	}
}
