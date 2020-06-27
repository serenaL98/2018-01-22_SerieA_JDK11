package it.polito.tdp.seriea.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		for( AnnataPunteggio a: m.prendiPareggio("Bologna"))
			System.out.println(a.toString()+"\n");
		for( AnnataPunteggio a: m.prendiVittorie("Bologna"))
			System.out.println(a.toString()+"\n");
		
		System.err.println(m.listaCompleta("Bologna"));
	}

}
