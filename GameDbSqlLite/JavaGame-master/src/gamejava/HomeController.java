/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejava;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.PATTERN;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**background_image.jpg
 * FXML Controller class
 *
 * @author safwat
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button pc;
    @FXML
    Button friendPlay;
    @FXML
    Button online;
    @FXML
    Button record;
   
    // record
    private Vector<String> Player1StepRecod =  new Vector<>();
    private Vector<String> Player2StepRecod =  new Vector<>();
    private String recodPlayer1 = "";
    private String recodPlayer2 = "";
    private String WinnerName="";
    public String playerlogin = "User";
    public String playerfriend = "Unknown";
    public int id_user=0;
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
           
            showAlert("Error not Connect");
        }
    }
    private void showAlert(String Message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
 
        // alert.setHeaderText("Results:");
        alert.setContentText(Message);
 
        alert.showAndWait();
    }
    
    @FXML
    private void goPc(ActionEvent event)
    {
       
        try {
             FXMLLoader x = new FXMLLoader();
             x.setLocation(getClass().getResource("FXMLDocument.fxml"));
             Parent root = x.load();
             FXMLDocumentController c = x.getController();
             System.out.println(id_user);
              System.out.println(playerlogin);
             c.u_id =id_user;
             c.playerName1 = playerlogin;
            // System.out.println(id_user);
             Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Want To Recod The Game");
            alert.setHeaderText("Record the game to watch Later");
            alert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                c.recordState = true;
            } 
             c.makeGame(playerlogin, "PC", 1);
             Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // connect sqllit 
    private void connect() {
     
         con = null;
        try {
            
            con = DriverManager.getConnection("jdbc:sqlite:tictac"); 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //return con;
    }
     @FXML
    private void gorecord(ActionEvent event)
    {
        
            boolean db = true;
            
            try {
                //conectionDB();
                connect();
                try {
                pst = con.prepareStatement("select * from game");
                rs = pst.executeQuery();
                
                 if (!rs.next() ) {
                    db = false;
                } 
                 while(rs.next()){  //rs is result set
                    //lastValue = ...;
                     System.out.println(rs.getString("player1"));
                    
                     {
                    
                        WinnerName = rs.getString("winner");
                        recodPlayer2 = rs.getString("againt");
                        recodPlayer1 = playerlogin;
                        Player1StepRecod = new Vector<String>(Arrays.asList(rs.getString("player1").split(",")));
                        Player2StepRecod = new Vector<String>(Arrays.asList(rs.getString("player2").split(",")));

                    }
                }
                
               
            } catch (SQLException ex) {
                 ex.printStackTrace();
                 db  = false;
            }
             if(Player1StepRecod.isEmpty() || Player2StepRecod.isEmpty())
             {
                 System.out.println("not record yet");
                  Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning ..");
                    alert.setHeaderText("Look, a Warning Not have Any Record ");
                    alert.setContentText("Record The next Game Thanks ..");

                    alert.showAndWait();
             }
             else
             {
                
                FXMLLoader x = new FXMLLoader();
                x.setLocation(getClass().getResource("FXMLDocument.fxml"));
                Parent root = x.load();
                FXMLDocumentController c = x.getController();
                c.u_id = id_user;
                c.makeRecod(recodPlayer1, recodPlayer2, Player1StepRecod, Player2StepRecod,WinnerName);
                Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
             }
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
     private void goFriend(ActionEvent event)
    {
       
        try {
             FXMLLoader x = new FXMLLoader();
             x.setLocation(getClass().getResource("FXMLDocument.fxml"));
             Parent root = x.load();
             FXMLDocumentController c = x.getController();
             Alert alert = new Alert(AlertType.CONFIRMATION);
             System.out.println(id_user);
             System.out.println(playerlogin);
             c.u_id =id_user;
             c.playerName1 = playerlogin;
             alert.setTitle("Want To Recod The Game");
             alert.setHeaderText("Record the game to watch Later");
             alert.setContentText("Are you ok with this?");
 
             Optional<ButtonType> result = alert.showAndWait();
             if (result.get() == ButtonType.OK){
                // ... user chose OK
                c.recordState = true;
             } 
             //get friend name
             TextInputDialog dialog = new TextInputDialog("");
             dialog.setTitle("Friend Name");
             dialog.setHeaderText("Look, a Text Input ");
             dialog.setContentText("Please enter your Friend name:");

            // Traditional way to get the response value.
            Optional<String> namePlayer2 = dialog.showAndWait();
            if (namePlayer2.isPresent() && !namePlayer2.get().isEmpty()){
                playerfriend = namePlayer2.get();
            }
             c.makeGame(playerlogin, playerfriend, 0);
             Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      @FXML
     private void out(MouseEvent event) 
    {
        FXMLLoader x = new FXMLLoader();
               
        try {
             x.setLocation(getClass().getResource("login.fxml"));
             Parent root;
            root = x.load();
                Stage stage2 = (Stage) record.getScene().getWindow();
                stage2.close();
                Stage stage =new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
     @FXML
     private void goOnline(ActionEvent event)
    {
       String ip = "127.0.0.1";
        try {
             FXMLLoader x = new FXMLLoader();
             x.setLocation(getClass().getResource("player.fxml"));
             Parent root = x.load();
             playerController c = x.getController();
             Alert alert = new Alert(AlertType.CONFIRMATION);
             System.out.println(id_user);
             System.out.println(playerlogin);
             c.u_id =id_user;
             c.playerName1 = playerlogin;
             alert.setTitle("Want To Recod The Game");
             alert.setHeaderText("Record the game to watch Later");
             alert.setContentText("Are you ok with this?");
 
             Optional<ButtonType> result = alert.showAndWait();
             if (result.get() == ButtonType.OK){
                // ... user chose OK
                c.recordState = true;
             } 
//             asdda

             //get friend name
             TextInputDialog dialog = new TextInputDialog("");
             dialog.setTitle("Friend Name");
             dialog.setHeaderText("Look, a Text Input ");
             dialog.setContentText("Please enter your IP || now work localhost only:");

            // Traditional way to get the response value.
            
            Optional<String> namePlayer2 = dialog.showAndWait();
            //System.out.println(validate(namePlayer2.get()));
            if (namePlayer2.isPresent() && !namePlayer2.get().isEmpty() && validate(namePlayer2.get()) ){
                ip = namePlayer2.get();
            }
            
            if(!namePlayer2.isPresent() || !validate(namePlayer2.get()) )
            {
                 Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                alert3.setTitle("Message");

                // alert.setHeaderText("Results:");
                alert3.setContentText("Will play in local host Temporary ....");

                alert3.showAndWait();
            }

            
             c.prepareConnection(playerlogin, ip);
             Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public  boolean validate( String strIP) {
          
            if (strIP == null || strIP.isEmpty()) return false;
            strIP = strIP.trim();
            if ((strIP.length() < 6) & (strIP.length() > 15)) return false;
            return strIP.matches("^(?:(?:25[1-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
}
     public void PutRecod(Vector<String> play1 , Vector<String> play2,String name1,String name2, String W)
     {
         Player1StepRecod.clear();
         Player2StepRecod.clear();
         Player1StepRecod = (Vector)play1.clone();
         Player2StepRecod = (Vector)play2.clone();
         recodPlayer1 = name1;
         recodPlayer2 = name2;
         WinnerName  = W;
         //Player1StepRecod.forEach((x)->System.out.println(x));
     }
     public void makeLogin(String user_nname,int id)
     {
         playerlogin = user_nname;
         id_user = id;
         
     }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
