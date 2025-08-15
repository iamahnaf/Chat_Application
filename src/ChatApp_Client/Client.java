package ChatApp_Client;

import ChatApp_Server.Controller;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

public class Client {
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;

  public Client(Socket socket) {
      try{
          this.socket = socket;
          this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
          this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      }catch(IOException e){
          e.printStackTrace();
          System.out.println("Error creating client socket");
          closeEverything(socket, bufferedReader, bufferedWriter);
      }
  }
  //sendmsgtoserver
  public void sendMessageToServer(String messageToServer){
      try{
          bufferedWriter.write(messageToServer);
          bufferedWriter.newLine();
          bufferedWriter.flush();
      }catch (IOException e){
          System.out.println("error sending message to Server  ");
          closeEverything(socket,bufferedReader,bufferedWriter);
          e.printStackTrace();
      }
  }
  //rcvFromserver
    public void recieveMessageFromServer(VBox vBox){
        new Thread(new Runnable() {
            public void run() {
                while(socket.isConnected()){
                    try {
                        String messageFromServer = bufferedReader.readLine();
                        Controller.addLabel(messageFromServer,vBox);
                    }catch (IOException e){
                        System.out.println("error receiving message from client");
                        e.printStackTrace();
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
