//
//  CategoryViewController.swift
//  Renovar
//
//  Created by Macbook on 11/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents

struct Categories : Decodable{
    let id : Int
    let category : String
    let image_url : String
}

class CategoryViewController: UIViewController ,UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout  {
    
    @IBOutlet weak var categoryCollectionView: UICollectionView!
    var appBarViewController = MDCAppBarViewController()
    var categories = [Categories]();
    
    var isSelection : Bool = false
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.categories.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
       let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellItem", for: indexPath as IndexPath) as! CategoryCollectionViewCell
        
        cell.lblCategoryName.text = categories[indexPath.item].category
    
        let url = URL(string: categories[indexPath.item].image_url)
        cell.imageCategory.kf.setImage(with: url)
        
        cell.imageCategory.layer.cornerRadius = 8
        cell.layer.cornerRadius = 8
        cell.setShadowColor(UIColor.black, for: .highlighted)
        cell.contentView.layer.masksToBounds = true
        cell.layer.masksToBounds = false
        cell.setShadowElevation(ShadowElevation(rawValue: 2), for: .normal)
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "CategorizedViewController") as! CategorizedViewController
        
        viewController.id = categories[indexPath.item].id
        viewController.collection = categories[indexPath.item].category
        viewController.isSelection = self.isSelection
        
        present(viewController, animated: true, completion: nil)
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width:collectionView.frame.size.width - 10, height:200)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let jsonUrlString = "http://renovar.health/renovarmobile/get_categories.php";
        guard let url = URL(string: jsonUrlString) else {return}
        
        URLSession.shared.dataTask(with:url) {(data,response,err) in
            guard let data = data else { return }
            
            print("Retrieving Data")
            
            do{
                self.categories = try JSONDecoder().decode([Categories].self
                    , from:data)
                print(self.categories.count)
                
                DispatchQueue.main.sync(execute: {
                   self.categoryCollectionView.reloadData()
                })
                
            }catch let jsonErr{
                print("Error parsing json ", jsonErr)
            }
            }.resume()
        
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
        // Set the tracking scroll view.
        self.appBarViewController.headerView.trackingScrollView = self.categoryCollectionView
        
        self.title = "Collections"
        if(isSelection != false)
        {
            self.title = "Select Collection"
        }
        
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
        self.categoryCollectionView?.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
    }
    
    @objc func cartItemTapped(sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "ShoppingCartViewController")
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    func setTherapy(item : Item)
    {
        print(item.name)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            if let presenter = self.presentingViewController as? AddScheduleViewController {
                presenter.getTherapy(value : item)
            }
            self.dismiss(animated: false, completion: nil)
        }
    }
}

extension CategoryViewController {
    
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
