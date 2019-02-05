//
//  VeggiesController.swift
//  lab01
//
//  Created by Isaac Sheets on 2/2/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import Foundation


class VeggiesController {
    var allData = [Veggies]()
    let fileName = "veggies"
    
    func loadData(){
        print("we're loading data")
        if let pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                allData = try plistdecoder.decode([Veggies].self, from: data)
            } catch {
                // handle error
                print("ah! error! bad!")
                print(error)
            }
        }
    }
    
    func getCategories() -> [String]{
        var categories = [String]()
        for category in allData{
            categories.append(category.category)
        }
        return categories
    }
    
    func getVeggies(index:Int) -> [String] {
        return allData[index].veggies
    }
}
