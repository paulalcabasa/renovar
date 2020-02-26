//
//  MainMenuViewController.swift
//  Renovar
//
//  Created by Macbook on 09/06/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents

class MainMenuViewController: UIViewController {

    @IBOutlet weak var imageLogo: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()

        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
        
        lastStoryBoard = viewController
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            self.present(viewController, animated: true, completion: nil)
        }
        
        let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(imageTapped(tapGestureRecognizer:)))
        imageLogo.isUserInteractionEnabled = true
        imageLogo.addGestureRecognizer(tapGestureRecognizer)
    }
    
    @objc func imageTapped(tapGestureRecognizer : UITapGestureRecognizer)
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
        
        lastStoryBoard = viewController
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    var lastStoryBoard = UIViewController()
    @IBAction func onCollectionsClick(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "CategoryViewController")
        lastStoryBoard = viewController
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    @IBAction func onSchedulerClick(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "SchedulerViewController")
        lastStoryBoard = viewController
        
        self.present(viewController, animated: true, completion: nil)
    }
    @IBAction func onFaceClick(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "FaceChangedViewController")
        lastStoryBoard = viewController
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    @IBAction func onAboutClick(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "AboutUsViewController")
        lastStoryBoard = viewController
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    @IBAction func onRegistrationClick(_ sender: Any) {
        let url = URL(string: "http://renovar.health/product_registry")!
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
    }
    
    
}
