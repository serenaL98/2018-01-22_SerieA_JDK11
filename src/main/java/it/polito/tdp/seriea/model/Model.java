package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private List<Team> squadre;
	private List<AnnataPunteggio> pareggi;
	private List<AnnataPunteggio> vittorie;
	private List<AnnataPunteggio> punteggi;
	
	//grafo semplice, pesato, orietato
	private Graph<String, DefaultWeightedEdge> grafo;
	
	private List<String> vertici;
	
	public Model() {
		this.dao = new SerieADAO();
		this.squadre = this.dao.listTeams();
		this.vertici = new ArrayList<String>();
		this.punteggi = new ArrayList<>();
		
	}
	
	public List<Team> elencoSquadre(){
		return this.squadre;
	}
	
	public List<AnnataPunteggio> prendiPareggio(String squadra){
		this.pareggi = this.dao.prendiPareggi(squadra);
		return this.pareggi;
	}
	
	public List<AnnataPunteggio> prendiVittorie(String squadra){
		this.vittorie = this.dao.prendiVittorie(squadra);
		return this.vittorie;
	}
	
	public String stampaPunteggioSquadra(List<AnnataPunteggio> pare, List<AnnataPunteggio> vitt) {
		String stampa = "";
		AnnataPunteggio nuovo = null;
		int punti = 0;
		
		for(AnnataPunteggio a: pare) {
			for(AnnataPunteggio b: vitt) {
				if(a.getAnno().equals(b.getAnno())) {
					punti = (a.getPunti() + b.getPunti());
					nuovo = new AnnataPunteggio(a.getAnno(), punti, a.getStag());
					stampa += nuovo.toString()+"\n";
				}
			}
		}
	
		return stampa;
	}

	public List<AnnataPunteggio> listaCompleta(String squadra){
		int punti = 0;
		
		AnnataPunteggio nuovo = null;
		
		for(AnnataPunteggio a : this.pareggi) {
			for(AnnataPunteggio b: this.vittorie) {
				if(a.getAnno().equals(b.getAnno())) {
					punti = (a.getPunti() + b.getPunti());
					nuovo = new AnnataPunteggio(a.getAnno(), punti, a.getStag());
					this.punteggi.add(nuovo);
				}
			}
		}
		return this.punteggi;
	}
	
	public void creaGrafo(String squadra) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//VERTICI
		this.vertici = (this.dao.prendiStagioni(squadra));
		
		this.punteggi = this.listaCompleta(squadra);
		Graphs.addAllVertices(this.grafo, this.vertici);
		System.out.println(this.punteggi);
		
		//ARCHI
		int peso = 0;
		for(AnnataPunteggio a: this.punteggi) {
			for(AnnataPunteggio b :this.punteggi) {
				if(!a.getAnno().equals(b.getAnno())) {
					if(a.getPunti()> b.getPunti()) {
						peso = a.getPunti()-b.getPunti();
						Graphs.addEdge(this.grafo, a.getStag(), b.getStag(), peso);
					}
				}
			}
		}
		
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public int calcolaPesi(String annata) {
		
		int uscenti = 0;
		int entranti = 0;
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(annata)) {
			uscenti += this.grafo.getEdgeWeight(e);
		}
		for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(annata)) {
			entranti += this.grafo.getEdgeWeight(e);
		}
		
		return entranti-uscenti;
	}
	
	public String annataDoro() {
		int max = 0;
		String stampa = "";
		
		for(String s: this.grafo.vertexSet()) {
			if(this.calcolaPesi(s)>max) {
				max = this.calcolaPesi(s);
				stampa = s+ " "+max;
			}
		}
		
		return stampa;
	}
	
	//-------PUNTO 2-------
	private List<AnnataPunteggio> soluzione;
	private int migliore;
	
	public String camminoVirtuoso() {
		//inizializzo
		this.soluzione = new ArrayList<>();
		this.migliore = 0;
		
		List<AnnataPunteggio> parziale = new ArrayList<>();
		parziale.add(punteggi.get(0));
		List<AnnataPunteggio> rimanenti = new ArrayList<>(this.punteggi);
		ricorsione(parziale, 0, rimanenti);
		
		String stampa = "";
		for(AnnataPunteggio a: soluzione) {
			stampa += a.toString()+"\n";
		}
		
		return stampa;
	}
	
	public void ricorsione(List<AnnataPunteggio> parziale, int livello, List<AnnataPunteggio> rimanenti) {
		//caso finale
		if(rimanenti.size()==0) {
			//if(parziale.get(parziale.size()-1).getPunti()>migliore) {
				//migliore = parziale.get(parziale.size()-1).getPunti();
				this.soluzione = new ArrayList<>(parziale);
			//}
		}
		
		//caso intermedio
		List<AnnataPunteggio> copia = new ArrayList<>(rimanenti);
		
		for(AnnataPunteggio aa: rimanenti) {
			//se quello che vorrei aggiungere è precedente all'ultimo nella lista del parziale
			if(this.consecutivi(aa, parziale.get(parziale.size()-1), this.punteggi)==true && aa.getPunti()>migliore) {
				System.out.println("CIAO");
				migliore = aa.getPunti();
				parziale.add(aa);
				copia.remove(aa);
				ricorsione(parziale, livello+1, copia);
				parziale.remove(aa);
				
			}
			
		}
	}
	
	public boolean consecutivi(AnnataPunteggio attuale,AnnataPunteggio precedente, List<AnnataPunteggio> lista) {
		
		for(int i=1; i<=lista.size(); i++) {
			if(lista.get(i-1).equals(precedente) && lista.get(i).equals(attuale)) {
				return true;
			}
		}
		return false;
	}
}
