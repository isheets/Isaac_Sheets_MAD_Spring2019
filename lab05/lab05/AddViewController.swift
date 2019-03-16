//
//  AddViewController.swift
//  lab05
//
//  Created by Isaac Sheets on 3/13/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class AddViewController: UIViewController {
    
    @IBOutlet weak var timeInPicker: UIDatePicker!
    @IBOutlet weak var timeOutPicker: UIDatePicker!
    
    var newHours = String()
    var newDate = String()
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "save" {
            let timeIn = timeInPicker.date
            let timeOut = timeOutPicker.date
            print(timeIn)
            if timeIn < timeOut {
                print("valid dates")
                let cal = Calendar.current
                let components = cal.dateComponents([.hour, .minute], from: timeIn, to: timeOut)
                newHours = String(components.hour!) + "h " + String(components.minute!) + "m"
                let formatter = DateFormatter()
                // initially set the format based on your datepicker date / server String
                formatter.dateFormat = "MMMM dd"
                let weekDay = formatter.weekdaySymbols[cal.component(.weekday, from: timeOut) - 1]
                newDate = String(weekDay) + ", " + formatter.string(from: timeOut)
            }
            else {
                print("invalid input: time in must be before time out")
            }
            
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func timeInPickerChanged(_ sender: Any) {
    }
    
    @IBAction func timeOutPickerChanged(_ sender: Any) {
    }
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
