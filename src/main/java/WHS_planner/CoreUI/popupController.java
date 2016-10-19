package WHS_planner.CoreUI;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class popupController implements Initializable{

    @FXML
    private AnchorPane sportsPopupAnchorPane;

    @FXML
    private JFXButton addListButton;

    @FXML
    private JFXTreeTableView treeView;

    private ObservableList<SportsEvent> listViewContents;
    private Stage stage = null;
    private HashMap<String, Object> result = new HashMap<String, Object>();

    public popupController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void plusButtonPressed(MouseEvent event) {
        System.out.println(new SportsEvent(new Date(), new Date(), "test").toString());

    }

    public HashMap<String, Object> getResult() {
        return this.result;
    }

    /**
     * setting the stage of this view
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Closes the stage of this view
     */
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }

    class SportsEvent extends RecursiveTreeObject<SportsEvent>{
//        private Date date;
//        private String event;
        private SimpleDateFormat dateFormat;
        private SimpleDateFormat timeFormat;
        private JFXTextField dateField = new JFXTextField();
        private JFXTextField timeField = new JFXTextField();
        private JFXTextField eventField = new JFXTextField();

        Date time;
        Date date;
        StringProperty event;

        private SportsEvent(Date d, Date t, String s){
            date = d;
            time = t;
            event = new SimpleStringProperty(s);
            dateFormat = new SimpleDateFormat("h:mm a");
            timeFormat = new SimpleDateFormat("EEE, MMM d");

            dateFormat.parse(date.toString(), new ParsePosition(0));
            timeFormat.parse(time.toString(), new ParsePosition(0));

            dateField.setText(dateFormat.format(date));
            timeField.setText(timeFormat.format(date));
            eventField.setText(event.toString());


        }


        public StringProperty getEventDate() {
            return new SimpleStringProperty(dateFormat.format(date));
        }

        public StringProperty getEventTime() {
            return new SimpleStringProperty(timeFormat.format(time));
        }

        @Override
        public String toString() {
            return timeFormat.format(time) + " " + dateFormat.format(date) +
                    ": " + event;
        }
    }
}
