package graph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Edge {

	//private String relation;
	private int poids,idRelation;
	private String type;
	private Node x,y;
	public static HashMap<Integer, String> typesEdge = new HashMap<Integer, String>() {
	{
	    put(0, "r_associated");
	    put(3, "r_domain");
	    put(4, "r_pos");
	    put(6, "r_isa");
	    put(8, "r_hypo");
	    put(9, "r_has_part");
	    put(10, "r_holo");
	    put(11, "r_locution");
	    put(12, "r_flpot");
	    put(13, "r_agent");
	    put(15, "r_lieu");
	    put(17, "r_carac");
	    put(18, "r_data");
	    put(19, "r_lemma");
	    put(20, "r_has_magn");
	    put(23, "r_carac-1");
	    put(24, "r_agent-1");
	    put(27, "r_domain-1");
	    put(28, "r_lieu-1");
	    put(32, "r_sentiment");
	    put(33, "r_error");
	    put(36, "r_infopot");
	    put(68, "r_set>item");
	    put(72, "r_syn_strict");
	    put(73, "r_is_smaller_than");
	    put(74, "r_is_bigger_than");
	    put(102, "r_can_eat");
	    put(115, "r_sentiment-1");
	    put(128, "r_node2relnode");
	    put(200, "r_context");
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
	
	



}
