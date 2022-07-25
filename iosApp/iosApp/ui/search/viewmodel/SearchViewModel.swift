//
//  SearchViewModel.swift
//  iosApp
//
//  Created by Данила Еремин on 24.07.2022.
//

import Foundation
import SwiftUI
import shared

extension ContentView {
    
    struct SearchState{
        let isLoadind: Bool
        let searchValue: String
        let listSearched: [UIStock]
    }
    
    struct UIStock: Hashable, Identifiable{
        let id = UUID()
        let description: String
        let exchange: String
        let logoId: String
    }
    
    class ViewModel: ObservableObject{
        @Published var searchState = SearchState(isLoadind: false, searchValue: "", listSearched: [])
        
        let repoSearch: RepositorySearch
        
        init(repo: RepositorySearch){
            self.repoSearch = repo
        }
        
        func onSearch(newValue: String) async {
            do{
                let list = try await repoSearch.getFindQuotes(text: newValue, lang: "ru", type: "stock", exchange: "")
                
                let uiStock = list.map{(stock: StockDTO) in
                    
                    return UIStock(description: stock.getDescriptions(), exchange: stock.exchange ?? "", logoId: stock.getURLImg())
                }
            
                DispatchQueue.main.async {
                    self.searchState = SearchState(
                        isLoadind: false,
                        searchValue: newValue,
                        listSearched: uiStock
                    )
                }
                
            }catch{
                print(error)
            }
        }
    }
}
