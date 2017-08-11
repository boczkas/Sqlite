/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author przem
 */
public class SqliteController implements Initializable {

    public ArrayList<SqlModel> sqlModels = new ArrayList<SqlModel>();
    private ObservableList<ObservableList> data;
    
    @FXML
    private Label isConnected;
    
    @FXML
    private TreeView<String> treeView;
    
    @FXML
    private TableView tableView;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override 
    public void initialize(URL url, ResourceBundle rb) {
        

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Kliknieto myszke");
                int index = treeView.getSelectionModel().getSelectedIndex();
                System.out.println(index);
                tableView.getColumns().clear();
                try {
                    fillTableViewWithContent(index);
                } catch (SQLException ex) {
                    Logger.getLogger(SqliteController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
        });
    }    
    
    public void Login(ActionEvent event) throws SQLException{
        
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose database directory");
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = chooser.showDialog(null);
        tableView.getColumns().clear();
        
        if (file != null) {
            System.out.println("absolute chosen path = " + file.getAbsolutePath());
        }

        ArrayList<String> fileList;
        fileList = TreeViewController.getDataBaseFilesListFromDirectory(file.getAbsolutePath());
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        treeView.setShowRoot(false);
        treeView.setRoot(root);

        for(int i = 0; i < fileList.size(); i++){
            TreeItem<String> treeItem = new TreeItem<>(fileList.get(i));
            System.out.println(treeItem.toString());
            root.getChildren().add(treeItem);
            System.out.println("Path to file = " + file.getAbsolutePath() + "\\" + fileList.get(i));
            
            sqlModels.add(new SqlModel(file.getAbsolutePath() + "\\" + fileList.get(i)));
            
            if(sqlModels.get(i).isLogin()){
                isConnected.setText("Connected");
            }
            else{
                isConnected.setText("Not Connected");
            }
        }
        
        fillTableViewWithContent(0);
    }
    
    public void fillTableViewWithContent(int index) throws SQLException{
        try (ResultSet resultSet = sqlModels.get(index).getResultSet()) {
            data = FXCollections.observableArrayList();
            
            for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++){
                final int j = i;
                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                
                tableView.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }
            
            String oneRowFromResultSet;
            while (resultSet.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                
                for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++){
                    oneRowFromResultSet = resultSet.getString(i);
                    if(resultSet.wasNull()) oneRowFromResultSet = "";
                    row.add(oneRowFromResultSet);
                }
                System.out.println("Row [1] added " + row );
                data.add(row);
            }
            
            tableView.setItems(data);
            treeView.getSelectionModel().select(index);
        }
    }
}
