/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model.Layers;

import java.util.ArrayList;
import pry1_redes.Enums.EventType;
import pry1_redes.Model.DataInfo.Frame;

/**
 *
 * @author ricardosoto
 */
public class PhysicalLayer {
    private ArrayList<EventType> events;
    private Frame frame;
    
    
    
     public EventType validFrame(Frame frame){
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
    public PhysicalLayer(Frame frame) {
        this.frame = frame;
        this.events = new ArrayList<EventType>();
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    
    
}
