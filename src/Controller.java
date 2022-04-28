import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    private TextField textField;

    @FXML
    private WebView webView;

    private WebEngine engine;

    private String homePage;

    private double webZoom;

    private WebHistory history;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        engine = webView.getEngine();
        homePage = "www.google.com";
        textField.setText(homePage);
        webZoom = 1;
        loadPage();

        webView.setOnKeyPressed((key) ->{
            if (key.getCode() == KeyCode.F12) {
                showSource(null);
            }
        }
        );
    }

    public void loadPage() {
        // engine.load("http://www.google.com");
        engine.load("http://" + textField.getText());
    }

    public void refreshPage() {
        engine.reload();
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
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        history.go(-1);
        textField.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

    public void forward() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        history.go(1);
        textField.setText(entries.get(history.getCurrentIndex()).getUrl());
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

