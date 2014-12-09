//
//  Created by R2D2 on 2014/11/27.
//  Copyright (c) 2014 Polymorph. All rights reserved.
//

var PolymorphDoorViewController = function() {
    var _xmlhttp;

    function openDoor() {
//        console.log("///////////// PolymorphDoorViewController.openDoor()");
        _xmlhttp.open("POST","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/digitalwrite",true);
        _xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        _xmlhttp.send("access_token=YOUR_ACCESS_TOKEN&args=D0,HIGH");

        document.getElementById("door0OpenButton").style.display = "none";
        document.getElementById("door0CloseButton").style.display = "inline";
    }

    function closeDoor() {
        _xmlhttp.open("POST","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/digitalwrite",true);
        _xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        _xmlhttp.send("access_token=YOUR_ACCESS_TOKEN&args=D0,LOW");

        document.getElementById("door0OpenButton").style.display = "inline";
        document.getElementById("door0CloseButton").style.display = "none";
    }

    this.render = function() {
        this.el.html(PolymorphDoorViewController.template());
        return this;
    };

    this.initialize = function() {
        // initialize this badboy
        _xmlhttp = new XMLHttpRequest();

        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        this.el.on('click', '.door0OpenButton', openDoor);
        this.el.on('click', '.door0CloseButton', closeDoor);
    };
}

PolymorphDoorViewController.template = Handlebars.compile($("#open-polymorph-door-template").html());
