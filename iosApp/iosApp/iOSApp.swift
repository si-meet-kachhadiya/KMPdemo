import SwiftUI
import SharedLogic

@main
struct iOSApp: App {
    init() {
        IosSdkBootstrapKt.bootstrapKmpSdkForIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
