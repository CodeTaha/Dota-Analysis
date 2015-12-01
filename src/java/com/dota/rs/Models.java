/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dota.rs;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author taha
 */
public class Models {
    final String Base_Url="http://localhost:8084/BookRead/index.jsp?trans=";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/DOTA";
    
    static final String USER = "root";
    static final String PASS = "root123";
    Statement stmt = null;
    Connection conn = null;
    //  Database credentials
    Gson gson=new Gson();
    public Models() {
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");
        }catch(SQLException se){
            System.out.println("SQLException ->models->models()="+se);
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFoundException ->models->models()="+e);
        }//end try
    }
    
    public String getMz(String tier, int low, int high, int colorsel){
      JsonArray res = new JsonArray();
      try {
        String query="";
        switch(tier){
          case "pro":{
            query = "SELECT * FROM DOTA.pro where tsync>=? and tsync<? ORDER BY tsync";
          }break;
          case "vhigh":{
            query = "SELECT * FROM DOTA.vhigh where tsync>=? and tsync<? ORDER BY tsync";
          }break;
          case "high":{
            query = "SELECT * FROM DOTA.high where tsync>=? and tsync<? ORDER BY tsync";
          }break;
          case "normal":{
            query = "SELECT * FROM DOTA.normal where tsync>=? and tsync<? ORDER BY tsync";
          }break;
          default:{
            query = "SELECT * FROM DOTA.pro where tsync>=? and tsync<? ORDER BY tsync";
          }
            
        }
        
        
        
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        
        preparedStmt.setInt(1, low);
        preparedStmt.setInt(2, high);
        ResultSet rs=preparedStmt.executeQuery();
          while(rs.next())
          {
            JsonObject jobj= new JsonObject();
            if(colorsel==1){
              if(rs.getString("team").equals("\"radiant\"")) {
                jobj.addProperty("team", 0);
              } else {
                jobj.addProperty("team", 1);
              }
            } else {
              if(rs.getInt("won")==1) {
                jobj.addProperty("team", 1);
              } else {
                jobj.addProperty("team", 0);
              }
            }
            jobj.addProperty("t", rs.getInt("t"));
            jobj.addProperty("x", rs.getInt("x"));
            jobj.addProperty("y", rs.getInt("y"));
            jobj.addProperty("match", rs.getInt("mtch"));
            
            jobj.addProperty("won", rs.getInt("won"));
            jobj.addProperty("tstd", rs.getInt("tstd"));
            jobj.addProperty("tsync", rs.getInt("tsync"));
            jobj.addProperty("tper", rs.getInt("tper"));
            jobj.addProperty("tier", rs.getString("tier"));
            
            res.add(jobj);
          }
        return gson.toJson(res);
      } catch(Exception e){
        System.out.println("Exception="+e);
        return "{\"error\":\""+e+"\"}";
      }
    }

public ArrayList getDD(String tier, String win, int low, int high) {
    ArrayList res = new ArrayList();
    try{
      String query="";
      query = "SELECT dd,won FROM DOTA.master_distance where tier=? and won=? and tsync>=? and tsync<?;";
      
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      
      preparedStmt.setString(1, tier);
      preparedStmt.setString(2, win);
      preparedStmt.setInt(3, low);
      preparedStmt.setInt(4, high);
      ResultSet rs=preparedStmt.executeQuery();
      while(rs.next())
      {
        //JsonObject jobj= new JsonObject();
        //jobj.addProperty("team", rs.getString("team"));
        //jobj.addProperty("tsync", rs.getInt("tsync"));
        //jobj.addProperty("dd", (int)rs.getDouble("dd"));
        //jobj.addProperty("tier", rs.getString("tier"));
        //jobj.addProperty("win", rs.getString("won"));
        /*if("Win\r".equals(rs.getString("won"))) {
          jobj.addProperty("won", 1);
        } else {
          jobj.addProperty("won", 0);
        }*/
        res.add(rs.getDouble("dd"));
      }
      return res;  
        
    } catch(Exception e){
      JsonObject jobj= new JsonObject();
      jobj.addProperty("error", e.toString());
      res.add(jobj);
      return res;
    }
  }
}
