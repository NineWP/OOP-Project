import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;

public class Controller {

    @FXML
    private Button btBack;

    @FXML
    private Button btForward;

    @FXML
    private Button btLoadPage;

    @FXML
    private Button btRefreshPage;

    @FXML
    private Button btZoomIn;

    @FXML
    private Button btZoomOut;

    @FXML
    private TextField textField;

    @FXML
    private WebView webView;

    private WebEngine engine;

    private String homePage;

    private double webZoom;

    private WebHistory history;

    public Tab currentTab;

    @FXML
    public void initialize() {
        webView.getEngine().locationProperty().addListener((ov, oldstr, newstr) ->{
            textField.setText(newstr);
        });
        webView.getEngine().getLoadWorker().stateProperty().addListener((obs,oldvalue,newvalue) -> {
            if(newvalue == State.SUCCEEDED){
                currentTab.setText(webView.getEngine().getTitle());
            }
       }); 

        homePage = "www.google.com";
        textField.setText(homePage);
        loadPage();
        webZoom = 1;

        webView.setOnKeyPressed((key) ->{
            if (key.getCode() == KeyCode.F12) {
                showSource(null);
            }
        }
        );
    }

    public void loadPage() {
        // engine.load("http://www.google.com");
        //engine.load("http://" + textField.getText());
        String URL = textField.getText();

        if (!URL.contains(".")) {
            webView.getEngine().load("https://www.google.com/search?q=" + URL);
            return;
        }
        if (!URL.startsWith("http://") && !URL.startsWith("https://")) {
            URL = "https://" + URL;
        }

        webView.getEngine().load(URL);
    }

    @FXML
    void addNewTab(ActionEvent event) {
        currentTab.getTabPane().getSelectionModel().selectLast();
    }    

    public void refreshPage() {
        webView.getEngine().reload();
    }

    public void zoomIn() {
        webZoom += 0.25;
        webView.setZoom(webZoom);
    }

    public void zoomOut() {
        webZoom -= 0.25;
        webView.setZoom(webZoom);
    }

    public void displayHistory() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();

        for (WebHistory.Entry entry : entries) {
            // System.out.println(entry);
            System.out.println(entry.getUrl() + " " + entry.getLastVisitedDate());
        }
    }

    public void back() {
        webView.getEngine().getHistory().go(-1);
    }

    public void forward() {
        webView.getEngine().getHistory().go(1);
    }

    public void showText(String title, Stage window, String text) {
        TextArea root = new TextArea(text);
        Scene secondScene = new Scene(root, 600, 600);
        Stage secondWindow = new Stage();
        secondWindow.setTitle(title);
        secondWindow.setScene(secondScene);
        secondWindow.initOwner(window);
        secondWindow.show();
    }

    @FXML
    void showSource(ActionEvent event) {
        showText("Source of " + webView.getEngine().getTitle(), (Stage) webView.getScene().getWindow() ,(String)webView.getEngine().executeScript("document.documentElement.outerHTML"));
    }
}

