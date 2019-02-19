//
//  AddVeggieViewController.swift
//  lab02
//
//  Created by Isaac Sheets on 2/16/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class AddVeggieViewController: UIViewController {

    @IBOutlet weak var veggieTextField: UITextField!
    
    var addedVeggie = String()
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "done"{

            if ((veggieTextField.text?.isEmpty) == false){
                addedVeggie=veggieTextField.text!
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
