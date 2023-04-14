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
 * @author Administrador
 */
public class SlidingWindow implements Protocol {
    
    public int MAX_SEQ = 1; 
    public int frame_expected = 0;
    public int next_frame_to_send = 0;
    public Frame r, s = new Frame("",0,1,"");
    public Packet buffer;
    
    public SlidingWindow(){
        
    }
    @Override
    public void send(Machine receiver, Machine sender) {
        
        // Fetch a packet from the network layer
        this.buffer = sender.fromNetworkLayer();
        
        // Prepare to send the initial frame
        this.s.setPacketInformation(this.buffer.getHeader());
        this.s.setSequenceNumber(this.next_frame_to_send);
        this.s.setConfirmNumber(1-this.frame_expected); // piggybacked ack
        System.out.println("Se recibe de la maquina"+sender.getInfo());
        System.out.println("Se obtuvo el FRAME ACTUAL: SqcNumber" + this.s.getSequenceNumber());
        System.out.println("Se obtuvo el FRAME ACTUAL: CfrNumber" + this.s.getConfirmNumber());
        System.out.println("---------------------------");
        
        // Transmit the frame
        receiver.toPhysicalLayer(s);  
        // Start timer
        
        //StartTImer
            EventType event = receiver.getPhysical().getLastEvent();
            if(event == EventType.frame_arrival){
                
                //GET r from physical layer
                this.r = receiver.fromPhysicalLayer();
                
                if(r.getSequenceNumber() == this.frame_expected){
                    System.out.println("true1");
                    receiver.toNetworkLayer(new Packet(""));
                    this.frame_expected = invert(this.frame_expected);
                }
                if(r.getConfirmNumber()== this.next_frame_to_send){
                    System.out.println("true2");
                    // Stop timer
                    this.buffer= sender.fromNetworkLayer();
                    this.next_frame_to_send = invert(this.next_frame_to_send);
                    System.out.println("---------------------------");
                    System.out.println("Se recibe de la maquina"+sender.getInfo());
                    System.out.println("Se obtuvo el FRAME ACTUAL: SqcNumber" + this.r.getSequenceNumber());
                    System.out.println("Se obtuvo el FRAME ACTUAL: CfrNumber" + this.r.getConfirmNumber());
                    System.out.println("---------------------------");
                }
                // Prepare to send the initial frame
                this.s.setPacketInformation(this.buffer.getHeader());
                this.s.setSequenceNumber(this.next_frame_to_send);
                this.s.setConfirmNumber(1-this.frame_expected); // piggybacked ack

                // Transmit the frame
                receiver.toPhysicalLayer(s);  
                // Start timer
            }
        /*
        System.out.println("---------------------------");
        System.out.println("Se recibe de la maquina"+sender.getInfo());
        System.out.println("Se obtuvo el FRAME ACTUAL: SqcNumber" + frame.getSequenceNumber());
        System.out.println("Se obtuvo el FRAME ACTUAL: CfrNumber" + frame.getConfirmNumber());
        System.out.println("---------------------------");*/
        
    }
    
    private int invert(int seqNum) {
        return (seqNum + 1) % (this.MAX_SEQ + 1);
    }

    @Override
    public void receive(Machine receiver, Machine sender) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
