//
//  AddScheduleViewController.swift
//  Renovar
//
//  Created by Macbook on 14/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import DeckTransition
import UserNotifications
import SQLite


class AddScheduleViewController: UIViewController,UNUserNotificationCenterDelegate {

    @IBOutlet weak var txtSelectTherapy: MDCTextField!
    var appBarViewController = MDCAppBarViewController()
    
    @IBOutlet weak var dpSchedule: UIDatePicker!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
    
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) {
            (granted, error) in
            if granted {
                print("yes")
            } else {
                print("No")
            }
        }
        
        self.title = "Schedule Therapy"
        
        let menuItemImage = UIImage(named: "Back")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
        self.view.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func selectTherapy(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        //SelectTherapyController
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "CategoryViewController") as! CategoryViewController
        
        viewController.isSelection = true
        
//        let transitionDelegate = DeckTransitioningDelegate()
//        viewController.transitioningDelegate = transitionDelegate
//        viewController.modalPresentationStyle = .custom
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    var itemTherapy : Item!
    
    @IBAction func onScheduleSet(_ sender: Any) {
//        scheduleNotification(notificationTitle: "Renovar", notificationBody: "Time for your therapy",therapy: itemTherapy)
        if(txtSelectTherapy.text == "")
        {
            let message = MDCSnackbarMessage()
            message.text = "Select therapy first"
            MDCSnackbarManager.show(message)
        }
        else{
            scheduleTherapy(therapy: itemTherapy)
            dismiss(animated: true, completion: nil )
        }
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        if response.actionIdentifier == "action1"
        {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "CameraViewController") as! CameraViewController
            
            var summary = (response.notification.request.content.summaryArgument).split{$0 == ","}.map(String.init)
            
            print("TIME     : "+summary[0])
            print("REPEAT   : "+summary[1])
            print("THERAPY  : "+summary[2])
            print("MESSAGE  : "+summary[3])
            
            
            viewController.cam_message = summary[3]
            viewController.therapy_id = response.notification.request.identifier
            viewController.therapy = summary[2]
            
        
            self.present(viewController, animated: true, completion: nil)
        }
    }

    
    func scheduleTherapy(therapy : Item)
    {
        let action1 = UNNotificationAction(identifier: "action1", title: "Add Therapy Record", options: UNNotificationActionOptions.foreground)
        let action2 = UNNotificationAction(identifier: "action2", title: "Cancel", options: UNNotificationActionOptions.foreground)
        
        let actionCategory = UNNotificationCategory(identifier: "categoryAction", actions: [action1,action2], intentIdentifiers: [], options: [])
        
        UNUserNotificationCenter.current().setNotificationCategories([actionCategory])
        
        let content = UNMutableNotificationContent()
        content.title = "Renovar"
        content.subtitle = "Reminder"
        content.body = "Time for your therapy"
        content.categoryIdentifier = "categoryAction"
        content.badge = 1
        
        let date = self.dpSchedule.date
        
        let calendar = Calendar.current
        let hour = calendar.component(.hour, from: date)
        let minutes = calendar.component(.minute, from: date)
        let seconds = calendar.component(.second, from: date)
        
        var dateComponent = DateComponents()
//        dateComponent.day = therapy.interval
        dateComponent.hour = hour
        dateComponent.minute = minutes
        
        var interval : String?
        if(therapy.interval == 1)
        {
            interval = "Everyday"
        }
        else
        {
            interval = "Every " + String(therapy.interval) + " days"
        }
        
        content.summaryArgument = "\(hour):\(minutes),\(interval ?? ""),\(txtSelectTherapy.text ?? ""),\(therapy.cam_message ?? "")"
    
        
        let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponent, repeats: true)
        let request = UNNotificationRequest(identifier: "\(date.timeIntervalSince1970)", content: content, trigger: trigger)
        UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
    }
    
    func scheduleNotification(notificationTitle: String,notificationBody: String,therapy : Item) {
        let content = UNMutableNotificationContent()
        let notificationCenter = UNUserNotificationCenter.current()
        
        let date = self.dpSchedule.date
        
        let calendar = Calendar.current
        let hour = calendar.component(.hour, from: date)
        let minutes = calendar.component(.minute, from: date)
        let seconds = calendar.component(.second, from: date)
        
        var dateComponent = DateComponents()
        dateComponent.weekday = therapy.interval
        dateComponent.hour = hour
        dateComponent.minute = minutes
        let notificationTrigger = UNCalendarNotificationTrigger(dateMatching: dateComponent, repeats: true)
        
        content.title = notificationTitle
        content.body =  notificationBody
        content.sound = UNNotificationSound.default
        content.badge = 1
        
        var interval : String?
        if(therapy.interval == 1)
        {
            interval = "Everyday"
        }
        else
        {
             interval = "Every " + String(therapy.interval) + " days"
        }
        
        content.summaryArgument = "\(hour):\(minutes),\(interval ?? ""),\(txtSelectTherapy.text ?? ""),\(therapy.cam_message ?? "")"
//
//        let identifier = "Local Notification"
//        let request = UNNotificationRequest(identifier: identifier, content: content, trigger: notificationTrigger)
//
//        notificationCenter.add(request) { (error) in
//            if let error = error {
//                print("Error \(error.localizedDescription)")
//            }
//        }
        
        let notificationRequest = UNNotificationRequest(identifier: "\(date.timeIntervalSince1970)", content: content, trigger: notificationTrigger)
        UNUserNotificationCenter.current().add(notificationRequest) { (error) in
            if let error = error
            {
                let errorString = String(format: NSLocalizedString("Unable to Add Notification Request %@, %@", comment: ""), error as CVarArg, error.localizedDescription)
                print(errorString)
            }
        }
    }
    
    func getTherapy(value : Item)
    {
        txtSelectTherapy.text = value.name;
        itemTherapy = value
    }
    
    
}
