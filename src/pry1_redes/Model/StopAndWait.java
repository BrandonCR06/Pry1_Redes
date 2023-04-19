/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import java.util.Random;
import pry1_redes.Enums.EventType;
import pry1_redes.Enums.FrameKind;
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
        
        Frame data = new Frame(FrameKind.data,0,0,buffer.getHeader());
        String result = "Info: sender sending to receiver\nFrame:\t\n\tType:"+data.getFrameType()+"\n\tSecuenceNum:"+data.getSequenceNumber()+
        "\n\tPacket:"+data.getPacketInformation()+"\n\tConfNumb:"+data.getConfirmNumber()+ "\nEventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent();
        receiver.toPhysicalLayer(data);        
        receiver.getProtocol().receive(receiver, sender);
        
        receiver.info = result;
        
                                
        
        
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        Frame s= receiver.fromPhysicalLayer();      
        Packet buffer = new Packet("");        
        s.setPacketInformation(buffer.getHeader());
        receiver.toNetworkLayer(new Packet(""));
        Frame d = new Frame(FrameKind.ack,0,0,"");// dummy frame        
        sender.toPhysicalLayer(d);        
        String result = "\nInfo: Receiver sending dummy ack\nFrame:\t\n\tType:"+d.getFrameType()+"\n\tSecuenceNum:"+d.getSequenceNumber()+
        "\n\tPacket:"+d.getPacketInformation()+"\n\tConfNumb:"+d.getConfirmNumber()+ "EventTriggeredOnSender:"+receiver.getPhysical().getLastEvent();
        sender.info = result;
        
        
    }
    
}
