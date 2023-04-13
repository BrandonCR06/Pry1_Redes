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
public class PAR implements Protocol{
    private int next_frame_to_send = 0;
    private int frame_expected = 0;
    @Override
    public void send(Machine receiver, Machine sender) {
        Packet buffer = sender.fromNetworkLayer();         
        
            Frame s = new Frame("",next_frame_to_send,next_frame_to_send,buffer.getHeader());
            receiver.toPhysicalLayer(s);                        
            //StartTImer
            EventType event = receiver.getPhysical().getLastEvent();
            if(event == EventType.frame_arrival){
                if(s.getConfirmNumber()==next_frame_to_send){
                    //stopTimer
                    buffer = sender.fromNetworkLayer();
                    next_frame_to_send+=1;
                                        
                }
            }

        
        
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        EventType event = receiver.getPhysical().getLastEvent();
        frame_expected =0;
        Frame s = receiver.fromPhysicalLayer();
       
        if(event == EventType.frame_arrival){
                if(s.getConfirmNumber()==next_frame_to_send){
                    //stopTimer
                    receiver.toNetworkLayer(new Packet(s.getPacketInformation()));
                    frame_expected+=1;
                                        
                }
                
            }
        
        
        
    }
    
    
}
