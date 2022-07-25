//
//  iosAppApp.swift
//  iosApp
//
//  Created by Данила Еремин on 24.07.2022.
//

import SwiftUI
import SDWebImageSVGCoder
import shared

@main
struct iosAppApp: App {
    
    init(){
        setUp()
    }
    var body: some Scene {
        WindowGroup {
            SearchScreen(
                viewModel: .init(repo: EngineSDK().repoSearch.apiSearchRepository)
            )
        }
    }
}

private extension iosAppApp{
    func setUp(){
        SDImageCodersManager.shared.addCoder(SDImageSVGCoder.shared)
    }
}
