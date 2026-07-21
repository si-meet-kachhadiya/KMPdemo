import SwiftUI
import SharedLogic

struct ContentView: View {
    @StateObject private var apiModel = MviApiScreenModel()

    var body: some View {
        NavigationStack {
            Group {
                if apiModel.isLoading && apiModel.items.isEmpty {
                    ProgressView("Loading listings...")
                } else if let error = apiModel.errorMessage, apiModel.items.isEmpty {
                    ContentUnavailableView(
                        "Could not load listings",
                        systemImage: "wifi.exclamationmark",
                        description: Text(error)
                    )
                } else if apiModel.items.isEmpty {
                    ContentUnavailableView(
                        "No listings",
                        systemImage: "photo.on.rectangle",
                        description: Text("Tap Refresh to load the KKR listing feed.")
                    )
                } else {
                    List(apiModel.items, id: \.id) { item in
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
                        apiModel.refreshListings()
                    }
                }
            }
        }
    }
}

/// SwiftUI wrapper around the single shared [MviApiController].
/// One controller exposes listing + photo-detail APIs for iOS.
final class MviApiScreenModel: ObservableObject {
    @Published var items: [ListingItem] = []
    @Published var isLoading = false
    @Published var errorMessage: String?

    private let api = MviApiController(scope: KmpSdk.shared.scope)
    private var listingCancellable: Cancellable?

    init() {
        listingCancellable = api.observeListings { [weak self] snapshot in
            DispatchQueue.main.async {
                self?.items = snapshot.items
                self?.isLoading = snapshot.isLoading
                self?.errorMessage = snapshot.errorMessage
            }
        }
        api.loadListings()
    }

    func refreshListings() {
        api.refreshListings()
    }

    /// Example for later: open photo detail by titleAlias.
    func loadPhotoDetail(titleAlias: String) {
        api.loadPhotoDetail(titleAlias: titleAlias)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
