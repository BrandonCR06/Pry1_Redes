/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pry1_redes.Enums.EventType;
import pry1_redes.Main_Menu;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.Layers.NetworkLayer;
import pry1_redes.Model.Layers.PhysicalLayer;
import pry1_redes.Model.Machine;
import pry1_redes.Model.PAR;
import pry1_redes.Model.Protocol;
import pry1_redes.Model.ProtocolFactory;


/**
 *
 * @author ricardosoto
 */
public class Controller {
    private Main_Menu menu;
    private String protocol = "Utopia";
    private boolean buttonStop = false;
    private double probErr = 0;
    

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
            System.out.println(e);
        }
      
       
          if(this.protocol.equals("Utopia") || this.protocol.equals("Stop-and-wait")){
               NetworkLayer network1 = new NetworkLayer(10);
                Frame data = new Frame("",0,0,"");
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
                         try {
                            Thread.sleep(2000); // Detener durante 5 segundos
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }

                         //Aqui se seteo, debería imprimir cualquier cosa

                         menu.jTextArea1.setText(machine1.info);
                         System.out.println(machine2.getNetwork().getPacket().getHeader());           
                         }
                         
                         System.out.println("Process Ended");
                         menu.jTextArea1.setText("System Paused");

                         buttonStop = false;
                      }
                  };
                  myThread.start(); // start the new thread
            
         }else if(this.protocol.equals("PAR")){
            
           NetworkLayer network1 = new NetworkLayer(10);
            Frame data = new Frame("",0,0,"");
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
                         try {
                            Thread.sleep(2000); // Detener durante 5 segundos
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }
                         //Aqui se seteo, debería imprimir cualquier cosa

                         menu.jTextArea1.setText(machine1.info);
                         System.out.println(machine2.getNetwork().getPacket().getHeader());           
                         }
                         PAR par = (PAR)machine1.getProtocol();
                         par.stopTimer();
                         System.out.println("Process Ended");
                         menu.jTextArea1.setText("System Paused");                         

                         buttonStop = false;
                      }
                  };
                  myThread.start(); // start the new thread
          
         } else if(this.protocol.equals("sliding window de 1 bit")) {
                        NetworkLayer network1 = new NetworkLayer(10);
               Frame data = new Frame("",0,1,"");
               //Crear una capa de fisica
               PhysicalLayer physic1 = new PhysicalLayer(data);
               PhysicalLayer physic2 = new PhysicalLayer(data);
               physic1.seProb(probErr);
               physic2.seProb(probErr);
               physic1.addEvent(EventType.frame_arrival);
               physic2.addEvent(EventType.frame_arrival);
               NetworkLayer network2 = new NetworkLayer(10);
               Protocol toUseProtocol = new ProtocolFactory().createProtocol(this.protocol);

               Machine machine1 = new Machine(toUseProtocol,physic1, network1);
               machine1.setName("A");
               Machine machine2 = new Machine(toUseProtocol,physic2, network2);
               machine2.setName("B");
               //Aqui no se ha seteado el paquete debería imprimir cualquier vara
               System.out.println(machine1.getNetwork().getPacket().getHeader());
               //Crear un nuevo hilo para poder hacer la pausa
               Thread myThread = new Thread() {
               public void run() {
                   while(!buttonStop){
                        machine1.getProtocol().send(machine1, machine2);
                        System.out.println("PARKOUR 1" );
                        
                        menu.jTextArea1.setText(machine1.info);
                        menu.jTextArea1.repaint();
                        
                        try {
                            Thread.sleep(5000); // Detener durante 5 segundos
                        } catch (InterruptedException e) {
                            // Manejar la excepción si es necesario
                        }

                        machine2.getProtocol().send(machine2, machine1);
                        menu.jTextArea1.setText(machine2.info);
                        menu.jTextArea1.repaint();
                        
                        System.out.println("PARKOUR 2");
                        //Aqui se seteo, debería imprimir cualquier cosa

                        
                        System.out.println(machine2.getNetwork().getPacket().getHeader());


                   }

                   System.out.println("Process Ended");
                   menu.jTextArea1.setText("System Paused");

                   buttonStop = false;
                }
            };
            myThread.start(); // start the new thread
             
         }
          
        
       
      
     
       
    }
     public Controller(){
         menu = new Main_Menu();
         
         
     }
     
}
