//
//  iosAppApp.swift
//  iosApp
//
//  Created by Данила Еремин on 24.07.2022.
//

import SwiftUI
import shared

@main
struct iosAppApp: App {
    var body: some Scene {
        WindowGroup {
            SearchScreen(
                viewModel: .init(repo: EngineSDK().repoSearch.apiSearchRepository)
            )
        }
    }
}
