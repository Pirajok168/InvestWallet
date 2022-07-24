//
//  WidgetSearch.swift
//  iosApp
//
//  Created by Данила Еремин on 24.07.2022.
//

import SwiftUI
import shared

struct WidgetSearch: View {
    let description: String
    let exchange: String
    
    var body: some View {
        HStack{
            Text(description)
                .bold()
                .lineLimit(1)
            
            Spacer()
            
            Text(exchange)
                .fontWeight(.light)
        }
    }
}

struct WidgetSearch_Previews: PreviewProvider {
    static var previews: some View {
        WidgetSearch(description: "Apple Inc.", exchange: "NASDAQ")
    }
}
