package symjava.examples;

import java.util.ArrayList;
import java.util.List;

import symjava.math.Transformation;
import symjava.symbolic.Domain;
import symjava.symbolic.Expr;


public class Element extends Domain {
	int index;
	int dim;
	List<Node> nodes = new ArrayList<Node>();
	
	public Element(String label, Expr ...coordVars) {
		this.label = label;
		this.coordVars = coordVars;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setNodes(Node ...nodes) {
		for(Node n : nodes) {
			this.nodes.add(n);
		}
		this.dim = nodes[0].getDim();
	}
	
	@Override
	public Domain transform(String label, Transformation trans) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDim() {
		return dim;
	}
	
	double[] getNodeCoords() {
		double[] rlt = new double[nodes.size()*dim];
		int index = 0;
		for(int j=0; j<dim; j++) {
			for(Node n : nodes) {
				rlt[index++] = n.coords[j];
			}
		}
		return rlt;
	}

}
