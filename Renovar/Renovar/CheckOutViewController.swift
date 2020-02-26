//
//  CheckOutViewController.swift
//  Renovar
//
//  Created by Macbook on 24/05/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import MessageUI
import SwiftyJSON
import SQLite3

class CheckOutViewController: UIViewController,MFMailComposeViewControllerDelegate, UIPickerViewDelegate, UIPickerViewDataSource {

    var total_price : String = ""
    var items = [Orders]();
    
    @IBOutlet weak var lblTotalPrice: UILabel!
    let thePicker = UIPickerView()
    @IBOutlet weak var txtCountry: UITextField!
    
    var appBarViewController = MDCAppBarViewController()
    
    
    var countries: [String] = []
    
    @IBOutlet weak var txtEmailAddress: UITextField!
    @IBOutlet weak var txtGivenName: UITextField!
    @IBOutlet weak var txtFamilyName: UITextField!
    @IBOutlet weak var txtAddress1: UITextField!
    @IBOutlet weak var txtAddress2: UITextField!
    @IBOutlet weak var txtCity: UITextField!
    @IBOutlet weak var txtState: UITextField!
    @IBOutlet weak var txtZipCode: UITextField!
    
    var db : OpaquePointer?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
        self.title = "Check Out"
        
        let menuItemImage = UIImage(named: "Back")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem

        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
        
        
        lblTotalPrice.text = total_price
        
        

        txtCountry.inputView = thePicker
        
        thePicker.delegate = self
        
        for code in NSLocale.isoCountryCodes as [String] {
            let id = NSLocale.localeIdentifier(fromComponents: [NSLocale.Key.countryCode.rawValue: code])
            let name = NSLocale(localeIdentifier: "en_US").displayName(forKey: NSLocale.Key.identifier, value: id) ?? "Country not found for code: \(code)"
            countries.append(name)
        }
        
        
        let fileUrl = try!
            FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false).appendingPathComponent("Renovar.db")
        
        if sqlite3_open(fileUrl.path, &db) != SQLITE_OK{
            print("Error opening database")
            return
        }
        
        let createTableQuery = "CREATE TABLE IF NOT EXISTS Orders(id INTEGER PRIMARY KEY AUTOINCREMENT,item_name TEXT,quantity INTEGER,total_price REAL,image_url TEXT)"
        
        if sqlite3_exec(db, createTableQuery, nil, nil, nil) != SQLITE_OK{
            print("Error creating table")
            return
        }
        
        print("Success Creating Database")
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }

    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView( _ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return countries.count
    }
    
    func pickerView( _ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return countries[row]
    }
    
    func pickerView( _ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        txtCountry.text = countries[row]
    }
    
    
    @IBAction func checkOut(_ sender: Any) {
        if(txtCountry.text != "" && txtCity.text != "" && txtZipCode.text != "" && txtState.text != "" && txtAddress1.text != "" && txtFamilyName.text != "" && txtGivenName.text != "" && txtEmailAddress.text != "" && txtAddress2.text != "")
        {
            sendData()
        }
        else
        {
            let alert = UIAlertController(title: "Fill Empty Fields", message: "Please fill up all fields", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    


    func sendData() {
        
        let url = NSURL(string: "http://renovar.health/renovarmobile/transaction.php")
        
        var request = URLRequest(url: url! as URL)
        request.httpMethod = "POST"
        
        var dataString = ""
        
        dataString = dataString + "&email=\(txtEmailAddress.text!)"
        dataString = dataString + "&firstname=\(txtGivenName.text!)"
        dataString = dataString + "&lastname=\(txtFamilyName.text!)"
        dataString = dataString + "&street1=\(txtAddress1.text!)"
        dataString = dataString + "&street2=\(txtAddress2.text!)"
        dataString = dataString + "&country=\(txtCountry.text!)"
        dataString = dataString + "&zip=\(txtZipCode.text!)"
        dataString = dataString + "&province=\(txtState.text!)"
        dataString = dataString + "&city=\(txtCity.text!)"
        
        do {
            let jsonEncoder = JSONEncoder()
            let jsonData = try jsonEncoder.encode(self.items)
            let json = String(data: jsonData, encoding: String.Encoding.utf8)
            
            
            dataString = dataString + "&orders=\(json ?? "")"
            print(json ?? "")
        } catch {
            print("ERROR OCCURED")
        }

        
        let dataD = dataString.data(using: .utf8)
        
        do
        {
            let uploadJob = URLSession.shared.uploadTask(with: request, from: dataD)
            {
                data, response, error in
                
                if error != nil {
                    DispatchQueue.main.async
                        {
                            let alert = UIAlertController(title: "Upload Didn't Work?", message: "Looks like the connection to the server didn't work.  Do you have Internet access?", preferredStyle: .alert)
                            alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                            self.present(alert, animated: true, completion: nil)
                    }
                }
                else
                {
                    if let unwrappedData = data {
                        
                        let returnedData = NSString(data: unwrappedData, encoding: String.Encoding.utf8.rawValue)
                        
                        
                        if returnedData != "failed"
                        {
                            DispatchQueue.main.async
                                {
                                    let alert = UIAlertController(title: "Check Out", message: "\(returnedData ?? "")", preferredStyle: .alert)
                                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: { action in
                                        
                                            self.dismiss(animated: false, completion: nil)
                                        
                                        }))
                                    
                                    var stmnt : OpaquePointer?
                                    
                                    let insertQuery = "DELETE FROM Orders"
                                    _ = unsafeBitCast(OpaquePointer(bitPattern: -1), to: sqlite3_destructor_type.self)
                                    
                                    if(sqlite3_prepare(self.db, insertQuery, -1, &stmnt, nil)) != SQLITE_OK{
                                        print("Error binding query")
                                    }
                                    
                                    
                                    if sqlite3_step(stmnt) == SQLITE_DONE
                                    {
                                        print("Record Deleted")
                                        
                                        if let presenter = self.presentingViewController as? ShoppingCartViewController {
                                            presenter.loadData()
                                            presenter.itemsCollectionView.reloadData()
                                        }
                                        self.present(alert, animated: true, completion: nil)
                                    }
                            }
                        }
                        else
                        {
                            DispatchQueue.main.async
                                {
                                    
                                    let alert = UIAlertController(title: "Check Out", message: "\(returnedData ?? "")", preferredStyle: .alert)
                                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                                    self.present(alert, animated: true, completion: nil)
                            }
                        }
                    }
                }
            }
            uploadJob.resume()
        }
    }
}
