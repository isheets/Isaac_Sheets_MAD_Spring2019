//
//  PunchDataController.swift
//  lab05
//
//  Created by Isaac Sheets on 3/13/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import Foundation
import FirebaseDatabase
class PunchDataController {
    var ref: DatabaseReference!
    
    var punchData = [Punch]()
    //property with a closure as its value
    //closure takes an array of Recipe as its parameter and Void as its return type
    var onDataUpdate: (() -> Void)?
    
    func setupFirebaseListener(){
        ref = Database.database().reference().child("punches")
        //set up a listener for Firebase data change events
        //this event will fire with the initial data and then all data changes
        ref.observe(DataEventType.value, with: {snapshot in
            self.punchData.removeAll()
            //DataSnapshot represents the Firebase data at a given time
            //loop through all the child data nodes
            for snap in snapshot.children.allObjects as! [DataSnapshot]{
                //print(snap)
                let punchId = snap.key
                if let punchDict = snap.value as? [String: String], //get value as a Dictionary
                    let punchDate = punchDict["date"],
                    let punchHours = punchDict["hours"]
                {
                    let newPunch = Punch(id: punchId, date: punchDate, hours: punchHours)

                    self.punchData.append(newPunch)
                }
            }
            //call onDataUpdate
            self.onDataUpdate?()
        })
    }
    
    func getPunches()->[Punch]{
        return punchData
    }
    
    func deletePunch(punchId: String){
        // Delete the object from Firebase
        ref.child(punchId).removeValue()
    }
    
    func addPunch(date:String, hours:String){
        //create Dictionary
        let newPunchDict = ["date": date, "hours": hours]
        
        print(newPunchDict)
        
        //create a new ID
        let punchref = ref.childByAutoId()
        
        //write data to the new ID in Firebase
        punchref.setValue(newPunchDict)
    }
}
