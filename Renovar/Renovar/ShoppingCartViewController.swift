//
//  ShoppingCartViewController.swift
//  Renovar
//
//  Created by Macbook on 23/05/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import SQLite3

struct Orders : Encodable {
    var item_name : String
    var quantity : String
    var total_price : String
    var image_url : String
    
    init(item_name :String, quantity :String, total_price : String,image_url : String) {
        self.item_name = item_name
        self.quantity = quantity
        self.total_price = total_price
        self.image_url = image_url
    }
}

class ShoppingCartViewController: UIViewController,UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout {

    var db : OpaquePointer?
    var items = [Orders]();
    
    var appBarViewController = MDCAppBarViewController()
    
    @IBOutlet weak var itemsCollectionView: UICollectionView!
    
    var subtotal : Double = 0
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.items.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellItem", for: indexPath as IndexPath) as! ShoppingCartCollectionViewCell
        
        
        cell.lblName.text =  "\(items[indexPath.item].quantity) pc/s of \(items[indexPath.item].item_name)"
        cell.lblPrice.text =  "$\(items[indexPath.item].total_price)"
        let url = URL(string: items[indexPath.item].image_url)
        cell.imageProduct.kf.setImage(with: url)
        
    
        cell.setShadowColor(UIColor.black, for: .highlighted)
        cell.contentView.layer.masksToBounds = true
        cell.layer.masksToBounds = false
        cell.setShadowElevation(ShadowElevation(rawValue: 2), for: .normal)
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width:collectionView.frame.size.width - 10, height:85)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let alert = UIAlertController(title: "Order Options", message: "", preferredStyle: UIAlertController.Style.alert)

        alert.addAction(UIAlertAction(title: "Remove Item", style: UIAlertAction.Style.default, handler: {action in
            var stmnt : OpaquePointer?
            
            let insertQuery = "DELETE FROM Orders WHERE item_name = ?"
            let SQLITE_TRANSIENT = unsafeBitCast(OpaquePointer(bitPattern: -1), to: sqlite3_destructor_type.self)
            
            if(sqlite3_prepare(self.db, insertQuery, -1, &stmnt, nil)) != SQLITE_OK{
                print("Error binding query")
            }
            
            if(sqlite3_bind_text(stmnt, 1, "\(self.items[indexPath.item].item_name)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
            {
                print("Error binding item_name")
            }
            
            if sqlite3_step(stmnt) == SQLITE_DONE
            {
                print("Order Deleted")
                self.loadData()
                self.itemsCollectionView.reloadData()
                
            }
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let fileUrl = try!
            FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false).appendingPathComponent("Renovar.db")
        
        if sqlite3_open(fileUrl.path, &db) != SQLITE_OK{
            print("Error opening database")
            return
        }
        
        print("Success Creating Database")
        
        loadData()

        
        
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
        // Set the tracking scroll view.
        self.appBarViewController.headerView.trackingScrollView = self.itemsCollectionView
        
        self.title = "Shopping Cart"
        
        let menuItemImage = UIImage(named: "Back")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
        let checkOutImage = UIImage(named: "Cart")
        let templatedCOItemImage = checkOutImage?.withRenderingMode(.alwaysTemplate)
        let coItem = UIBarButtonItem(image: templatedCOItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(checkOutTapped(sender:)))
        self.navigationItem.rightBarButtonItem = coItem
        
        self.view.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        self.itemsCollectionView?.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
        
    }
    
    func loadData()
    {
        items.removeAll()
        subtotal = 0
        var stmnt : OpaquePointer?
        
        if sqlite3_prepare_v2(db, "SELECT item_name,SUM(quantity) as quantity,SUM(total_price) as total_price,image_url FROM Orders GROUP BY item_name", -1, &stmnt, nil) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("error preparing select: \(errmsg)")
        }
        
        while sqlite3_step(stmnt) == SQLITE_ROW {
            var item_name : String?
            var quantity : String?
            var total_price : String?
            var image_url : String?
            
            if let cID = sqlite3_column_text(stmnt, 0) {
                let name = String(cString: cID)
                item_name = name
            } else {
                print("not found")
            }
            
            if let cTID = sqlite3_column_text(stmnt, 1) {
                let qty = String(cString: cTID)
                quantity = qty
            } else {
                print("not found")
            }
            
            if let cIU = sqlite3_column_text(stmnt, 2) {
                let total = String(cString: cIU)
                total_price = total
                subtotal += Double(total) ?? 0
            } else {
                print("not found")
            }
            
            if let cIUM = sqlite3_column_text(stmnt, 3) {
                let image = String(cString: cIUM)
                image_url = image
            } else {
                print("not found")
            }
            
            let order : Orders = Orders(item_name: item_name ?? "", quantity: quantity ?? "", total_price: total_price ?? "",image_url: image_url ?? "")
            items.append(order)
        }
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @objc func checkOutTapped(sender: Any) {
        if(subtotal == 0)
        {
            let message = MDCSnackbarMessage()
            message.text = "Your cart is empty"
            MDCSnackbarManager.show(message)
        }
        else
        {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "CheckOutViewController") as!
            CheckOutViewController
            
            viewController.total_price = String(format: "$%.02f", subtotal)
            viewController.items = self.items
            
            self.present(viewController, animated: true, completion: nil)
        }
    }
}

extension ShoppingCartViewController {
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if (scrollView == self.appBarViewController.headerView.trackingScrollView) {
            self.appBarViewController.headerView.trackingScrollDidScroll()
        }
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if (scrollView == self.appBarViewController.headerView.trackingScrollView) {
            self.appBarViewController.headerView.trackingScrollDidEndDecelerating()
        }
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView,
                                  willDecelerate decelerate: Bool) {
        let headerView = self.appBarViewController.headerView
        if (scrollView == headerView.trackingScrollView) {
            headerView.trackingScrollDidEndDraggingWillDecelerate(decelerate)
        }
    }
    
    func scrollViewWillEndDragging(_ scrollView: UIScrollView,
                                   withVelocity velocity: CGPoint,
                                   targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        let headerView = self.appBarViewController.headerView
        if (scrollView == headerView.trackingScrollView) {
            headerView.trackingScrollWillEndDragging(withVelocity: velocity,
                                                     targetContentOffset: targetContentOffset)
        }
    }
    
}


