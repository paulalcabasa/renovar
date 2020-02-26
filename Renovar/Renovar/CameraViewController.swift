//
//  CameraViewController.swift
//  Renovar
//
//  Created by Macbook on 25/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import CameraManager
import ImageIO
import CoreData
import SQLite3

class CameraViewController: UIViewController {
    
    var cam_message : String  = ""
    var therapy_id : String  = ""
    var therapy : String  = ""
    
    @IBOutlet weak var lblMessage: UILabel!
    let cameraManager = CameraManager()
    @IBOutlet weak var cameraView: UIView!
    
    var db : OpaquePointer?

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
    
        cameraManager.addPreviewLayerToView(self.cameraView)
        
        cameraManager.cameraDevice = .front
        
        lblMessage.text = cam_message
        
        print(therapy_id)
        
        let fileUrl = try!
            FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false).appendingPathComponent("Renovar.db")
        
        if sqlite3_open(fileUrl.path, &db) != SQLITE_OK{
            print("Error opening database")
            return
        }
        
//        let createTableQuery = "CREATE TABLE IF NOT EXISTS Gallery(id TEXT PRIMARY KEY,therapy_id TEXT,image_url TEXT,therapy TEXT)"
//        
//        if sqlite3_exec(db, createTableQuery, nil, nil, nil) != SQLITE_OK{
//            print("Error creating table")
//            return
//        }
//        
        print("Success Creating Database")
    }
    
    @IBAction func onCapture(_ sender: Any) {
        cameraManager.capturePictureWithCompletion({ result in
            switch result {
            case .failure:
                print("Failed saving image")
            case .success(let content):
                
                let id = "\(NSDate().timeIntervalSince1970)"
                if let data = content.asImage?.jpegData(compressionQuality: 0.8) {
                    let filename = self.getDocumentsDirectory().appendingPathComponent("\(id).jpg")
                    try? data.write(to: filename)
                    
                    
                    var stmnt : OpaquePointer?
                    
                    let insertQuery = "INSERT INTO Gallery (id,therapy_id,image_url,therapy) VALUES (?,?,?,?)"
                    let SQLITE_TRANSIENT = unsafeBitCast(OpaquePointer(bitPattern: -1), to: sqlite3_destructor_type.self)
                    
                    if(sqlite3_prepare(self.db, insertQuery, -1, &stmnt, nil)) != SQLITE_OK{
                        print("Error binding query")
                    }
                    
                    if(sqlite3_bind_text(stmnt, 1, "\(id)" , -1, SQLITE_TRANSIENT)) != SQLITE_OK
                    {
                        print("Error binding id")
                    }
                    
                    if(sqlite3_bind_text(stmnt, 2, "\(self.therapy_id)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
                    {
                        print("Error binding therapy_id")
                    }
                    
                    if(sqlite3_bind_text(stmnt, 3, "\(id).jpg", -1, SQLITE_TRANSIENT)) != SQLITE_OK
                    {
                        print("Error binding image_url")
                    }
                    
                    if(sqlite3_bind_text(stmnt, 4, "\(self.therapy)", -1, SQLITE_TRANSIENT)) != SQLITE_OK
                    {
                        print("Error binding therapy_name")
                    }
                    
                    if sqlite3_step(stmnt) == SQLITE_DONE
                    {
                        print("Therapy Saved")
                        self.dismiss(animated: true, completion: nil)
                    }

//                    let appDelegate = UIApplication.shared.delegate as! AppDelegate
//
//                    let context = appDelegate.persistentContainer.viewContext
//
//                    let newGallery = NSEntityDescription.insertNewObject(forEntityName: "Gallery", into: context)
//
//                    newGallery.setValue(id,forKey: "id")
//                    newGallery.setValue(self.therapy_id,forKey: "therapy_id")
//                    newGallery.setValue("\(id).jpg",forKey: "img_url")
//
//                    do {
//                        try context.save()
//                        print("saved")
//                        self.dismiss(animated: true, completion: nil)
//                    } catch {
//
//                    }
                }
                
              
            }
        })
    }
    
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    @IBAction func switchCamera(_ sender: Any) {
        if(cameraManager.cameraDevice == .front)
        {
            cameraManager.cameraDevice = .back
        }
        else{
            cameraManager.cameraDevice = .front
        }
    }
    
}
    
