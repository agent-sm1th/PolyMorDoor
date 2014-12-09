//
//  ViewController.swift
//  PolyMorDoor
//
//  Created by Duke Nukem on 2014/11/27.
//  Copyright (c) 2014 Polymorph. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    let kPolyMainDoor = "1"
    let kBuildingMainDoor = "2"

    let kUnlock = "HIGH"
    let kLock = "LOW"
    
    @IBOutlet var polyMainDoor: UIButton!
    @IBOutlet var buildingMainDoor: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


  @IBAction func getDeviceStateTapped(sender: AnyObject) {
    doDeviceStateGet()
  }


  @IBAction func getTemperatureTapped(sender: AnyObject) {
    doTemperatureGet()
  }


    @IBAction func polyTopDoorTapped(sender: UIButton) {
      doOpenPost("3025", lockState: kUnlock, door: kPolyMainDoor, delay: 10) // Hard-coded pin and delay for now.
    }

    @IBAction func buildingMainDoorTapped(sender: UIButton) {
      doOpenPost("3025", lockState: kUnlock, door: kBuildingMainDoor, delay: 10) // Hard-coded pin and delay for now.
    }


  @IBAction func getDeviceInfoTapped(sender: AnyObject) {
    doDeviceInfo()
  }




  //////////
  ///// Get State
  //////////



  func doDeviceStateGet() {

    var request = NSMutableURLRequest(URL: NSURL(string: "https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/topdoorstate?access_token=YOUR_ACCESS_TOKEN")!)
    var session = NSURLSession.sharedSession()
    request.HTTPMethod = "GET"

    var requestError: NSError?
    var urlResponse : NSURLResponse?
    var response1 = NSURLConnection.sendSynchronousRequest(request, returningResponse: &urlResponse, error: &requestError)
    var json = NSJSONSerialization.JSONObjectWithData(response1!, options: .MutableLeaves, error: &requestError) as? NSDictionary

    // Did the JSONObjectWithData constructor return an error? If so, log the error to the console
    if(requestError != nil) {
      println(requestError!.localizedDescription)
      let jsonStr = NSString(data: response1!, encoding: NSUTF8StringEncoding)
      println("Error could not parse JSON: '\(jsonStr)'")
    }
    else {
      // The JSONObjectWithData constructor didn't return an error. But, we should still
      // check and make sure that json has a value using optional binding.
      if let parseJSON = json {
        // Okay, the parsedJSON is here, let's get the value for 'success' out of it

          println(parseJSON)

        // 0: Open; 1: Closed
        var result = parseJSON["result"] as? Int

        var message : String = ""
        if (result == 0) {
          message = "The door is open."
        } else if (result == 1) {
          message = "The door is closed."
        }

        let alert = UIAlertView()
        alert.title = "Door State"
        alert.message = message
        alert.addButtonWithTitle("OK")
        alert.show()

      }
      else {
        // Woa, okay the json object was nil, something went worng. Maybe the server isn't running?
        let jsonStr = NSString(data: response1!, encoding: NSUTF8StringEncoding)
        println("Error could not parse JSON: \(jsonStr)")
      }
    }
    
  }



  //////////
  ///// Do Temperature Get
  //////////


  func doTemperatureGet() {
    var request = NSMutableURLRequest(URL: NSURL(string: "https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/temperature?access_token=YOUR_ACCESS_TOKEN")!)
    var session = NSURLSession.sharedSession()
    request.HTTPMethod = "GET"

    var requestError: NSError?
    var urlResponse : NSURLResponse?
    var response1 = NSURLConnection.sendSynchronousRequest(request, returningResponse: &urlResponse, error: &requestError)
    var json = NSJSONSerialization.JSONObjectWithData(response1!, options: .MutableLeaves, error: &requestError) as? NSDictionary

    // Did the JSONObjectWithData constructor return an error? If so, log the error to the console
    if(requestError != nil) {
      println(requestError!.localizedDescription)
      let jsonStr = NSString(data: response1!, encoding: NSUTF8StringEncoding)
      println("Error could not parse JSON: '\(jsonStr)'")
    }
    else {
      // The JSONObjectWithData constructor didn't return an error. But, we should still
      // check and make sure that json has a value using optional binding.
      if let parseJSON = json {
        // Okay, the parsedJSON is here, let's get the value for 'success' out of it
        var temp = parseJSON["result"] as? Float

        let s = NSString(format: "%.2f", temp!)

        println("Temperature: \(temp)")

        let ss = String(s)

        let alert = UIAlertView()
        alert.title = "Temperature"
        alert.message = ss
        alert.addButtonWithTitle("OK")
        alert.show()

      }
      else {
        // Woa, okay the json object was nil, something went worng. Maybe the server isn't running?
        let jsonStr = NSString(data: response1!, encoding: NSUTF8StringEncoding)
        println("Error could not parse JSON: \(jsonStr)")
      }
    }

  }


  //////////
  ///// Do Open Post
  //////////

  func doOpenPost(pin: String, lockState: String, door: String, delay: Int) {

    var request = NSMutableURLRequest(URL: NSURL(string: "https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID/door")!)
    var session = NSURLSession.sharedSession()
    request.HTTPMethod = "POST"

    var doorParam : String = "access_token=YOUR_ACCESS_TOKEN" + "&" + "args=" + pin + "," + door + ",\(delay)"

    println(doorParam)

    var err: NSError?
    let data = (doorParam as NSString).dataUsingEncoding(NSUTF8StringEncoding)
    request.HTTPBody = data
    request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
    request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Accept")

    var task = session.dataTaskWithRequest(request, completionHandler: {data, response, error -> Void in
      println("Response: \(response)")
      var strData = NSString(data: data, encoding: NSUTF8StringEncoding)
      println("Body: \(strData)")
      var err: NSError?
      var json = NSJSONSerialization.JSONObjectWithData(data, options: .MutableLeaves, error: &err) as? NSDictionary

      // Did the JSONObjectWithData constructor return an error? If so, log the error to the console
      if(err != nil) {
        println(err!.localizedDescription)
        let jsonStr = NSString(data: data, encoding: NSUTF8StringEncoding)
        println("Error could not parse JSON: '\(jsonStr)'")
      }
      else {
        // The JSONObjectWithData constructor didn't return an error. But, we should still
        // check and make sure that json has a value using optional binding.
        if let parseJSON = json {
          // Okay, the parsedJSON is here, let's get the value for 'success' out of it
          var returnValue = parseJSON["return_value"] as? Int
          println("return_value: \(returnValue)")
        }
        else {
          // Woa, okay the json object was nil, something went worng. Maybe the server isn't running?
          let jsonStr = NSString(data: data, encoding: NSUTF8StringEncoding)
          println("Error could not parse JSON: \(jsonStr)")
        }
      }
    })

    task.resume()

  }


  //////////
  ///// Get Device Info With Endpoints
  //////////



  func doDeviceInfo() {

    var request = NSMutableURLRequest(URL: NSURL(string: "https://api.spark.io/v1/devices/YOUR_CORE_DEVICE_ID?access_token=YOUR_ACCESS_TOKEN")!)
    var session = NSURLSession.sharedSession()
    request.HTTPMethod = "GET"

    var requestError: NSError?
    var urlResponse : NSURLResponse?
    var response1 = NSURLConnection.sendSynchronousRequest(request, returningResponse: &urlResponse, error: &requestError)
    var json = NSJSONSerialization.JSONObjectWithData(response1!, options: .MutableLeaves, error: &requestError) as? NSDictionary

    // Did the JSONObjectWithData constructor return an error? If so, log the error to the console
    if(requestError != nil) {
      println(requestError!.localizedDescription)
      let jsonStr = NSString(data: response1!, encoding: NSUTF8StringEncoding)
      println("Error could not parse JSON: '\(jsonStr)'")
    }
    else {
      // The JSONObjectWithData constructor didn't return an error. But, we should still
      // check and make sure that json has a value using optional binding.
      if let parseJSON = json {

        println(parseJSON)

      }
      else {
        // Woa, okay the json object was nil, something went worng. Maybe the server isn't running?
        let jsonStr = NSString(data: response1!, encoding: NSUTF8StringEncoding)
        println("Error could not parse JSON: \(jsonStr)")
      }
    }
    
  }



}

