/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package finalproject_mari_kanepaul;

import com.formdev.flatlaf.FlatLightLaf;

/**
 *
 * @author Kane
 */
public class FinalProject_Mari_KanePaul {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();
    java.awt.EventQueue.invokeLater(() -> {
        new MainDashboard().setVisible(true);
    });
    
    }
    
    
}
