/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import pry1_redes.Enums.EventType;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;

/**
 *
 * @author ricardosoto
 */
public class StopAndWait implements Protocol {
     @Override
    public void send(Machine receiver, Machine sender) {
        
        Packet buffer= sender.fromNetworkLayer();
        
        Frame data = new Frame("",0,0,buffer.getHeader());
        
        receiver.toPhysicalLayer(data);
        receiver.getPhysical().addEvent(EventType.frame_arrival);
        receiver.getProtocol().receive(receiver, sender);
        String result = "Info: sender sending to receiver\nFrame:\t\nType:"+data.getFrameType()+"\nSecuenceNum::"+data.getSequenceNumber()+
        "\nPacket:"+data.getPacketInformation()+"\nConfNumb:"+data.getConfirmNumber()+ "EventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent();
        receiver.info = result;
        
                                
        
        
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        Frame s= receiver.fromPhysicalLayer();      
        Packet buffer = new Packet("");        
        s.setPacketInformation(buffer.getHeader());
        receiver.toNetworkLayer(new Packet(""));
        Frame d = new Frame("",0,0,"");// dummy frame        
        sender.toPhysicalLayer(d);
        sender.getPhysical().addEvent(EventType.frame_arrival);
        String result = "Info: Receiver\nFrame:\t\nType:"+d.getFrameType()+"\nSecuenceNum::"+d.getSequenceNumber()+
        "\nPacket:"+d.getPacketInformation()+"\nConfNumb:"+d.getConfirmNumber()+ "EventTriggeredOnSender:"+receiver.getPhysical().getLastEvent();
        sender.info = result;
        
        
    }
    
}
