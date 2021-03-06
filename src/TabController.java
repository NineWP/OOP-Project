import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TabController extends Controller{

    @FXML
    private Tab add;

    @FXML
    private TabPane tabPane;

    public void initialize(){

        add.setGraphic(new ImageView(new Image(TabController.class.getResource("add.png").toString(),14,14,true,false)));
        tabPane.getTabs().add(0, createNewTab("New tab"));
        tabPane.getSelectionModel().select(0);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab == add) {
                Tab t = createNewTab("New tab");
                tabPane.getTabs().add(tabPane.getTabs().size()-1, t);
                tabPane.getSelectionModel().select(t);
            }
        });
    }

    public Tab createNewTab(String name){
        Tab tab = new Tab(name);
        try {
            FXMLLoader loader = new FXMLLoader(TabController.class.getResource("screen.fxml"));
            Parent root = loader.load();
            Controller con = (Controller)loader.getController();
            con.currentTab = tab;
            tab.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }
}