//
//  ViewController.swift
//  lab04
//
//  Created by Isaac Sheets on 3/3/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var questionLabel: UILabel!
    @IBOutlet weak var option1: UIButton!
    @IBOutlet weak var option2: UIButton!
    @IBOutlet weak var option3: UIButton!
    @IBOutlet weak var option4: UIButton!
    
    @IBOutlet weak var feedbackLabel: UILabel!
    
    var correctAnswer = "test"
    var questionData = QuestionDataController()
    var allAnswers : [String] = []

    
    @IBAction func newQuestion(_ sender: Any) {
        feedbackLabel.isHidden = true
        questionData.loadjson()
        questionData.onDataUpdate = {[weak self] (data:[Question]) in self?.render()}
        option1.isSelected = false
        option2.isSelected = false
        option3.isSelected = false
        option4.isSelected = false
        option1.isEnabled = true
        option2.isEnabled = true
        option3.isEnabled = true
        option4.isEnabled = true
    }
    
    //check if sender
    @IBAction func pickAnswer(_ sender: UIButton) {
        if sender.titleLabel?.text == correctAnswer {
            print("correct")
            feedbackLabel.textColor = UIColor.green
            feedbackLabel.text = "Correct!"
            feedbackLabel.isHidden = false
            sender.isSelected = true
            option1.isEnabled = false
            option2.isEnabled = false
            option3.isEnabled = false
            option4.isEnabled = false
        }
        else {
            feedbackLabel.textColor = UIColor.red
            feedbackLabel.text = "Wrong!"
            feedbackLabel.isHidden = false
            sender.isSelected = true
            option1.isEnabled = false
            option2.isEnabled = false
            option3.isEnabled = false
            option4.isEnabled = false
        }
    }
    
    
    
    func render() {
        var questionJson = questionData.getQuestion()
        
        //decode strings
        questionJson.correct_answer = questionJson.correct_answer.removingPercentEncoding!
        for i in 0..<questionJson.incorrect_answers.count {
            questionJson.incorrect_answers[i] = questionJson.incorrect_answers[i].removingPercentEncoding!
        }
        questionJson.question = questionJson.question.removingPercentEncoding!
        
        
        print(questionJson)
        questionLabel.text = questionJson.question
        
        allAnswers = questionJson.incorrect_answers
        allAnswers.append(questionJson.correct_answer)
        allAnswers.shuffle()
        
        correctAnswer = questionJson.correct_answer
        
        
        option1.setTitle(allAnswers[0], for: .normal)
        option2.setTitle(allAnswers[1], for: .normal)
        option3.setTitle(allAnswers[2], for: .normal)
        option4.setTitle(allAnswers[3], for: .normal)
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        feedbackLabel.isHidden = true
        questionData.onDataUpdate = {[weak self] (data:[Question]) in self?.render()}
        questionData.loadjson()
    }


}

