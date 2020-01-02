package graph;


import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	
	public int id;
	public String nom, type, poids; // pour les termes
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
		
		this.id = Integer.valueOf(elsnode[1]);
		setName(elsnode[2]);
		this.type = typesNode.get(Integer.valueOf(elsnode[3]));
		this.poids = elsnode[4];
	}
	
	// Ne sert que dans les cas où on rencontre un mot non trouvable
	public Node() {
		this.id = -1;
		this.nom = "";
		this.type = "";
		this.poids = "";
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getName(){
		return this.nom;
	}
	

	public void setName(String name) {
		String[] sep = name.split("'");
		this.nom = sep[1];
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPoids() {
		return poids;
	}

	public void setPoids(String poids) {
		this.poids = poids;
	}

	public static HashMap<Integer, String> getTypesNode() {
		return typesNode;
	}

	public static void setTypesNode(HashMap<Integer, String> typesNode) {
		Node.typesNode = typesNode;
	}

	public static void main(String[] args) {
		Node n = new Node("e;47319;'colonne vertébrale';1;260");
		System.out.println(n.toString());
	}

	public String toString() {
		return "Node [id=" + id + ", name=" + nom + ", type=" + type + ", poids=" + poids + "]";
	}
	
	public static Node getNodeFromString(String name) {
		for (Node n : main.Main.nodesH.values()) {
			if (n.nom.equals(name)) return n;
		}
		
		return null;
	}
	
	
	
	

}
