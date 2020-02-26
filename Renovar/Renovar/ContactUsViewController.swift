//
//  ContactUsViewController.swift
//  Renovar
//
//  Created by Macbook on 05/05/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents
import MapKit

class CityLocation: NSObject, MKAnnotation {
    var title: String?
    var coordinate: CLLocationCoordinate2D
    
    
    init(title: String, coordinate: CLLocationCoordinate2D) {
        self.title = title
        self.coordinate = coordinate
    }
}

class ContactUsViewController: UIViewController {

    @IBOutlet weak var mapView: MKMapView!
    var appBarViewController = MDCAppBarViewController()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.addChild(self.appBarViewController)
        self.view.addSubview(self.appBarViewController.view)
        self.appBarViewController.didMove(toParent: self)
        
        self.title = "Contact"
        
        let menuItemImage = UIImage(named: "Back")
        let templatedMenuItemImage = menuItemImage?.withRenderingMode(.alwaysTemplate)
        let menuItem = UIBarButtonItem(image: templatedMenuItemImage,
                                       style: .plain,
                                       target: self,
                                       action: #selector(menuItemTapped(sender:)))
        self.navigationItem.leftBarButtonItem = menuItem
        
  
        MDCAppBarColorThemer.applyColorScheme(ApplicationScheme.shared.colorScheme
            , to:self.appBarViewController)
        
        initializeMap()
        
    }
    
    let annotation = MKPointAnnotation()
    
    var arr = [[Double]]()
    
    func initializeMap()
    {
        let r2 = CityLocation(title: "Renovar Main Office", coordinate: CLLocationCoordinate2D(latitude: 14.5444461, longitude: 121.0453407))
        
        let r1 = CityLocation(title: "Renovar Eastwood City Eulogio Rodriguez Jr. Ave, Quezon City", coordinate: CLLocationCoordinate2D(latitude: 14.5995, longitude:120.9842))
    
        
        mapView.addAnnotations([r1,r2])
        
    }
    
    @objc func menuItemTapped(sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func callAction(_ sender: Any) {
        dialNumber(number: "+63923456741")
        
    }
    
    func dialNumber(number : String) {
        if let url = NSURL(string: "tel://\(number)"), UIApplication.shared.canOpenURL(url as URL) {
            UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
