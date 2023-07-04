/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnGrandoMax"
    private Button btnGrandoMax; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="txtRank"
    private TextField txtRank; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMovie"
    private ComboBox<Movie> cmbMovie; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	
    	if (this.cmbMovie.getValue() == null) {
    		txtResult.setText("Scegli un film!");
    		return;
    	}
    	
    	List<Movie> soluzione = model.getSequenza(this.cmbMovie.getValue());
    	
    	txtResult.setText("CAMMINO:\n");
    	
    	for (Movie m : soluzione)
    		txtResult.appendText(m.toString() + '\n');
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	if (this.txtRank.getText() == null) {
    		txtResult.setText("Inserisci un rank");
    		return;
    	}
    	double rank;
    	try {
    		rank = Double.parseDouble(txtRank.getText());
    		if (rank<0) {
    			txtResult.setText("Inserisci un rank >= 0");
    			return;
    		}
    	}
    	catch(Exception e) {
    		txtResult.setText("Inserisci un valore numerico");
    		return;
    	}
    	
    	SimpleWeightedGraph<Movie,DefaultWeightedEdge> graph = model.creaGrafo(rank);
    	
    	txtResult.setText("Grafo creato con " + graph.vertexSet().size() + " vertici e " +
    			graph.edgeSet().size() + " archi.\n\n");
    	
    	cmbMovie.getItems().addAll(graph.vertexSet());
    	
    	this.btnCammino.setDisable(false);
    	this.btnGrandoMax.setDisable(false);
    }

    @FXML
    void doGradoMax(ActionEvent event) {
    	
    	txtResult.setText("FILM DI GRADO MASSIMO:\n");
    	this.txtResult.appendText(model.getMax().toString() + " (" + model.getWeight() + ")\n");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGrandoMax != null : "fx:id=\"btnGrandoMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtRank != null : "fx:id=\"txtRank\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMovie != null : "fx:id=\"cmbMovie\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.btnCammino.setDisable(true);
    	this.btnGrandoMax.setDisable(true);
    }
}
