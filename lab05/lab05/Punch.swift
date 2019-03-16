//
//  Punch.swift
//  lab05
//
//  Created by Isaac Sheets on 3/13/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import Foundation

struct Punch {
    var id : String
    var date : String
    var hours : String
    
    init(id: String, date: String, hours: String) {
        self.id = id
        self.date = date
        self.hours = hours
    }
}
