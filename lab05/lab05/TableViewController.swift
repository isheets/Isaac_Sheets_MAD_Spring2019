//
//  TableViewController.swift
//  lab05
//
//  Created by Isaac Sheets on 3/13/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class TableViewController: UITableViewController {

    var punches = [Punch]()
    var punchData = PunchDataController()
    
    func render(){
        punches=punchData.getPunches()
        //reload the table data
        tableView.reloadData()
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        punchData.onDataUpdate = {[weak self] () in self?.render()}
        punchData.setupFirebaseListener()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
    
    @IBAction func unwindSegue(segue: UIStoryboardSegue){
        print("segued back")
        if segue.identifier == "save" {
            let source = segue.source as! AddViewController
            if source.newHours.isEmpty == false && source.newDate.isEmpty == false{
                punchData.addPunch(date: source.newDate, hours: source.newHours)
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
        return punches.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "punchCell", for: indexPath)

        // Configure the cell...
        cell.textLabel?.text = punches[indexPath.row].date
        cell.detailTextLabel?.text = punches[indexPath.row].hours
        return cell
    }
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            let punchId = punches[indexPath.row].id
            punchData.deletePunch(punchId: punchId)
            punches.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
            //        else if editingStyle == .insert {
//            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
//        }
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
