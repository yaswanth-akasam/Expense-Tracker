Expense Tracker Android App
A simple, modern expense tracking Android application built with Java and Material Design components.

## Features

✅ **Implemented & Polished**

*   **Modern UI**: Clean, minimal interface using Material Design components.
*   **Light & Dark Mode**: Fully functional dark mode support, toggled from the settings screen.
*   **Comprehensive Categories**: A rich set of pre-populated default categories with unique, custom icons.
*   **Add & Manage Expenses**: Easily add new expenses using a floating action button. Edit and delete functionality is available from the category detail view.
*   **Dynamic Home Dashboard**: Includes a budget progress bar and a list of recent expenses.
*   **Monthly Reports**: A dedicated reports screen with:
    *   Month-by-month navigation.
    *   A pie chart showing the breakdown of expenses by category.
    *   A circular progress bar showing spending against the monthly budget.
*   **Smart Category View**: The main categories screen intelligently displays only the categories you have actually used, keeping the UI clean.
*   **Customizable Settings**:
    *   Set and update a monthly budget.
    *   Choose your preferred currency (with corresponding flags).
    *   Toggle between light and dark mode.
    *   Reset the current month's expense data with a confirmation dialog.
*   **Robust Database**: Uses Room for local data persistence with `Expense` and `Category` entities.

🚧 **Coming Soon**

*   Swipe-to-edit/delete gestures on expense lists.
*   Custom category management (add/edit user-defined categories).
*   CSV export functionality for expenses.
*   More advanced reporting and filtering options.

## Project Structure
app/src/main/java/com/expensetracker/
├── model/              # Data models (Expense, Category)
├── database/           # Room database (DAOs, Database)
├── ui/                 # UI components
│   ├── home/          # Home fragment & ViewModel
│   ├── categories/    # Categories management
│   ├── reports/       # Reports & analytics
│   └── settings/      # App settings
├── adapter/           # RecyclerView & Spinner adapters
└── utils/             # Utility classes (DateUtils, CurrencyUtils)

## Database Schema
**Expenses Table**
*   `id` (Primary Key)
*   `amount` (Double)
*   `category_id` (Foreign Key)
*   `date_time` (Long timestamp)
*   `note` (String)

**Categories Table**
*   `id` (Primary Key)
*   `name` (String)
*   `color_hex` (String)
*   `icon_name` (String)
*   `is_default` (Boolean)

## Getting Started
1.  Open the project in Android Studio
2.  Sync Gradle files
3.  Run the app on an emulator or device (API 24+)

## Dependencies
*   Room Database: Local data persistence
*   Material Design Components: Modern UI components
*   Navigation Component: Fragment navigation
*   ViewBinding: Type-safe view references
*   Lifecycle Components: ViewModel and LiveData

## Build Requirements
*   Android Studio Giraffe or newer
*   Gradle 8.2+
*   Android SDK 34
*   Minimum SDK 24 (Android 7.0)

## Next Steps
*   Implement custom category management.
*   Add CSV export functionality.
*   Enhance reports with more detailed analytics.
