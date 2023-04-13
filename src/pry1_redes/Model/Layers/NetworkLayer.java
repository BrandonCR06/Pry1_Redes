/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model.Layers;

import java.util.ArrayList;
import pry1_redes.Enums.EventType;
import pry1_redes.Model.DataInfo.Packet;

/**
 *
 * @author ricardosoto
 */
public class NetworkLayer {
    private ArrayList<EventType> events;
    private Packet packet;

    public NetworkLayer(Packet packet) {
        this.packet = packet;
        this.events = new ArrayList<EventType>();
    }
     public EventType validPacket(Packet frame){
        return EventType.frame_arrival;
    }
      public EventType getLastEvent(){
        if(!events.isEmpty()){
            return this.events.get(this.events.size()-1);
        }
        return null;
    }
    public void addEvent(EventType event){
        this.events.add(event);
    }
    

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
