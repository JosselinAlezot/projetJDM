

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	
	public String id, name, type, poids; // pour les termes
	public static HashMap<Integer, String> typesNode = new HashMap<Integer, String>() {
	{
	    put(0, "n_generic");
	    put(1, "n_term");
	    put(2, "n_form");
	    put(4, "n_pos");
	    put(5, "n_concept");
	    put(6, "n_flpot");
	    put(8, "n_chunk");
	    put(9, "n_question");
	    put(10, "n_relation");
	    put(11, "r_rule");
	    put(12, "n_analogy");
	    put(13, "n_commands");
	    put(14, "f_synt_function");
	    put(18, "n_data");
	    put(36, "n_data_pot");
	    put(444, "n_link");
	    put(666, "n_AKI");
	    put(777, "n_wikipedia");
	}};
	//public String node1, node2, rtype; // pour les relations (+ id et type)
	
	
	
	public Node(String node) {
		
		String[] elsnode = node.split(";");
		
		this.id = elsnode[1];
		this.name = elsnode[2];
		this.type = typesNode.get(Integer.valueOf(elsnode[3]));
		this.poids = elsnode[4];
	}
	
	public static void main(String[] args) {
		Node n = new Node("e;47319;'colonne vertébrale';1;260");
		System.out.println(n.toString());
	}

	public String toString() {
		return "Node [id=" + id + ", name=" + name + ", type=" + type + ", poids=" + poids + "]";
	}
	
	
	
	

}
