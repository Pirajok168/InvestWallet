//
//  WidgetSearch.swift
//  iosApp
//
//  Created by Данила Еремин on 24.07.2022.
//

import SwiftUI
import shared
import SVGKit
import SDWebImageSwiftUI


struct WidgetSearch: View {
    let description: String
    let exchange: String
    let logoId: String
    
    
    var body: some View {
        HStack{
        
            
        
            WebImage(url: URL(string: logoId), context: [.imageThumbnailPixelSize : CGSize(width: 0, height: 56), .imagePreserveAspectRatio: false])
                .resizable()
                .placeholder(content: {
                    Circle().foregroundColor(.gray)
                })
                .scaledToFit()
                .frame(width: 56, height: 56, alignment: .center)
                .clipShape(Circle())
                
        
            
            Text(description)
                .bold()
                .lineLimit(1)
            
            Spacer()
            
            Text(exchange)
                .fontWeight(.light)
        }.padding()
    }
}

struct WidgetSearch_Previews: PreviewProvider {
    static var previews: some View {
        WidgetSearch(description: "Apple Inc.", exchange: "NASDAQ", logoId: "")
    }
}
