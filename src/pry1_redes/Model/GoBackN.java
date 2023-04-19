/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Enums.EventType;
import java.util.Random;


/**
 *
 * @author ricardosoto
 */
public class GoBackN implements Protocol{

    
    public HashMap<Integer,Timer> timers;
    public int MAX_SEQ;
    public Packet[] buffer;
    private int nextFrameToSend = 0;
    private int ackExpected = 0;
    private int timerDelay = 0;
    private int frameExpected = 0;
    private boolean timerEx = false;
    public String info = "";
    private int nBuffered = 0;
    private boolean network_layer_state = true;
    
    
    
    public Timer startTimer(int id){         
        
         if(timers.containsKey(id)){
             timers.remove(id);
         }
             
             TimerTask task;
            timerEx = false;
            Timer timer = new Timer();
            Random rand = new Random();

            timerDelay = rand.nextInt(401) + 100; // generate a random delay between 100 ms and 400 ms
            int randomWait = rand.nextInt(501) + 100; // generate a random delay between 100 ms and 600 ms
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
    public GoBackN(){
        timers = new HashMap<Integer,Timer>();
    }
    @Override
    public void send(Machine receiver, Machine sender){
        this.send_receive(receiver, sender);
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
                
            
        } 
       
    }
    public void sendData(Machine receiver, Machine sender, Packet[] buffer) {
        Frame new_frame = new Frame(null, ackExpected, ackExpected, null);
        
        new_frame.setPacketInformation(this.buffer[this.nextFrameToSend].getHeader());
        new_frame.setSequenceNumber(this.nextFrameToSend);
        new_frame.setConfirmNumber((this.frameExpected + MAX_SEQ) % (MAX_SEQ + 1));
        info+="\n";
        info+=("---------------------------");
        info+="\n";
        info+=("Se envia desde la maquina "+sender.getName());
        info+="\n";
        info+=("SqcNumber " + new_frame.getSequenceNumber());               
        info+="\n";
        info+=("CfrNumber " + new_frame.getConfirmNumber());
        info+="\n";
        info+=("frame_nr " +this.nextFrameToSend);
        info+="\n";        
        info+=("---------------------------");
        info+="\n";
        
        receiver.info = info;
        receiver.toPhysicalLayer(new_frame); /* transmit the frame */
        
        receiver.getNetwork().disableNetworkLayer();
        
        receiver.getProtocol().send(receiver, sender);
        
        generateRandomTimeout(startTimer(nextFrameToSend), receiver);
        
        
        
        
        
    
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    public void send_receive(Machine receiver, Machine sender) {
        Frame frame = null;        
        EventType event = receiver.getPhysical().getLastEvent();
        if(receiver.getNetwork().networkReady() && event!= EventType.timeout){
            event = EventType.network_layer_ready;
        }
        switch(event){
             case network_layer_ready:
                    this.buffer[this.nextFrameToSend] = sender.fromNetworkLayer();
                    nBuffered +=  1;
                    sendData(receiver, sender, buffer);
                    
                    nextFrameToSend = inc(this.nextFrameToSend);
                    break;  
                case ack_timeout:
                    break;
                case frame_arrival:
                     frame = receiver.fromPhysicalLayer();
                     info+="\n";
                     info+=("---------------------------");
                     info+="\n";
                     info+=("Se recibe desde la maquina "+receiver.getName());
                     info+="\n";
                     info+=("SqcNumber frame recibido" + frame.getSequenceNumber());
                     info+="\n";
                     info+=("CfrNumber " + frame.getConfirmNumber());
                     info+="\n";
                     info+=("Expected " + this.frameExpected);
                     info+=("---------------------------");
                     info+="\n";
                     receiver.info = info;
                    if (frame.getSequenceNumber() == this.frameExpected){
                        receiver.toNetworkLayer(new Packet(frame.getPacketInformation()));
                        frameExpected =inc(this.frameExpected);
                    }
                    while (between(this.ackExpected, frame.getConfirmNumber(), this.nextFrameToSend)){
                        nBuffered = nBuffered - 1;
                        stopTimer(this.ackExpected % MAX_SEQ);                        
                        ackExpected= inc(this.ackExpected);
                    }
                    break;
                case cksum_err:
                    info+=("\nXXXXXX ChecksumError XXXXXX");
                    break;
                case timeout:                    
                    info+=("\nXXXXXX TimeoutErr XXXXXX  retransmiting...\n");
                    this.nextFrameToSend = this.ackExpected;
                    for (int i = 1; i <= nBuffered; i++){
                        
                        sendData(receiver, sender, buffer);
                        this.nextFrameToSend = inc(this.nextFrameToSend);
                        
                    }
                    
                    break;
               
                default:
                    break;
            }
            if (nBuffered < MAX_SEQ){
                receiver.getNetwork().enableNetworkLayer();
            }
            else{
                receiver.getNetwork().disableNetworkLayer();
            }
            info  ="";
        
    }
        

    public static boolean between(int a, int b, int c) {
        if (((a <= b) && (b < c)) || ((c < a) && (a <= b)) || ((b < c) && (c < a))){
            return true;
        }
        else{
            return false;
        }
    }

    public int inc(int k) {
        int c  = k;
        if (c < MAX_SEQ) {
            c = c + 1;
        } else {
            c = 0;
        }
        return c;
    }

    // public void start_timer(){

    // }

    public void enable_network_layer(Machine receiver){
        this.network_layer_state = true;
        
    }

    public void disable_network_layer(){
        this.network_layer_state = false;
    }
    
}

