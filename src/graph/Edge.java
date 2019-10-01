package graph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Edge {

	private String relation;
	private int poids,idRelation;
	private Node x,y;

	public Edge(String rel){
		this.relation = rel;

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



	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
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



}
