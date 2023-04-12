/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes;


import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Model.Layers.NetworkLayer;
import pry1_redes.Model.Layers.PhysicalLayer;
import pry1_redes.Model.Protocol;
import pry1_redes.Model.Utopia;

/**
 *
 * @author ricardosoto
 */
public class Main {
   public static void main(String[] args) {
       NetworkLayer net = new NetworkLayer(new Packet("cualquier vara"));
       Frame data = new Frame("",0,0,"");
       PhysicalLayer physic = new PhysicalLayer(data);
       PhysicalLayer physic2 = new PhysicalLayer(data);
       NetworkLayer net2 = new NetworkLayer(new Packet("cualquier mierda"));
       
       
       //Protocol maquina1 = new Utopia(physic2, net);
       
       //Protocol maquina2 = new Utopia(physic2, net2);
       
      
      
      
      
      //System.out.println(net2.getPacket().getHeader());
      //maquina1.send();
      //maquina2.receive();
      //System.out.println(net2.getPacket().getHeader());
      
      
      
   }
}

