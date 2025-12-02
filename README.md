# Card Scanner Android App

A modern Android application that scans business cards using the device camera and extracts name and address information using ML Kit text recognition.

## Features

- **Camera Integration**: Real-time camera preview with CameraX
- **ML Kit Text Recognition**: Automatic text extraction from business cards
- **Smart Parsing**: Intelligent extraction of name and address fields
- **Local Storage**: Save scanned cards using Room database
- **Material Design**: Clean, modern UI with Material Design 3
- **MVVM Architecture**: Clean architecture with ViewModel and LiveData

## Tech Stack

- **Language**: Kotlin
- **Camera**: CameraX
- **Text Recognition**: Google ML Kit
- **Database**: Room
- **Architecture**: MVVM
- **UI**: Material Design 3, ViewBinding
- **Async**: Kotlin Coroutines

## Requirements

- Android Studio Hedgehog or later
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9.20+

## Setup

1. Clone the repository:
```bash
git clone https://github.com/dexina-u/card-scanner-android.git
cd card-scanner-android
```

2. Open in Android Studio

3. Sync Gradle files

4. Run on device or emulator (camera required)

## Permissions

The app requires camera permission to scan cards. Permission is requested at runtime.

## How It Works

1. **Scan**: Tap "Scan New Card" and point camera at business card
2. **Capture**: Tap capture button to take photo
3. **Process**: ML Kit extracts text automatically
4. **Parse**: Smart algorithm identifies name and address
5. **Save**: Review and save to local database
6. **View**: Access all saved cards from main screen

## Architecture

```
app/
├── data/
│   ├── CardEntity.kt       # Room entity
│   ├── CardDao.kt          # Database operations
│   ├── CardDatabase.kt     # Room database
│   └── CardRepository.kt   # Data layer
├── utils/
│   └── TextParser.kt       # Text parsing logic
├── viewmodel/
│   └── CardViewModel.kt    # ViewModel
└── Activities
    ├── MainActivity.kt     # Home screen
    ├── ScanActivity.kt     # Camera & scanning
    └── CardListActivity.kt # Saved cards list
```

## Text Parsing Algorithm

The app uses heuristics to extract name and address:
- First line typically contains the name
- Address identified by keywords (street, road, city, etc.)
- Lines with numbers often indicate addresses
- Fallback to raw text if parsing fails

## Future Enhancements

- [ ] Edit saved cards
- [ ] Export to contacts
- [ ] Cloud sync
- [ ] OCR accuracy improvements
- [ ] Support for phone numbers and emails
- [ ] Batch scanning
- [ ] Search functionality

## License

MIT License - feel free to use and modify

## Contributing

Pull requests welcome! Please follow existing code style.

## Support

For issues or questions, open a GitHub issue.