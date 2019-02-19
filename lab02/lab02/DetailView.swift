//
//  DetailView.swift
//  lab02
//
//  Created by Isaac Sheets on 2/16/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class DetailView: UITableViewController {

    var categoriesData = CategoriesDataModelController()
    var selectedCategory = 0
    var veggieList = [String]()
    var searchController = UISearchController()
    let resultsController = SearchResultsController()
    
    override func viewWillAppear(_ animated: Bool) {
        veggieList = categoriesData.getVeggies(index: selectedCategory)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView()
        veggieList = categoriesData.getVeggies(index: selectedCategory)
        //search results
        
        resultsController.veggieList = veggieList
        searchController = UISearchController(searchResultsController: resultsController)
        
        //search bar configuration
        searchController.searchBar.placeholder = "Enter a search term"
        //searchController.searchBar.sizeToFit() //sets appropriate size for the search bar
        
        // For iOS 11 and later, place the search bar in the navigation bar.
        //navigationItem.searchController = searchController
        
        // Make the search bar always visible.
        //navigationItem.hidesSearchBarWhenScrolling = false
        
        //iOS 10 and earlier
        tableView.tableHeaderView=searchController.searchBar //install the search bar as the table header
        searchController.searchResultsUpdater = resultsController //sets the instance to update search results

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return veggieList.count
    }
    
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "VeggieIdentifier", for: indexPath)
        cell.textLabel?.text = veggieList[indexPath.row]
        return cell
    }
    
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            //Delete the country from the array
            veggieList.remove(at: indexPath.row)
            //Delete from the data model instance
            categoriesData.deleteVeggie(index: selectedCategory, veggieIndex: indexPath.row)
            // Delete the row from the table
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }
    }
    
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to toIndexPath: IndexPath) {
        let fromRow = fromIndexPath.row //row being moved from
        let toRow = toIndexPath.row //row being moved to
        let moveVeggie = veggieList[fromRow]
        
        //move in array
        veggieList.swapAt(fromRow, toRow)

        //move in data model instance
        categoriesData.deleteVeggie(index: selectedCategory, veggieIndex: fromRow)
        categoriesData.addVeggie(index: selectedCategory, newVeggie: moveVeggie, newIndex: toRow)
    }
    
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    
    @IBAction func unwindSegue(_ segue:UIStoryboardSegue){
        if segue.identifier=="done"{
            let source = segue.source as! AddVeggieViewController
            //only add a country if there is text in the textfield
            if ((source.addedVeggie.isEmpty) == false){
                //add country to our data model instance
                categoriesData.addVeggie(index: selectedCategory, newVeggie: source.addedVeggie, newIndex: veggieList.count)
                //add country to the array
                veggieList.append(source.addedVeggie)
                //update country to searchcontroller
                resultsController.veggieList = veggieList
                tableView.reloadData()
                
            }
        }
    }

}
