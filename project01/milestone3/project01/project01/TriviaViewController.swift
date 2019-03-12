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
    @IBOutlet weak var feedbackLabel: UILabel!
    @IBOutlet weak var nextButton: UIButton!
    
    var correct : Bool?
    
    var correctAnswer = "test"
    var questionData = QuestionDataController()
    var allAnswers : [String] = []
    
    var buttonArray : [UIButton] = []
    
    //colors
    var yellow = UIColor(red:0.95, green:0.96, blue:0.82, alpha:1.0)
    var green = UIColor(red:0.49, green:0.69, blue:0.36, alpha:1.0)
    var red = UIColor(red:0.86, green:0.29, blue:0.23, alpha:1.0)
    var gray = UIColor(red:0.47, green:0.50, blue:0.53, alpha:1.0)
    
    
    //check if sender
    @IBAction func pickAnswer(_ sender: UIButton) {
        if sender.titleLabel?.text == correctAnswer {
            print("correct")
            
            correct = true
            
            feedbackLabel.textColor = green
            feedbackLabel.text = "Correct!"
            
            //display feedback
            feedbackLabel.fadeIn()
            
            //display next button
            nextButton.backgroundColor = green
            nextButton.titleLabel?.text = "Next Level"
            nextButton.fadeIn()
        
            //disable all buttons, show selection
            for button in buttonArray {
                button.isEnabled = false
                if button == sender {
                    button.setTitleColor(UIColor.white, for: .disabled)
                    button.backgroundColor = green
                    removeShadow(button: button)
                }
                else {
                    button.pushTransition()
                    button.setTitleColor(UIColor.darkGray, for: .disabled)
                    removeShadow(button: button)
                    button.backgroundColor = gray
                }
            }
        }
            
        //got it wrong
        else {
            correct = false
            
            //show feedback
            feedbackLabel.textColor = red
            feedbackLabel.text = "Wrong!"
            feedbackLabel.fadeIn()
            
            //button to get to return to home screen
            nextButton.backgroundColor = red
            nextButton.setTitleColor(UIColor.white, for: .normal)
            nextButton.titleLabel?.text = "Restart Level"
            nextButton.fadeIn()
            
            
            //disable all buttons
            for button in buttonArray {
                button.isEnabled = false
                if button.titleLabel?.text == correctAnswer {
                    removeShadow(button: button)
                    button.pushTransition()
                    button.setTitleColor(green, for: .disabled)
                    button.backgroundColor = gray
                    button.titleLabel?.font = UIFont.boldSystemFont(ofSize: 16)
                }
                else if button == sender {
                    removeShadow(button: button)
                    button.setTitleColor(UIColor.white, for: .disabled)
                    button.backgroundColor = red
                }
                else {
                    button.pushTransition()
                    button.backgroundColor = gray
                    button.setTitleColor(UIColor.darkGray, for: .disabled)
                    removeShadow(button: button)
                }
            }
        }
    }
    
    
    
    func render() {
        var questionJson = questionData.getQuestion()
        
        //decode strings to get rid of URL encoding
        questionJson.correct_answer = questionJson.correct_answer.removingPercentEncoding!
        for i in 0..<questionJson.incorrect_answers.count {
            questionJson.incorrect_answers[i] = questionJson.incorrect_answers[i].removingPercentEncoding!
        }
        questionJson.question = questionJson.question.removingPercentEncoding!
        
        
        print(questionJson)
        //display question
        questionLabel.text = questionJson.question
        
        //
        allAnswers = questionJson.incorrect_answers
        allAnswers.append(questionJson.correct_answer)
        allAnswers.shuffle()
        
        //remember the correct answer
        correctAnswer = questionJson.correct_answer
        
        //display questions
        option1.setTitle(allAnswers[0], for: .normal)
        option2.setTitle(allAnswers[1], for: .normal)
        option3.setTitle(allAnswers[2], for: .normal)
        option4.setTitle(allAnswers[3], for: .normal)
        for button in buttonArray {
            button.fadeIn()
        }
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        //hide stuff
        feedbackLabel.fadeOut(0)
        nextButton.fadeOut(0)
        nextButton.setTitleColor(UIColor.white, for: .normal)
        addShadow(button: nextButton)
        
        //add outline to button
        nextButton.layer.borderWidth = 0.5
        nextButton.layer.borderColor = UIColor.white.cgColor
        
        //pass render method to onDataUpdate
        questionData.onDataUpdate = {[weak self] () in self?.render()}
        //get the question
        questionData.loadjson()
        
        buttonArray.append(option1)
        buttonArray.append(option2)
        buttonArray.append(option3)
        buttonArray.append(option4)
        
        for button in buttonArray {
            button.fadeOut(0)
            button.backgroundColor = green
            addShadow(button: button)
        }
    }
    
    //status bar
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    //button styles
    func addShadow(button: UIButton) {
        button.layer.shadowColor = gray.cgColor
        button.layer.shadowOffset = CGSize(width: 0.0, height: 0.0)
        button.layer.shadowOpacity = 1.0
        button.layer.shadowRadius = 2.0
        button.layer.masksToBounds = false
        button.layer.cornerRadius = 10.0
    }
    func removeShadow(button: UIButton) {
        button.layer.shadowOpacity = 0
    }

}
