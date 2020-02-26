//
//  AboutUsViewController.swift
//  Renovar
//
//  Created by Macbook on 13/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import ImageSlideshow
import Alamofire
import AlamofireImage

class AboutUsViewController: UIViewController {

    @IBOutlet var mainView: UIView!
    @IBOutlet weak var imageSlideShow: ImageSlideshow!
    
    var appBarViewController = MDCAppBarViewController()
    override func viewDidLoad() {
        super.viewDidLoad()

        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
//        // Set the tracking scroll view.
//        self.appBarViewController.headerView.trackingScrollView = self.mainView
        
        self.title = "About Us"
        
        let menuItemImage = UIImage(named: "Back")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
//        self.view.backgroundColor = ApplicationScheme.shared.colorScheme
//            .surfaceColor
//        self.mainView?.backgroundColor = ApplicationScheme.shared.colorScheme
//            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
        
        imageSlideShow.setImageInputs([
            AlamofireSource(urlString: "http://renovar.health/renovarmobile/assets/cat3.jpg")!,
            AlamofireSource(urlString: "http://renovar.health/renovarmobile/assets/download-1.jpg")!,
                  AlamofireSource(urlString: "http://renovar.health/renovarmobile/assets/download.jpg")!,
        ])
        
        imageSlideShow.slideshowInterval = 4
        imageSlideShow.contentScaleMode = .scaleAspectFill
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    

}
