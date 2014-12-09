//
//  Created by R2D2 on 2014/11/27.
//  Copyright (c) 2014 Polymorph. All rights reserved.
//

var BottomDoorViewController = function() {
    var _xmlhttp;

    function openDoor() {
//        console.log("///////////// BottomDoorViewController.openDoor()");
        _xmlhttp.open("POST","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/digitalwrite",true);
        _xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        _xmlhttp.send("access_token=YOUR_ACCESS_TOKEN&args=D1,HIGH");

        document.getElementById("door1OpenButton").style.display = "none";
        document.getElementById("door1CloseButton").style.display = "inline";
    }

    function closeDoor() {
        _xmlhttp.open("POST","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/digitalwrite",true);
        _xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        _xmlhttp.send("access_token=YOUR_ACCESS_TOKEN&args=D1,LOW");

        document.getElementById("door1OpenButton").style.display = "inline";
        document.getElementById("door1CloseButton").style.display = "none";
    }

    this.render = function() {
        this.el.html(BottomDoorViewController.template());
        return this;
    };

    this.initialize = function() {
        // initialize this badboy
        _xmlhttp = new XMLHttpRequest();

        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        this.el.on('click', '.door1OpenButton', openDoor);
        this.el.on('click', '.door1CloseButton', closeDoor);
    };
}

BottomDoorViewController.template = Handlebars.compile($("#open-bottom-door-template").html());
