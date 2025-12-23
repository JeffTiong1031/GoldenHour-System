# ⚙️ Functionality

## System Architecture
```
┌──────────────────────────────────┐
│      User Interface (UI)         │ ← Console/GUI
├──────────────────────────────────┤
│     Service Layer (Logic)        │ ← Business ops
│  Auth, Sales, Stock, Attendance  │
├──────────────────────────────────┤
│     Storage Layer (I/O)          │ ← Persistence
│     DatabaseHandler (SQLite),    |
|     CSVHandler (backup)          │
├──────────────────────────────────┤
│   In-Memory Cache (DataLoad)     │ ← Fast access
│     Static lists of all data     │
└──────────────────────────────────┘
```

## 🆕 (Updates) Hybrid Storage Model
SQLite is the primary store; CSV is backup/export. Migration from CSV to SQLite is a one-time operation via `SyncDataCSVSQL`. **Note: Migration has been completed, only run this when you want to sync SQL with CSV**

One-time Migration (only when enabling hybrid):
```
Run: SyncDataCSVSQL.migrate() → Populate SQLite from existing CSV
```

Runtime Model:
```
SQLite (source of truth)
    ↓
DatabaseHandler.query*() → Load into DataLoad (in-memory)
    ↓
Services: Read from DataLoad; Write to SQLite
    ↓
CSV: Optional backup/export only (no runtime reads)
```

To run the one-time migration:
```bash
cd goldenhour
mvn exec:java -Dexec.mainClass="com.goldenhour.main.SyncDataCSVSQL"
// or you can just run java
```

## 🚀 Core Workflows

### 1️⃣ Startup & Data Loading
```
Main.java starts
  ↓
DatabaseHandler.connect/query → Load from SQLite
  ↓
DataLoad.fetchallxxx() → In-memory lists ready
  ↓
LoginUI displayed (system ready)
```

### 2️⃣ Authentication
```
LoginUI → AuthService.login(id, password)
  ↓
Search DataLoad.allEmployees (in-memory)
  ↓
Validate and proceed
```

### 3️⃣ Attendance Logging
```
AttendanceUI → clock in/out
  ↓
AttendanceService → create record (timestamp)
  ↓
Persist to SQLite (primary)
  ↓
Update CSV (backup)
```

### 4️⃣ Stock Management
```
StockUI → count/transfer/search
  ↓
Services update Model/Stock in DataLoad
  ↓
Persist changes to SQLite (CSV as backup)
  ↓
ReceiptHandler → text receipts (as needed)
```

### 5️⃣ Sales Processing
```
SalesUI → product + qty
  ↓
SalesService → create sale, update inventory
  ↓
Persist to SQLite
  ↓
ReceiptHandler → sales receipt
  ↓
Update CSV (backup)
```

### 6️⃣ Search Operations
```
SalesSearch/StockSearch → iterate DataLoad (in-memory)
  ↓
Return matches (no disk I/O)
```

### 7️⃣ Data Editing
```
EditXXX → select item + new values
  ↓
Service finds object in DataLoad and updates via setters
  ↓
Persist to SQLite
  ↓
Optional CSV export (backup)
```

## Data Models (POJOs)
| Entity | Fields | Storage |
|--------|--------|---------|
| Employee | id, name, role, password | SQLite (primary), CSV (backup) |
| Model | code, name, price, outlet | SQLite (primary), CSV (backup) |
| Outlet | code, name | SQLite |
| Stock | model_code, outlet_code, quantity | SQLite |
| Sales | id, model_code, qty, total, timestamp | SQLite (primary), CSV (backup) |
| Attendance | emp_id, date, clock_in, clock_out | SQLite (primary), CSV (backup) |

## Key Classes
| Package | Class | Responsibility |
|---------|-------|----------------|
| main | `Main.java` | Entry point |
| main | `MainGUI.java` | Entry point GUI |
| main | `SyncDataCSVSQL.java` | One-time CSV → SQLite migration |
| categories | `Employee.java`, `Model.java`, `Sales.java`, `Attendance.java`, `Outlet.java` | POJOs with `fromCSV()`/`toCSV()` |
| service/attendance | `AttendanceService.java` | Attendance logic |
| service/loginregister | `AuthService.java`, `RegistrationService.java` | Auth/registration |
| service/salessys | `SalesService.java`, `SalesSearch.java` | Sales ops |
| service/stocksys | `StockCountService.java`, `StockMovementService.java`, `StockSearch.java` | Inventory ops |
| storage | `DatabaseHandler.java` | SQLite CRUD/schema |
| storage | `CSVHandler.java` | CSV backup/export |
| storage | `ReceiptHandler.java` | Receipt generation |
| dataload | `DataLoad.java` | In-memory cache of runtime data |
| ui | `LoginUI.java`, `SalesUI.java`, `StockUI.java`, `SearchUI.java`, `AttendanceUI.java`, `EditUI.java` | Console UI |

## Data Flow Summary
- **Read:** Populate `DataLoad` from SQLite via `DatabaseHandler` (no CSV reads at runtime).
- **Write:** Services persist to SQLite; CSV used only for backup/export.
- **Query:** Use SQLite for complex queries; UI reads from `DataLoad`.
- **Backup:** CSV serves solely as export/backup.

---