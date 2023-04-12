/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model.DataInfo;

/**
 *
 * @author ricardosoto
 */
public class Frame {
    private String frameType;
    private int sequenceNumber;
    private int confirmNumber;
    private String packetInformation;

    public Frame(String frameType, int sequenceNumber, int confirmNumber, String packetInformation) {
        this.frameType = frameType;
        this.sequenceNumber = sequenceNumber;
        this.confirmNumber = confirmNumber;
        this.packetInformation = packetInformation;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getConfirmNumber() {
        return confirmNumber;
    }

    public void setConfirmNumber(int confirmNumber) {
        this.confirmNumber = confirmNumber;
    }

    public String getPacketInformation() {
        return packetInformation;
    }

    public void setPacketInformation(String packetInformation) {
        this.packetInformation = packetInformation;
    }
}
