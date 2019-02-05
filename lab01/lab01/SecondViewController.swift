//
//  SecondViewController.swift
//  lab01
//
//  Created by Isaac Sheets on 2/2/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class SecondViewController: UIViewController {

    @IBAction func rideButt(_ sender: Any) {
        //check to see if there's an app installed to handle this URL scheme
        if(UIApplication.shared.canOpenURL(URL(string: "lyft://")!)){
            //open the app with this URL scheme
            UIApplication.shared.open(URL(string: "lyft://")!, options: [:], completionHandler: nil)
        }else {
            if(UIApplication.shared.canOpenURL(URL(string: "uber://")!)){
                UIApplication.shared.open(URL(string: "uber://")!, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.open(URL(string: "https://itunes.apple.com/us/app/lyft-on-demand-ridesharing/id529379082?mt=8")!, options: [:], completionHandler: nil)
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }


}

