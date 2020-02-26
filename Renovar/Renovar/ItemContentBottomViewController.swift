//
//  ItemContentBottomViewController.swift
//  Renovar
//
//  Created by Macbook on 11/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents

class ItemContentBottomViewController: UIViewController {
    var item_name : String = ""
    var item_desc : String = ""
    var item_image : String = ""
    var item_price : String = ""
    

    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblPrice: UILabel!
    @IBOutlet weak var lblDesc: UITextView!
    @IBOutlet weak var imageProduct: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let url = URL(string: item_image)
        imageProduct.kf.setImage(with: url)
        lblName.text = item_name
        lblDesc.text = item_desc
        lblPrice.text = item_price
        
    
    }

    @IBAction func addToCart(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "CartViewController") as! CartViewController

        viewController.item_image = item_image
        viewController.item_name = item_name
        viewController.item_price = item_price
        
        present(viewController, animated: true, completion: nil)
    }
    
    func itemAdded()
    {
        let message = MDCSnackbarMessage()
        message.text = "Item Added to Cart"
        
        let action = MDCSnackbarMessageAction()
        let actionHandler = {() in
            let answerMessage = MDCSnackbarMessage()
            answerMessage.text = "Item added to cart"
            MDCSnackbarManager.show(answerMessage)
        }
        action.handler = actionHandler
        action.title = "VIEW CART"
        message.action = action
        
        MDCSnackbarManager.show(message)
    }
}
