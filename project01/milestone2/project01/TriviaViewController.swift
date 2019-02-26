//
//  TriviaViewController.swift
//  project01
//
//  Created by Isaac Sheets on 2/23/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class TriviaViewController: UIViewController {

    @IBOutlet weak var questionLabel: UILabel!
    @IBOutlet weak var option1: UIButton!
    @IBOutlet weak var option2: UIButton!
    @IBOutlet weak var option3: UIButton!
    @IBOutlet weak var option4: UIButton!
    @IBOutlet weak var nextButton: UIBarButtonItem!
    @IBOutlet weak var toolbar: UIToolbar!
    
    @IBAction func selOption1(_ sender: UIButton) {
    }
    @IBAction func selOption2(_ sender: UIButton) {
    }
    @IBAction func selOption3(_ sender: UIButton) {
    }
    @IBAction func selOption4(_ sender: UIButton) {
        self.toolbar.isHidden = false
    }
    @IBAction func next(_ sender: UIBarButtonItem) {
        print("button clicked")
        self.performSegue(withIdentifier: "questionDone", sender: nil)
        
    }
        
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "questionDone" {
            if let nextViewController = segue.destination as? CollectionViewController {
                print(String(nextViewController.levelNum) + " this is the levelNum before we segue")
                nextViewController.levelNum = nextViewController.levelNum + 1
                

            }
        }
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.toolbar.isHidden = true

        // Do any additional setup after loading the view.
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
