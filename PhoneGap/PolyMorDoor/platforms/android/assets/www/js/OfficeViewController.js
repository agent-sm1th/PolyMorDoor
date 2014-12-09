var OfficeViewController = function() {

    function showAlert (message, title) {
        if (navigator.notification) {
            navigator.notification.alert(message, null, title, 'OK');
        } else {
            alert(title ? (title + ": " + message) : message);
        }
    }

    function openMountDoom() {
        console.log("///////////// OfficeViewController.openMountDoom()");

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/door",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");

        xmlhttp.send("access_token=YOUR_ACCESS_TOKEN&args=3025,1,10");
    }

    function openMorDor() {
        console.log("///////////// OfficeViewController.openMorDor()");

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/door",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");

        xmlhttp.send("access_token=YOUR_ACCESS_TOKEN&args=3025,2,10");
    }

    function getTempMountDoom() {
        console.log("///////////// OfficeViewController.getTempMountDoom()");

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/temperature?access_token=YOUR_ACCESS_TOKEN",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");

        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                console.log("///////////// OfficeViewController.getTempMountDoom response: " + xmlhttp.responseText);
                showAlert (xmlhttp.responseText, "TempMountDoom")
            }
        }

        xmlhttp.send();
    }

    function getStateMountDoom() {
        console.log("///////////// OfficeViewController.getStateMountDoom()");

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET","https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/topdoorstate?access_token=YOUR_ACCESS_TOKEN",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                console.log("///////////// OfficeViewController.getStateMountDoom response: " + xmlhttp.responseText);
                showAlert (xmlhttp.responseText, "StateMountDoom")
            }
        }

        xmlhttp.send();
    }

    this.render = function() {
        this.el.html(OfficeViewController.template());
        return this;
    };

    this.initialize = function() {

        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        this.el.on('click', '.doorMountDoomOpenButton', openMountDoom);
        this.el.on('click', '.doorMorDorOpenButton', openMorDor);
        this.el.on('click', '.tempMountDoomButton', getTempMountDoom);
        this.el.on('click', '.doorMountDoomStateButton', getStateMountDoom);
    };
}

OfficeViewController.template = Handlebars.compile($("#office-template").html());
