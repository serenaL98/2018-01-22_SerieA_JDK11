package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.AnnataPunteggio;
import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {

    	txtResult.clear();
    	
    	Team squadra = this.boxSquadra.getValue();
    	if(squadra == null) {
    		txtResult.setText("Selezionare una squadra da menù.\n");
    		return;
    	}
    	List<AnnataPunteggio> pare = new ArrayList<AnnataPunteggio>();
    	pare = this.model.prendiPareggio(squadra.getTeam());
    	List<AnnataPunteggio> vitt = new ArrayList<AnnataPunteggio>();
    	vitt = this.model.prendiVittorie(squadra.getTeam());
    	
    	txtResult.appendText("PUNTEGGI COMPLESSIVI:\n"+this.model.stampaPunteggioSquadra(pare, vitt));
    	this.model.creaGrafo(squadra.getTeam());
    	
    	txtResult.appendText("\n\n#VERTICI: "+this.model.numeroVertici());
    	txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    	
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	
    	txtResult.appendText("\n\nL'annata d'oro è: "+this.model.annataDoro());
    	
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    	
    	txtResult.appendText("\n\nIl cammino virtuoso è:\n"+this.model.camminoVirtuoso());

    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSquadra.getItems().addAll(this.model.elencoSquadre());
	}
}
