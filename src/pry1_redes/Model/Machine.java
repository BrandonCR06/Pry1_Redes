/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import java.util.ArrayList;
import java.util.Random;
import pry1_redes.Enums.EventType;
import pry1_redes.Model.DataInfo.Frame;
import pry1_redes.Model.DataInfo.Packet;
import pry1_redes.Model.Layers.NetworkLayer;
import pry1_redes.Model.Layers.PhysicalLayer;

/**
 *
 * @author ricardosoto
 */
public class Machine {
    private Protocol protocol;
    private PhysicalLayer physical;
    private NetworkLayer network;
    public String info = "";
    

    public Machine(Protocol protocol, PhysicalLayer physical, NetworkLayer network) {
        this.protocol = protocol;
        this.physical = physical;
        this.network = network;
        
        
    }   
   
    public Frame fromPhysicalLayer(){
        return physical.getFrame();
    }
    public void toPhysicalLayer( Frame frame){             
        double p = this.getPhysical().getErrPor();
        System.out.println(p);
        EventType option = new Random().nextDouble() > p  ? EventType.frame_arrival  : EventType.cksum_err;
        this.getPhysical().addEvent(option);
        physical.setFrame(frame);
    }
    public Packet fromNetworkLayer(){
        
        return network.getPacket();
    }
    
    public void toNetworkLayer( Packet packet){
        //this.network.addEvent(this.network.validPacket(packet));
        network.setPacket(packet);  
    }
   

    public Protocol getProtocol() {
        return protocol;
    }
    

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public PhysicalLayer getPhysical() {
        return physical;
    }

    public void setPhysical(PhysicalLayer physical) {
        this.physical = physical;
    }

    public NetworkLayer getNetwork() {
        return network;
    }

    public void setNetwork(NetworkLayer network) {
        this.network = network;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    
    
    
    
}
