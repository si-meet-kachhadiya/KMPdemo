import SwiftUI
import SharedLogic

struct ContentView: View {
    @StateObject private var listingModel = MviListingScreenModel()

    var body: some View {
        NavigationStack {
            Group {
                if listingModel.isLoading && listingModel.items.isEmpty {
                    ProgressView("Loading listings...")
                } else if let error = listingModel.errorMessage, listingModel.items.isEmpty {
                    ContentUnavailableView(
                        "Could not load listings",
                        systemImage: "wifi.exclamationmark",
                        description: Text(error)
                    )
                } else if listingModel.items.isEmpty {
                    ContentUnavailableView(
                        "No listings",
                        systemImage: "photo.on.rectangle",
                        description: Text("Tap Refresh to load the KKR listing feed.")
                    )
                } else {
                    List(listingModel.items, id: \.id) { item in
                        VStack(alignment: .leading, spacing: 4) {
                            Text(item.title)
                                .font(.headline)
                            if let date = item.publishDate {
                                Text(date)
                                    .font(.caption)
                                    .foregroundStyle(.secondary)
                            }
                            if let type = item.assetType {
                                Text("Type: \(type)")
                                    .font(.caption2)
                            }
                        }
                    }
                }
            }
            .navigationTitle("KKR Listing (MVI)")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button("Refresh") {
                        listingModel.refresh()
                    }
                }
            }
        }
    }
}

/// SwiftUI wrapper around shared Kotlin [MviListingController].
/// iOS only observes state and renders UI — API + MVI live in SharedLogic.
final class MviListingScreenModel: ObservableObject {
    @Published var items: [ListingItem] = []
    @Published var isLoading = false
    @Published var errorMessage: String?

    private let controller = MviListingController(scope: KmpSdk.shared.scope)
    private var cancellable: Cancellable?

    init() {
        cancellable = controller.observeListingAPI { [weak self] snapshot in
            DispatchQueue.main.async {
                self?.items = snapshot.items
                self?.isLoading = snapshot.isLoading
                self?.errorMessage = snapshot.errorMessage
            }
        }
    }

    func refresh() {
        controller.refresh()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
