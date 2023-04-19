/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pry1_redes.Enums.EventType;
import pry1_redes.Enums.FrameKind;
import pry1_redes.Main_Menu;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Model.GoBackN;
import pry1_redes.Model.Layers.NetworkLayer;
import pry1_redes.Model.Layers.PhysicalLayer;
import pry1_redes.Model.Machine;
import pry1_redes.Model.PAR;
import pry1_redes.Model.Protocol;
import pry1_redes.Model.ProtocolFactory;
import pry1_redes.Model.SlidingWindow;


/**
 *
 * @author ricardosoto
 */
public class Controller {
    private Main_Menu menu;
    private String protocol = "Utopia";
    private boolean buttonStop = false;
    private double probErr = 0;
    private int windowSize = 7;
    

    public static void main(String[]     args) {
        Controller controller = new Controller();
        controller.makeMenu();
        

        
    }
    
    public void makeMenu(){
        menu.setVisible(true);
        menu.jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                String selectedItem = (String) menu.jComboBox1.getSelectedItem();                
                protocol = selectedItem;                                               
            }
        });
         menu.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                
                buttonStop = true;
            }
        });
        
        menu.jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                
                initializeProtocolProcess();
            }
        });
        
        
    }
    public void initializeProtocolProcess(){
        try {
            String text = menu.errorRateEntry.getText();
            this.probErr = Double.parseDouble(text);
            
            
            
            
            
            // use the parsed integer here
        } catch (NumberFormatException e) {
            // handle the exception if the text cannot be parsed to integer
            
        }
        try {
            Integer window =(Integer)menu.jSpinner1.getValue();
            this.windowSize = window;
            
            
            
            
            // use the parsed integer here
        } catch (NumberFormatException e) {
            // handle the exception if the text cannot be parsed to integer
            
        }
      
        
       
          if(this.protocol.equals("Utopia") || this.protocol.equals("Stop-and-wait")){
               NetworkLayer network1 = new NetworkLayer(10);
                Frame data = new Frame(FrameKind.data,0,0,"");
                //Crear una capa de fisica
                PhysicalLayer physic1 = new PhysicalLayer(data);
                PhysicalLayer physic2 = new PhysicalLayer(data);
                physic1.seProb(0);
                physic2.seProb(0);
                NetworkLayer network2 = new NetworkLayer(10);
                Protocol toUseProtocol = new ProtocolFactory().createProtocol(this.protocol);
       
          
               Machine machine1 = new Machine(toUseProtocol,physic1, network1);
                Machine machine2 = new Machine(toUseProtocol,physic2, network2);
                machine1.setName("Maquina 1");
                machine2.setName("Maquina 1");
                //Aqui no se ha seteado el paquete debería imprimir cualquier vara
                System.out.println(machine1.getNetwork().getPacket().getHeader());
                //Crear un nuevo hilo para poder hacer la pausa
                Thread myThread = new Thread() {
                public void run() {
                    while(!buttonStop){
                        
                         machine1.getProtocol().send(machine1, machine2);      
                         try {
                            Thread.sleep(2000); // Detener durante 5 segundos
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }

                         //Aqui se seteo, debería imprimir cualquier cosa

                         menu.jTextArea1.setText(machine1.info+"\n"+machine2.info);
                         
                         System.out.println(machine2.getNetwork().getPacket().getHeader());           
                         }
                         
                         System.out.println("Process Ended");
                         

                         buttonStop = false;
                      }
                  };
                  myThread.start(); // start the new thread
            
         }else if(this.protocol.equals("PAR")){
            
           NetworkLayer network1 = new NetworkLayer(10);
            Frame data = new Frame(FrameKind.data,0,0,"");
            //Crear una capa de fisica
            PhysicalLayer physic1 = new PhysicalLayer(data);
            PhysicalLayer physic2 = new PhysicalLayer(data);
            physic1.seProb(probErr);
            physic2.seProb(probErr);
            NetworkLayer network2 = new NetworkLayer(10);
            Protocol toUseProtocol = new ProtocolFactory().createProtocol(this.protocol);
       
            Machine machine1 = new Machine(toUseProtocol,physic1, network1);
                Machine machine2 = new Machine(toUseProtocol,physic2, network2);
                //Aqui no se ha seteado el paquete debería imprimir cualquier vara
                System.out.println(machine1.getNetwork().getPacket().getHeader());
                //Crear un nuevo hilo para poder hacer la pausa
                Thread myThread = new Thread() {
                public void run() {
                    while(!buttonStop){
                         machine1.getProtocol().send(machine1, machine2);
                         menu.jTextArea1.setText(machine1.info);
                         machine1.info= "";
                                 
                         
                         try {
                            Thread.sleep(2000); // Detener durante 5 segundos
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }
                         //Aqui se seteo, debería imprimir cualquier cosa

                         
                         System.out.println(machine2.getNetwork().getPacket().getHeader());           
                         }
                         PAR par = (PAR)machine1.getProtocol();
                         
                         System.out.println("Process Ended");
                                                

                         buttonStop = false;
                      }
                  };
                  myThread.start(); // start the new thread
          
         } else if(this.protocol.equals("sliding window de 1 bit")) {
                        NetworkLayer network1 = new NetworkLayer(10);
               Frame data = new Frame(FrameKind.data,0,1,"");
               //Crear una capa de fisica
               PhysicalLayer physic1 = new PhysicalLayer(data);
               PhysicalLayer physic2 = new PhysicalLayer(data);
               physic1.seProb(probErr);
               physic2.seProb(probErr);
               physic1.addEvent(EventType.frame_arrival);
               physic2.addEvent(EventType.frame_arrival);
               NetworkLayer network2 = new NetworkLayer(10);
               Protocol toUseProtocol1 = new ProtocolFactory().createProtocol(this.protocol);
               Protocol toUseProtocol2 = new ProtocolFactory().createProtocol(this.protocol);
               
               SlidingWindow prot = (SlidingWindow)toUseProtocol1;
               SlidingWindow prot2 = (SlidingWindow)toUseProtocol2;
               
               Machine machine1 = new Machine(toUseProtocol1,physic1, network1);
               machine1.setName("A");
               Machine machine2 = new Machine(toUseProtocol2,physic2, network2);
               machine2.setName("B");
               //Aqui no se ha seteado el paquete debería imprimir cualquier vara
               System.out.println(machine1.getNetwork().getPacket().getHeader());
               //Crear un nuevo hilo para poder hacer la pausa
               Machine receiver = machine1;
               Machine sender = machine2;
               receiver.info = "";
        
        // Fetch a packet from the network layer
        
        prot.buffer = sender.fromNetworkLayer();
        prot2.buffer = receiver.fromNetworkLayer();
        prot.s = new Frame(FrameKind.data,0,1,"");
        // Prepare to send the initial frame
        prot.s.setPacketInformation(prot.buffer.getHeader());
        prot.s.setSequenceNumber(0);
        prot.s.setConfirmNumber(1-0); // piggybacked ack
        prot2.s = prot.s;
       
        // Transmit the frame
        
        
        // Start timer
               Thread myThread = new Thread() {
               public void run() {                   
                   while(!buttonStop){
                       machine1.info = "";
                       machine2.info = "";
                        machine1.toPhysicalLayer(prot.s);                         
                        machine1.getProtocol().send(machine1, machine2);                                                
                        menu.jTextArea1.setText(machine1.info);
                        menu.jTextArea1.repaint();
                        System.out.println("PARKOUR 1" );
                        
                        
                        
                        
                        try {
                            Thread.sleep(2000); // Detener durante 5 segundos
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }
                       
                        System.out.println("PARKOUR 2");
                        //Aqui se seteo, debería imprimir cualquier cosa

                        
                        System.out.println(machine2.getNetwork().getPacket().getHeader());


                   }

                   System.out.println("Process Ended");
                   

                   buttonStop = false;
                }
            };
            myThread.start(); // start the new thread
             
         }else if (this.protocol.equals("go-back-n")){
             NetworkLayer network1 = new NetworkLayer(10);
                Frame data = new Frame(FrameKind.data,0,0,"");
                //Crear una capa de fisica
                PhysicalLayer physic1 = new PhysicalLayer(data);
                PhysicalLayer physic2 = new PhysicalLayer(data);
                physic1.seProb(probErr);
                physic2.seProb(probErr);
                NetworkLayer network2 = new NetworkLayer(10);
                Protocol toUseProtocol = new ProtocolFactory().createProtocol(this.protocol);
                //Protocol toUseProtocol2 = new ProtocolFactory().createProtocol(this.protocol);
                
                ((GoBackN)toUseProtocol).MAX_SEQ=this.windowSize;
               ((GoBackN)toUseProtocol).buffer=new Packet[this.windowSize + 1];
                
       
               
                Machine machine1 = new Machine(toUseProtocol,physic1, network1);
                Machine machine2 = new Machine(toUseProtocol,physic2, network2);
                machine1.setName("1");
                machine2.setName("2");
                machine1.getNetwork().enableNetworkLayer();
                //machine2.getNetwork().enableNetworkLayer();
                //Aqui no se ha seteado el paquete debería imprimir cualquier vara
                //System.out.println(machine1.getNetwork().getPacket().getHeader());
                //Crear un nuevo hilo para poder hacer la pausa
                Thread myThread = new Thread() {
                public void run() {
                    while(!buttonStop){
                        machine1.info = "";
                        machine2.info = "";
                        
                         machine1.getProtocol().send(machine1, machine2);    
                         menu.jTextArea1.setText(machine1.info);
                         menu.jTextArea1.repaint();
                           try {
                            Thread.sleep(3000); 
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }
                           
                          
                         
                       

                         //Aqui se seteo, debería imprimir cualquier cosa
                                                
                         
                         }
                       try {
                            Thread.sleep(3000); 
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }
                       
                           
                         
                         
                         

                         buttonStop = false;
                      }
                  };
                  myThread.start(); // start the new thread
         } else if (this.protocol.equals("selective-repeat")){
             NetworkLayer network1 = new NetworkLayer(10);
                Frame data = new Frame(FrameKind.data,0,0,"");
                //Crear una capa de fisica
                PhysicalLayer physic1 = new PhysicalLayer(data);
                PhysicalLayer physic2 = new PhysicalLayer(data);
                physic1.seProb(probErr);
                physic2.seProb(probErr);
                NetworkLayer network2 = new NetworkLayer(10);
                Protocol toUseProtocol = new ProtocolFactory().createProtocol(this.protocol);
                Protocol toUseProtocol2 = new ProtocolFactory().createProtocol(this.protocol);
                
                
                
       
          
               Machine machine1 = new Machine(toUseProtocol,physic1, network1);
                Machine machine2 = new Machine(toUseProtocol2,physic2, network2);
                machine1.setName("1");
                machine2.setName("2");
                machine1.getNetwork().enableNetworkLayer();
                machine2.getNetwork().enableNetworkLayer();
                //Aqui no se ha seteado el paquete debería imprimir cualquier vara
                System.out.println(machine1.getNetwork().getPacket().getHeader());
                //Crear un nuevo hilo para poder hacer la pausa
                Thread myThread = new Thread() {
                public void run() {
                    while(!buttonStop){
                        
                         machine1.getProtocol().send(machine1, machine2);      
                           try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }
                           
                         machine2.getProtocol().send(machine2, machine1);      
                       

                         //Aqui se seteo, debería imprimir cualquier cosa

                         menu.jTextArea1.setText(machine1.info);
                         System.out.println(machine2.getNetwork().getPacket().getHeader());           
                         }
                         
                         System.out.println("Process Ended");
                         menu.jTextArea1.setText("System Paused");

                         buttonStop = false;
                      }
                  };
                  myThread.start();
         }
          
        
       
      
     
       
    }
     public Controller(){
         menu = new Main_Menu();
         
         
     }
     
}
