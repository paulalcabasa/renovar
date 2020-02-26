//
//  SchedulerViewController.swift
//  Renovar
//
//  Created by Macbook on 13/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import DeckTransition
import UserNotifications
import CoreData

struct Therapy : Decodable{
    let id : String?
    let schedule : String?
    let therapy : String?
    let time : String?
    let message : String?
    
    init(id :String, schedule :String,therapy:String,time:String,message:String) {
        self.id = id
        self.schedule = schedule
        self.therapy = therapy
        self.time = time
        self.message = message
    }
}

class SchedulerViewController: UIViewController,UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout {

    var appBarViewController = MDCAppBarViewController()
    var therapy = [Therapy]();
    @IBOutlet weak var itemsCollectionView: UICollectionView!
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.therapy.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellItem", for: indexPath as IndexPath) as! ThearapyCollectionViewCell
        
        
        cell.lblTherapy.text = self.therapy[indexPath.item].therapy
        cell.lblSchedule.text = self.therapy[indexPath.item].schedule
        
        let date = Date(timeIntervalSince1970: (therapy[indexPath.item].id! as NSString).doubleValue)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "GMT+08:00")
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "hh:mm a"
        let strDate = dateFormatter.string(from: date)
        
        cell.lblTime.text = strDate
        
        cell.layer.cornerRadius = 8
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
        let alert = UIAlertController(title: "Schedule Options", message: "", preferredStyle: UIAlertController.Style.alert)

        alert.addAction(UIAlertAction(title: "Add Therapy Record", style: UIAlertAction.Style.default, handler: {action in
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "CameraViewController") as! CameraViewController
        
            viewController.cam_message = self.therapy[indexPath.item].message ?? ""
            viewController.therapy_id = self.therapy[indexPath.item].id ?? ""
            viewController.therapy = self.therapy[indexPath.item].therapy ?? ""
            
            
            
            self.present(viewController, animated: true, completion: nil)
            
        
        }))
    
        alert.addAction(UIAlertAction(title: "Delete Schedule", style: UIAlertAction.Style.default, handler: {action in
            
            UNUserNotificationCenter.current().getPendingNotificationRequests { (notificationRequests) in
                var identifiers: [String] = []
                for notification:UNNotificationRequest in notificationRequests {
                    if notification.identifier == self.therapy[indexPath.item].id {
                        identifiers.append(notification.identifier)
                    }
                }
                UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: identifiers)
                
                DispatchQueue.main.sync(execute: {
                    self.loadSchedules()
                    collectionView.reloadData()
                })
                
            }
            
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: nil))
      
        self.present(alert, animated: true, completion: nil)
    }
    
    
   
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
        // Set the tracking scroll view.
        self.appBarViewController.headerView.trackingScrollView = self.itemsCollectionView
        
        self.title = "Therapy Scheduler"
        
        let menuItemImage = UIImage(named: "MenuItem")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
        let menuAddSchedule = UIImage(named: "Add")
        let templatedMenuAddImage = menuAddSchedule?.withRenderingMode(.alwaysTemplate)
        let addItem = UIBarButtonItem(image: templatedMenuAddImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(addScheduleTapped(sender:)))
        self.navigationItem.rightBarButtonItem = addItem
    
        
        self.view.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        self.itemsCollectionView?.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
        
        loadSchedules()
       
    }
    
    func loadSchedules()
    {
        self.therapy.removeAll()
        let center = UNUserNotificationCenter.current()
        center.getPendingNotificationRequests(completionHandler: { requests in
            for request in requests {
                print("ID    : "+request.identifier)
                print("TITLE : "+request.content.title)
                print("BODY  : "+request.content.body)
                
                if(!request.content.summaryArgument.isEmpty)
                {
                    var summary = (request.content.summaryArgument).split{$0 == ","}.map(String.init)
                    
                    print("TIME     : "+summary[0])
                    print("REPEAT   : "+summary[1])
                    print("THERAPY  : "+summary[2])
                    print("MESSAGE  : "+summary[3])
                    
                    let therapy : Therapy = Therapy(id: request.identifier, schedule: summary[1], therapy: summary[2], time: summary[0],message: summary[3])
                    
                    self.therapy.append(therapy)
                }
            }
        })
        
    }
   
    
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @objc func addScheduleTapped(sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "AddScheduleViewController")
    

//        let transitionDelegate = DeckTransitioningDelegate()
//        viewController.transitioningDelegate = transitionDelegate
//        viewController.modalPresentationStyle = .custom
        
        self.present(viewController, animated: true, completion: nil)
    }

}


extension SchedulerViewController {
    
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
