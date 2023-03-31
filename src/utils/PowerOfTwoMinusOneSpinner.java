/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.ArrayList;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

/**
 *
 * @author Administrador
 */
public class PowerOfTwoMinusOneSpinner extends JSpinner {
    
    public PowerOfTwoMinusOneSpinner() {
        super(new PowerOfTwoMinusOneSpinnerModel());
    }
    
    private static class PowerOfTwoMinusOneSpinnerModel extends SpinnerListModel {
        private static final ArrayList<Integer> values = new ArrayList<>();
        static {
            for (int i = 1; i <= 65535; i = (i << 1) | 1) {
                values.add(i);
            }
        }
        
        public PowerOfTwoMinusOneSpinnerModel() {
            super(values);
        }
    }
}