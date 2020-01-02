package graph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class Edge {

	//private String relation;
	private int poids,idRelation;
	private String type;
	private Node x,y;
	public static HashMap<Integer, String> typesEdge = new HashMap<Integer, String>() {
	{
		/*
		 * Avant ajout
		 *   put(0, "n_generic");
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
		 */
	    put(0, "r_associated");
	    put(1,"r_raff_sem");
	    put(3, "r_domain");
	    put(4, "r_pos");
	    put(5,"r_syn");
	    put(6, "r_isa");
	    put(7,"r_anto");
	    put(8, "r_hypo");
	    put(9, "r_has_part");
	    put(10, "r_holo");
	    put(11, "r_locution");
	    put(12, "r_flpot");
	    put(13, "r_agent");
	    put(14,"r_patient");
	    put(15, "r_lieu");
	    put(16,"r_instr");
	    put(17, "r_carac");
	    put(18, "r_data");
	    put(19, "r_lemma");
	    put(20, "r_has_magn");
	    put(21,"r_has_antimagn");
	    put(23, "r_carac-1");
	    put(24, "r_agent-1");
	    put(25,"r_instr-1");
	    put(26,"r_patient-1");
	    put(27, "r_domain-1");
	    put(28, "r_lieu-1");
	    put(30,"r_lieu_action");
	    put(31,"r_action_lieu");
	    put(32, "r_sentiment");
	    put(33, "r_error");
	    put(34,"r_manner");
	    put(35,"r_meaning/glose");
	    put(36, "r_infopot");
	    put(37,"r_telic_role");
	    put(38,"r_agentif_role");
	    put(39,"r_verbe-action");
	    put(41,"r_conseq");
	    put(42,"r_causatif");
	    put(44,"r_verbe-adj");
	    put(49,"r_time");
	    put(50,"r_object>mater");
	    put(51,"r_mater>object");
	    put(52,"r_successeur-time");
	    put(53,"r_make");
	    put(54,"r_product_of");
	    put(55,"r_against");
	    put(56,"r_against-1");
	    put(57,"r_implication");
	    put(59,"r_masc");
	    put(60,"r_fem");
	    put(61,"r_equiv");
	    put(62,"r_manner-1");
	    put(64,"r_has_instance");
	    put(65,"r_verb_real");
	    put(67,"r_similar");
	    put(68, "r_set>item");
	    put(69,"r_item>set");
	    put(70,"r_processus>agent");
	    put(71,"r_variante");
	    put(72, "r_syn_strict");
	    put(73, "r_is_smaller_than");
	    put(74, "r_is_bigger_than");
	    put(75,"r_accomp");
	    put(76,"r_processus>patient");
	    put(80,"r_processus>instr");
	    put(100,"r_has_auteur");
	    put(102, "r_can_eat");
	    put(106,"r_color");
	    put(107,"r_cible");
	    put(108,"r_symptomes");
	    put(109,"r_predecesseur-time");
	    put(110,"r_diagnostique");
	    put(115, "r_sentiment-1");
	    put(119,"r_but");
	    put(121,"r_own");
	    put(122,"r_own-1");
	    put(127,"r_incompatible");
	    put(128, "r_node2relnode");
	    put(129,"r_require");
	    put(130,"r_is_instance_of");
	    put(131,"r_is_concerned_by");
	    put(132,"r_symptomes-1");
	    put(133,"r_units");
	    put(134,"r_promote");
	    put(135,"r_circumstances");
	    put(136,"r_has_auteur-1");
	    put(137,"r_processus>agent-1r_processus>agent-1");
	    put(138,"r_processus>patient-1");
	    put(139,"r_processus>instr-1");
	    put(150,"r_beneficiaire");
	    put(153,"r_prop");
	    put(155,"r_make_use_of");
	    put(156,"r_is_used_by");
	    put(159,"r_adj-adv");
	    put(160,"r_adv-adj");
	    put(161,"r_homophone");
	    put(163,"r_concerning");
	    put(164,"r_adj>nom");
	    put(165,"r_nom>adj");
	    put(200, "r_context");
	    put(333,"r_translation");
	    put(444, "r_link");
	    put(555, "r_cooccurrence");
	    put(666, "r_aki");
	    put(777, "r_wiki");
	    put(999, "r_inhib");
	}};

	public Edge(String rel){
		
		String[] elsrel = rel.split(";");
		
		idRelation = Integer.valueOf(elsrel[1]);
		x = main.Main.nodesH.get(Integer.valueOf(elsrel[2]));
		y = main.Main.nodesH.get(Integer.valueOf(elsrel[3]));
		type = typesEdge.get(Integer.valueOf(elsrel[4]));
		poids = Integer.valueOf(elsrel[5]);

	}

	public void getRel(){
		File fichierRel = new File("./Relations/Relations.txt");
		BufferedReader br = null;
		String line = "";
		String lineSplitBy = "\n";

		try {
			br = new BufferedReader(new FileReader(fichierRel));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while ((line = br.readLine()) != null){
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPoids() {
		return poids;
	}
	public void setPoids(int poids) {
		this.poids = poids;
	}
	public int getIdRelation() {
		return idRelation;
	}
	public void setIdRelation(int idRelation) {
		this.idRelation = idRelation;
	}
	public Node getX() {
		return x;
	}
	public void setX(Node x) {
		this.x = x;
	}
	public Node getY() {
		return y;
	}
	public void setY(Node y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Edge [poids=" + poids + ", idRelation=" + idRelation + ", type=" + type + ", x=" + x + ", y=" + y + "]";
	}
	
	public static ArrayList<Edge> getRelationsFromX(HashMap<Integer,graph.Edge> edges, graph.Node node) {
		ArrayList<Edge> ret = new ArrayList<Edge>();
		
		for (Edge e : edges.values()) {
			if (e.getX().equals(node)) ret.add(e);
		}
		
		return ret;
	}
	
	public static ArrayList<Edge> getRelationsFromY(HashMap<Integer,graph.Edge> edges, graph.Node node) {
		ArrayList<Edge> ret = new ArrayList<Edge>();
		
		for (Edge e : edges.values()) {
			if (e.getY().equals(node)) ret.add(e);
		}
		
		return ret;
	}

	public boolean containsX(String x) {
		return x.equals(this.getX().getName());
	}
	
	public boolean containsY(String y) {
		return y.equals(this.getY().getName());
	}
	



}
