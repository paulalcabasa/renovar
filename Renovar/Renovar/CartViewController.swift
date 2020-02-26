//
//  CartViewController.swift
//  Renovar
//
//  Created by Macbook on 23/05/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import SQLite3

class CartViewController: UIViewController {
    
    var item_name : String = ""
    var item_image : String = ""
    var item_price : String = ""
    var total_price : Double = 0
    
    @IBOutlet weak var productImage: UIImageView!
    @IBOutlet weak var productName: UILabel!
    @IBOutlet weak var productPrice: UILabel!
    @IBOutlet weak var quantityStepper: UIStepper!
    @IBOutlet weak var totalPrice: UILabel!
    
    var db : OpaquePointer?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let url = URL(string: item_image)
        productImage.kf.setImage(with: url)
        productName.text = item_name
        productPrice.text = item_price
        
        compute(sender: quantityStepper)
        
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
    
    @IBAction func onConfirmCart(_ sender: Any) {
        
        var stmnt : OpaquePointer?
        
        let insertQuery = "INSERT INTO Orders (item_name,quantity,total_price,image_url) VALUES (?,?,?,?)"
        let SQLITE_TRANSIENT = unsafeBitCast(OpaquePointer(bitPattern: -1), to: sqlite3_destructor_type.self)
        
        if(sqlite3_prepare(self.db, insertQuery, -1, &stmnt, nil)) != SQLITE_OK{
            print("Error binding query")
        }
        
        if(sqlite3_bind_text(stmnt, 1, "\(self.item_name)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
        {
            print("Error binding item_name")
        }
        
        if(sqlite3_bind_text(stmnt, 2, "\(self.quantityStepper?.value ?? 0)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
        {
            print("Error binding quantity")
        }
        
        if(sqlite3_bind_text(stmnt, 3, "\(self.total_price)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
        {
            print("Error binding total_price")
        }
        
        if(sqlite3_bind_text(stmnt, 4, "\(self.item_image)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
        {
            print("Error binding image_url")
        }
        
        if sqlite3_step(stmnt) == SQLITE_DONE
        {
            print("Order Saved")
            
            self.dismiss(animated: true, completion: nil)
            
            if let presenter = presentingViewController as? ItemContentBottomViewController {
                presenter.itemAdded()
            }
        }
    }
    
    @IBAction func onValueChange(_ sender: UIStepper) {
        compute(sender: sender)
    }
    
    func compute(sender : UIStepper)
    {
        var price : Double = Double(item_price.replacingOccurrences(of: "$", with: "", options: NSString.CompareOptions.literal, range: nil)) as! Double
        
        let total : Double = price * sender.value
        
        total_price = total
        
        totalPrice.text = ("$\(total) / \(Int(sender.value)) pc/s")
    }
    
    @IBAction func cancel(_ sender: Any) {
        dismiss(animated: false, completion: nil)
    }
}
