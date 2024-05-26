import SwiftUI

struct ContentView: View {
    var body: some View {
        NavigationView {
            MainViewController()
                .navigationBarTitle("Home", displayMode: .inline)
        }
    }
}

@main
struct iOSApp: App {

     init() {
        KoinModuleKt.initializeKoin()
     }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}