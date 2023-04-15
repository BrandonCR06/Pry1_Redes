/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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
    Timer timer;
    TimerTask task;
    private int MAX_SEQ = 1;
    public PAR(){
      
     timer = new Timer();
        task = new TimerTask() {
            int count = 0;
            public void run() {
                count++;
                System.out.println("Timer ran " + count + " times.");
            }
        };
        
    }
    
   
     public void startTimer(){  
         
         timer.purge();
         
        task = new TimerTask() {
            int count = 0;
            public void run() {
                count++;
                System.out.println("Timer ran " + count + " times.");
            }
        };
        timer.schedule(task, 0, 500);

     }
     public void stopTask(){
        task.cancel();

     }
     public void stopTimer(){
        timer.cancel();

     }
    @Override
    public void send(Machine receiver, Machine sender) {
        Packet buffer = sender.fromNetworkLayer();         
        
            Frame s = new Frame("",next_frame_to_send,next_frame_to_send,buffer.getHeader());
            receiver.toPhysicalLayer(s);                        
            this.startTimer();                        
            receiver.getProtocol().receive(receiver, sender);
            EventType event = sender.getPhysical().getLastEvent();
            if(event == EventType.frame_arrival){
                if(s.getConfirmNumber()==next_frame_to_send){
                    this.stopTask();
                    buffer = sender.fromNetworkLayer();
                    next_frame_to_send = invert(next_frame_to_send);
                     String result = "Info: sender sending to receiver\nFrame:\t\nType:"+s.getFrameType()+"\nSecuenceNum:"+s.getSequenceNumber()+
                "\nPacket:"+s.getPacketInformation()+"\nConfNumb:"+s.getConfirmNumber()+ "\nEventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent();
                receiver.info = result;
                                        
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
                    receiver.toNetworkLayer(new Packet(s.getPacketInformation()));
                    frame_expected = invert(frame_expected);
                                        
                }
                s.setConfirmNumber(1-frame_expected);                
                sender.toPhysicalLayer(s);
                 String result = "Info: sender sending to receiver\nFrame:\t\nType:"+s.getFrameType()+"\nSecuenceNum:"+s.getSequenceNumber()+
                "\nPacket:"+s.getPacketInformation()+"\nConfNumb:"+s.getConfirmNumber()+ "\nEventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent();
                receiver.info = result;
                
            }
        
        
        
    }
     private int invert(int seqNum) {
        return (seqNum + 1) % (this.MAX_SEQ + 1);
    }

    
    
}
