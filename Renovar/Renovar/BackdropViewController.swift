//
//  BackdropViewController.swift
//  Renovar
//
//  Created by Macbook on 11/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import DeckTransition
import SQLite3


class BackdropViewController: UIViewController {

    var appBar = MDCAppBar()
    
    var db : OpaquePointer?
    
    var containerView: UIView = {
        //TODO: Change the following line from UIView to ShapedShadowedView and apply the shape.
        let view = ShapedShadowedView(frame: .zero)
        let shapeGenerator = MDCRectangleShapeGenerator()
        view.shapeGenerator = shapeGenerator
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = ApplicationScheme.shared.colorScheme.surfaceColor
        return view
    }()
    
    let collectionButton: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Collections", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()
    
    let reDefineButton: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("ReDefine", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()
    
    let reGenerateButton: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("ReGenerate", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()
    
    let aboutUsButton: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("About Us", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()
    
    let therapySchedulerButton: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Therapy Scheduler", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()

    let productRegistration: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Product Registration", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()
    
    let myfaceChanged: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("My Face Changed", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()
    
    let contactUsButton: MDCButton = {
        let button = MDCButton()
        button.isUppercaseTitle = false
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Contact Us", for: .normal)
        button.addTarget(self, action: #selector(didTapCategory(sender:)), for: .touchUpInside)
        MDCTextButtonThemer.applyScheme(ApplicationScheme.shared.buttonScheme, to: button)
        return button
    }()

    
    var embeddedView: UIView?
    var embeddedViewController: UIViewController?
    
    var isFocusedEmbeddedController: Bool = false {
        didSet {
            UIView.animate(withDuration: 0.2) {
                self.containerView.frame = self.frameForEmbeddedController()
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = ApplicationScheme.shared.colorScheme.surfaceColor
        
        self.title = "Renovar"
        
        // Setup Navigation Items
        let menuItemImage = UIImage(named: "Close")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
        let menuAddSchedule = UIImage(named: "Cart")
        let templatedMenuAddImage = menuAddSchedule?.withRenderingMode(.alwaysTemplate)
        let addItem = UIBarButtonItem(image: templatedMenuAddImage,
                                      style: .plain,
                                      target: self,
                                      action: #selector(cartItemTapped(sender:)))
        self.navigationItem.rightBarButtonItem = addItem

        
        // AppBar Init
        self.addChild(appBar.headerViewController)
        appBar.addSubviewsToParent()
        MDCAppBarColorThemer.applySemanticColorScheme(ApplicationScheme.shared.colorScheme, to: appBar)
//        self.appBar.navigationBar.translatesAutoresizingMaskIntoConstraints = true
        
        // Buttons
        self.view.addSubview(collectionButton)
//        self.view.addSubview(reDefineButton)
//        self.view.addSubview(reGenerateButton)
        self.view.addSubview(therapySchedulerButton)
        self.view.addSubview(myfaceChanged)
        self.view.addSubview(aboutUsButton)
        self.view.addSubview(productRegistration)
        
        var constraints = [NSLayoutConstraint]()
        constraints.append(NSLayoutConstraint(item: self.collectionButton,
                                              attribute: .centerX,
                                              relatedBy: .equal,
                                              toItem: self.view,
                                              attribute: .centerX,
                                              multiplier: 1.0,
                                              constant: 0.0))
        
        let nameView = [
            "navigationbar" : self.appBar.navigationBar,
            "collection" : self.collectionButton,
//            "redefine" : self.reDefineButton,
//            "regenerate" : self.reGenerateButton,
            "scheduler" : self.therapySchedulerButton,
            "facechanged" : self.myfaceChanged,
            "aboutus" : self.aboutUsButton,
            "registration" : self.productRegistration,
            "contactus" : self.contactUsButton
        ]
        
        constraints.append(contentsOf:
                NSLayoutConstraint.constraints(withVisualFormat: "V:|[navigationbar]-[collection]-[scheduler]-[facechanged]-[aboutus]-[registration]", options: .alignAllCenterX, metrics: nil, views: nameView))
        
        NSLayoutConstraint.activate(constraints)
        
        let recognizer = UITapGestureRecognizer(target: self, action: #selector(filterItemTapped(sender:)))
        self.view.addGestureRecognizer(recognizer)
        
        self.view.addSubview(self.containerView)
    
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
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
        
        lastStoryBoard = viewController
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    func openAbout()
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "AboutUsViewController")
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            self.present(viewController, animated: false, completion: nil)
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        
        let embeddedFrame = self.frameForEmbeddedController()
        self.containerView.frame = embeddedFrame
        self.embeddedView?.frame = self.containerView.bounds
    }
    
    func frameForEmbeddedController() -> CGRect {
        var embeddedFrame = self.view.bounds
        var insetHeader = UIEdgeInsets()
        insetHeader.top = self.appBar.headerViewController.view.frame.maxY
    
        
        if !isFocusedEmbeddedController {
            embeddedFrame.origin.y = self.view.bounds.size.height - self.appBar.navigationBar.frame.height
        }
        
        if (embeddedView == nil) {
            embeddedFrame.origin.y = self.view.bounds.maxY
        }
        
        return embeddedFrame
    }
    
    //MARK - Actions
    @objc func menuItemTapped(sender: Any) {
        self.present(lastStoryBoard, animated: true, completion: nil)
    }
    
    @objc func cartItemTapped(sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "ShoppingCartViewController")
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    @objc func filterItemTapped(sender: Any) {
        isFocusedEmbeddedController = !isFocusedEmbeddedController
    }
    
    var lastStoryBoard = UIViewController()
    @objc func didTapCategory(sender: Any) {
        let view = sender as! UIView
        if (view == self.reGenerateButton) {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "ViewController")
            lastStoryBoard = viewController
            
            self.present(viewController, animated: true, completion: nil)
        }
        
        if (view == self.reDefineButton) {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "CollectionsViewController")
            lastStoryBoard = viewController
            self.present(viewController, animated: true, completion: nil)
        }
        
        if(view == self.collectionButton)
        {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "CategoryViewController")
            lastStoryBoard = viewController
            
            self.present(viewController, animated: true, completion: nil)
        }
        
        if(view == self.therapySchedulerButton)
        {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "SchedulerViewController")
            lastStoryBoard = viewController
            
            self.present(viewController, animated: true, completion: nil)
        }
        
        if(view == self.myfaceChanged)
        {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "FaceChangedViewController")
            lastStoryBoard = viewController
            
            self.present(viewController, animated: true, completion: nil)
        }
        
        
        if(view == self.aboutUsButton)
        {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController =
                storyboard.instantiateViewController(withIdentifier: "AboutUsViewController")
             lastStoryBoard = viewController
            
            self.present(viewController, animated: true, completion: nil)
        }
        
        if(view == self.productRegistration)
        {
            let url = URL(string: "http://renovar.health/product_registry")!
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
        
        isFocusedEmbeddedController = !isFocusedEmbeddedController
    }

}

extension BackdropViewController {
    func insert(_ controller: UIViewController) {
//        if let controller = self.embeddedViewController,
//            let view = self.embeddedView {
//            controller.willMove(toParent: nil)
//            controller.removeFromParent()
//            self.embeddedViewController = nil
//            
//            view.removeFromSuperview()
//            self.embeddedView = nil
//
//            isFocusedEmbeddedController = true
//        }
        controller.willMove(toParent: self)
        self.addChild(controller)
        self.embeddedViewController = controller
        
        self.containerView.addSubview(controller.view)
        self.embeddedView = controller.view
        self.embeddedView?.backgroundColor = .clear
        
        isFocusedEmbeddedController = false
    }
    
    func removeController() {
        if let controller = self.embeddedViewController,
            let view = self.embeddedView {
            controller.willMove(toParent: nil)
            controller.removeFromParent()
            self.embeddedViewController = nil
            
            view.removeFromSuperview()
            self.embeddedView = nil
            
            isFocusedEmbeddedController = false
        }
    }
}
