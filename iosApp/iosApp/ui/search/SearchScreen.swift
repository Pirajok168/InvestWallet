//
//  SearchScreen.swift
//  iosApp
//
//  Created by Данила Еремин on 24.07.2022.
//

import SwiftUI
import shared


struct SearchScreen: View {
    @State private var searchValue: String = ""
    @ObservedObject var viewModel: ContentView.ViewModel
    
    var body: some View {
        VStack{
            HStack{
                TextField("Поиск", text: $searchValue)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: searchValue){ _ in
                        Task{
                            await viewModel.onSearch(newValue: searchValue)
                        }
        
                    }
            }
            List{
                ForEach(viewModel.searchState.listSearched){stock in
                    WidgetSearch(description: stock.description, exchange: stock.exchange, logoId: stock.logoId)
                }
            }.listStyle(.plain)
           
        }
        
    }
}

struct SearchScreen_Previews: PreviewProvider {
    static var previews: some View {
        SearchScreen(
            viewModel: .init(repo: EngineSDK().repoSearch.apiSearchRepository)
        )
    }
}
