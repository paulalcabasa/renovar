//
//  ApplicationScheme.swift
//  Renovar
//
//  Created by Macbook on 11/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit
import MaterialComponents

class ApplicationScheme: NSObject {
    private static var singleton = ApplicationScheme()
    
    static var shared: ApplicationScheme {
        return singleton
    }
    
    override init() {
        self.buttonScheme.colorScheme = self.colorScheme
        super.init()
    }
    
    public let buttonScheme = MDCButtonScheme()
    
    public let colorScheme: MDCColorScheming = {
        let scheme = MDCSemanticColorScheme(defaults: .material201804)
        scheme.primaryColor =
            UIColor(red: 220.0/255.0, green: 166.0/255.0, blue: 145.0/255.0, alpha: 1.0)
        scheme.primaryColorVariant =
            UIColor(red: 68.0/255.0, green: 44.0/255.0, blue: 46.0/255.0, alpha: 1.0)
        scheme.onPrimaryColor =
            UIColor(red: 68.0/255.0, green: 44.0/255.0, blue: 46.0/255.0, alpha: 1.0)
        scheme.secondaryColor =
            UIColor(red: 254.0/255.0, green: 234.0/255.0, blue: 230.0/255.0, alpha: 1.0)
        scheme.onSecondaryColor =
            UIColor(red: 68.0/255.0, green: 44.0/255.0, blue: 46.0/255.0, alpha: 1.0)
        scheme.surfaceColor =
            UIColor(red: 255.0/255.0, green: 251.0/255.0, blue: 250.0/255.0, alpha: 1.0)
        scheme.onSurfaceColor =
            UIColor(red: 68.0/255.0, green: 44.0/255.0, blue: 46.0/255.0, alpha: 1.0)
        scheme.backgroundColor =
            UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 1.0)
        scheme.onBackgroundColor =
            UIColor(red: 68.0/255.0, green: 44.0/255.0, blue: 46.0/255.0, alpha: 1.0)
        scheme.errorColor =
            UIColor(red: 197.0/255.0, green: 3.0/255.0, blue: 43.0/255.0, alpha: 1.0)
        return scheme
    }()
    
    public let colorSchemeTransparent: MDCColorScheming = {
        let scheme = MDCSemanticColorScheme(defaults: .material201804)
        scheme.primaryColor = UIColor(white: 1, alpha: 0.0)
        scheme.primaryColorVariant =
            UIColor(white: 1, alpha: 0.0)
        scheme.onPrimaryColor =
           UIColor(white: 1, alpha: 0.0)
        scheme.secondaryColor =
          UIColor(white: 1, alpha: 0.0)
        scheme.onSecondaryColor =
            UIColor(white: 1, alpha: 0.0)
        scheme.surfaceColor =
            UIColor(white: 1, alpha: 0.0)
        scheme.onSurfaceColor =
            UIColor(white: 1, alpha: 0.0)
        scheme.backgroundColor =
           UIColor(white: 1, alpha: 0.0)
        scheme.onBackgroundColor =
            UIColor(white: 1, alpha: 0.0)
        scheme.errorColor =
         UIColor(white: 1, alpha: 0.0)
        return scheme
    }()
    
    public let shapeScheme: MDCShapeScheming = {
        let scheme = MDCShapeScheme()
        scheme.largeComponentShape = MDCShapeCategory(cornersWith: .cut, andSize: 20)
        return scheme
    }()
}
