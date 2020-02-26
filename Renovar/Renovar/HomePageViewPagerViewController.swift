//
//  HomePageViewPagerViewController.swift
//  Renovar
//
//  Created by Macbook on 09/06/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit

class HomePageViewPagerViewController: UIPageViewController,UIPageViewControllerDelegate,UIPageViewControllerDataSource {
    
    lazy var orderedViewControllers : [UIViewController] = {
        return [self.newVC(viewController: "page1"),
                self.newVC(viewController: "page2")]
    }()
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else{
            return nil
        }
        
        let nextIndex = viewControllerIndex + 1
        
        guard orderedViewControllers.count != nextIndex else {
            return orderedViewControllers.first
        }
        
        guard orderedViewControllers.count > nextIndex else {
            return nil
        }
        
        return orderedViewControllers[nextIndex]
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else{
            return nil
        }
        
        let previousIndex = viewControllerIndex - 1
        
        guard previousIndex >= 0 else {
            return orderedViewControllers.last
        }
        
        guard orderedViewControllers.count > previousIndex else {
            return nil
        }
        
        return orderedViewControllers[previousIndex]
    }
    
    func newVC(viewController : String) -> UIViewController{
        return UIStoryboard(name : "Main",bundle : nil).instantiateViewController(withIdentifier: viewController)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.dataSource = self
        if let firstViewController = orderedViewControllers.first {
            setViewControllers([firstViewController], direction: .forward, animated: true, completion: nil)
        }
    }


}
