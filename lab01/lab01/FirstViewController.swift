//
//  FirstViewController.swift
//  lab01
//
//  Created by Isaac Sheets on 2/2/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class FirstViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    

    @IBOutlet weak var veggieLabel: UILabel!
    @IBOutlet weak var veggiePicker: UIPickerView!
    
    let categoryComp = 0
    let veggieComp = 1
    
    var veggieCat = VeggiesController()
    var categories = [String]()
    var veggies = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        veggieCat.loadData()
        categories = veggieCat.getCategories()
        veggies = veggieCat.getVeggies(index: 0)
        print(categories)
        print(veggies)
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if component == categoryComp {
            return categories.count
        }
        else {
            return veggies.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if component == categoryComp {
            return categories[row]
        }
        else {
            return veggies[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {

        if component == categoryComp {
            veggies = veggieCat.getVeggies(index: row)
            veggiePicker.reloadComponent(veggieComp)
            veggiePicker.selectRow(0, inComponent: veggieComp, animated: true)
        }
        
        let categoryRow = pickerView.selectedRow(inComponent: categoryComp)
        let veggieRow = pickerView.selectedRow(inComponent: veggieComp)
        veggieLabel.text = "You like \(categories[categoryRow]) (specifically \(veggies[veggieRow]))"
    }


}

