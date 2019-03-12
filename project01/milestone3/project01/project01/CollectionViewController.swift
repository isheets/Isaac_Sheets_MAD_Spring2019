//  CollectionViewController.swift
//  project01
//
//  Created by Isaac Sheets on 2/20/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

private let reuseIdentifier = "Cell"

class CollectionViewController: UICollectionViewController, UICollectionViewDelegateFlowLayout {
    
    
    // MARK: variables
    var sequence = [Int]()
    
    var levelNum : Int = 0
    var sequenceNum : Int = 0
    var numInLevel : Int = 0
    
    //array of all grid buttons
    var buttons : [UIButton] = [UIButton]()
    
    var tapNum = 0
    
    //header and footer references
    var footer: CollectionReusableView?
    var header: CollectionReusableView?
    
    //colors
    var yellow = UIColor(red:0.95, green:0.96, blue:0.82, alpha:1.0)
    var green = UIColor(red:0.49, green:0.69, blue:0.36, alpha:1.0)
    var red = UIColor(red:0.86, green:0.29, blue:0.23, alpha:1.0)
    var gray = UIColor(red:0.47, green:0.50, blue:0.53, alpha:1.0)
    
    @IBOutlet var buttCollection: UICollectionView!
    
    
    // MARK: game interactions
    //progressing through game
    @IBAction func footerButtPress(_ sender: UIButton) {
        if sender.currentTitle == "New Game" {
            newGame()
        }
        else if sender.currentTitle == "Go to Question" {
            winRound()
        }
        else if sender.currentTitle == "Next Level" {
            header?.headerSubTitle.pushTransition()
            header?.headerSubTitle.text = ""
            unfadeCells()
            footer?.footerButton.fadeOut(onCompletion: {
                self.nextLevel()
            })
        }
        else if sender.currentTitle == "Restart Level" {
            numInLevel = 0
            tapNum = 0
            resetBG()
            unfadeCells()
            randomSequence()
            footer?.footerButton.fadeOut(onCompletion: {
                self.header?.headerText.pushTransition()
                self.header?.headerText.text = "Level \(self.levelNum + 1)"
                self.playSequence()
            })
        }
    }
    //replicating sequence (touching buttons in collection)
    @IBAction func touchButt(_ sender: UIButton) {
        
        print(sender.tag)
        
        
        if sender.tag == sequence[tapNum] {
            
            sender.backgroundColor = green
            
            
            if tapNum == numInLevel {
                if numInLevel == sequence.count - 1{
                    
                    
                    header?.headerText.pushTransition()
                    header?.headerText.text = "Checkpoint Reached"
                    header?.headerSubTitle.pushTransition()
                    header?.headerSubTitle.text = "Answer a question to continue."
                    footer?.footerButton.setTitle("Go to Question", for: .normal)
                    flashVictory(onCompletion: {self.fadeCells()})
                    footer?.footerButton.fadeIn()
                    disableButtons()
                    
                }
                else {numInLevel = numInLevel + 1
                    disableButtons()
                    header?.headerText.pushTransition()
                    header?.headerText.text = "Nice!"
                    header?.headerSubTitle.pushTransition()
                    header?.headerSubTitle.text = "Sequence correct"
                    DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(100), execute: {
                        self.flashVictory(onCompletion: self.nextLevel)
                    })
                    
                }
            }
            else {
                tapNum = tapNum + 1
                
            }
            
            
        }
            //wrong button, game over
        else {
            //update header
            self.header?.headerText.pushTransition()
            self.header?.headerText.text = "Oops!"
            self.header?.headerSubTitle.pushTransition()
            self.header?.headerSubTitle.text = "Sequence incorrect"
            
            //disable buttons and fade
            disableButtons()
            fadeCells()
            
            //highlight incorrect/correct
            sender.backgroundColor = red
            sender.alpha = 1
            let cells = getOrderedCells()
            cells[sequence[tapNum]].gridButt.backgroundColor = green
            cells[sequence[tapNum]].gridButt.alpha = 1
           
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(500), execute: {
                //update footer button to enable new game
                self.footer?.footerButton.backgroundColor = self.green
                self.footer?.footerButton.setTitle("Restart Level", for: .normal)
                self.footer?.footerButton.fadeIn()
            })
            
        }
    }
    
    //MARK: helper functions for game progression
    
    //start a new game
    func newGame() {
        sequence = []
        newRow()
        numInLevel = 0
        header?.headerSubTitle.pushTransition()
        header?.headerSubTitle.text = ""
        randomSequence()
        unfadeCells()
        resetBG()
        levelNum = 0
        header?.headerText.pushTransition()
        header?.headerText.text = "Level " + String(levelNum + 1)
        footer?.footerButton.fadeOut(onCompletion: {
            self.playSequence()
        })
        
    }
    
    //start next level after returning from question correctly
    func nextLevel() {
        //transition header text
        header?.headerText.pushTransition()
        header?.headerSubTitle.pushTransition()
        header?.headerSubTitle.text = ""
        header?.headerText.pushTransition()
        header?.headerText.text = "Level \(levelNum + 1)"
        //play new sequence
        resetBG()
        playSequence()
    }
    
    //level complete, transition to question
    func winRound() {
        tapNum = 0
        resetBG()
        self.performSegue(withIdentifier: "showQuestion", sender: nil)
    }
    
    //win game after completing level 4
    func winGame() {
        print("you won!")
        flashVictory()
        header?.headerText.pushTransition()
        header?.headerText.text = "You Won!"
        header?.headerSubTitle.pushTransition()
        header?.headerSubTitle.text = "Wow! Incredible!"
        footer?.footerButton.setTitle("New Game", for: .normal)
        
    }
    
    
    // MARK: messing with cells/buttons
    func playSequence() {
        //disable buttons during sequence
        let cells = getCells()
        for i in 0..<cells.count {
            cells[i].gridButt.isEnabled = false
        }
        
        //set and change the background according to sequence
        for i in 0...numInLevel {
            let index = IndexPath(row: sequence[i], section: 0)
            let curCell = buttCollection.cellForItem(at: index) as! CollectionViewCell
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(300 + (i*400)), execute: {
                curCell.gridButt.pushTransition(0.1)
                curCell.gridButt.backgroundColor = self.green
            })
            
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(700 + (i*400)), execute: {
                curCell.gridButt.pushTransition(0.1)
                curCell.gridButt.backgroundColor = self.yellow
                //reenable buttons
                if i == self.numInLevel {
                    for i in 0..<cells.count {
                        
                        cells[i].gridButt.isEnabled = true
                        self.header?.headerSubTitle.pushTransition()
                        self.header?.headerSubTitle.text = "Replicate the sequence"
                    }
                }
            })
        }
        //reset taps
        tapNum = 0
    }
    
    //fade all cells to full opacity
    func unfadeCells() {
        let cells = getOrderedCells()
        for i in 0..<cells.count {
            cells[i].gridButt.pushTransition()
            cells[i].gridButt.alpha = 1
        }
    }
    //fade all cells to 50% opacity
    func fadeCells() {
        let cells = getOrderedCells()
        for i in 0..<cells.count {
            cells[i].gridButt.pushTransition()
            cells[i].gridButt.alpha = 0.5
        }
    }
    
    //disable button interaction
    func disableButtons() {
        let cells = getCells()
        for i in 0..<cells.count {
            cells[i].gridButt.isEnabled = false
        }
    }
    
    //if they get the sequence wrong, then flash red
    func flashLose() {
        let cells = getCells()
        for i in 0..<cells.count {
            
            cells[i].gridButt.pushTransition(0.2)
            cells[i].gridButt.backgroundColor = red
        }
        
        
    }
    
    //play victory sequence
    func flashVictory(onCompletion: (() -> Void)? = nil) {
        let cells = getOrderedCells()
        for i in 0..<cells.count {
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(i*40), execute: {
                cells[i].gridButt.pushTransition(0.1)
                cells[i].gridButt.backgroundColor = self.green
                //call completion function if it exists and we're at the end of array
                if(i == cells.count - 1) {
                    if let complete = onCompletion {
                        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(500), execute: {
                            //complete() will likely be fadeCells()
                            complete()
                        })
                        
                    }
                }
            })
        }
    }
    
    //reset bg color for all cells to default yellow
    func resetBG() {
        let cells = getCells()
        for i in 0..<cells.count {
            cells[i].gridButt.pushTransition(0.2)
            cells[i].gridButt.backgroundColor = yellow
        }
    }
    
    // MARK: get cell instances and return array
    
    //ordered by sequence[]
    func getCells() -> [CollectionViewCell] {
        var allCells : [CollectionViewCell] = [CollectionViewCell]()
        for i in 0..<sequence.count {
            let index = IndexPath(row: sequence[i], section: 0)
            let curCell = buttCollection.cellForItem(at: index) as! CollectionViewCell
            allCells.append(curCell)
        }
        return allCells
    }
    
    //ordered numerically
    func getOrderedCells() -> [CollectionViewCell] {
        var allCells : [CollectionViewCell] = [CollectionViewCell]()
        for i in 0..<sequence.count {
            let index = IndexPath(row: i, section: 0)
            let curCell = buttCollection.cellForItem(at: index) as! CollectionViewCell
            allCells.append(curCell)
        }
        return allCells
    }
    
    //MARK: sequence array stuff
    
    //randomize sequence[]
    func randomSequence() {
        sequence.shuffle()
        print(sequence)
    }
    
    //add a new row to collectionview
    func newRow() {
        for i in sequence.count...sequence.count+3 {
            sequence.append(i)
        }
        randomSequence()
        let section = IndexSet(integer: 0)
        buttCollection.reloadSections(section)
    }
    
    // MARK: return from question
    
    @IBAction func unwindToCollection(segue:UIStoryboardSegue) {
        if let sender = segue.source as? TriviaViewController {
            
            //question answered correctly
            if sender.correct == true {
                levelNum = levelNum + 1
                if levelNum == 4{
                    winGame()
                }
                else {
                    numInLevel = 0
                    sequenceNum = sequenceNum + 4
                    newRow()
                    resetBG()
                    //disableButtons()
                    header?.headerText.pushTransition()
                    header?.headerText.text = "Nice!"
                    header?.headerSubTitle.pushTransition()
                    header?.headerSubTitle.text = ""
                    footer?.footerButton.setTitle("Next Level", for: .normal)
                }
            }
                
                //question answered incorrectly
            else {
                disableButtons()
                flashLose()
                header?.headerText.pushTransition()
                header?.headerText.text = "Oops!"
                header?.headerSubTitle.pushTransition()
                header?.headerSubTitle.text = "Better luck next time"
                footer?.footerButton.setTitle("Restart Level", for: .normal)
            }
        }
    }
    
    // MARK: button styling helpers
    //shadows/corners
    func roundCorners(button: UIButton) {
        button.layer.masksToBounds = false
        button.layer.cornerRadius = 5.0
    }
    //shadows/corners
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
    
    let userData = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("view loaded")
        

    }

    
    //status bar color
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    //restrict to portrait
    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        get {
            return .portrait
            
        }
    }
    
    
    
    // MARK: UICollectionViewDataSource
    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return sequence.count
    }
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as! CollectionViewCell
        
        // Configure the cell
        cell.gridButt.tag = indexPath.row
        roundCorners(button: cell.gridButt)
        //fade it out little
        cell.gridButt.alpha = 0.5
        //disabled by default
        cell.gridButt.isEnabled = false
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        //size cells based on screen size
        let screenW = UIScreen.main.bounds.width
        return CGSize(width: (screenW/4) - 20, height: (screenW / 4) - 20)
    }
    
    //UICollectionViewDelegateFlowLayout method
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        //define margins for spacing
        let sectionInsets = UIEdgeInsets(top: 20.0, left: 20.0, bottom: 20.0, right: 20.0)
        return sectionInsets
    }
    
    override func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        
        //intialize footer
        if kind == UICollectionView.elementKindSectionFooter {
            footer = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: "Footer", for: indexPath) as? CollectionReusableView
            footer?.footerButton.setTitle("New Game", for: .normal)
            footer?.footerButton.setTitleColor(UIColor.white, for: .normal)
            footer?.footerButton.backgroundColor = green
            //add shadow
            addShadow(button: (footer?.footerButton)!)
            //add outline
            footer?.footerButton.layer.borderWidth = 0.5
            footer?.footerButton.layer.borderColor = UIColor.white.cgColor
            
            
            //return rendered footer
            return footer!
        }
            
            //intialize header
        else {
            header = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: "Header", for: indexPath) as? CollectionReusableView
            header?.headerText.text = "Welcome to Trivia Tangler!"
            header?.headerTitle.title = "Trivia Tangler"
            header?.headerSubTitle.text = "Press new game to get started."
            return header!
        }
        
        
    }
    
    
}

//customize UIView for smooooooth transitions
extension UIView {
    
    func fadeIn(_ duration: TimeInterval? = 0.4, onCompletion: (() -> Void)? = nil) {
        self.alpha = 0
        self.isHidden = false
        UIView.animate(withDuration: duration!,
                       animations: { self.alpha = 1 },
                       completion: { (value: Bool) in
                        if let complete = onCompletion { complete() }
        }
        )
    }
    
    func fadeOut(_ duration: TimeInterval? = 0.4, onCompletion: (() -> Void)? = nil) {
        UIView.animate(withDuration: duration!,
                       animations: { self.alpha = 0 },
                       completion: { (value: Bool) in
                        self.isHidden = true
                        if let complete = onCompletion { complete() }
        }
        )
        
    }
    func pushTransition(_ duration: CFTimeInterval? = 0.3) {
        let animation:CATransition = CATransition()
        animation.timingFunction = CAMediaTimingFunction(name:
            CAMediaTimingFunctionName.easeInEaseOut)
        animation.type = CATransitionType.fade
        animation.duration = duration!
        layer.add(animation, forKey: CATransitionType.push.rawValue)
    }
    
}
