package vadique.khpi.year2.stats.third;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.math3.distribution.FDistribution;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.converter.DoubleStringConverter;

public class FisherController implements Initializable {

	private ObservableList<Double> obsListX;
	private ObservableList<Double> obsListY;
	

	@FXML Button buttonAnalyze;
	@FXML Button buttonRemoveX;
	@FXML Button buttonAddX;
	@FXML Button buttonAddY;
	@FXML Button buttonRemoveY;
	@FXML Button buttonClear;
	@FXML TextField textFieldXMean;
	@FXML TextField textFieldYMean;
	@FXML TextField textFieldXVariance;
	@FXML TextField textFieldYVariance;
	@FXML TextField textFieldFCrit;
	@FXML TextField textFieldFReal;
	@FXML TextField textFieldConfidLevel;
	@FXML TableColumn<Double,Double> tableColumnX;
	@FXML TableColumn<Double,Double> tableColumnY;
	@FXML ListView<Double> listViewX;
	@FXML ListView<Double> listViewY;
	@FXML Label labelTestConclusion;
	@FXML Label labelXN;
	@FXML Label labelYN;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listViewX.setPlaceholder(new Label(""));
		listViewX.setCellFactory(TextFieldListCell.forListView(new DoubleStringConverter()));
		listViewY.setPlaceholder(new Label(""));
		listViewY.setCellFactory(TextFieldListCell.forListView(new DoubleStringConverter()));
		obsListX = FXCollections.observableArrayList(2d,3d,3d,3d,4d,4d,5d,2d,3d,3d,3d,4d,4d,5d,2d,3d,3d,3d);
		obsListY = FXCollections.observableArrayList(3d,3d,3d,4d,4d,5d,2d,3d,3d,3d,4d,4d,3d,3d,3d,4d,4d,5d,3d,3d,4d,4d,5d);
		initListView();
	}
	
	private void initList() {
		List<Double> listX = new ArrayList<Double>();
		obsListX = FXCollections.observableList(listX);
		List<Double> listY = new ArrayList<Double>();
		obsListY = FXCollections.observableList(listY);
	}
	
	private void initListView() {
		listViewX.setItems(obsListX);
		listViewY.setItems(obsListY);
	}

	@FXML public void doAddX(ActionEvent event) {
		if (obsListX == null) {
			initList();
		}
		obsListX.add(0.0);
		initListView();
	}

	@FXML public void doRemoveX(ActionEvent event) {
		if (obsListX == null) {
            return;
        }
        if (obsListX.size() > 0) {
            obsListX.remove(obsListX.size() - 1);
        }
        if (obsListX.size() <= 0) {
            obsListX = null;
        }
	}

	@FXML public void doAddY(ActionEvent event) {
		if (obsListY == null) {
			initList();
		}
		obsListY.add(0.0);
		initListView();
	}

	@FXML public void doRemoveY(ActionEvent event) {
		if (obsListY == null) {
            return;
        }
        if (obsListY.size() > 0) {
        	obsListY.remove(obsListY.size() - 1);
        }
        if (obsListY.size() <= 0) {
        	obsListY = null;
        }
	}

	@FXML public void doClear(ActionEvent event) {
		textFieldXMean.setText("");
		textFieldYMean.setText("");
		textFieldXVariance.setText("");
		textFieldYVariance.setText("");
		textFieldFCrit.setText("");
		textFieldFReal.setText("");
		textFieldConfidLevel.setText("");
		listViewX.setItems(null);
		listViewY.setItems(null);
		initList();
		initListView();
	}

	@FXML public void doAnalyze(ActionEvent event) {
		if (obsListX.size()  <= 1 || obsListY.size()  <= 1 || 
				textFieldConfidLevel.getText().length() <= 0) {
			return;
		}
		updateLabels();
		updateTextFields();
	}
	
	public double getSampleMean(ObservableList<Double> list) {
		double total = 0;
		for(int i = 0; i < list.size(); i++) {
			total += list.get(i);
		}
		return total / list.size();
	}
	
	public double getSampleVar(ObservableList<Double> list) {
		double sum = 0;
		double mean = getSampleMean(list);
		for(int i = 0; i < list.size(); i++) {
			double xi = list.get(i) - mean;
			sum += xi * xi;
		}
		return sum / (list.size()-1);
	}
	
	public double getFCrit() {
		int xSize = obsListX.size();
		int ySize = obsListY.size();
		double xVar = getSampleVar(obsListX);
		double yVar = getSampleVar(obsListY);
		double confid = Double.parseDouble(textFieldConfidLevel.getText());
		FDistribution fCrit;
		if(xVar > yVar) {
			fCrit = new FDistribution(xSize-1,ySize-1);
		}
		else {
			fCrit = new FDistribution(ySize-1,xSize-1);
		}
		return fCrit.inverseCumulativeProbability(1-confid/2);
	}
	
	public double getFReal() {
		double xVar = getSampleVar(obsListX);
		double yVar = getSampleVar(obsListY);
		if (xVar > yVar)
			return xVar/yVar;
		else
			return yVar/xVar;
	}
	
	public String getConclusion() {
		if (getFReal() > getFCrit()) {
			return "Null hypothesis is rejected. Vx != Vy";
		}
		else
			return "Null hypothesis is accepted. Vx ~ Vy";
	}
	
	
	private void updateTextFields() {
		textFieldXMean.setText(getSampleMean(obsListX)+"");
		textFieldXVariance.setText(getSampleVar(obsListX)+"");
		textFieldYMean.setText(getSampleMean(obsListY)+"");
		textFieldYVariance.setText(getSampleVar(obsListY)+"");
		textFieldFReal.setText(getFReal()+"");
		textFieldFCrit.setText(getFCrit()+"");
		labelTestConclusion.setText(getConclusion());
	}
	
	private void updateLabels() {
		labelXN.setText("nx: " + obsListX.size());
		labelYN.setText("ny: " + obsListY.size());
	}
	
	
}
