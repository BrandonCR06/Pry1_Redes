/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import pry1_redes.Enums.EventType;
import pry1_redes.Enums.FrameKind;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;

/**
 *
 * @author ricardosoto
 */
public class PAR implements Protocol{
    private int next_frame_to_send = 0;
    private int frame_expected = 0;
    private HashMap<Integer,Timer> timers;
    Packet buffer;
    private int MAX_SEQ =1;
    private boolean timerEx;
    private int timerDelay;
    public PAR(){
      timers = new HashMap<Integer,Timer>();
        
    }
    
   
     public Timer startTimer(int id){         
        
         if(timers.containsKey(id)){timers.remove(id);}
             
             TimerTask task;
            timerEx = false;
            Timer timer = new Timer();
            Random rand = new Random();
            
            
            
            timerDelay = rand.nextInt(401) + 100; // generate a random delay between 100 ms and 400 ms
            int randomWait = rand.nextInt(801) + 100;
            task = new TimerTask() {            
            int count = 0;
            
            

            public void run() {   
                if(count> 0){
                    
                    timerEx = true;
                }
                count++;
                
            }
        };
            timer.schedule(task, 0, randomWait);
        
           
             timers.put(id, timer);
             return timer;
         

                                                           

     }
     public void stopTimer(int id){
         if(timers.get(id)!= null){
            timers.get(id).cancel();
          }
        
        
        

     }
     public void generateRandomTimeout(Timer timer,Machine receiver){
        if(timer==null){return;}
        try {
            Thread.sleep(timerDelay); // wait for the timeout duration plus a buffer of 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
            if (!timerEx) {
                
                receiver.getPhysical().addEvent(EventType.timeout);
                timer.cancel();
               System.out.println("Timeoutnotok!");
            
        } else {
                System.out.println("Timer ok!");
            }
       
    }
    @Override
    public void send(Machine receiver, Machine sender) {
            buffer = sender.fromNetworkLayer(); 
        
            Frame s = new Frame(FrameKind.data,next_frame_to_send,0,buffer.getHeader());
            receiver.toPhysicalLayer(s);             
            generateRandomTimeout(startTimer(s.getSequenceNumber()), receiver);
            
             String result = "\nInfo: sender sending to receiver\nFrame:\t\nType:"+s.getFrameType()+"\nSecuenceNum:"+s.getSequenceNumber()+
                "\nPacket:"+s.getPacketInformation()+"\nConfNumb:"+s.getConfirmNumber()+ "\nEventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent();
                receiver.info += result;
            receiver.getProtocol().receive(receiver, sender);
            EventType event = sender.getPhysical().getLastEvent();
            if(event == EventType.frame_arrival){
                s = sender.fromPhysicalLayer();
                if(s.getConfirmNumber()==next_frame_to_send){
                    this.stopTimer(s.getConfirmNumber());
                    buffer = sender.fromNetworkLayer();
                    next_frame_to_send = invert(next_frame_to_send);
                    
                                        
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
                 String result = "\nInfo: receiver sending to sender\nFrame:\t\nType:"+s.getFrameType()+"\nSecuenceNum:"+s.getSequenceNumber()+
                "\nPacket:"+s.getPacketInformation()+"\nConfNumb:"+s.getConfirmNumber()+ "\nEventTriggeredOnReceiver:"+receiver.getPhysical().getLastEvent()+"\n\n";
                receiver.info += result;
                
            }
        
        
        
    }
     private int invert(int seqNum) {
        return (seqNum + 1) % (this.MAX_SEQ + 1);
    }

    
    
}
