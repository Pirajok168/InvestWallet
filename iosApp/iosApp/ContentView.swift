//
//  ContentView.swift
//  iosApp
//
//  Created by Данила Еремин on 24.07.2022.
//

import SwiftUI
import shared

struct ContentView: View {
    @State var greet = "Loading..."
    
    var body: some View {
        VStack {
            Text(greet)
                .padding()
            Button("Click", action: {
                Task{
                   
                    do{
                        let result = try await EngineSDK().repoSearch.apiSearchRepository.getFindQuotes(text: "APPLE",
                                                                                          lang: "ru",
                                                                                           type: "stock",
                                                                                          exchange: "")
                        greet = result[0].country ??  ""
                    }catch{
                        print(error)
                    }
                     
                }
            })
        }
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
