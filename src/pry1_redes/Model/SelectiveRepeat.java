/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Enums.EventType;

/**
 *
 * @author ricardosoto
 */
public class SelectiveRepeat implements Protocol{

    private static final int MAX_SEQ = 7;
    private Packet[] buffer = new Packet[MAX_SEQ + 1];
    private int nextFrameToSend = 0;
    private int ackExpected = 0;
    private int frameExpected = 0;
    private int nBuffered = 0;
    private boolean network_layer_state = true;

    @Override
    public void send(Machine receiver, Machine sender){
        this.send_receive(receiver, sender);
    }
    public void sendData(Machine receiver, Machine sender, Packet[] buffer) {
        Frame s = new Frame(null, ackExpected, ackExpected, null);
        
        this.buffer[this.nextFrameToSend] = sender.fromNetworkLayer(); /* fetch new packet */
        s.setPacketInformation(this.buffer[this.nextFrameToSend].getHeader());
        s.setSequenceNumber(this.nextFrameToSend);
        s.setConfirmNumber((this.frameExpected + MAX_SEQ) % (MAX_SEQ + 1));
        
        receiver.toPhysicalLayer(s); /* transmit the frame */
        
        receiver.getNetwork().disableNetworkLayer();
        receiver.getProtocol().send(receiver, sender);
        
        // Falta el timer
        // start_timer(frame_nr % NR_BUFS);
        inc(this.nextFrameToSend); /* advance sender’s upper window */
        
    
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    public void send_receive(Machine receiver, Machine sender) {
        Frame frame = null;
        EventType event = receiver.getPhysical().getLastEvent();
        if(receiver.getNetwork().networkReady()){
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
                       System.out.println("---------------------------");
                System.out.println("Se recibe desde la maquina "+receiver.toString());
                System.out.println("SqcNumber frame recibido" + frame.getSequenceNumber());
                System.out.println("CfrNumber" + frame.getConfirmNumber());
                System.out.println("Expected" + this.frameExpected);
                System.out.println("---------------------------");
                    if (frame.getSequenceNumber() == this.frameExpected){
                        receiver.toNetworkLayer(new Packet(frame.getPacketInformation()));
                        frameExpected =inc(this.frameExpected);
                    }
                    while (between(this.ackExpected, frame.getConfirmNumber(), this.nextFrameToSend)){
                        nBuffered = nBuffered - 1;
                        // stop_timer(this.ackExpected % MAX_SEQ);
                        ackExpected= inc(this.ackExpected);
                    }
                    break;
                case cksum_err:
                    break;
                case timeout:
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

