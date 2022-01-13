import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Frame.*;
import java.awt.*;
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
public class Client extends JFrame{
          Socket socket;
          BufferedReader br;
          PrintWriter out;

          private JFrame frame=new JFrame();
          private JLabel heading=new JLabel("Client Area");
          private JTextArea messageArea=new JTextArea();
          private JTextField messageInput=new JTextField();
          private Font font=new Font("Roboto",Font.PLAIN,40);



          public Client()
          {
                    try {
                              System.out.println("Sending request to server...");
                              socket=new Socket("127.0.0.1",7777);
                              System.out.println("Connection Established!");
                              br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                              out=new PrintWriter(socket.getOutputStream());
                              createGUI();
                              handleEvents();
                              startReading();
                              //startWriting();
                    } catch (Exception e) {
                             
                              e.printStackTrace();
                    }
          }
          private void createGUI() {
                    frame.setTitle("Client Messager");
                    frame.setSize(600,600);
                    //frame.setLocationRelativeTo(null);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    heading.setFont(font);
                    messageArea.setFont(font);
                    messageInput.setFont(font);
		          heading.setIcon(new ImageIcon("Lets chat.jpg"));
                    heading.setHorizontalTextPosition(SwingConstants.CENTER);
                    heading.setVerticalTextPosition(SwingConstants.BOTTOM);
                    heading.setHorizontalAlignment(SwingConstants.CENTER);
                    heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                    messageArea.setEditable(false);
                    messageInput.setHorizontalAlignment(SwingConstants.CENTER);
                    frame.setLayout(new BorderLayout());
                    frame.add(heading,BorderLayout.NORTH);
                    JScrollPane jScrollPane=new JScrollPane(messageArea);
                    frame.add(jScrollPane,BorderLayout.CENTER);
                    frame.add(messageInput,BorderLayout.SOUTH);
                    frame.setVisible(true);
          }
          private void handleEvents(){
               messageInput.addKeyListener(new KeyListener(){
                    @Override
                    public void keyTyped(KeyEvent e){

                    }
                    @Override
                    public void keyPressed(KeyEvent e){


                    }
                    @Override
                    public void keyReleased(KeyEvent e){
                       //  System.out.println("Key Released"+e.getKeyCode());
                         if(e.getKeyCode()==10){
                              String contentToSend=messageInput.getText();
                              messageArea.append("Me: "+contentToSend+"\n");
                              out.println(contentToSend);
                              out.flush();
                              messageInput.setText(" ");
                              messageInput.requestFocus();
                         }
                    }
               });
          }
          public void startReading(){
                    Runnable r1=()->{
                              System.out.println("Reader Started");
                              try{
                              while(true){
                                        
                                        String message=br.readLine();
                                        if(message.equals("exit")){
                                                  System.out.println("Server terminated the loop");
                                                  JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                                                  messageInput.setEnabled(false);
                                                  socket.close();
                                                  break;
                                        
                                        }
                                        messageArea.append("Server: "+message+"\n");
                                        
                                       
                              
                             
                              }
                    }catch(Exception e){
                              //e.printStackTrace();
                              System.out.println("Connection Closed");
                    }


                    };
                    new Thread(r1).start();
          }
          public void startWriting(){
                    Runnable r2=()->{
                              System.out.println("Writer Started");
                              try{
                              while(! socket.isClosed()){

                                        
                                                  BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                                                  String content=br1.readLine();
                                                  out.println(content);
                                                  out.flush();
                                                  if(content.equals("exit")){
                                                            socket.close();
                                                            break;
                                                  }

                              }
                         //     System.out.println("Connection Closed");
                    }catch(Exception e){
                              //e.printStackTrace();
                              System.out.println("Connection Closed");
                    }
                    };
                    new Thread(r2).start();
          }
          public static void main(String[] args) {
                    new Client();
                    System.out.println("This is client");
          }
}
