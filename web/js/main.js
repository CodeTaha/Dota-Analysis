/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global d3 */

var margin = {top: 10, right: 10, bottom: 20, left: 10};
var width = 500 - margin.left - margin.right;
var height = 500 - margin.top - margin.bottom;
var pixl= 4;

var x = d3.scale.linear().range([0, width]);
var y = d3.scale.linear().range([height, 0]);

var bitmap_data;
var proObj;
var high=200, low=0;
var colorScale=d3.scale.ordinal();
var sele=true;
var runFlag=true;
var colorsel=1;
      colorScale.range([
         "#262424", //void dire
         "#425623", // void radiant
         "#46514B", //toplane dire
         "#f210f2", // laneshop dire
         "#7B8F34", // base1 radiant
         "#537739", // toplane radiant
         "#486b2e", // jungle radiant
         "#4f6499", // river
         "#414347", // jungle dire
         "#537739", // midlane radiant
         "#f4ed1a", // secretshop radiant
         "#537739", // bottomlane radiant
         "#46514B", // midlane dire
         "#f74909", // pit dire
         "#7f7f7f", // base2 dire
         "#f4ed1a", //secretshop dire
         "#46514B", // bottomlane dire
         "#f210f2"]);// laneshop radiant
$( document ).ready(function() {
  proObj=new DOTA("pro");
  vhighObj=new DOTA("vhigh");
  highObj=new DOTA("high");
  normalObj=new DOTA("normal");
  d3.csv("Dota_Labels.csv", function(error, data) {
    //Converting my strings in int's
    data.forEach(function(d){
      d.x = +d.x;
      d.y  = +d.y;
      d.Label_Value  = +d.Label_Value;
    });
    bitmap_data= data;
    
    x.domain(d3.extent(data, function(d){ return d.x})).nice();
    y.domain(d3.extent(data, function(d){ return d.y})).nice();
    colorScale.domain(data.map(function(d) { return d.Section_Label; }));
    
    proObj.createMap();
    vhighObj.createMap();
    highObj.createMap();
    normalObj.createMap();
   
  });
  
  $("#clearMaps").click(function(){
    proObj.clearMap();
    vhighObj.clearMap();
    highObj.clearMap();
    normalObj.clearMap();
  });
  $(".highlow").change(function(){
    low = $("#low").val();
    high = $("#highv").val();
    sele = $("#sel1").val() === "0"? false: true;
    colorsel = $("#sel2").val();
    console.log(low,high,sele,colorsel);
  });
  $("#submit").click(function(){
    proObj.clearMap();
    
    proObj.spawn(sele, function(data){
      proObj.tempf(data);
    });
    vhighObj.clearMap();
    highObj.clearMap();
    normalObj.clearMap();
    vhighObj.spawn(sele, function(data){
      vhighObj.tempf(data);
    });
    highObj.spawn(sele, function(data){
      highObj.tempf(data);
    });
    normalObj.spawn(sele, function(data){
      normalObj.tempf(data);
    });
    
  });
});


var DOTA= function(tier) {
  this.tier = tier;
  this.svg = d3.select("#"+tier).append("svg")
          .attr("width", width + margin.left + margin.right)
          .attr("height", height + margin.top + margin.bottom)
          .append("g")
          .attr("transform", "translate(" + (margin.left) + "," + margin.top + ")");
  this.i = 0;
  this.nest;
 
};

DOTA.prototype.createMap = function() {
  var bitmap = this.svg.append("g")
              .selectAll("rect")
              .data(bitmap_data)
              .enter()
              .append("rect")
              .attr("x", function(d){ return x(d.x)})
              .attr("y", function(d){ return y(d.y)})
              .attr("height", pixl)
              .attr("width", pixl)
              .style("fill", function(d) {return colorScale(d.Section_Label)});
      
};

DOTA.prototype.spawn = function(sele,callback){
  var svg1=this.svg;
  var tier= this.tier;
  d3.json("api/dota/getMZ?tier="+tier+"&high="+high+"&low="+low+"&colorsel="+colorsel, function(error, data) {
    
    var tsync=new Array();
    //Converting my strings in int's
    data.forEach(function(d){
      // console.log(d)
      d.x = +d.x;
      d.y  = +d.y;
      d.tsync  = +d.tsync;
      if(tsync.indexOf(d.tsync)==-1)
      {
        tsync.push(d.tsync);
      }
    });
    var nest = d3.nest().key(function(d){return d.tsync}).entries(data);
    var nestLength = nest.length;
    //this.dataReadyFlag=false;\
    //callback(nest);
    var plot = function(timepoint){
      var temp=timepoint.values;
      $("."+tier).remove();
      svg1.selectAll("."+tier)
              .data(temp)
              .enter()
              .append("circle")
              .attr("class",tier)
              .attr("cx", function(d){return x(d.x);})
              .attr("cy", function(d){ return y(d.y)})
              .attr("r", pixl/2)
              //.attr("height", pixl)
              //.attr("width", pixl)
              .style("opacity", 0.8)
      //.style("fill", "none")
              .style("fill", function(d){if(d.team===0){return "blue";} else{return "red";}});
    };
    var i=0;
    var animationLoop = function(){
      if(i<nestLength && runFlag===true){
        setTimeout(function() {
          requestAnimationFrame(animationLoop);
          // Drawing code goes here
          plot(nest[i%nestLength]);
          i++;
        }, 1000 / 10);
      }
    };
    if(sele){
      animationLoop();
    } else{
      callback(nest);
    }
  });
};

DOTA.prototype.tempf= function(data){
  console.log("tempf");
  this.nest=data;
  this.nestLength = this.nest.length;
  this.animationLoop();
};

DOTA.prototype.plotHeat = function (timepoint){
  var temp=timepoint.values;
  var players=this.svg.append("g")
          .attr("class","heat")
          .selectAll("."+this.tier)
          .data(temp)
          .enter()
          .append("rect")
          .attr("class",this.tier)
          .attr("x", function(d){return x(d.x);})
          .attr("y", function(d){ return y(d.y);})
          .attr("height", pixl)
          .attr("width", pixl)
          .style("opacity", 0.05)
  //.style("fill", "none")
          .style("fill", function(d){if(d.team===1){return "blue";} else{return "red";}});    
};

DOTA.prototype.animationLoop = function() {
  if(this.i<this.nestLength){
    this.plotHeat(this.nest[this.i]);
    this.i++;
    setTimeout(this.animationLoop(), 1000/100);
  } else {
    //this.clearMap();
  }
};

DOTA.prototype.clearMap = function(){
  console.log("clearing Map of "+this.tier);
  runflag=false;
  $(".heat").remove();
  $("."+this.tier).remove();
};

