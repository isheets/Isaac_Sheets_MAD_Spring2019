//
//  Question.swift
//  project01
//
//  Created by Isaac Sheets on 3/6/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import Foundation

struct Question : Decodable {
    var question : String
    var correct_answer : String
    var incorrect_answers : [String]
}

struct QuestionData: Decodable {
    var results = [Question]()
}
