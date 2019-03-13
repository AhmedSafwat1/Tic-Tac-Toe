/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejava;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author safwat
 */
public class playerController implements Initializable {
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
    private Line line;
    //connection
    Socket s;
    PrintStream pw;
    DataInputStream dis;
     int  f1 ;
    listnerServer th = null;
    // array from button
    private final ArrayList<Button> btns = new ArrayList<>(9);
    private  ArrayList<Button> btnsCpy ;
    private Vector<String> Player1Step =  new Vector<>();
    private Vector<String> Player2Step =  new Vector<>();
    //Button[] btnArray = new Button[9];
    // attribute use
    private volatile boolean turnFlag = true;
    private volatile int turn  = 1;
    private volatile boolean gameFlag = true;
    public volatile String playerName1 = "Name"; 
    private volatile String playerName2 = "PC"; 
    private int mode = 1;
    private boolean display =false;
    private String Winer = "No-One";
    public boolean recordState = false;
    private volatile String Type1 = "X";
    private volatile String Type2 = "O";
    public int u_id;
    
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
    @FXML
    private void Exit(ActionEvent event)
    {
        try {
             FXMLLoader x = new FXMLLoader();
             x.setLocation(getClass().getResource("Home.fxml"));
             Parent root = x.load();
             HomeController c = x.getController();
             c.playerlogin = playerName1;
             c.id_user = u_id;
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
                    pst.execute();
                    con.close();
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
                 if(recordState && !display)
                 {
                     Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning ..");
                    alert.setHeaderText("Look, a Warning Not commplete the game ");
                    alert.setContentText("Careful The record not save!");

                    alert.showAndWait();
                 }
             }
             //pw.close();
             //dis.close();
            // s.close();
             Stage stage =(Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     //convertVector to string
    public String conv_Vect(Vector<String> x)
    {
        
        return String.join(",", x);
    }
     @FXML
    private void reset(ActionEvent event)
    {
        turnFlag = true;
        gameFlag = true;
        stautus.setText("Game Start "+playerName1 + " will Play X");
        btns.forEach((x)->{
            x.setText("");
            x.setDisable(false);
            
        });
        Player1Step.clear();
        Player2Step.clear();
        again.setVisible(false);
        
    }
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(recordState)
            labelRecord.setText("Record ..");
        
        
        Button temp =  (Button)event.getSource();
        //computer
        System.out.println(turnFlag);
        pw.println(playerName1+","+f1+","+temp.getId()+","+playerName2);
        
        
          
                  
                    
                
        

        
    }
    //private using method
    //disable button untill
    private void disableBtn()
    {
        
        btnsCpy.forEach(x->x.setDisable(true));
    }
    private void enableBtn()
    {
        btnsCpy.forEach(x->x.setDisable(false));
    }
    // player 1 play
    private void player1(Button temp ,String type ,String name)
    {
         temp.setDisable(true);
         btnsCpy.remove(temp);
         Player1Step.add(temp.getId().replace("b", type));
         temp.setTextFill(Color.GREEN);
         temp.setText(type);
         if(type.equals("X"))
         {
           stautus.setText("Turn of "+name + " Play O");
           stautus.setTextFill(Color.RED);
         }
         else
         {
           stautus.setText("Turn of "+name + " Play X");
           stautus.setTextFill(Color.GREEN);
         }
            
    }
    //test
    public synchronized void addOne()
    {
        turnFlag = !turnFlag;
    }
    public synchronized boolean get()
    {
        return turnFlag;
    }
    // player 2 play
    private void player2(Button temp,String type,String name)
    {
         temp.setDisable(true);
          btnsCpy.remove(temp);
         temp.setTextFill(Color.RED);
         
         temp.setText(type);
         Player2Step.add(temp.getId().replace("b", type));
         if(type.equals("X"))
         {
           stautus.setText("Turn of "+name + " Play O");
           stautus.setTextFill(Color.RED);
         }
         else
         {
             stautus.setText("Turn of "+name + " Play X");
             stautus.setTextFill(Color.GREEN);
         }
         //stautus.setText("Turn of "+playerName1 + " Play X");
         
    }
   // Check winer
    private boolean CheckWinner(String TypePlyer)
    {
        if(b1.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b9.getText().equals(TypePlyer) )
        {
            playwinAnimation(6);
            return true;
        }
        else if(b3.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b7.getText().equals(TypePlyer) )
        {
            playwinAnimation(7);
            return true;
            
        }
        else if(b1.getText().equals(TypePlyer) && b2.getText().equals(TypePlyer) && b3.getText().equals(TypePlyer) )
        {
            playwinAnimation(1);
            return true;
        }
        else if(b4.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b6.getText().equals(TypePlyer) )
        {
            playwinAnimation(4);
            return true;
        }
        else if(b7.getText().equals(TypePlyer) && b8.getText().equals(TypePlyer) && b9.getText().equals(TypePlyer) )
        {
            playwinAnimation(5);
            return true;
        }
        else if(b1.getText().equals(TypePlyer) && b4.getText().equals(TypePlyer) && b7.getText().equals(TypePlyer) )
        {
            playwinAnimation(2);
            return true;
        }
        else if(b2.getText().equals(TypePlyer) && b5.getText().equals(TypePlyer) && b8.getText().equals(TypePlyer) )
        {
            playwinAnimation(8);
            
            return true;
        }
        else if(b3.getText().equals(TypePlyer) && b6.getText().equals(TypePlyer) && b9.getText().equals(TypePlyer) )
        {
            playwinAnimation(9);
            return true;
        }
        else
            return false;
    }
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
    // check if game end
    private boolean checkEndGame()
    {
        return(Player1Step.size() + Player2Step.size() == 9);     
    }
    //computer player
   
    public void  makeGame(String player1Name,String player2Name,int _mode)
    {
        playerName1 = player1Name;
        playerName2 = player2Name;
        labelName1.setText(player1Name + " : X");
        labelName2.setText(player2Name + " : O");
        mode = _mode;
    }
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
     //function for alert
    private void showAlert(String Message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
 
        // alert.setHeaderText("Results:");
        alert.setContentText(Message);
 
        alert.showAndWait();
    }
    public void prepareConnection(String player1Name ,String ip ) 
    {
        playerName1 = playerName1;
        disableBtn();
        System.out.println(ip);
        Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    disableBtn();
                    stautus.setText("connection .... ");
                    labelName1.setText(playerName1);
                    labelName2.setText("......");
                    
                }
            });
       
        Thread t =  new Thread()
        {
            @Override
            public void run() {
                try
                {
                    s =  new Socket(ip, 5000);
                    pw = new PrintStream(s.getOutputStream());
                    dis = new DataInputStream(s.getInputStream());
                    pw.println("name,"+playerName1);
                    String mesg = dis.readLine();
                    
                    playerName2 = mesg.split(",")[1];
                       
                    f1 = Integer.parseInt(mesg.split(",")[2].substring(1));
                    setf(f1);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            labelName2.setText(playerName2);
                        }
                    });
                    if(f1 == 1)
                    {
                        Platform.runLater(new  Runnable() {
                            @Override
                            public void run() {
                                stautus.setText("Game Start "+playerName1 + " will Play X");
                                labelName1.setText(playerName1+ ": X");
                                labelName2.setText(playerName2+ " : O ");
                                enableBtn();
                                
                            }
                        });
                        
                    }
                    else if(f1 == 2)
                    {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stautus.setText("Game Start "+playerName2 + " will Play X");
                                labelName1.setText(playerName1+ ": O");
                                labelName2.setText(playerName2+ " : X ");
                                System.out.println("name2"+playerName2);
                                System.out.println("name1"+playerName1);
                                System.out.println("!if 2"+f1);
                                turnFlag = false;
                                disableBtn();
                            }
                        });
                    }
                    

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            new listnerServer().start();
                        }
                    });
                }
                catch (IOException ex) {
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!gameFlag)
                                showAlert("server Shudown 1! pleaz Exit");
                            disableBtn();
                            
                        }
                    });
                    
                }
            }
            
        };
        t.start();
        if(!t.isAlive())
        {
            
        }


                        
             
         
             
             
            
             
             
             
        
    }
    synchronized  int  getF()
    {
        return f1;
    }
    synchronized  void  setf(int d)
    {
         f1 = d;
    }
    class listnerServer extends Thread
    {

        @Override
        public void run() {
           String mesage  = "";
            int f3 = -1;
           while(gameFlag && !s.isClosed())
           {
               
               try {
                   
                     mesage = dis.readLine();
                     final String mmmm = mesage;
                    
                    if(mesage != null || !mesage.isEmpty())
                    {
                        turn = Integer.parseInt(mesage.split(",")[1]);
                        String x = mesage.split(",")[4];
                        //setf(f3);
                        
                        Button temp = btns.get(Integer.parseInt(mesage.split(",")[2].substring(1))-1);
                        if(x.equals("X"))
                        {
                            
                            System.out.println("X");
                                  Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   enableBtn();
                                    player1(temp, "X",mmmm.split(",")[3]);
                                    if(CheckWinner("X"))
                                    {

                                        gameFlag = !gameFlag;
                                        if(f1 == 1)
                                            Winer = playerName1;
                                        else
                                            Winer = playerName2;
                                        stautus.setText("The Player "+mmmm.split(",")[0] + " Winner");
                                        stautus.setTextFill(Color.GREEN);
                                        //again.setVisible(true);
                                         disableBtn();


                                    }
                                    if(checkEndGame() && gameFlag)
                                    {
                                        gameFlag = !gameFlag;
                                        stautus.setText("The Game End");
                                        stautus.setTextFill(Color.AQUA);
                                       // again.setVisible(true);
                                         disableBtn();

                                    }
                                    if(f1 == turn )
                                    {
                                        disableBtn();
                                    }
                                    else
                                    {
                                        if(gameFlag)
                                            enableBtn();
                                    }
                                        turnFlag = !turnFlag;
                                    //disableBtn();
                                }
                            });
                        }
                        else if(x.equals("O"))
                        {
                            enableBtn();
                            //System.out.println("O");
                             Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   if(turnFlag)
                                    {
                                       // enableBtn();
                                    }
                                   player2(temp, "O",mmmm.split(",")[3]);
                                   if(CheckWinner("O"))
                                    {
                                        gameFlag = !gameFlag;
                                         gameFlag = !gameFlag;
                                         if(f1 == 2)
                                            Winer = playerName2;
                                        else
                                            Winer = playerName1;
                                        stautus.setText("Tahe Player "+mmmm.split(",")[0] + " Winner");
                                        stautus.setTextFill(Color.BLUE);
                                        disableBtn();
                                        //again.setVisible(true);
                                    }
                                    if(checkEndGame()&& gameFlag)
                                    {
                                        stautus.setText("The Game End");
                                        stautus.setTextFill(Color.AQUA);
                                        disableBtn();
                                       // again.setVisible(true);
                                    }
                                    if(f1 == turn && gameFlag)
                                    {
                                        disableBtn();
                                    }
                                    else
                                    {
                                        if(gameFlag)
                                          enableBtn();
                                    }
                                        
                                         turnFlag = !turnFlag;
                                    //disableBtn();
                                }
                            });
                                    
                        }
                    }
                    else
                    {
                        
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    showAlert("User logout ! pleaz Exit");
                                   disableBtn();
                                }
                            });
                            break;
                    }
               } catch (IOException ex) {
                  
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(gameFlag);
                                    if(gameFlag)
                                        showAlert("server Close ! ");
                                   disableBtn();
                                }
                            });
                             break;
               }
               catch(NullPointerException ex)
               {
                
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   showAlert("server Shudown! goodby");
                                   
                                   disableBtn();
                                }
                            });
                 break;
               }
                       
                      
               
           }
        }
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // handl the name 
        labelName1.setText(playerName1 + " : X");
        System.out.println(turnFlag);
        line.setVisible(false);
        labelName1.setTextFill(Color.GREEN);
        labelName2.setText(playerName2 + " : O");
        labelName2.setTextFill(Color.RED);
        btns.addAll(Arrays.asList(b1,b2,b3,b4,b5,b6,b7,b8,b9));
        btnsCpy = (ArrayList<Button>) btns.clone();
        stautus.setText("Game Start "+playerName1 + " will Play X");
       
        
    }    
    
}
