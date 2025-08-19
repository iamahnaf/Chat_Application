package ChatApp_Client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button buttonSend;
    @FXML
    private TextField MessageField;
    @FXML
    private VBox vbMain;
    @FXML
    private ScrollPane spMain;
    @FXML
    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            client = new Client(new Socket("localhost",5001));
            System.out.println("Conneted to server");
        }catch (Exception e){
            e.printStackTrace();

        }

        vbMain.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
               spMain.setVvalue((Double) newValue);
            }
        });

        client.recieveMessageFromServer(vbMain);

        buttonSend.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String messageToSend = MessageField.getText();
                if(!messageToSend.isEmpty()){
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5,5,5,10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px");

                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.965));

                    hBox.getChildren().add(textFlow);
                    vbMain.getChildren().add(hBox);

                    client.sendMessageToServer(messageToSend);
                    MessageField.clear();

                }
            }
        });
    }

    public static void addLabel(String msgFromServer,VBox vbox){

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPadding(new Insets(5,5,5,10));

        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
                "-fx-background-color: rgb(233,233,235);" +
                        "-fx-background-radius: 20px");

        textFlow.setPadding(new Insets(5,10,5,10));
        hb.getChildren().add(textFlow);


        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                vbox.getChildren().add(hb);
            }
        });

    }

}
