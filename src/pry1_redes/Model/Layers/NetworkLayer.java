/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model.Layers;

import java.util.ArrayList;
import java.util.Random;
import pry1_redes.Enums.EventType;
import pry1_redes.Model.DataInfo.Packet;

/**
 *
 * @author ricardosoto
 */
public class NetworkLayer {
    private ArrayList<EventType> events;
    private ArrayList<Packet> packets = new ArrayList<Packet>();
    
    
    public NetworkLayer(int n) {
        generatePackets(n);
        this.events = new ArrayList<EventType>();
        
    }
    public  void generatePackets(int n) {
        ArrayList<Packet> packets = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            String message = generateRandomString(random.nextInt(20) + 1); // Genera un string aleatorio de longitud entre 1 y 20
            Packet packet = new Packet(message);
            packets.add(packet);
        }

        this.packets = packets;
    }
    private String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(26) + 'a'); // Genera una letra aleatoria minúscula
            sb.append(c);
        }

        return sb.toString();
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
         Random random = new Random();
         
        int randomIndex = random.nextInt(this.packets.size());
        
        Packet p = this.packets.get(randomIndex);
        
        return p;
    }

    public void setPacket(Packet packet) {
        this.packets.add(packet);
    }
}
