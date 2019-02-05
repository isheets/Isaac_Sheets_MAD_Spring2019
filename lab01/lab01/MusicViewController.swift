//
//  MusicViewController.swift
//  lab01
//
//  Created by Isaac Sheets on 2/2/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
// tutorial used as reference: https://www.ioscreator.com/tutorials/play-music-avaudioplayer-ios-tutorial

import UIKit
import AVFoundation

class MusicViewController: UIViewController {

    var audioPlayer = AVAudioPlayer()
    
    @IBAction func playButt(_ sender: Any) {
         audioPlayer.play()
    }
    @IBAction func pauseButt(_ sender: Any) {
         audioPlayer.pause()
    }
    @IBAction func stopButt(_ sender: Any) {
         audioPlayer.stop()
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        let path = Bundle.main.path(forResource: "It's Raining Men", ofType: "mp3")!
        let url = URL(fileURLWithPath: path)
        
        do {
            audioPlayer =  try AVAudioPlayer(contentsOf: url)
        } catch {
            print("can't load file")
        }
    }
    
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
