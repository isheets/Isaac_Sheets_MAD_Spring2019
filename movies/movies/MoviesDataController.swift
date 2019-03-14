//
//  MoviesDataController.swift
//  movies
//
//  Created by Isaac Sheets on 3/14/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import Foundation

struct Movie : Codable {
    var name : String
    var url : String
    
    init(name: String, url: String) {
        self.name = name
        self.url = url
    }
}

class MoviesDataModelController {
    var allData = [Movie]()
    let fileName = "movies"
    let datafilename = "data.plist"
    
    func getDataFile(datafile: String) -> URL {
        //get path for data file
        let dirPath = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let docDir = dirPath[0] //documents directory
        print(docDir)
        
        // URL for our plist
        return docDir.appendingPathComponent(datafile)
    }
    
    func loadData(){
        let pathURL:URL?
        
        // URL for our plist
        let dataFileURL = getDataFile(datafile: datafilename)
        print(dataFileURL)
        
        //if the data file exists, use it
        if FileManager.default.fileExists(atPath: dataFileURL.path){
            pathURL = dataFileURL
        }
        else {
            // URL for our plist
            pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist")
        }
        
        //creates a property list decoder object
        let plistdecoder = PropertyListDecoder()
        do {
            let data = try Data(contentsOf: pathURL!)
            //decodes the property list
            allData = try plistdecoder.decode([Movie].self, from: data)
        } catch {
            // handle error
            print(error)
        }
    }
    
    func getMovies() -> [Movie]{
        var movies = [Movie]()
        for item in allData{
            movies.append(item)
        }
        return movies
    }
    
   
    
    func addMovie(newMovie:Movie){
        allData.append(newMovie)
    }

    func deleteMovie(index:Int){
        allData.remove(at: index)
    }
    
    func writeData(){
        // URL for our plist
        let dataFileURL = getDataFile(datafile: datafilename)
        print(dataFileURL)
        //creates a property list decoder object
        let plistencoder = PropertyListEncoder()
        plistencoder.outputFormat = .xml
        do {
            let data = try plistencoder.encode(allData.self)
            try data.write(to: dataFileURL)
        } catch {
            // handle error
            print(error)
        }
    }
}
