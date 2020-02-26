//
//  SelectTherapyViewController.swift
//  Renovar
//
//  Created by Macbook on 18/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents

class SelectTherapyViewController: UIViewController ,UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout{
    
    @IBOutlet weak var itemsCollectionView: UICollectionView!
    
    var appBarViewController = MDCAppBarViewController()
    var items = [Item]();
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.items.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellItem", for: indexPath as IndexPath) as! SelectTherapyCollectionViewCell
        
        
        cell.lblProduct.text = self.items[indexPath.item].name
        
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
        return CGSize(width:collectionView.frame.size.width - 10, height:85)
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if let presenter = presentingViewController as? AddScheduleViewController {
            presenter.getTherapy(value: items[indexPath.item])
        }
        dismiss(animated: true, completion: nil)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let jsonUrlString = "http://renovar.health/renovarmobile/get_all_products.php";
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
        
        self.title = "Select Therapy"
        
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
        
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
}

extension SelectTherapyViewController {
    
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


