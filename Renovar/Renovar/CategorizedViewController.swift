//
//  CategorizedViewController.swift
//  Renovar
//
//  Created by Macbook on 30/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents

struct Collection : Decodable {
    var id : Int
    var collection : String
    var image_url : String
    var category_id : Int
}

class CategorizedViewController: UIViewController ,UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout  {
    
    var id : Int = 0
    var collection : String = ""
    var isSelection : Bool = false
    
    var appBarViewController = MDCAppBarViewController()
    var categories = [Collection]();
    
    @IBOutlet weak var categoryCollectionView: UICollectionView!
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.categories.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellItem", for: indexPath as IndexPath) as! CategorizedCollectionViewCell
        
        cell.lblCategory.text = categories[indexPath.item].collection
        
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
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width:collectionView.frame.size.width - 10, height:200)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController =
            storyboard.instantiateViewController(withIdentifier: "ViewController") as! ViewController
        
        viewController.category_id = categories[indexPath.item].id
        viewController.category = categories[indexPath.item].collection
        viewController.isSelection = self.isSelection
        
        present(viewController, animated: true, completion: nil)    
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let jsonUrlString = "http://renovar.health/renovarmobile/get_collections.php?category_id=\(id)";
        guard let url = URL(string: jsonUrlString) else {return}
        
        URLSession.shared.dataTask(with:url) {(data,response,err) in
            guard let data = data else { return }
            
            print("Retrieving Data")
            
            do{
                self.categories = try JSONDecoder().decode([Collection].self
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
        
        self.title = collection
        if(isSelection != false)
        {
            self.title = "Select \(collection)"
        }
        
        let menuItemImage = UIImage(named: "Back")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
        self.view.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        self.categoryCollectionView?.backgroundColor = ApplicationScheme.shared.colorScheme
            .surfaceColor
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    func setTherapy(item : Item)
    {
        print(item.name)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.0) {
            if let presenter = self.presentingViewController as? CategoryViewController {
                presenter.setTherapy(item : item)
            }
            self.dismiss(animated: false, completion: nil)
        }
    }
}

extension CategorizedViewController {
    
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

