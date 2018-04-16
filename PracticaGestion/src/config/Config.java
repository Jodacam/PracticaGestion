/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import javax.xml.bind.annotation.*;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
@XmlRootElement(name = "Config")
public class Config {

    
    private int numColumn;
    private int numRow;
    private int imageSize;    
    private String gameName; 

    
    @XmlElement(name = "gameName")
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    
    
    @XmlElement(name = "numColumn")
    public int getNumColumn() {
        return numColumn;
    }
   
   
    public void setNumColumn(int numColumn) {
        this.numColumn = numColumn;
    }
    
    @XmlElement(name = "numRow")
    public int getNumRow() {
        return numRow;
    }
    

    public void setNumRow(int numRow) {
        this.numRow = numRow;
    }
    
    @XmlElement(name = "imageSize")
    public int getImageSize() {
        return imageSize;
    }

    @Override
    public String toString() {
        return "element Config { element numColumn{'" + numColumn + "'}, element numRow {'" + numRow + "'}, element imageSize {'" + imageSize + "'},"
                + " element gameName {'" + gameName + "'} }";
    }
    
    
    
    
    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }



    
    
    
    
}
