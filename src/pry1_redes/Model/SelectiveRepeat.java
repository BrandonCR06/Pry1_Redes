/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Enums.EventType;
import pry1_redes.Enums.FrameKind;

/**
 *
 * @author ricardosoto
 */
public class SelectiveRepeat implements Protocol{

    private static final int MAX_SEQ = 7;
    private static final int NR_BUFS = (MAX_SEQ+1)/2;
    private Packet[] buffer = new Packet[MAX_SEQ + 1];
    private int nextFrameToSend = 0;
    private int ackExpected = 0;
    private int frameExpected = 0;
    private int nBuffered = 0;
    private int too_far = 0;
    int i = 0;
    private boolean timerEx = false;
    private int timerDelay = 0;
    private HashMap<Integer,Timer> timers;
    boolean no_nak = true;
    int oldest_frame = MAX_SEQ+1;
    Frame r;
    private Packet[] out_buf = new Packet[NR_BUFS];
    private Packet[] in_buf = new Packet[NR_BUFS];
    private boolean[] arrived = new boolean[NR_BUFS];
    
    public SelectiveRepeat(){
        for (i = 0; i < NR_BUFS; i++){arrived[i] = false;};
        
    }    

    
    
    
    private boolean network_layer_state = true;

    @Override
    public void send(Machine receiver, Machine sender){
        this.send_receive(receiver, sender);
    }
    
    public Timer startTimer(int id){         
        
         if(timers.containsKey(id)){
             timers.remove(id);
         }
             
             TimerTask task;
            timerEx = false;
            Timer timer = new Timer();
            Random rand = new Random();

            timerDelay = rand.nextInt(401) + 100; // generate a random delay between 100 ms and 400 ms
            int randomWait = rand.nextInt(600) + 100; // generate a random delay between 100 ms and 600 ms
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
            ;
        
        
        

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
    public void sendData(FrameKind kind,int frameNr, int frameExp, Machine receiver, Machine sender, Packet[] buffer) {
        Frame s = new Frame(null, ackExpected, ackExpected, null);
        s.setFrameType(kind);
        if(kind==FrameKind.data){
            s.setPacketInformation(buffer[frameNr%NR_BUFS].getHeader());
        }
        s.setSequenceNumber(frameNr);
        s.setConfirmNumber((frameExp+this.MAX_SEQ)% (MAX_SEQ + 1));
        if(kind==FrameKind.nak) no_nak=false;
        receiver.toPhysicalLayer(s);
        //receiver.getProtocol().send(receiver, sender);
        if(kind==FrameKind.data) {}
        //stopactTimer(); 
        
       
        
        // Falta el timer
        // start_timer(frame_nr % NR_BUFS);
        
        
    
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    public void send_receive(Machine receiver, Machine sender) {
        
        EventType event = receiver.getPhysical().getLastEvent();
        if(receiver.getNetwork().networkReady()){
            event = EventType.network_layer_ready;
        }
        
        switch(event){
             case network_layer_ready:
                    nBuffered +=  1;
                    this.buffer[this.nextFrameToSend] = sender.fromNetworkLayer();
                    this.out_buf[this.nextFrameToSend% NR_BUFS] = sender.fromNetworkLayer();
                    sendData(FrameKind.data,this.nextFrameToSend,this.frameExpected,receiver, sender, buffer);
                    nextFrameToSend = inc(this.nextFrameToSend);
                    break;
                
                case frame_arrival:
                     this.r = receiver.fromPhysicalLayer();
                     Frame frame = null;
                    System.out.println("---------------------------");
                    System.out.println("Se recibe desde la maquina "+receiver.toString());
                    System.out.println("SqcNumber frame recibido" + r.getSequenceNumber());
                    System.out.println("CfrNumber" + r.getConfirmNumber());
                    System.out.println("Expected" + this.frameExpected);
                    System.out.println("---------------------------");
                     if(this.r.getFrameType()==FrameKind.data){
                         if (this.r.getSequenceNumber() == this.frameExpected && no_nak){
                            receiver.toNetworkLayer(new Packet(this.r.getPacketInformation()));
                            frameExpected =inc(this.frameExpected);
                        
                        }else{
                             //startacktimer
                         }
                         if (between(frameExpected,r.getSequenceNumber(),too_far) && (arrived[r.getSequenceNumber() % NR_BUFS]==false)) {

                         arrived[r.getSequenceNumber() % NR_BUFS] = true; /* mark buffer as full */
                        in_buf[r.getSequenceNumber() % NR_BUFS] = new Packet(r.getPacketInformation()); /* insert data into buffer */
                        while (arrived[frameExpected % NR_BUFS]) {
                                /* Pass frames and advance window. */
                                receiver.toNetworkLayer(in_buf[frameExpected % NR_BUFS]);
                                no_nak = true;
                                arrived[frameExpected % NR_BUFS] = false;
                                frameExpected =inc(frameExpected); /* advance lower edge of receiver’s window */
                                too_far = inc(too_far); /* advance upper edge of receiver’s window */
                                //start_ack_timer(); /* to see if a separate ack is needed */
                        }
                          
                    
                         
                     }
                         //(r.getConfirmNumber()+1) % (MAX_SEQ + 1)
                    if((r.getFrameType()==FrameKind.nak) && between(this.ackExpected,(r.getConfirmNumber()+1)%(MAX_SEQ+1),nextFrameToSend)){
                                   sendData(FrameKind.data,(r.getConfirmNumber()+1) % (MAX_SEQ + 1),this.frameExpected,receiver,sender, out_buf);
                    }
                                    
                           while (between(this.ackExpected, r.getConfirmNumber(), this.nextFrameToSend)) {
                                   nBuffered-=1; /* handle piggybacked ack */                                   
                                   //stoptimer(this.ackExpected % NR_BUFS); /* frame arrived intact */
                                   ackExpected=inc(this.ackExpected); /* advance lower edge of sender’s window */
                           }
                     }
                      
                    
                    break;
                case cksum_err:
                    if(this.no_nak)
                        sendData(FrameKind.nak, 0, this.frameExpected,receiver,sender, out_buf); /* we timed out */
                    break;
                case timeout:
                    sendData(FrameKind.data, oldest_frame, this.frameExpected,receiver,sender, out_buf); /* we timed out */

                    break;
                case ack_timeout:
                    sendData(FrameKind.ack, 0, this.frameExpected,receiver,sender, out_buf); /* we timed out */
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
        if (k < MAX_SEQ) {
            k = k + 1;
        } else {
            k = 0;
        }
        return k;
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

