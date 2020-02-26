//
//  GalleryViewController.swift
//  Renovar
//
//  Created by Macbook on 29/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import SQLite3

class GalleryViewController: UIViewController ,UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout {
    
    var therapy : String = ""
    @IBOutlet weak var itemsCollectionView: UICollectionView!
    var appBarViewController = MDCAppBarViewController()
    var items = [Albums]();
    
    var db : OpaquePointer?
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.items.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellItem", for: indexPath as IndexPath) as! ImageContentCollectionViewCell
        
        
        cell.imageContent.image = load(fileName: items[indexPath.item].image_url)
        
        let date = Date(timeIntervalSince1970: (items[indexPath.item].id as NSString).doubleValue)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "GMT")
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "MMMM dd, yyyy"
        let strDate = dateFormatter.string(from: date)
        
        cell.lblDate.text = strDate
     
        

        cell.imageContent.layer.cornerRadius = 8
        cell.layer.cornerRadius = 8
        cell.setShadowColor(UIColor.black, for: .highlighted)
        cell.contentView.layer.masksToBounds = true
        cell.layer.masksToBounds = false
        cell.setShadowElevation(ShadowElevation(rawValue: 2), for: .normal)
        
        return cell
    }
    
    private func load(fileName: String) -> UIImage? {
        let fileURL = documentsUrl.appendingPathComponent(fileName)
        do {
            let imageData = try Data(contentsOf: fileURL)
            return UIImage(data: imageData)
        } catch {
            print("Error loading image : \(error)")
        }
        return nil
    }
    
    var documentsUrl: URL {
        return FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    }
    
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding: CGFloat =  30
        let collectionViewSize = collectionView.frame.size.width - padding
        
        return CGSize(width: collectionViewSize/2, height: collectionViewSize/1.3)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let alert = UIAlertController(title: "Record Options", message: "", preferredStyle: UIAlertController.Style.alert)

        
        alert.addAction(UIAlertAction(title: "Delete Record", style: UIAlertAction.Style.default, handler: {action in
            var stmnt : OpaquePointer?
            
            let insertQuery = "DELETE FROM Gallery WHERE id = ?"
            let SQLITE_TRANSIENT = unsafeBitCast(OpaquePointer(bitPattern: -1), to: sqlite3_destructor_type.self)
            
            if(sqlite3_prepare(self.db, insertQuery, -1, &stmnt, nil)) != SQLITE_OK{
                print("Error binding query")
            }
            
            if(sqlite3_bind_text(stmnt, 1, "\(self.items[indexPath.item].id)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
            {
                print("Error binding id")
            }
            
            if sqlite3_step(stmnt) == SQLITE_DONE
            {
                print("Record Deleted")
                self.loadData()
                self.itemsCollectionView.reloadData()
                
            }
            
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
        // Set the tracking scroll view.
        self.appBarViewController.headerView.trackingScrollView = self.itemsCollectionView
        
        self.title = self.therapy
        
        let menuItemImage = UIImage(named: "Back")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
        self.view.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        self.itemsCollectionView?.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
        
        
        loadData()
        
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    func loadData()
    {
        items.removeAll()
        let fileUrl = try!
            FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false).appendingPathComponent("Renovar.db")
        
        if sqlite3_open(fileUrl.path, &db) != SQLITE_OK{
            print("Error opening database")
            return
        }
        
        let createTableQuery = "CREATE TABLE IF NOT EXISTS Gallery(id TEXT PRIMARY KEY,therapy_id TEXT,image_url TEXT,therapy TEXT)"
        
        if sqlite3_exec(db, createTableQuery, nil, nil, nil) != SQLITE_OK{
            print("Error creating table")
            return
        }
        
        print("Success Creating Database")
        
        var stmnt : OpaquePointer?
        
        if sqlite3_prepare_v2(db, "SELECT * FROM Gallery WHERE therapy = '\(therapy)'", -1, &stmnt, nil) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("error preparing select: \(errmsg)")
        }
        
        while sqlite3_step(stmnt) == SQLITE_ROW {
            var image_id : String?
            var img_url : String?
            var therapy_name : String?
            
            if let cID = sqlite3_column_text(stmnt, 0) {
                let id = String(cString: cID)
                print("id = \(id)")
                image_id = id
            } else {
                print("id not found")
            }
            
            if let cTID = sqlite3_column_text(stmnt, 1) {
                let name = String(cString: cTID)
                print("therapy_id = \(name)")
            } else {
                print("therapy_id not found")
            }
            
            if let cIU = sqlite3_column_text(stmnt, 2) {
                let image_url = String(cString: cIU)
                print("image_url = \(image_url)")
                img_url = image_url;
            } else {
                print("image_url not found")
            }
            
            if let cT = sqlite3_column_text(stmnt, 3) {
                let ther_name = String(cString: cT)
                print("ther_name = \(ther_name)")
                therapy_name = ther_name;
            } else {
                print("ther_name not found")
            }
            
            let album : Albums = Albums(id : image_id ?? "",image_url : img_url ?? "",therapy: therapy_name ?? "")
            items.append(album)
        }
        
        
    }
    
}

extension GalleryViewController {
    
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

