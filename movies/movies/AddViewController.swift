//
//  AddViewController.swift
//  movies
//
//  Created by Isaac Sheets on 3/14/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class AddViewController: UIViewController {
    
    var addedMovie : Movie?

    @IBOutlet weak var newTitle: UITextField!
    @IBOutlet weak var newUrl: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        print(segue.identifier!)
        if segue.identifier == "save" {
            if ((newTitle.text?.isEmpty) == false && (newUrl.text?.isEmpty) == false){
                addedMovie = Movie(name: newTitle.text!, url: newUrl.text!)
            }
            else {
                print("invalid inputs: all text fields must be filled")
            }
        }
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
