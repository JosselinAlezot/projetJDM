package graph;


import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	
	public int id;
	public String nom, type, poids; // pour les termes
	public boolean valid;
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
	
	
	/*
	 * Structure de la node : e ; id ; 'nom' ; type ; poids ; 'formatted name' eventuellement
	 * 		Node n = new Node("e;47319;'colonne vertébrale';1;260");
	 */
	public Node(String node) {
		
		//String[] elsnode = node.split(";");
		String[] varRecupName = node.split("'");
		String[] elsnode1 = varRecupName[0].split(";");
		
		this.valid = (elsnode1.length==2);
		if (!valid) return;
		
		int indiceName = 1;
		String tempname = "";
		
		while (!varRecupName[indiceName+1].startsWith(";")) {
			tempname += varRecupName[indiceName]+"'";
			indiceName++;
		}
		
		String[] elsnode2 = varRecupName[indiceName+1].split(";");
		
		this.id = Integer.valueOf(elsnode1[1]);
		setName(tempname+varRecupName[indiceName]);
		this.type = typesNode.get(Integer.valueOf(elsnode2[1]));
		this.poids = elsnode2[2];
	}
	
	public boolean isValid() {
		return this.valid;
	}
	
	// Ne sert que dans les cas où on rencontre un mot non trouvable, n'arrivera jamais en théorie sauf dans des cas de tests précis
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
		//String[] sep = name.split("'");
		this.nom = name;
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

	/*
	 * Pour tester les créations de nodes individuellement
	 */
	public static void main(String[] args) {
		Node n = new Node("e;47319;'colonne vertébrale';1;260");
		Node n3 = new Node("e;47319;'colonne vertébrale test de l'apostrophe';1;260");
		Node n2 = new Node("e;12207332;'t(9;22)(q34.1;q11)(ABL1,BCR) b3a2 transcrit de fusion/transcrit de référence (échelle internationale)';1;0");
		System.out.println(n.toString());
		System.out.println(n2.toString());
		System.out.println(n3.toString());
	}

	public String toString() {
		return "Node [id=" + id + ", name=" + nom + ", type=" + type + ", poids=" + poids + "]";
	}
	
	/*
	 * Obtention d'un node souhaité en fonction de son nom
	 */
	public static Node getNodeFromString(String name) {
		for (Node n : main.Main.nodesH.values()) {
			if (n.nom.equals(name)) return n;
		}
		
		return null;
	}
	
	
	
	

}
