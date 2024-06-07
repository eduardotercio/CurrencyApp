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

	var body: some Scene {
		WindowGroup {
			ContentView().ignoreSafeArea
		}
	}
}