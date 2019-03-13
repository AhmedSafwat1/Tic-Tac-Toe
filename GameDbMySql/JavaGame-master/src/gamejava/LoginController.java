/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejava;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author safwat
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button login;
    @FXML
    Button register;
    @FXML
    TextField name;
    @FXML
    PasswordField pass;
    @FXML
    Label info;
    // databse
    public Connection con;
    public ResultSet rs;
    public PreparedStatement pst;
    private void conectionDB()
    {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tictac","root","");
            System.out.println("connect sucess");
        } catch (SQLException e) {
           
          info.setText("Error in connection ...");
        }
    }
    @FXML
    private void goLogin(ActionEvent event) 
    {
           boolean flag = true;
        int id_u =0;
        conectionDB();
         try {
            pst = con.prepareStatement("select id,name from users where name = ? and password = ? limit 1");
            pst.setString(1, name.getText());
            pst.setString(2, pass.getText());
            rs = pst.executeQuery();
            if (!rs.next() ) {
                flag = false;
            } 
                
        } catch (SQLException ex) {
             System.out.println(ex.getMessage());
            info.setText("Error in Login Plz try again...");
            flag = false;
        }
         if(flag)
         {
             try {
             FXMLLoader x = new FXMLLoader();
             x.setLocation(getClass().getResource("Home.fxml"));
             Parent root = x.load();
             HomeController c = x.getController();
             c.playerlogin = rs.getString("name");
             c.id_user =rs.getInt("id");
                 System.out.println( c.id_user);
             c.makeLogin(rs.getString("name"), rs.getInt("id"));
             Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }catch (SQLException ex) {
                   Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
               }
         }
         else
         {
             info.setText("Error in Login password or  name not vaild");
         }
    }
    @FXML
    private void goRegister(ActionEvent event)//register
    {
        boolean flag = true;
        int id_u =0;
        String messgage = "";
        
        conectionDB();
        
         try {
             if(name.getText().isEmpty() || pass.getText().isEmpty())
            {
                messgage = "Forget passord or name";
                throw  new SQLException();
            }
            pst = con.prepareStatement("insert into users(name,password) values(?,?)",Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name.getText());
            pst.setString(2, pass.getText());

            id_u = pst.executeUpdate();
             System.out.println("");
            
            
        } catch (SQLException ex) {
            info.setText("Error in Register Plz try again ...");
            flag = false;
        }
         if(flag)
         {
             try {
             FXMLLoader x = new FXMLLoader();
             x.setLocation(getClass().getResource("Home.fxml"));
             Parent root = x.load();
             HomeController c = x.getController();
             c.playerlogin = name.getText();
             c.id_user =id_u;
             c.makeLogin(name.getText(), id_u);
             Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
         }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
