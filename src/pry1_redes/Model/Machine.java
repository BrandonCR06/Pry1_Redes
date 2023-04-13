/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import java.util.ArrayList;
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
        //this.physical.addEvent(this.physical.validFrame(frame));
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
    
}
