/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pry1_redes.Model;

import java.util.HashMap;

/**
 *
 * @author ricardosoto
 */
public class ProtocolFactory {
    private HashMap<String, Protocol> hashProtocols = new HashMap<>();
    
    public ProtocolFactory(){
        hashProtocols.put("Utopia", new Utopia());
        hashProtocols.put("Stop-and-wait", new StopAndWait());
        hashProtocols.put("PAR", new PAR());
        hashProtocols.put("go-back-n", new GoBackN());
        hashProtocols.put("selective-repeat", new SelectiveRepeat());
        
        
    }
    
    public Protocol createProtocol(String protocol){
        return hashProtocols.get(protocol);
    }
}
