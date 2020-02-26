//
//  DismissAnimator.swift
//  Renovar
//
//  Created by Macbook on 11/04/2019.
//  Copyright © 2019 Macbook. All rights reserved.
//

import UIKit

class DismissAnimator: NSObject {

}

extension DismissAnimator : UIViewControllerAnimatedTransitioning {
    func transitionDuration(transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return 0.6
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        guard
            let fromVC = transitionContext.viewControllerForKey(UITransitionContextViewControllerKey.from),
            let toVC = transitionContext.viewControllerForKey(UITransitionContextViewControllerKey.to),
            let containerView = transitionContext.containerView
            else {
                return
        }
        containerView.insertSubview(toVC.view, belowSubview: fromVC.view)
        let screenBounds = UIScreen.mainScreen.bounds
        let bottomLeftCorner = CGPoint(x: 0, y: screenBounds.height)
        let finalFrame = CGRect(origin: bottomLeftCorner, size: screenBounds.size)
        
        UIView.animateWithDuration(
            transitionDuration(transitionContext),
            animations: {
                fromVC.view.frame = finalFrame
        },
            completion: { _ in
                transitionContext.completeTransition(!transitionContext.transitionWasCancelled())
        }
        )
    }
}
