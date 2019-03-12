//
//  QuestionDataController.swift
//  project01
//
//  Created by Isaac Sheets on 3/6/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import Foundation
class QuestionDataController {
    var questionData = QuestionData()
    var onDataUpdate: (() -> Void)?
    
    let categories = [9,10,11,12,17,22,23,27]
    let difficulties = ["easy","medium"]
    
    func loadjson(){
        
        let category = String(categories.randomElement()!)
        let difficulty = difficulties.randomElement()!
        
        let urlPath = "https://opentdb.com/api.php?amount=1&type=multiple&encode=url3986&category=\(category)&difficulty=\(difficulty)"
        
        guard let url = URL(string: urlPath)
            else {
                print("url error")
                return
        }
        
        let session = URLSession.shared.dataTask(with: url, completionHandler: {(data, response, error) in
            let httpResponse = response as! HTTPURLResponse
            let statusCode = httpResponse.statusCode
            guard statusCode == 200
                else {
                    print("file download error")
                    return
            }
            //download successful
            print("download complete")
            //parse json asynchronously
            DispatchQueue.main.async {self.parsejson(data!)}
        })
        //must call resume to run session
        session.resume()
    }
    
    func parsejson(_ data: Data){
        do
        {
            let api = try JSONDecoder().decode(QuestionData.self, from: data)
            
            questionData.results = api.results
            
        }
        catch let jsonErr
        {
            print("json error")
            print(jsonErr)
            return
        }
        print("parsejson done")
        //call onDataUpdate closure
        onDataUpdate?()
        
    }
    
    func getQuestion()->Question{
        return questionData.results[0]
    }
    
}
