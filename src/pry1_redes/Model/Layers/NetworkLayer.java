/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model.Layers;

import pry1_redes.Model.DataInfo.Packet;

/**
 *
 * @author ricardosoto
 */
public class NetworkLayer {
    private Packet packet;

    public NetworkLayer(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
