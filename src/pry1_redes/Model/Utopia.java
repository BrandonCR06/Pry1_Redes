/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import pry1_redes.Enums.EventType;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Model.Layers.NetworkLayer;
import pry1_redes.Model.Layers.PhysicalLayer;

/**
 *
 * @author ricardosoto
 */



public class Utopia implements Protocol {
    
    
    public Utopia(){
        
    }
    @Override
    public void send(Machine receiver, Machine sender) {
        Packet packet= sender.fromNetworkLayer();
        Frame data = new Frame("",0,0,packet.getHeader());
        receiver.toPhysicalLayer(data);
        receiver.getProtocol().receive(receiver, sender);
         String result = "Info: sender sending to receiver\nFrame:\t\nType:"+data.getFrameType()+"\nSecuenceNum:"+data.getSequenceNumber()+
        "\nPacket:"+data.getPacketInformation()+"\nConfNumb:"+data.getConfirmNumber()+ "\nEventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent();
        receiver.info = result;
        
        
        
        
        
        
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        Frame data= receiver.fromPhysicalLayer();       
        receiver.toNetworkLayer(new Packet(data.getPacketInformation()));
         String result = "Info: receiver receiving \nFrame:\t\nType:"+data.getFrameType()+"\nSecuenceNum:"+data.getSequenceNumber()+
        "\nPacket:"+data.getPacketInformation()+"\nConfNumb:"+data.getConfirmNumber()+ "\nEventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent();
        receiver.info = result;
        
        
        
        
    }
    
    
}
