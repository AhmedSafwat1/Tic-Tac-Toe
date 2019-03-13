/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author safwat
 */
public class ServerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button runServer;
    @FXML
    Label st;
    @FXML
    Button stopServer;
    // my variable
   private  ServerSocket serverSocket;
   private  Socket s;
   private  int flag =1;
   private boolean flageGame =false;
   private Player p1=null;
   private Player p2=null;
   private Thread Th = null;
  Vector<Player>  players = new  Vector<>();
    @FXML
    private void putOnline()
    {
        // handle show
        stopServer.setVisible(true);
        runServer.setVisible(false);
        st.setVisible(true);
        
        //handle server
        try {
            serverSocket = new ServerSocket(5000);
        } 
        catch (IOException e1) {
           Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   showAlert("Port using");
                                   //disableBtn();
                                }
                            });
        }
      Th = new Thread()
       {
            @Override
            public void run() {
                 while(true)
                {
                        try {
                            System.out.println("wait"+flag);
                                s = serverSocket.accept();
                                new Player(s, "X");
                                if(flag % 2 == 0)
                                    makeGame(flag-2, flag-1);
                               flag++;       

                        }catch (IOException e) {
                                // TODO Auto-generated catch block
                               Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   showAlert("Connection error");
                                   //disableBtn();
                                }
                            });
                                break;
                        }

                }
            }
           
       };
      Th.start();
       
//        while(!flageGame)
//        {
//            try {
//                System.out.println("a");
//                System.out.println(p1.dis.readLine());
//                p2.ps.println(p1.dis.readLine());
//                p1.ps.println(p2.dis.readLine());
//            } catch (IOException ex) {
//                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
      
    }
    private void makeGame(int x,int y)
        {
            p1 = players.get(x);
            p1.p1 =x;
            p1.p2 =y;
            p1.type = "X";
            p2 = players.get(y);
            p2.p1 =x;
            p2.p2 =y;
            p2.type = "O";
            try {
                    String nameFirst = p1.dis.readLine().split(",")[1];
                    String nameSecond = p2.dis.readLine().split(",")[1];
                    p1.ps.println("Name,"+nameSecond+",f1");
                    p2.ps.println("Name,"+nameFirst+",f2");


                } catch (IOException ex) {
                    Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   showAlert("Cant init the game try again");
                                   //disableBtn();
                                }
                            });
                }
            p1.start();

            p2.start();
        }  
    private void showAlert(String Message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
 
        // alert.setHeaderText("Results:");
        alert.setContentText(Message);
 
        alert.showAndWait();
    }
    class Player extends Thread
    {
            DataInputStream dis;
            PrintStream ps;
            Socket s;
            String type  = "";
            public int p1;
            public int p2;
            
            //BufferedReader inputLine ;
            public Player(Socket cs,String _type)
            {
                    s = cs;
                    type = _type;
                    try {
                            dis = new DataInputStream(s.getInputStream());
                            
                            ps = new PrintStream(s.getOutputStream());
                    } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
                    players.add(this);
                    System.out.println(_type);
                   System.out.println("size"+players.size());
                  // start();
            }
            public void closeAll() 
            {
                try {
                    dis.close();
                    ps.close();
                    s.close();
                    //flag = 1;
                   
                } catch (IOException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
            public void run()
            {
                    while(!s.isClosed())
                    {
                            String str;
                            try {
                                    System.out.println("run server");
                                    str = dis.readLine();
                                    System.out.println(str);
                                    if(str != null)
                                        sendMoveToAll(str);
                                    else
                                          break;
                            } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                   closeAll();
                            }

                    }
                    
            }
            void sendMoveToAll(String msg)
            {
                //System.out.println("s"+players.size());
              // players.remove(this);
                System.out.println("sent message");
                    
                    {
                          
                            players.get(p1).ps.println(msg+","+type);
                            players.get(p2).ps.println(msg+","+type);
                    }
            }
 }
    @FXML
    private void DisableServer()
    {
        try {
            Th.stop();
            serverSocket.close();
            runServer.setVisible(true);
            flag=1;
            st.setVisible(false);
            players.forEach(x->x.closeAll());
            players.clear();
            
        } catch (IOException ex) {
           Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   showAlert("Error in closing");
                                   //disableBtn();
                                }
                            });
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
