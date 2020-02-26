//
//  CollectionsViewController.swift
//  Renovar
//
//  Created bys
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import DeckTransition

class CollectionsViewController: UIViewController ,UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout{
  
    @IBOutlet weak var itemsCollectionView: UICollectionView!
    
    var appBarViewController = MDCAppBarViewController()
    var items = [Item]();
    
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.items.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellItem", for: indexPath as IndexPath) as! ItemCollectionViewCell
        
        
        cell.lblName.text = self.items[indexPath.item].name
        
        let url = URL(string: items[indexPath.item].image_url)
        cell.imageProduct.kf.setImage(with: url)
        
        cell.layer.cornerRadius = 8
        cell.setShadowColor(UIColor.black, for: .highlighted)
        cell.contentView.layer.masksToBounds = true
        cell.layer.masksToBounds = false
        cell.setShadowElevation(ShadowElevation(rawValue: 2), for: .normal)
        
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding: CGFloat =  30
        let collectionViewSize = collectionView.frame.size.width - padding
        
        return CGSize(width: collectionViewSize/2, height: collectionViewSize/1.5)
    }
    
  
    
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "ItemContentBottomSheet") as! ItemContentBottomViewController
        
        viewController.item_name = items[indexPath.item].name
        viewController.item_desc = items[indexPath.item].description
        viewController.item_image = items[indexPath.item].image_url
        viewController.item_price = "$"+self.items[indexPath.item].price
        
        
//        let bottomSheet: MDCBottomSheetController = MDCBottomSheetController(contentViewController: viewController)
    

        let transitionDelegate = DeckTransitioningDelegate()
        viewController.transitioningDelegate = transitionDelegate
        viewController.modalPresentationStyle = .custom


   
        present(viewController, animated: true, completion: nil)
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let jsonUrlString = "http://renovar.health/renovarmobile/get_products.php?category=1";
        guard let url = URL(string: jsonUrlString) else {return}
        
        URLSession.shared.dataTask(with:url) {(data,response,err) in
            guard let data = data else { return }
            
            print("Retrieving Data")
            
            do{
                self.items = try JSONDecoder().decode([Item].self
                    , from:data)
                print(self.items.count)
                
                DispatchQueue.main.sync(execute: {
                    self.itemsCollectionView.reloadData()
                })
                
            }catch let jsonErr{
                print("Error parsing json ", jsonErr)
            }
            }.resume()
        
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
        // Set the tracking scroll view.
        self.appBarViewController.headerView.trackingScrollView = self.itemsCollectionView
        
        self.title = "ReDefine"
        
        let menuItemImage = UIImage(named: "MenuItem")
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

        
        self.view.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        self.itemsCollectionView?.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
        
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @objc func cartItemTapped(sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "ShoppingCartViewController")
        
        self.present(viewController, animated: true, completion: nil)
    }
}

extension CollectionsViewController {
    
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

