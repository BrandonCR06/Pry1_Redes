/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model.DataInfo;

/**
 *
 * @author ricardosoto
 */
public class Packet {
    private String header;
    
    public Packet(String header){
        this.header = header;
    }
    
    public String getHeader(){
        return header;
    }
    
}
