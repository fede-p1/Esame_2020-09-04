package it.polito.tdp.imdb.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO dao;
	SimpleWeightedGraph<Movie,DefaultWeightedEdge> graph;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public SimpleWeightedGraph<Movie,DefaultWeightedEdge> creaGrafo(double rank){
		
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		List<Movie> vertex = new ArrayList<>(dao.listMovies());
		
		Graphs.addAllVertices(graph, vertex);
		
		Map<Integer,Movie> movieMap = new HashMap<>();
		
		for (Movie m : vertex) 
			movieMap.put(m.getId(), m);
		
		dao.setActors(movieMap, rank);
		
		/*for (Movie m : vertex) {
			System.out.println(m.getActors().size() + '\n');
		}*/
		
		for (Movie m1 : graph.vertexSet())
			for (Movie m2 : graph.vertexSet())
				if (!m1.equals(m2) && !graph.containsEdge(m2, m1)) {
					Set<Integer> diff = new HashSet<>(m1.getActors());
					diff.retainAll(m2.getActors());
					if (diff.size()>0){
						Set<Integer> tot = new HashSet<>(m1.getActors());
						System.out.println("m1: " + tot.size() + " + ");
						System.out.println("m2: " + m2.getActors().size() + " = ");
						tot.addAll(m2.getActors());
						System.out.println(tot.size() + '\n');
						if (tot.size()>0)
							Graphs.addEdge(graph, m1, m2, tot.size());
					}
				}
		
		for (DefaultWeightedEdge edge : graph.edgeSet())
			System.out.println(graph.getEdgeWeight(edge) + '\n');
		
		return graph;			
		
	}
	double weightMax;
	public ImdbDAO getDao() {
		return dao;
	}

	public void setDao(ImdbDAO dao) {
		this.dao = dao;
	}

	public SimpleWeightedGraph<Movie, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public void setGraph(SimpleWeightedGraph<Movie, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public double getWeight() {
		return weightMax;
	}

	public void setWeight(double weight) {
		this.weightMax = weight;
	}

	public Movie getMax() {
		
		weightMax = 0;
		
		Movie result = null;
		
		for (Movie m : graph.vertexSet()) {
			double peso=0;
			for (Movie m2 : Graphs.neighborListOf(graph, m)) {
				peso+= graph.getEdgeWeight(graph.getEdge(m2, m));	
			}
			if (peso>weightMax) {
				weightMax = peso;
				result=m;
			}
		}
		
		return result;
				
	}
	
	List<Movie> soluzione;
	
	public List<Movie> getSequenza(Movie m){
		
		List<Movie> parziale = new ArrayList<>();
		soluzione = new ArrayList<>();
		parziale.add(m);
		
		ricorsiva(parziale,0);
		
		return soluzione;
		
	}
	
	private void ricorsiva(List<Movie> parziale, double pesoCorrente) {
		
		if (parziale.size()>soluzione.size()) {
			soluzione = new ArrayList<>(parziale);
		}
		
		Movie current = parziale.get(parziale.size()-1);
		/*if (parziale.size()>1)
			pesoCorrente = graph.getEdgeWeight(graph.getEdge(parziale.get(parziale.size()-2), current));
		*/
		for (Movie m : Graphs.neighborListOf(graph, current)) {
			if (graph.getEdgeWeight(graph.getEdge(m, current)) > pesoCorrente) {
				parziale.add(m);
				ricorsiva(parziale,graph.getEdgeWeight(graph.getEdge(m, current)));
				parziale.remove(parziale.size()-1);
			}
		}
			
		
		
	}
}
