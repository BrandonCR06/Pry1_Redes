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
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Model.Layers.NetworkLayer;
import pry1_redes.Model.Layers.PhysicalLayer;
import pry1_redes.Model.Machine;
import pry1_redes.Model.Protocol;
import pry1_redes.Model.ProtocolFactory;


/**
 *
 * @author ricardosoto
 */
public class testSliding {
    private Main_Menu menu;
    private String protocol = "Utopia";
    private boolean buttonStop = false;
    

    public static void main(String[]     args) {
        testSliding controller = new testSliding();
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
        //Crear una capa de red 
        NetworkLayer network1 = new NetworkLayer(new Packet("cualquier vara"));
       Frame data = new Frame("",0,1,"");
       //Crear una capa de fisica
       PhysicalLayer physic1 = new PhysicalLayer(data);
       PhysicalLayer physic2 = new PhysicalLayer(data);
       physic1.addEvent(EventType.frame_arrival);
       physic2.addEvent(EventType.frame_arrival);
       NetworkLayer network2 = new NetworkLayer(new Packet("cualquier cosa"));
       Protocol toUseProtocol = new ProtocolFactory().createProtocol(this.protocol);
       
       Machine machine1 = new Machine(toUseProtocol,physic1, network1);
       machine1.setInfo("Maquina A");
       Machine machine2 = new Machine(toUseProtocol,physic2, network2);
       machine2.setInfo("Maquina B");
       //Aqui no se ha seteado el paquete debería imprimir cualquier vara
       System.out.println(machine1.getNetwork().getPacket().getHeader());
       //Crear un nuevo hilo para poder hacer la pausa
       Thread myThread = new Thread() {
       public void run() {
           while(!buttonStop){
                machine1.getProtocol().send(machine1, machine2);       
                System.out.println("PARKOUR 1" );
                machine2.getProtocol().send(machine2, machine1);  
                System.out.println("PARKOUR 2");
                //Aqui se seteo, debería imprimir cualquier cosa
           
                menu.jLabel3.setText(machine1.info);
                System.out.println(machine2.getNetwork().getPacket().getHeader());           
                     
                
           }
           
           System.out.println("Process Ended");
           menu.jLabel3.setText("System Paused");
           
           buttonStop = false;
        }
    };
    myThread.start(); // start the new thread
     
       
    }
     public testSliding(){
         menu = new Main_Menu();
         
         
     }
     
}
