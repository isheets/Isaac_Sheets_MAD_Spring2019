//
//  ViewController.swift
//  lab02
//
//  Created by Isaac Sheets on 2/16/19.
//  Copyright © 2019 Isaac Sheets. All rights reserved.
//

import UIKit

class ViewController: UITableViewController {

    var categoryList = [String]()
    var categoriesData = CategoriesDataModelController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        categoriesData.loadData()
        categoryList=categoriesData.getCategories()
        
        navigationController?.navigationBar.prefersLargeTitles = true
        //remove empty cells by setting 0 height footer
        tableView.tableFooterView = UIView()
        
        
    }
    
    //Required methods for UITableViewDataSource
    //Number of rows in the section
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return categoryList.count
    }
    
    // Displays table view cells
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //configure the cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "CategoryIdentifier", for: indexPath)
        cell.textLabel?.text = categoryList[indexPath.row]
        return cell
    }
    
    //Handles segues to other view controllers
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {

            let detailVC = segue.destination as! DetailView
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            //sets the data for the destination controller
            detailVC.title = categoryList[indexPath.row]
            detailVC.categoriesData = categoriesData
            detailVC.selectedCategory = indexPath.row
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

