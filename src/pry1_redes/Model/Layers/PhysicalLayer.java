/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model.Layers;

import pry1_redes.Model.DataInfo.Frame;

/**
 *
 * @author ricardosoto
 */
public class PhysicalLayer {
    
    private Frame frame;
    
    public PhysicalLayer(Frame frame) {
        this.frame = frame;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    
    
}
