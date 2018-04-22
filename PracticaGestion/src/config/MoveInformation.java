/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */@XmlRootElement(name = "comando")
public class MoveInformation {
    
    private int n1;
    private int n2;
    
    @XmlElement(name = "n1")
    public int getN1() {
        return n1;
    }

    public void setN1(int n1) {
        this.n1 = n1;
    }
    @XmlElement(name = "n2")
    public int getN2() {
        return n2;
    }

    public void setN2(int n2) {
        this.n2 = n2;
    }
    
    
    
}
