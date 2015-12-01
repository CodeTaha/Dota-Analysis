/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dota.rs;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author taha
 */
@Path("dota")
public class dota {
  Models model;
  Gson gson= new Gson();
  @Context
  private UriInfo context;

  /**
   * Creates a new instance of dota
   */
  public dota() {
    model= new Models();
  }

  /**
   * Retrieves representation of an instance of com.dota.rs.dota
   * @return an instance of java.lang.String
   */
  @GET
  @Produces("application/json")
  public String getJson() {
    //TODO return proper representation object
    throw new UnsupportedOperationException();
  }
  
  /**
   * Retrieves representation of an instance of com.dota.rs.dota
   * @param tier
   * @param high
   * @param low
   * @param colorsel
   * @return an instance of java.lang.String
   */
  @Path("getMZ")
  @GET
  @Produces("application/json")
  public String getMz(
          @DefaultValue("high") @QueryParam("tier") String tier,
          @DefaultValue("200") @QueryParam("high") int high,
          @DefaultValue("0") @QueryParam("low") int low,
          @DefaultValue("0") @QueryParam("colorsel") int colorsel
  ) {
    System.out.println("Reached getMZ"+tier+high+low);
    //TODO return proper representation object
    return(model.getMz(tier,low,high,colorsel));
  }
  
  /**
   * Retrieves representation of an instance of com.dota.rs.dota
   * @param tier
   * @param high
   * @param low
   * @param colorsel
   * @return an instance of java.lang.String
   */
  @Path("getDD")
  @GET
  @Produces("application/json")
  public String getDD(
          @DefaultValue("0") @QueryParam("low") int low,
          @DefaultValue("50") @QueryParam("high") int high
  ) {
    ArrayList res = new ArrayList();
    res.add(model.getDD("Pro","Win\r",low,high));
    res.add(model.getDD("Pro","Lose\r",low,high));
    res.add(model.getDD("VeryHigh","Win\r",low,high));
    res.add(model.getDD("VeryHigh","Lose\r",low,high));
    res.add(model.getDD("High","Win\r",low,high));
    res.add(model.getDD("High","Lose\r",low,high));
    res.add(model.getDD("Normal","Win\r",low,high));
    res.add(model.getDD("Normal","Lose\r",low,high));
    return gson.toJson(res);
  }

  /**
   * PUT method for updating or creating an instance of dota
   * @param content representation for the resource
   * @return an HTTP response with content of the updated or created resource.
   */
  @PUT
  @Consumes("application/json")
  public void putJson(String content) {
  }
}
