# Expense Tracker Android App

A simple, modern expense tracking Android application built with Java and Material Design 3.

## Features

### ✅ Implemented (Milestone A & B)
- **Modern UI**: Material Design 3 with clean, minimal interface
- **Bottom Navigation**: Home, Categories, Reports, Settings tabs
- **Database**: Room database with expense and category entities
- **Add Expenses**: Floating Action Button to add new expenses
- **Home Dashboard**: Budget progress bar and recent expenses list
- **Categories**: Pre-populated default categories (Food, Travel, Bills, etc.)
- **Date/Time Selection**: Date and time picker for expenses

### 🚧 Coming Soon (Milestones C-E)
- Edit/Delete expenses with swipe gestures
- Custom category management
- Monthly budget settings
- Reports with category breakdown
- Month-wise expense filtering
- CSV export functionality
- Dark mode support

## Project Structure

```
app/src/main/java/com/expensetracker/
├── model/              # Data models (Expense, Category)
├── database/           # Room database (DAOs, Database)
├── ui/                 # UI components
│   ├── home/          # Home fragment & ViewModel
│   ├── categories/    # Categories management
│   ├── reports/       # Reports & analytics
│   └── settings/      # App settings
├── adapter/           # RecyclerView adapters
└── utils/             # Utility classes (DateUtils)
```

## Database Schema

### Expenses Table
- `id` (Primary Key)
- `amount` (Double)
- `category_id` (Foreign Key)
- `date_time` (Long timestamp)
- `note` (String)

### Categories Table
- `id` (Primary Key)
- `name` (String)
- `color_hex` (String)
- `icon_name` (String)
- `is_default` (Boolean)

## Getting Started

1. Open the project in Android Studio
2. Sync Gradle files
3. Run the app on an emulator or device (API 24+)

## Dependencies

- **Room Database**: Local data persistence
- **Material Design 3**: Modern UI components
- **Navigation Component**: Fragment navigation
- **ViewBinding**: Type-safe view references
- **Lifecycle Components**: ViewModel and LiveData

## Build Requirements

- Android Studio Arctic Fox or newer
- Gradle 8.2+
- Android SDK 34
- Minimum SDK 24 (Android 7.0)

## Next Steps

Follow the step-by-step plan to implement:
1. Edit/Delete functionality (Milestone C)
2. Budget management (Milestone D)  
3. Reports and analytics (Milestone D)
4. Polish and extras (Milestone E)

The app follows the vertical slice approach - each milestone delivers a working, usable version with incremental improvements.