//
//  VeggieCategories.swift
//  lab02
//
//  Created by Isaac Sheets on 2/16/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import Foundation

struct VeggiesDataModel : Codable {
    var category : String
    var veggies : [String]
}

class CategoriesDataModelController {
    var allData = [VeggiesDataModel]()
    let fileName = "VeggieCategories"
    
    func loadData(){
        if let pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                allData = try plistdecoder.decode([VeggiesDataModel].self, from: data)
            } catch {
                // handle error
                print(error)
            }
        }
    }
    
    func getCategories() -> [String]{
        var categories = [String]()
        for item in allData{
            categories.append(item.category)
        }
        return categories
    }
    
    func getVeggies(index:Int) -> [String] {
        return allData[index].veggies
        
    }
    
    func addVeggie(index:Int, newVeggie:String, newIndex: Int){
        allData[index].veggies.insert(newVeggie, at: newIndex)
    }
    
    func deleteVeggie(index:Int, veggieIndex: Int){
        allData[index].veggies.remove(at: veggieIndex)
    }
}
