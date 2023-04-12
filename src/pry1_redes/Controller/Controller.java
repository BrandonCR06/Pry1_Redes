/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class Controller {
    private Main_Menu menu;
    private String protocol = "Utopia";
    

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
       Frame data = new Frame("",0,0,"");
       //Crear una capa de fisica
       PhysicalLayer physic1 = new PhysicalLayer(data);
       PhysicalLayer physic2 = new PhysicalLayer(data);
       NetworkLayer network2 = new NetworkLayer(new Packet("cualquier mierda"));
       Protocol toUseProtocol = new ProtocolFactory().createProtocol(this.protocol);
       
       Machine machine1 = new Machine(toUseProtocol,physic1, network1);
       Machine machine2 = new Machine(toUseProtocol,physic2, network2);
       //Aqui no se ha seteado el paquete debería imprimir cualquier vara
       System.out.println(machine1.getNetwork().getPacket().getHeader());
       machine1.getProtocol().send(machine1, machine2);
       machine2.getProtocol().receive(machine1, machine2);
       //Aqui se seteo, debería imprimir cualquier mierda
       System.out.println(machine1.getNetwork().getPacket().getHeader());
    }
     public Controller(){
         menu = new Main_Menu();
         
         
     }
     
}
