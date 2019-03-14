//
//  MoviesViewController.swift
//  movies
//
//  Created by Isaac Sheets on 3/14/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class MoviesViewController: UITableViewController {
    
    var movieList = [Movie]()
    var movieData = MoviesDataModelController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        movieData.loadData()
        movieList = movieData.getMovies()

        //subscribe to the UIApplicationWillResignActiveNotification notification
        NotificationCenter.default.addObserver(self, selector: #selector(applicationWillResignActive(_:)), name: UIApplication.willResignActiveNotification, object: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showDetail" {
            let detailVC = segue.destination as! WebViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            let movie = movieList[indexPath.row]
            //sets the data for the destination controller
            detailVC.title = movie.name
            detailVC.webpage = movie.url
        }
    }
    
    @IBAction func unwindSegue(_ segue:UIStoryboardSegue){
        if segue.identifier=="save"{
            let source = segue.source as! AddViewController
            //only add a country if there is text in the textfield
            if let newMovie = source.addedMovie {
                //add country to our data model instance
                print(newMovie)
                movieData.addMovie(newMovie: newMovie)
                //add country to the array
                movieList.append(newMovie)
                tableView.reloadData()
            }
        }
    }
    

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return movieList.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "movieCell", for: indexPath)

        // Configure the cell...
        
        cell.textLabel!.text = movieList[indexPath.row].name
        return cell
    }
    

    
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            //Delete the movie from the array
            movieList.remove(at: indexPath.row)
            //Delete from the data model instance
            movieData.deleteMovie(index: indexPath.row)
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } //else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        //}    
    }
    
    @objc func applicationWillResignActive(_ notification: NSNotification){
        print("resign active called")
        movieData.writeData()
    }


    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
