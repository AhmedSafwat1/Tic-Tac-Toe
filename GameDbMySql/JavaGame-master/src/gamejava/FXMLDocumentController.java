/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejava;


import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author safwat
 */
public class FXMLDocumentController implements Initializable {
    //button
    @FXML
    private Button exit;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;
    @FXML
    private Button b4;
    @FXML
    private Button b5;
    @FXML
    private Button b6;
    @FXML
    private Button b7;
    @FXML
    private Button b8;
    @FXML
    private Button b9;
    @FXML
    private Button again;
    @FXML
    private Button exitCong;
    // label
    @FXML
    private  Label stautus;
    @FXML
    private Label labelName1;
    @FXML
    private Label labelName2;
    @FXML
    private Label labelRecord;
    @FXML
    private Label popermessage;
    @FXML
    private Line line;
    @FXML
    private AnchorPane poper;
    
    // array from button
    private final ArrayList<Button> btns = new ArrayList<>(9);
    private  ArrayList<Button> btnsCpy ;
    private Vector<String> Player1Step =  new Vector<>();
    private Vector<String> Player2Step =  new Vector<>();
    //Button[] btnArray = new Button[9];
    // attribute use
    private boolean turnFlag = true;
    private boolean gameFlag = true;
    public String playerName1 = "Name1"; 
    private String playerName2 = "PC"; 
    private int mode = 1;
    private boolean display =false;
    private String Winer = "No-One";
    public boolean recordState = false;
    public int u_id;
    
     //connection server
    Socket s;
    PrintStream pw;
    DataInputStream dis;
    
    // databse connect
    public Connection con;
    public ResultSet rs;
    public PreparedStatement pst;
    //conect db sq
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
    @FXML
    private void ExitPoper(MouseEvent event)
    {
        poper.setVisible(false);
    }
    // function for player
   void popperAler(String Type)
    {
        if(Type.equals("X"))
        {
            popermessage.setText(playerName1+" Winner");
        }
        else
        {
            popermessage.setText(playerName2+" Winner");
        }
        String musicFile = "won.wav";     // For sound
        new Thread(){
            @Override
            public void run() {
                AudioClip win = new AudioClip(getClass().getResource("images/won.wav").toString());
                win.play();
            }
           
        }.start();
        
        poper.setVisible(true);
    }
    @FXML
    private void Exit(ActionEvent event) //for exit 
    {
        poper.setVisible(false); //hide poper
        try {
             FXMLLoader x = new FXMLLoader();
             x.setLocation(getClass().getResource("Home.fxml"));
             Parent root = x.load();
             HomeController c = x.getController();
             c.playerlogin = playerName1;
             c.id_user = u_id;
             System.out.println(c.id_user);
             boolean f = true;
             if(!gameFlag && recordState)
             {
                 c.PutRecod(Player1Step, Player2Step,playerName1,playerName2,Winer);
                 System.out.println(u_id);
                 conectionDB();
                 try {
                    pst = con.prepareStatement("insert into game(winner,player1,player2,againt,user_id) values(?,?,?,?,?)");
                    pst.setString(1, Winer);
                    pst.setString(2, conv_Vect(Player1Step));
                    pst.setString(3, conv_Vect(Player2Step));
                    pst.setString(4, playerName2);
                    pst.setInt(5, u_id);
                     System.out.println("hi"+u_id);
                    pst.executeUpdate();
                    //con.close();
                } catch (SQLException ex) {
                     ex.printStackTrace();
                     f = false;
                     showAlert("Don save Th record");
                }
                 if(f)
                 {
                     showAlert("sava Record sucess");
                 }
             }
             else
             {
                 if(recordState && !display)//alert for winner
                 {
                     Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning ..");
                    alert.setHeaderText("Look, a Warning Not commplete the game ");
                    alert.setContentText("Careful The record not save!");

                    alert.showAndWait();
                 }
             }
             Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        poper.setVisible(false);
    }
     @FXML
    private void reset(ActionEvent event)
    {
        poper.setVisible(false);
        boolean f5 = true;
        System.out.println("id"+u_id);
        if(!gameFlag && recordState)
             {
                 
                 System.out.println(u_id);
                 conectionDB();
                 try {
                    pst = con.prepareStatement("insert into game(winner,player1,player2,againt,user_id) values(?,?,?,?,?)");
                    pst.setString(1, Winer);
                    pst.setString(2, conv_Vect(Player1Step));
                    pst.setString(3, conv_Vect(Player2Step));
                    pst.setString(4, playerName2);
                    pst.setInt(5, u_id);
                  
                    pst.executeUpdate();
                    //con.close();

                } catch (SQLException ex) {
                     ex.printStackTrace();
                     f5 = false;
                     showAlert("Don save Th record");
                }
                 if(f5)
                 {
                     showAlert("sava Record sucess");
                 }
             }
        turnFlag = true;
        gameFlag = true;
        line.setVisible(false);
        stautus.setText("Game Start "+playerName1 + " will Play X");
        btns.forEach((x)->{
            x.setText("");
            x.setDisable(false);
            
        });
        btnsCpy = (ArrayList<Button>) btns.clone();
        Player1Step.clear();
        Player2Step.clear();
        again.setVisible(false);
        
    }
    @FXML
    private void handleButtonAction(ActionEvent event) { 
        if(recordState)
            labelRecord.setText("Record ..");
        System.out.println(""+ b1.getLayoutX());

        Button temp =  (Button)event.getSource();
        //computer
        
        if(mode == 1)
        {
            disableBtn();
            if(turnFlag && gameFlag)
            {
                player1(temp);
                if(CheckWinner("X"))
                {

                    gameFlag = !gameFlag;
                    Winer = playerName1;
                    stautus.setText("The Player "+playerName1 + " Winner");
                    stautus.setTextFill(Color.GREEN);
                    again.setVisible(true);


                }
                if(checkEndGame() && gameFlag)
                {
                    gameFlag = !gameFlag;
                    stautus.setText("The Game End");
                    stautus.setTextFill(Color.AQUA);
                    again.setVisible(true);

                }
                turnFlag = !turnFlag;
            }
             if(gameFlag && !turnFlag)
                {
                    
                   new Thread(){
                       @Override
                       public void run() {
                           
                            try {
                                sleep(2000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                           Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           
                           PcPlayer("X");
                        if(CheckWinner("O"))
                        {
                            gameFlag = !gameFlag;
                            Winer = playerName2;
                            stautus.setText("The Player "+playerName2 + " Winner");
                            stautus.setTextFill(Color.RED);
                            again.setVisible(true);
                        }
                        if(checkEndGame()&& gameFlag)
                        {
                            gameFlag = !gameFlag;
                            stautus.setText("The Game End");
                            stautus.setTextFill(Color.AQUA);
                            again.setVisible(true);
                        }
                        turnFlag = !turnFlag;
                        enableBtn();
                        }
                    });
                       }
                       
                   }.start();
                    
                }
        }
        else // friend
        {
            if(turnFlag && gameFlag)
            {
                player1(temp);
                if(CheckWinner("X"))
                {

                    gameFlag = !gameFlag;
                    Winer = playerName1;
                    stautus.setText("The Player "+playerName1 + " Winner");
                    stautus.setTextFill(Color.GREEN);
                    again.setVisible(true);


                }
                if(checkEndGame() && gameFlag)
                {
                    gameFlag = !gameFlag;
                    stautus.setText("The Game End");
                    stautus.setTextFill(Color.AQUA);
                    again.setVisible(true);

                }
                turnFlag = !turnFlag;
            }
             else
            {
                if(gameFlag)
                {
                    player2(temp);
                    if(CheckWinner("O"))
                    {
                        gameFlag = !gameFlag;
                        Winer = playerName2;
                        stautus.setText("Tahe Player "+playerName2 + " Winner");
                        stautus.setTextFill(Color.BLUE);
                        again.setVisible(true);
                    }
                    if(checkEndGame()&& gameFlag)
                    {
                        stautus.setText("The Game End");
                        stautus.setTextFill(Color.AQUA);
                        again.setVisible(true);
                    }
                    turnFlag = !turnFlag;
                }
            }
        }

        
    }
    //private using method
    //disable button untill
    private void disableBtn()
    {
        
        btnsCpy.forEach(x->x.setDisable(true));
    }
    private void enableBtn()//enable button
    {
        btnsCpy.forEach(x->x.setDisable(false));
    }
    // player 1 play
    private void player1(Button temp)
    {
         temp.setDisable(true);
         btnsCpy.remove(temp);
         Player1Step.add(temp.getId().replace("b", "x"));
         temp.setTextFill(Color.GREEN);
         temp.setText("X");
         stautus.setText("Turn of "+playerName2 + " Play O");
         stautus.setTextFill(Color.RED);
    }
    // player 2 play
    private void player2(Button temp)
    {
         temp.setDisable(true);
          btnsCpy.remove(temp);
         temp.setTextFill(Color.RED);
         temp.setText("O");
         Player2Step.add(temp.getId().replace("b", "o"));
         stautus.setText("Turn of "+playerName1 + " Play X");
         stautus.setTextFill(Color.GREEN);
    }
    // Check winer
    private boolean CheckWinner(String TypePlyer)
    {
        if(b1.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b9.getText().equals(TypePlyer) )
        {
            playwinAnimation(6);
            popperAler(TypePlyer);
            return true;
        }
        else if(b3.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b7.getText().equals(TypePlyer) )
        {
            playwinAnimation(7);
            popperAler(TypePlyer);
            return true;
            
        }
        else if(b1.getText().equals(TypePlyer) && b2.getText().equals(TypePlyer) && b3.getText().equals(TypePlyer) )
        {
            playwinAnimation(1);
            popperAler(TypePlyer);
            return true;
        }
        else if(b4.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b6.getText().equals(TypePlyer) )
        {
            playwinAnimation(4);
            popperAler(TypePlyer);
            return true;
        }
        else if(b7.getText().equals(TypePlyer) && b8.getText().equals(TypePlyer) && b9.getText().equals(TypePlyer) )
        {
            playwinAnimation(5);
            popperAler(TypePlyer);
            return true;
        }
        else if(b1.getText().equals(TypePlyer) && b4.getText().equals(TypePlyer) && b7.getText().equals(TypePlyer) )
        {
            playwinAnimation(2);
            popperAler(TypePlyer);
            return true;
        }
        else if(b2.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b8.getText().equals(TypePlyer) )
        {
            playwinAnimation(8);
            popperAler(TypePlyer);
            return true;
        }
        else if(b3.getText().equals(TypePlyer) && b6.getText().equals(TypePlyer) && b9.getText().equals(TypePlyer) )
        {
            playwinAnimation(9);
            popperAler(TypePlyer);
            return true;
        }
        else
            return false;
    }
    // check if game end
    private boolean checkEndGame()
    {
        return(Player1Step.size() + Player2Step.size() == 9);     
    }
   //convertVector to string
    public String conv_Vect(Vector<String> x)
    {
        
        return String.join(",", x);
    }
    //computer player
    private void PcPlayer(String player)
    {
        Random rn = new Random();
        int step = rn.nextInt(9) ;
        String another = "X";
        if(player.equals("X"))
           another = "O";
       
        
        while(btns.get(step).getText().equals(player) || btns.get(step).getText().equals(another))
        {
            step = rn.nextInt(9);
        }
        System.out.println(step);
        if(player.equals("X"))
           player2(btns.get(step));
        else
            player1(btns.get(step));
    }
//    draw line for winner
    private void playwinAnimation(int i ){
        line.setVisible(true);
        switch(i)
        {
            case 1 :
                line.setStartX(-36);
                line.setStartY(104);
                line.setEndX(239);
                line.setEndY(104);
                
                break;
            case 2 :
                line.setStartX(-36);
                line.setStartY(104);
                line.setEndX(-36);
                line.setEndY(345);
                
                break;
             case 3 :
                line.setLayoutX(0);
                line.setLayoutY(139);
                line.setStartX(-36);
                line.setStartY(104);
                line.setEndX(243);
                line.setEndY(104);
                
                break;
                case 4 :
                line.setLayoutX(216);
                line.setLayoutY(218);
                line.setStartX(-27);
                line.setStartY(104);
                line.setEndX(248);
                line.setEndY(104);
                
                break;
                case 5 :
                line.setLayoutX(216);
                line.setLayoutY(330);
                line.setStartX(-36);
                line.setStartY(104);
                line.setEndX(243);
                line.setEndY(104);
                
                break;
                case 6 :
                line.setLayoutX(216);
                line.setLayoutY(330);
                line.setStartX(-30);
                line.setStartY(-120);
                line.setEndX(263);
                line.setEndY(128);
                
                break;
                case 7 :
                line.setLayoutX(216);
                line.setLayoutY(330);
                line.setStartX(247);
                line.setStartY(-124);
                line.setEndX(-23);
                line.setEndY(122);
                
                break;
                case 8 :
                line.setLayoutX(216);
                line.setLayoutY(330);
                line.setStartX(108);
                line.setStartY(-129);
                line.setEndX(108);
                line.setEndY(119);
                
                break;
                case 9 :
                line.setLayoutX(350);
                line.setLayoutY(329);
                line.setStartX(108);
                line.setStartY(-129);
                line.setEndX(108);
                line.setEndY(119);
                
                break;
        }
    }
    public void  makeGame(String player1Name,String player2Name,int _mode)
    {
        playerName1 = player1Name;
        playerName2 = player2Name;
        labelName1.setText(player1Name + " : X");
        labelName2.setText(player2Name + " : O");
        mode = _mode;
    }
    // show the last record
    public void makeRecod(String player1Name,String player2Name,Vector<String> play1 , Vector<String> play2,String Winner)
    {
        display = true;
        playerName1 = player1Name;
        playerName2 = player2Name;
        Winer = Winner;
        labelName1.setText(playerName1 + " : X");
        labelName2.setText(playerName2 + " : O");
        Player1Step.clear();
        Player2Step.clear();
        Player1Step = (Vector)play1.clone();
        Player2Step = (Vector)play2.clone();
        System.out.println("1 : "+Player1Step.size());
        System.out.println("2 : "+Player2Step.size());
         btns.forEach((x)->{
            x.setText("");
            x.setDisable(true);
            
        });
        class rec extends Thread
        {

            @Override
            public void run() {
                int count = 0;
                int size1  = Player1Step.size();
                int size2 = Player2Step.size();
                int mainSize = size1 > size2 ? size1 : size2;
                 while(count < mainSize)
                 {
                      
                  try {
                     
                        try{
                          
                            
                             int index = Integer.parseInt(Player1Step.get(count).substring(1))-1;
                             Platform.runLater(new Runnable() {
                                 @Override
                                 public void run() {
                                    btns.get(index).setTextFill(Color.GREEN);
                                    btns.get(index).setText("X");
                                    stautus.setText("Turn of "+playerName2 + " Play O");
                                    stautus.setTextFill(Color.RED);
                                 }
                             });
                             
//          
                        }
                        catch(IndexOutOfBoundsException ex){}

                        if(count == size1-1 && size1 > size2)
                            break;
                        sleep(2000);
                        try
                        {
                          int index = Integer.parseInt(Player2Step.get(count).substring(1))-1;
                            Platform.runLater(new Runnable() {
                                 @Override
                                 public void run() {
                                     
                                     btns.get(index).setTextFill(Color.RED);
                                     btns.get(index).setText("O");
                                     stautus.setText("Turn of "+playerName1 + " Play X");
                                     stautus.setTextFill(Color.GREEN);
                                     if(size2 == mainSize)
                                     {
                                        
                                     }
                                 }
                             });
                            sleep(2000);
                        }
                        catch(IndexOutOfBoundsException ex){}
                        
                        
                    } catch (InterruptedException ex  ) {}
                    
                     count++;
                 }
                 gameFlag = !gameFlag;
                 Platform.runLater(new Runnable() {
                                 @Override
                                 public void run() {
                                    stautus.setText("Finsih Winner is  "+Winner );
                                    stautus.setTextFill(Color.AQUA);
                                 }
                             });
                 
            }
            
        }
        new rec().start();
    }
    private void showAlert(String Message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
 
        // alert.setHeaderText("Results:");
        alert.setContentText(Message);
 
        alert.showAndWait();
    }
    public void prepareConnection()
    {
        try {
             disableBtn();
             stautus.setText("connection .... ");
             s =  new Socket(InetAddress.getLocalHost(), 5000);
             pw = new PrintStream(s.getOutputStream());
             dis = new DataInputStream(s.getInputStream());
             pw.println("name,"+playerName1);
             String te = dis.readLine();
             String[] Name = te.split(",");
             playerName2 = Name[1];
             
             
             
         } catch (IOException ex) {
            ex.printStackTrace();
         }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // handl the name 
        poper.setVisible(false);
        labelName1.setText(playerName1 + " : X");
       
        line.setVisible(false);
        labelName1.setTextFill(Color.GREEN);
        labelName2.setText(playerName2 + " : O");
        labelName2.setTextFill(Color.RED);
        btns.addAll(Arrays.asList(b1,b2,b3,b4,b5,b6,b7,b8,b9));
        btnsCpy = (ArrayList<Button>) btns.clone();
        stautus.setText("Game Start "+playerName1 + " will Play X");
        
    }    
    
}
