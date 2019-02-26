//
//  CollectionViewController.swift
//  project01
//
//  Created by Isaac Sheets on 2/20/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

private let reuseIdentifier = "Cell"




class CollectionViewController: UICollectionViewController, UICollectionViewDelegateFlowLayout {
    
    var sequence = [0,1,2,3,4,5,6,7,8,9,10,11]
    
    var levelNum : Int = 0
    
    var buttons : [UIButton] = [UIButton]()
    
    var tapNum = 0

    
    @IBOutlet var buttCollection: UICollectionView!
    
    @IBAction func touchButt(_ sender: UIButton) {
        
        print(sender.tag)
        
        
        if sender.tag == sequence[tapNum] {
            if levelNum == 11 {
                winGame()
            }
            else {
                sender.backgroundColor = UIColor.green
                tapNum = tapNum + 1
                if tapNum == levelNum + 1 {
                    //func winRound() for this?
                   winRound()
                }
            }
            
        }
        else {
            print("wrong!")
            loseRound()
        }
    }
    
    func winRound() {
        tapNum = 0
        resetBG()
        print(String(levelNum) + " when we segue")
        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(1000), execute: {
            
            self.performSegue(withIdentifier: "showQuestion", sender: nil)
        })
        
    }
    
    //if they get the sequence wrong, then flash red
    func flashRed() {
        
    }
    func flashVictory() {
        
    }
    
    func loseRound() {
        print("you lose")
        //flash red or something
        
        //reset levelNum
        levelNum = 0
        
        //reset background
        resetBG()
        
        //get new sequence and start the next game or give option
        randomSequence()
        playSequence()
        
    }
    
    func winGame() {

        print("you won!")
        resetBG()
        
    }
    
    func getCells() -> [CollectionViewCell] {
        var allCells : [CollectionViewCell] = [CollectionViewCell]()
        for i in 0...11 {
            let index = IndexPath(row: sequence[i], section: 0)
            let curCell = buttCollection.cellForItem(at: index) as! CollectionViewCell
            allCells.append(curCell)
        }
        return allCells
    }
    
    func playSequence() {
        //how will we set and change the background
        for i in 0...levelNum {
            let index = IndexPath(row: sequence[i], section: 0)
            let curCell = buttCollection.cellForItem(at: index) as! CollectionViewCell
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(i*500), execute: {
                curCell.gridButt.backgroundColor = UIColor.green
            })
            
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(500 + (i*500)), execute: {
                curCell.gridButt.backgroundColor = UIColor.black
            })
        }
        //reset taps
        tapNum = 0
    }
    
    func resetBG() {
        let cells = getCells()
        for i in 0..<cells.count {
            cells[i].gridButt.backgroundColor = UIColor.black
        }
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        print(String(levelNum) + " levelNum in viewDidAppear")
        resetBG()
        playSequence()
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        print("view loaded")
        randomSequence()
        levelNum = 0
        
    
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Register cell classes
       // self.collectionView!.register(UICollectionViewCell.self, forCellWithReuseIdentifier: reuseIdentifier)

        // Do any additional setup after loading the view.
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
    }
    */

    // MARK: UICollectionViewDataSource
    
    func randomSequence() {
        sequence.shuffle()
        print(sequence)
    }

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        // #warning Incomplete implementation, return the number of sections

        return 1
    }


    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of items

        return 12
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as! CollectionViewCell
        
        // Configure the cell
        cell.gridButt.tag = indexPath.row

        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let screenW = UIScreen.main.bounds.width
        //let screenH = UIScreen.main.bounds.height
        
        return CGSize(width: (screenW / 3) - 30, height: ((screenW / 3) - 30))
    }
    
    //UICollectionViewDelegateFlowLayout method
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        //define margins for spacing
        let sectionInsets = UIEdgeInsets(top: 50.0, left: 20.0, bottom: 25.0, right: 20.0)
        return sectionInsets
    }
    


}
