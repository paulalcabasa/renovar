//
//  HomeViewController.swift
//  Renovar
//
//  Created by Macbook on 09/06/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import ImageSlideshow
import MaterialComponents
import SimpleAnimation


class HomeViewController: UIViewController {

  
    @IBOutlet weak var lblMessage: UILabel!
    @IBOutlet weak var imageSlider: ImageSlideshow!
    var texts = ["YOUR WISH IS\nOUR TECHNOLOGY","WE BELIEVE\nEveryone is entitled to\ndiscover a new world of advanced technology","OUR PHILOSOPHY\nTo help relieve\npain and issues related to\n Health and Wellness"]
    override func viewDidLoad() {
        super.viewDidLoad()
        

        imageSlider.setImageInputs([
            AlamofireSource(urlString: "http://renovar.health/renovarmobile/slide1.jpg")!,
            AlamofireSource(urlString: "http://renovar.health/renovarmobile/slide2.jpg")!,
            AlamofireSource(urlString: "http://renovar.health/renovarmobile/slide3.jpg")!,
            ])
        
        
        imageSlider.slideshowInterval = 5
        imageSlider.contentScaleMode = .scaleAspectFill
        imageSlider.circular = true
        
        self.lblMessage.text = self.texts[0]
        self.lblMessage.slideIn()

//        self.lblMessage.shineDuration = 3
//        self.lblMessage.fadeoutDuration = 1
//
//        self.lblMessage.shine {
//            self.lblMessage.fadeOut()
//        }
//
    
        
        imageSlider.currentPageChanged = { page in
            self.lblMessage.text = self.texts[page]
//            self.lblMessage.shine {
//                self.lblMessage.fadeOut()
//            }
            
            self.lblMessage.slideIn()
        }
    }
    
    
    @IBAction func onStartClick(_ sender: Any) {
        if let presenter = presentingViewController as? BackdropViewController {
            presenter.openAbout()
        }
        self.dismiss(animated: true, completion: nil)
    }
    
    
}
